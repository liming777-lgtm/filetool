package com.liming.tool.utils;

import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.KeyEvent;
import java.io.IOException;

public class RunTimeExec {

    private static WinUser.HHOOK hhook;
    private static final Logger LOGGER = LoggerFactory.getLogger(RunTimeExec.class);

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
            LOGGER.error("调用系统方法打开文件", e);
        }

    }

    public static void exec(String cmd) {
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            LOGGER.error("exec", e);
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
            LOGGER.error("对window注册表进行操作", e);
        }
        return null;
    }

    public static void keywordMonitor() {
        Kernel32 kernel32 = Kernel32.INSTANCE;
        WinDef.HMODULE hmodule = kernel32.GetModuleHandle(null);
        WinUser.LowLevelKeyboardProc keyboardProc = new WinUser.LowLevelKeyboardProc() {
            @Override
            public WinDef.LRESULT callback(int nCode, WinDef.WPARAM wParam, WinUser.KBDLLHOOKSTRUCT lParam) {
                if (isPressed(KeyEvent.VK_ALT) && isPressed(KeyEvent.VK_CONTROL) && isPressed(KeyEvent.VK_TAB)) {
                    return null;
                }
                return User32.INSTANCE.CallNextHookEx(hhook, nCode, wParam, new WinDef.LPARAM(lParam.flags));
            }
        };
        hhook = User32.INSTANCE.SetWindowsHookEx(User32.WH_KEYBOARD_LL, keyboardProc, hmodule, 0);
        if (hhook == null) {
            return;
        }
        WinUser.MSG msg = new WinUser.MSG();
        int result;
        while ((result = User32.INSTANCE.GetMessage(msg, null, 0, 0)) != 0) {
            if (result == -1) {
                //	System.err.println("error in get message");
                break;
            } else {
                System.out.println("got message");
                User32.INSTANCE.TranslateMessage(msg);
                User32.INSTANCE.DispatchMessage(msg);
            }
        }
        User32.INSTANCE.UnhookWindowsHookEx(hhook);
    }

    private static boolean isPressed(int key) {
        return (User32.INSTANCE.GetAsyncKeyState(key) & 0x8000) != 0;
    }
}
