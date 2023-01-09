package com.liming.tool.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RunStateUtil {
    private final static Map<String, Boolean> stateMap = new ConcurrentHashMap<>();

    public static boolean getState(String name) {
        return stateMap.getOrDefault(name, false);
    }

    public static void isRunning(String name) {
        stateMap.put(name, true);
    }

    public static void isOver(String name) {
        stateMap.put(name, false);
    }
}
