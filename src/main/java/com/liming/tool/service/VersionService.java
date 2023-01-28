package com.liming.tool.service;

import com.liming.tool.bean.ChoiceItem;
import com.liming.tool.controller.MainController;
import com.liming.tool.manager.ObjectManager;
import com.liming.tool.utils.AddPath;
import com.liming.tool.utils.Constant;
import com.liming.tool.utils.RunTimeExec;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;

import java.io.IOException;

public class VersionService {

    static {
        instance = new VersionService();
    }

    private VersionService() {
    }

    private final static VersionService instance;

    public static VersionService getInstance() {
        return instance;
    }

    public void chooseDirectory(AddPath path) throws IOException {
        DirectoryAddService.getInstance().showStage(Constant.DIRECTORY_ADD, path, path.name() + "目录选择");
    }

    public void saveGITPath(String text, String path) {
        MainController controller = ObjectManager.get(Constant.MAIN_CONTROLLER, MainController.class);
        if (controller == null) {
            return;
        }
        ChoiceBox<ChoiceItem<String>> name = controller.GITPathChoice;
        ObservableList<ChoiceItem<String>> items = name.getItems();
        items.add(new ChoiceItem<>(text, path));
        name.getSelectionModel().select(items.size() - 1);
    }

    public void saveSVNPath(String text, String path) {
        MainController controller = ObjectManager.get(Constant.MAIN_CONTROLLER, MainController.class);
        if (controller == null) {
            return;
        }
        ChoiceBox<ChoiceItem<String>> name = controller.SVNPathChoice;
        ObservableList<ChoiceItem<String>> items = name.getItems();
        items.add(new ChoiceItem<>(text, path));
        name.getSelectionModel().select(items.size() - 1);
    }

    public void SVNExecute() {
        MainController controller = ObjectManager.get(Constant.MAIN_CONTROLLER, MainController.class);
        if (controller == null) {
            return;
        }
        ChoiceItem<String> item = controller.SVNPathChoice.getSelectionModel().getSelectedItem();
        if (item == null) {
            return;
        }
        ChoiceItem<String> selectedItem = controller.SVNOperation.getSelectionModel().getSelectedItem();
        if ("删除".equals(selectedItem.getName())) {
            controller.SVNPathChoice.getItems().remove(item);
            return;
        }
        String cmdBase = selectedItem.getValue();
        RunTimeExec.exec(String.format(cmdBase, item.getValue()));
    }

    public void GITExecute() {
        MainController controller = ObjectManager.get(Constant.MAIN_CONTROLLER, MainController.class);
        if (controller == null) {
            return;
        }
        ChoiceItem<String> item = controller.GITPathChoice.getSelectionModel().getSelectedItem();
        if (item == null) {
            return;
        }
        ChoiceItem<String> selectedItem = controller.GITOperation.getSelectionModel().getSelectedItem();
        if ("删除".equals(selectedItem.getName())) {
            controller.GITPathChoice.getItems().remove(item);
            return;
        }
        String cmdBase = selectedItem.getValue();
        RunTimeExec.exec(String.format(cmdBase, item.getValue()));
    }
}
