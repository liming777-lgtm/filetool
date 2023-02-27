package com.liming.tool.service;

import com.liming.tool.manager.ObjectManager;
import com.liming.tool.utils.RunTimeExec;
import com.liming.tool.utils.ShowMessageUtil;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private final static Logger LOGGER = LoggerFactory.getLogger(ResetService.class);

    public static ResetService getInstance() {
        return instance;
    }


    public void navicatReset() {
        Process exec = RunTimeExec.registryOperation(RunTimeExec.RegistryOperation.QUERY, "HKCU\\SOFTWARE\\Classes\\CLSID /s /f info /k");
        if (exec == null) {
            return;
        }
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
                ShowMessageUtil.showInfo(null, "重置navicat成功");
            }
        } catch (IOException e) {
            LOGGER.error("重置navicat失败", e);
        }
    }
}
