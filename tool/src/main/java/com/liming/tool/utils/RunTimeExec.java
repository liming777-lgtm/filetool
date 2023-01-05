package com.liming.tool.utils;

import java.io.IOException;

public class RunTimeExec {

    public static void exec(String command) {
        try {
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler "+command);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
