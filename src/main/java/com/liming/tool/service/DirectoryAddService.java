package com.liming.tool.service;

import com.liming.tool.bean.StageController;
import com.liming.tool.controller.DirectoryAddController;
import com.liming.tool.manager.ObjectManager;
import com.liming.tool.utils.AddPath;
import com.liming.tool.utils.RunStateUtil;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

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
        StageController stageController = ObjectManager.get(DirectoryAddService.class.getSimpleName(), StageController.class);
        if (stageController == null) {
            return null;
        }
        Stage stage = stageController.getStage();
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("选择目录");
        if (!"".equals(text.trim())) {
            chooser.setInitialDirectory(new File(text));
        }
        return chooser.showDialog(stage);
    }

    public void showStage(String name, AddPath path, String title) {
        RunStateUtil.showStage(name, title, "fxml/directory-add-view.fxml", path);
    }
}
