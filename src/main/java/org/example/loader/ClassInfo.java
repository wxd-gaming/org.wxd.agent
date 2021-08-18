package org.example.loader;

import java.io.Serializable;

/**
 * @author: Troy.Chen(失足程序员, 15388152619)
 * @version: 2021-08-06 17:10
 **/
public class ClassInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Class<?> clazz;
    private byte[] clazzBytes;

    public String getSimpleName() {
        return clazz.getSimpleName();
    }

    public String getClassName() {
        return clazz.getName();
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public ClassInfo setClazz(Class<?> clazz) {
        this.clazz = clazz;
        return this;
    }

    public byte[] getClazzBytes() {
        return clazzBytes;
    }

    public ClassInfo setClazzBytes(byte[] clazzBytes) {
        this.clazzBytes = clazzBytes;
        return this;
    }
}
