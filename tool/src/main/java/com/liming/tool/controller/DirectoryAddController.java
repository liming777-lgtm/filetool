package com.liming.tool.controller;

import com.liming.tool.manager.StageManager;
import com.liming.tool.service.DirectoryAddService;
import com.liming.tool.service.MainService;
import com.liming.tool.utils.ShowMessageUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;

import java.io.File;

public class DirectoryAddController {

    @FXML
    public TextField name;

    @FXML
    public Label showPath;

    @FXML
    void save(ActionEvent event) {
        String text = name.getText();
        if ("".equals(text.trim())) {
            ShowMessageUtil.showInfo(StageManager.getStage(MainService.MAIN_STAGE), "名字不能为空");
            return;
        }
        MainService.getInstance().saveExcelPath(text, showPath.getText());
        StageManager.getStage("directoryAdd").close();
    }

    @FXML
    void openFile(ActionEvent event) {
        DirectoryAddService instance = DirectoryAddService.getInstance();
        File file = instance.openFile(showPath.getText());
        if (file == null) {
            return;
        }
        showPath.setText(file.getAbsolutePath());
        showPath.getTooltip().setText(file.getAbsolutePath());
    }
}
