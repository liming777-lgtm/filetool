package com.liming.tool.service;

import com.liming.tool.manager.ObjectManager;
import com.liming.tool.utils.Constant;
import com.liming.tool.utils.RunTimeExec;
import com.liming.tool.utils.ShowMessageUtil;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

public class ResetService {

    static {
        instance = new ResetService();
    }

    private ResetService() {
    }

    private final static ResetService instance;

    public static ResetService getInstance() {
        return instance;
    }


    public void navicatReset() {
        Process exec = RunTimeExec.registryOperation(RunTimeExec.RegistryOperation.QUERY, "HKCU\\SOFTWARE\\Classes\\CLSID /s /f info /k");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(exec.getInputStream()))) {
            String result;
            while ((result = reader.readLine()) != null) {
                if (StringUtils.isNotEmpty(result)) {
                    break;
                }
            }
            if (StringUtils.isNotEmpty(result)) {
                RunTimeExec.registryOperation(RunTimeExec.RegistryOperation.DELETE, result + " /f");
                RunTimeExec.registryOperation(RunTimeExec.RegistryOperation.DELETE, "HKEY_CURRENT_USER\\Software\\PremiumSoft\\NavicatPremium\\Registration16XCS" + " /f");
                RunTimeExec.registryOperation(RunTimeExec.RegistryOperation.DELETE, "HKEY_CURRENT_USER\\Software\\PremiumSoft\\NavicatPremium\\Update" + " /f");
                ShowMessageUtil.showInfo(Objects.requireNonNull(ObjectManager.get(Constant.MAIN_STAGE, Stage.class)), "重置navicat成功");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
