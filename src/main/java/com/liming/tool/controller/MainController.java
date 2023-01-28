package com.liming.tool.controller;

import com.liming.tool.bean.ChoiceItem;
import com.liming.tool.manager.ObjectManager;
import com.liming.tool.service.MainService;
import com.liming.tool.service.ResetService;
import com.liming.tool.service.VersionService;
import com.liming.tool.utils.AddPath;
import com.liming.tool.utils.Constant;
import com.liming.tool.utils.RunTimeExec;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;

public class MainController {
    @FXML
    public MenuItem addExcelPath;
    @FXML
    public ChoiceBox<ChoiceItem<String>> name;

    @FXML
    public ListView<String> files;
    @FXML
    public TextField search;
    @FXML
    public Button searchButton;
    @FXML
    public ChoiceBox<ChoiceItem<String>> SVNPathChoice;
    public ChoiceBox<ChoiceItem<String>> GITPathChoice;
    public ChoiceBox<ChoiceItem<String>> GITOperation;
    public ChoiceBox<ChoiceItem<String>> SVNOperation;


    @FXML
    void open(ActionEvent event) {
        try {
            MainService.getInstance().openExcel(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void search(ActionEvent event) {
        MainService.getInstance().searchExcelShellAndOpen();

    }

    @FXML
    public void addExcelPath(ActionEvent actionEvent) {
        try {
            MainService.getInstance().chooseDirectory();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void navicatReset(ActionEvent event) {
        ResetService.getInstance().navicatReset();
    }

    public void deleteFilePath(ActionEvent actionEvent) {
        MainService.getInstance().deleteFilePath();
    }

    public void GITOpen(ActionEvent actionEvent) {
        MainController mainController = ObjectManager.get(Constant.MAIN_CONTROLLER, MainController.class);
        if (mainController == null) {
            return;
        }
        ChoiceItem<String> item = mainController.GITPathChoice.getSelectionModel().getSelectedItem();
        if (item == null) {
            return;
        }
        RunTimeExec.openFile(item.getValue());
    }

    public void SVNOpen(ActionEvent actionEvent) {
        MainController mainController = ObjectManager.get(Constant.MAIN_CONTROLLER, MainController.class);
        if (mainController == null) {
            return;
        }
        ChoiceItem<String> item = mainController.SVNPathChoice.getSelectionModel().getSelectedItem();
        if (item == null) {
            return;
        }
        RunTimeExec.openFile(item.getValue());
    }

    public void SVNExecute(ActionEvent actionEvent) {
        VersionService.getInstance().SVNExecute();
    }

    public void GitExecute(ActionEvent actionEvent) {
        VersionService.getInstance().GITExecute();
    }

    public void addGITPath(ActionEvent actionEvent) {
        try {
            VersionService.getInstance().chooseDirectory(AddPath.GIT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addSVNPath(ActionEvent actionEvent) {
        try {
            VersionService.getInstance().chooseDirectory(AddPath.SVN);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}