package com.liming.tool.controller;

import com.liming.tool.ChoiceItem;
import com.liming.tool.service.MainService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

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
    void open(ActionEvent event) {
        try {
            MainService.getInstance().openExcel();
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

}