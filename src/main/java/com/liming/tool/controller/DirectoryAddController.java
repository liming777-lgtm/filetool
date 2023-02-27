package com.liming.tool.controller;

import com.liming.tool.bean.StageController;
import com.liming.tool.impl.DataInit;
import com.liming.tool.manager.ObjectManager;
import com.liming.tool.service.DirectoryAddService;
import com.liming.tool.utils.AddPath;
import com.liming.tool.utils.ShowMessageUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.File;

public class DirectoryAddController implements DataInit {

    @FXML
    public TextField name;

    @FXML
    public Label showPath;

    private AddPath addPath;

    @Override
    public void init(Object obj) {
        if (obj instanceof AddPath) {
            this.addPath = (AddPath) obj;
        }
    }

    @FXML
    void save(ActionEvent event) {
        String text = name.getText();
        if ("".equals(text.trim())) {
            ShowMessageUtil.showInfo(null, "名字不能为空");
            return;
        }

        StageController stageController = ObjectManager.get(DirectoryAddService.class.getSimpleName(), StageController.class);
        if (stageController == null) {
            return;
        }
        addPath.getExecute().accept(text, showPath.getText());
        stageController.getStage().close();
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
