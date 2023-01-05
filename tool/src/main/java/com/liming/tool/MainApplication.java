package com.liming.tool;

import com.liming.tool.manager.StageManager;
import com.liming.tool.service.MainService;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        //数据处理初始化


        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("fxml/main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(Objects.requireNonNull(MainApplication.class.getResource("css/showMessage.css")).toString());
        scene.getStylesheets().add(Objects.requireNonNull(MainApplication.class.getResource("css/style.css")).toString());
        stage.setTitle("工具");
        stage.setScene(scene);
        stage.show();
        StageManager.addStage(MainService.MAIN_STAGE,stage);
        MainService.getInstance().init(fxmlLoader.getController());
    }

    public static void main(String[] args) {
        launch();
    }
}