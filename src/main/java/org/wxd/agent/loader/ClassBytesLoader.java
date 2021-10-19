package org.wxd.agent.loader;


import lombok.extern.slf4j.Slf4j;

import javax.tools.JavaFileObject;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

/**
 * class byte 加载器
 *
 * @author: Troy.Chen(無心道, 15388152619)
 * @version: 2021-04-29 09:36
 **/
@Slf4j
public class ClassBytesLoader extends ClassLoader {

    /*已经加载的class类情况*/
    Map<String, Class<?>> classesMap = null;
    /*待加载的class byte 集合*/
    Map<String, byte[]> classBytesMap;

    public ClassBytesLoader(ClassLoader parent, Map<String, byte[]> classBytesMap) {
        super(parent == null ? Thread.currentThread().getContextClassLoader() : parent);
        this.classBytesMap = classBytesMap;
    }

    /**
     * 已经加载的class类情况
     * 相当于加载类的时候需要引用的类
     *
     * @param classesMap
     * @return
     */
    public ClassBytesLoader setClassesMap(Map<String, Class<?>> classesMap) {
        this.classesMap = classesMap;
        return this;
    }

    public Collection<ClassInfo> toAllClass() {
        TreeMap<String, ClassInfo> classMap = new TreeMap<>();
        for (String className : this.classBytesMap.keySet()) {
            try {
                Class<?> aClass = loadClass(className);
                final byte[] bytes = classBytesMap.get(className);
                classMap.put(className, new ClassInfo().setLoadClass(aClass).setLoadClassBytes(bytes));
            } catch (Throwable e) {
                log.warn("加载 class bytes " + className);
                e.printStackTrace(System.out);
            }
        }
        return classMap.values();
    }

    Class<?> findMyClass(String name) {
        if (!classBytesMap.containsKey(name)) {
            if (classesMap != null) {
                return classesMap.get(name);
            }
        }
        return null;
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        Class<?> myClass = findMyClass(name);
        if (myClass != null) {
            return myClass;
        }
        return super.loadClass(name);
    }

    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
        Class<?> myClass = findMyClass(name);
        if (myClass != null) {
            return myClass;
        }
        final byte[] bytes = classBytesMap.get(name);
        if (bytes != null) {
            return super.defineClass(null, bytes, 0, bytes.length);
        }
        return super.findClass(name);
    }

    @Override
    public InputStream getResourceAsStream(String name) {
        if (name.endsWith(JavaFileObject.Kind.CLASS.extension)) {
            String qualifiedClassName = name.substring(0, name.length() - JavaFileObject.Kind.CLASS.extension.length()).replace('/', '.');
            final byte[] bytes = classBytesMap.get(qualifiedClassName);
            if (null != bytes) {
                return new ByteArrayInputStream(bytes);
            }
        }
        return super.getResourceAsStream(name);
    }

}
