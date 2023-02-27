package com.liming.tool.utils;

import com.liming.tool.MainApplication;
import com.liming.tool.bean.StageController;
import com.liming.tool.manager.ObjectManager;
import com.liming.tool.service.MainService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RunStateUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(RunStateUtil.class);

    private RunStateUtil() {
    }

    private final static Map<String, Boolean> stateMap = new ConcurrentHashMap<>();

    public static boolean getState(String name) {
        return stateMap.getOrDefault(name, false);
    }

    public static void isRunning(String name) {
        stateMap.put(name, true);
    }

    public static void isOver(String name) {
        stateMap.put(name, false);
    }

    public static void showStage(String name, String title, String fxmlPath, Object userData) {
        final StageController mainSC = ObjectManager.get(MainService.class.getSimpleName(), StageController.class);
        if (mainSC == null) {
            return;
        }
        StageController showStageController = ObjectManager.get(name, StageController.class);
        if (showStageController == null) {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(fxmlPath));
            Scene scene;
            try {
                scene = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                LOGGER.error("初始化stage失败", e);
                return;
            }
            stage.setTitle(title);
            stage.setScene(scene);
            stage.initOwner(mainSC.getStage());
            stage.initModality(Modality.APPLICATION_MODAL);
            showStageController = new StageController(stage, fxmlLoader.getController());
            ObjectManager.add(name, showStageController, StageController.class);
        }
        final Stage stage = showStageController.getStage();
        if (stage.isShowing()) {
            return;
        }
        showStageController.getDataInit().init(userData);
        Scene scene = stage.getScene();
        stage.setOnShown(event -> {
            Stage tempStage = mainSC.getStage();
            if (tempStage != null) {
                stage.setX(tempStage.getX() + tempStage.getWidth() / 2 - scene.getWidth() / 2);
                stage.setY(tempStage.getY() + tempStage.getHeight() / 2 - scene.getHeight() / 2);
            }
        });
        stage.show();
    }
}
