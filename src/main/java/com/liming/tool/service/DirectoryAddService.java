package com.liming.tool.service;

import com.liming.tool.MainApplication;
import com.liming.tool.manager.ObjectManager;
import com.liming.tool.utils.Constant;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class DirectoryAddService {


    static {
        instance = new DirectoryAddService();
    }

    private DirectoryAddService() {
    }

    private final static DirectoryAddService instance;

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

    public void showStage() throws IOException {
        Stage directoryAdd = ObjectManager.get(Constant.DIRECTORY_ADD, Stage.class);
        if (directoryAdd == null) {
            directoryAdd = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("fxml/directory-add-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            directoryAdd.setTitle("选择目录");
            directoryAdd.setScene(scene);
            directoryAdd.initOwner(ObjectManager.get(Constant.MAIN_STAGE, Stage.class));
            directoryAdd.initModality(Modality.APPLICATION_MODAL);
            ObjectManager.add(Constant.DIRECTORY_ADD, directoryAdd, Stage.class);
        }
        if (directoryAdd.isShowing()) {
            return;
        }
        Scene scene = directoryAdd.getScene();

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
