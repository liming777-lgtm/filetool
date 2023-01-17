package com.liming.tool;

import com.liming.tool.controller.MainController;
import com.liming.tool.manager.ObjectManager;
import com.liming.tool.service.MainService;
import com.liming.tool.service.ResetService;
import com.liming.tool.utils.Constant;
import com.liming.tool.utils.ThreadUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        //数据处理初始化
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("fxml/main.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(Objects.requireNonNull(MainApplication.class.getResource("css/showMessage.css")).toString());
        scene.getStylesheets().add(Objects.requireNonNull(MainApplication.class.getResource("css/style.css")).toString());
        stage.setTitle("工具");
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(event -> ThreadUtil.pool.shutdown());

        ObjectManager.add(Constant.MAIN_STAGE, stage, Stage.class);
        ObjectManager.add(Constant.MAIN_CONTROLLER, fxmlLoader.getController(), MainController.class);

        MainService.getInstance().init();
    }

    public static void main(String[] args) {
        launch();
    }
}