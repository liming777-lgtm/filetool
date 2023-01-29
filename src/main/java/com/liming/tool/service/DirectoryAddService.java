package com.liming.tool.service;

import com.liming.tool.MainApplication;
import com.liming.tool.manager.ObjectManager;
import com.liming.tool.utils.AddPath;
import com.liming.tool.utils.Constant;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class DirectoryAddService {


    static {
        instance = new DirectoryAddService();
    }

    private DirectoryAddService() {
    }

    private final static DirectoryAddService instance;
    private final static Logger LOGGER = LoggerFactory.getLogger(DirectoryAddService.class);

    public static DirectoryAddService getInstance() {
        return instance;
    }

    public File openFile(String text) {
        Stage stage = ObjectManager.get(Constant.DIRECTORY_ADD, Stage.class);
        if (stage == null) {
            return null;
        }
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("选择目录");
        if (!"".equals(text.trim())) {
            chooser.setInitialDirectory(new File(text));
        }
        return chooser.showDialog(stage);
    }

    public void showStage(String name, AddPath path, String title) {
        Stage directoryAdd = ObjectManager.get(name, Stage.class);
        if (directoryAdd == null) {
            directoryAdd = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("fxml/directory-add-view.fxml"));
            Scene scene;
            try {
                scene = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                LOGGER.error("初始化stage失败", e);
                return;
            }
            directoryAdd.setTitle(title);
            directoryAdd.setScene(scene);
            directoryAdd.initOwner(ObjectManager.get(Constant.MAIN_STAGE, Stage.class));
            directoryAdd.initModality(Modality.APPLICATION_MODAL);
            ObjectManager.add(name, directoryAdd, Stage.class);
        }
        if (directoryAdd.isShowing()) {
            return;
        }
        Scene scene = directoryAdd.getScene();
        directoryAdd.setUserData(path);
        Stage finalDirectoryAdd = directoryAdd;
        directoryAdd.setOnShown(event -> {
            Stage stage = ObjectManager.get(Constant.MAIN_STAGE, Stage.class);
            if (stage != null) {
                finalDirectoryAdd.setX(stage.getX() + stage.getWidth() / 2 - scene.getWidth() / 2);
                finalDirectoryAdd.setY(stage.getY() + stage.getHeight() / 2 - scene.getHeight() / 2);
            }
        });
        directoryAdd.show();
    }
}
