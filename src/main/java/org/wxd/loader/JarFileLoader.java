package org.wxd.loader;


import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * jar 包资源加载
 * <p>
 * 非特殊需求请调用镜头方法：addJar2UrlClassLoader
 * <p>特别鸣谢 上海-念念（qq:596889735）
 *
 * @author: Troy.Chen(失足程序员, 15388152619)
 * @version: 2021-01-09 09:43
 **/
@Slf4j
public class JarFileLoader extends ClassLoader {


    /**
     * 把 jar 包附加到 UrlClassLoader 加载器里面
     *
     * @param checkClassName 需要验证的类名，全面，
     * @param jarPath        jar包路径
     */
    public static Class<?> addJar2UrlClassLoader(String checkClassName, String jarPath) {
        ClassLoader contextClassLoader = ClassLoader.getSystemClassLoader();
        try {
            final Class<?> aClass = contextClassLoader.loadClass(checkClassName);
            log.warn("原始加载器加载成功：" + checkClassName);
            return aClass;
        } catch (ClassNotFoundException cnfe) {
            final File file = new File(jarPath);
            if (!file.exists()) {
                throw new RuntimeException("jar包文件不存在：" + jarPath);
            }
            try {
                Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
                method.setAccessible(true);
                method.invoke(contextClassLoader, file.toURI().toURL());
                final Class<?> loadClass = contextClassLoader.loadClass(checkClassName);
                log.warn("附加外部 jar 包，加载器加载成功：" + checkClassName);
                return loadClass;
            } catch (Throwable e) {
                throw new RuntimeException("ClassLoader 附加 jar 包", e);
            }
        }
    }

    private File jarFile;
    private URL url1;
    private URLClassLoader myClassLoader;

    /**
     * 非特殊需求请调用镜头方法：addJar2UrlClassLoader
     *
     * @param jarFilePath
     */
    public JarFileLoader(String jarFilePath) {
        try {
            this.jarFile = new File(jarFilePath);
            url1 = jarFile.toURI().toURL();
            myClassLoader = new URLClassLoader(new URL[]{url1}, Thread.currentThread().getContextClassLoader());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public URL getUrl1() {
        return url1;
    }

    public URLClassLoader getMyClassLoader() {
        return myClassLoader;
    }

    public File getJarFile() {
        return jarFile;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            //通过将给定路径名字符串转换为抽象路径名来创建一个新File实例
            return myClassLoader.loadClass(name);
//            //通过jarFile和JarEntry得到所有的类
//            JarFile jar = new JarFile(jarFile);
//            //返回zip文件条目的枚举
//            Enumeration<JarEntry> enumFiles = jar.entries();
//            JarEntry entry;
//            //测试此枚举是否包含更多的元素
//            while (enumFiles.hasMoreElements()) {
//                entry = enumFiles.nextElement();
//                String classFullName = entry.getName();
//                if (classFullName.endsWith(".class")) {
//                    //去掉后缀.class
//                    String className = classFullName.substring(0, classFullName.length() - 6).replace("/", ".");
//                    Class<?> myclass = myClassLoader.loadClass(className);
//                    return myclass;
//                }
//            }
        } catch (Throwable e) {
            e.printStackTrace(System.out);
        }
        return super.findClass(name);
    }

}
