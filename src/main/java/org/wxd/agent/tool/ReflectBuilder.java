package org.wxd.agent.tool;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;

/**
 * 类反射
 *
 * @author: Troy.Chen(失足程序员, 15388152619)
 * @version: 2021-08-28 09:58
 **/
public class ReflectBuilder {

    public static ReflectBuilder builder(Class<?> c) {
        return new ReflectBuilder(c);
    }

    private Class<?> c;
    private HashMap<String, Field> fieldMap = new HashMap<>();
    private HashMap<String, Field> allFieldMap = new HashMap<>();

    private ReflectBuilder(Class<?> c) {
        this.c = c;
        Field[] fields = c.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (!Modifier.isStatic(field.getModifiers())) {
                fieldMap.put(field.getName(), field);
            }
            allFieldMap.put(field.getName(), field);
        }
    }

    public Class<?> getC() {
        return c;
    }

    public HashMap<String, Field> getFieldMap() {
        return fieldMap;
    }

    public HashMap<String, Field> getAllFieldMap() {
        return allFieldMap;
    }

    public Field getField(String name) {
        return allFieldMap.get(name);
    }

    public <R> R getFieldValue(String name, Object source) throws Exception {
        final Field field = allFieldMap.get(name);
        return (R) field.get(source);
    }

}
