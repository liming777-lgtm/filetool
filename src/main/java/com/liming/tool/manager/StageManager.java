package com.liming.tool.manager;

import javafx.stage.Stage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StageManager {
    private static final Map<String, Stage> map = new ConcurrentHashMap<>();

    public static void addStage(String name, Stage stage) {
        map.put(name, stage);
    }

    public static Stage removeStage(String name) {
        return map.remove(name);
    }

    public static Stage getStage(String name){
        return map.get(name);
    }
}
