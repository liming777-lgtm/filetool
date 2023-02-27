package com.liming.tool.service;

import com.liming.tool.bean.ChoiceItem;
import com.liming.tool.bean.StageController;
import com.liming.tool.controller.MainController;
import com.liming.tool.impl.DataInit;
import com.liming.tool.manager.ObjectManager;
import com.liming.tool.utils.AddPath;
import com.liming.tool.utils.RunTimeExec;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;

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

    public void chooseDirectory(AddPath path) {
        DirectoryAddService.getInstance().showStage(DirectoryAddService.class.getSimpleName(), path, path.name() + "目录选择");
    }

    public void saveGITPath(String text, String path) {
        StageController stageController = ObjectManager.get(MainService.class.getSimpleName(), StageController.class);
        if (stageController == null) {
            return;
        }
        DataInit dataInit = stageController.getDataInit();
        if (dataInit instanceof MainController) {
            MainController controller = (MainController) dataInit;
            ChoiceBox<ChoiceItem<String>> name = controller.GITPathChoice;
            ObservableList<ChoiceItem<String>> items = name.getItems();
            items.add(new ChoiceItem<>(text, path));
            name.getSelectionModel().select(items.size() - 1);
        }
    }

    public void saveSVNPath(String text, String path) {
        StageController stageController = ObjectManager.get(MainService.class.getSimpleName(), StageController.class);
        if (stageController == null) {
            return;
        }
        DataInit dataInit = stageController.getDataInit();
        if (dataInit instanceof MainController) {
            MainController controller = (MainController) dataInit;
            ChoiceBox<ChoiceItem<String>> name = controller.SVNPathChoice;
            ObservableList<ChoiceItem<String>> items = name.getItems();
            items.add(new ChoiceItem<>(text, path));
        }
    }

    public void SVNExecute() {
        StageController stageController = ObjectManager.get(MainService.class.getSimpleName(), StageController.class);
        if (stageController == null) {
            return;
        }
        DataInit dataInit = stageController.getDataInit();
        if (dataInit instanceof MainController) {
            MainController controller = (MainController) dataInit;
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
    }

    public void GITExecute() {
        StageController stageController = ObjectManager.get(MainService.class.getSimpleName(), StageController.class);
        if (stageController == null) {
            return;
        }
        DataInit dataInit = stageController.getDataInit();
        if (dataInit instanceof MainController) {
            MainController controller = (MainController) dataInit;
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
}
