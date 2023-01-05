package com.liming.tool.service;

import com.liming.tool.MainApplication;
import com.liming.tool.controller.DirectoryAddController;
import com.liming.tool.manager.StageManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Tooltip;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

import static com.liming.tool.service.MainService.MAIN_STAGE;

public class DirectoryAddService {

    public static final String DIRECTORY_ADD = "directoryAdd";

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
        Stage stage = StageManager.getStage("directoryAdd");
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
        String name = DIRECTORY_ADD;
        Stage directoryAdd = StageManager.getStage(name);
        if (directoryAdd == null) {
            directoryAdd = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("fxml/directory-add-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            directoryAdd.setTitle("选择目录");
            directoryAdd.setScene(scene);
            directoryAdd.initOwner(StageManager.getStage(MAIN_STAGE));
            directoryAdd.initModality(Modality.APPLICATION_MODAL);
            StageManager.addStage(name, directoryAdd);
        }
        if (directoryAdd.isShowing()) {
            return;
        }
        Scene scene = directoryAdd.getScene();

        Stage finalDirectoryAdd = directoryAdd;
        directoryAdd.setOnShown(event -> {
            Stage stage = StageManager.getStage(MAIN_STAGE);
            finalDirectoryAdd.setX(stage.getX() + stage.getWidth() / 2 - scene.getWidth() / 2);
            finalDirectoryAdd.setY(stage.getY() + stage.getHeight() / 2 - scene.getHeight() / 2);
        });
        directoryAdd.show();
    }
}
