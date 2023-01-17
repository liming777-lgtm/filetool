package com.liming.tool.utils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadUtil {
    private ThreadUtil() {
    }

    //查询单元表
    public static final ExecutorService pool = Executors.newSingleThreadExecutor();


}
