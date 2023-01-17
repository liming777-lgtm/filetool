package com.liming.tool.utils;

import java.io.IOException;

public class RunTimeExec {

    private RunTimeExec() {
    }

    /**
     * 注册表操作
     */
    public enum RegistryOperation {
        QUERY, DELETE, ADD

    }

    /**
     * 调用系统方法打开文件
     *
     * @param path 路径
     */
    public static void openFile(String path) {
        try {
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 对window注册表进行操作
     *
     * @param operation 操作
     * @param path      注册表路径
     * @return 执行结果
     */
    public static Process registryOperation(RegistryOperation operation, String path) {
        try {
            return Runtime.getRuntime().exec(String.format("REG %s %s", operation.name(), path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
