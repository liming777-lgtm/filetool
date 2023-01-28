package com.liming.tool.manager;

import com.liming.tool.bean.ObjectDescription;
import org.apache.poi.ss.formula.functions.T;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ObjectManager {

    private static final Map<String, ObjectDescription> map = new ConcurrentHashMap<>();

    private ObjectManager() {
    }

    public static void add(String name, Object k, Class<?> clz) {
        map.put(name, new ObjectDescription(k, clz));
    }

    public static void remove(String name) {
        map.remove(name);
    }

    public static <T> T get(String name, Class<T> clz) {
        ObjectDescription objectDescription = map.get(name);
        if (objectDescription == null) {
            return null;
        }
        Object object = objectDescription.getObject();
        Class<?> clz1 = objectDescription.getClz();
        if (clz == null || !clz.equals(clz1)) {
            return null;
        }
        return clz.cast(object);
    }
}
