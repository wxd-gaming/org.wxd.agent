package org.wxd.agent.loader;


import org.wxd.agent.tool.io.FileReadUtil;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 * 加载class文件
 *
 * @author: Troy.Chen(無心道, 15388152619)
 * @version: 2021-08-06 14:40
 **/
public class ClassFileLoader implements Serializable {

    private static final long serialVersionUID = 1L;

    final Map<String, byte[]> fileMap;
    final ClassBytesLoader classBytesLoader;

    public ClassFileLoader(String fileSource) throws Exception {
        this(null, fileSource);
    }

    public ClassFileLoader(ClassLoader parent, String fileSource) throws Exception {
        fileMap = FileReadUtil.loopReadBytes(fileSource, ".class");
        classBytesLoader = new ClassBytesLoader(parent, fileMap);
    }

    public ClassBytesLoader getClassBytesLoader() {
        return classBytesLoader;
    }

    public Collection<ClassInfo> toAllClass() {
        return classBytesLoader.toAllClass();
    }

    public Map<String, byte[]> getFileMap() {
        return fileMap;
    }

}
