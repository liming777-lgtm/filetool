package com.liming.tool.bean;

public class ObjectDescription {
    private final Object object;
    private final Class<?> clz;

    public ObjectDescription(Object object, Class<?> clz) {
        this.object = object;
        this.clz = clz;
    }

    public Object getObject() {
        return object;
    }

    public Class<?> getClz() {
        return clz;
    }
}
