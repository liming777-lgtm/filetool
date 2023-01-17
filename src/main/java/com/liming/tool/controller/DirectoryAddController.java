package com.liming.tool.controller;

import com.liming.tool.manager.ObjectManager;
import com.liming.tool.service.DirectoryAddService;
import com.liming.tool.service.MainService;
import com.liming.tool.utils.Constant;
import com.liming.tool.utils.ShowMessageUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.util.Objects;

public class DirectoryAddController {

    @FXML
    public TextField name;

    @FXML
    public Label showPath;

    @FXML
    void save(ActionEvent event) {
        String text = name.getText();
        if ("".equals(text.trim())) {
            ShowMessageUtil.showInfo(Objects.requireNonNull(ObjectManager.get(Constant.MAIN_STAGE, Stage.class)), "名字不能为空");
            return;
        }
        MainService.getInstance().saveExcelPath(text, showPath.getText());
        Objects.requireNonNull(ObjectManager.get("directoryAdd", Stage.class)).close();
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
