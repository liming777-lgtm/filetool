package com.liming.tool.controller;

import com.liming.tool.ChoiceItem;
import com.liming.tool.service.MainService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;

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
    void open(ActionEvent event) {
        try {
            MainService.getInstance().openExcel();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void addExcelPath(ActionEvent actionEvent) {
        try {
            MainService.getInstance().chooseDirectory();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}