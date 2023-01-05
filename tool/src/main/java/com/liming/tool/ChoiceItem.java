package com.liming.tool;

import java.io.Serializable;

public class ChoiceItem<K> implements Serializable {
    private String name;
    private K value;

    public ChoiceItem(String name, K value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public K getValue() {
        return value;
    }

    public void setValue(K value) {
        this.value = value;
    }
}
