package com.liming.tool.controller;

import com.liming.tool.bean.ChoiceItem;
import com.liming.tool.service.MainService;
import com.liming.tool.service.ResetService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MainController  {
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
            ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
            scheduledExecutorService.scheduleAtFixedRate(()->{
                MainService.getInstance().openExcel(null);
            },1,30, TimeUnit.SECONDS);
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

}