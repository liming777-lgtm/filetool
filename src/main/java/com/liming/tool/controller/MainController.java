package com.liming.tool.controller;

import com.alibaba.fastjson2.JSON;
import com.liming.tool.bean.ChoiceItem;
import com.liming.tool.bean.Password;
import com.liming.tool.bean.StageController;
import com.liming.tool.bean.TablePassword;
import com.liming.tool.impl.DataInit;
import com.liming.tool.manager.ObjectManager;
import com.liming.tool.service.MainService;
import com.liming.tool.service.PasswordService;
import com.liming.tool.service.ResetService;
import com.liming.tool.service.VersionService;
import com.liming.tool.utils.AddPath;
import com.liming.tool.utils.RunTimeExec;
import com.liming.tool.utils.ShowMessageUtil;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class MainController implements DataInit {
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
    public ChoiceBox<ChoiceItem<String>> SVNPathChoice;
    @FXML

    public ChoiceBox<ChoiceItem<String>> GITPathChoice;
    @FXML

    public ChoiceBox<ChoiceItem<String>> GITOperation;
    @FXML

    public ChoiceBox<ChoiceItem<String>> SVNOperation;
    @FXML
    public VBox loginPane;
    @FXML

    public TextField username;
    @FXML

    public PasswordField password;
    @FXML
    public TableView<TablePassword> table;

    @FXML
    public VBox showPane;

    @FXML
    void open(ActionEvent event) {
        try {
            MainService.getInstance().openExcel(null);
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
        MainService.getInstance().chooseDirectory();
    }

    @FXML
    void navicatReset(ActionEvent event) {
        ResetService.getInstance().navicatReset();
    }

    public void deleteFilePath(ActionEvent actionEvent) {
        MainService.getInstance().deleteFilePath();
    }

    public void GITOpen(ActionEvent actionEvent) {
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
            RunTimeExec.openFile(item.getValue());
        }

    }

    public void SVNOpen(ActionEvent actionEvent) {
        StageController stageController = ObjectManager.get(MainService.class.getSimpleName(), StageController.class);
        if (stageController == null) {
            return;
        }
        DataInit dataInit = stageController.getDataInit();
        if (dataInit instanceof MainController) {
            MainController mainController = (MainController) dataInit;
            ChoiceItem<String> item = mainController.SVNPathChoice.getSelectionModel().getSelectedItem();
            if (item == null) {
                return;
            }
            RunTimeExec.openFile(item.getValue());
        }
    }

    public void SVNExecute(ActionEvent actionEvent) {
        VersionService.getInstance().SVNExecute();
    }

    public void GitExecute(ActionEvent actionEvent) {
        VersionService.getInstance().GITExecute();
    }

    public void addGITPath(ActionEvent actionEvent) {
        VersionService.getInstance().chooseDirectory(AddPath.GIT);
    }

    public void addSVNPath(ActionEvent actionEvent) {
        VersionService.getInstance().chooseDirectory(AddPath.SVN);
    }


    @SuppressWarnings("unchecked")
    public void login(ActionEvent actionEvent) {
        StageController stageController = ObjectManager.get(MainService.class.getSimpleName(), StageController.class);
        if (stageController == null) {
            return;
        }
        DataInit dataInit = stageController.getDataInit();
        if (dataInit instanceof MainController) {
            MainController mainController = (MainController) dataInit;
            String usernameText = mainController.username.getText();
            if (StringUtils.isEmpty(usernameText)) {
                ShowMessageUtil.showInfo(null, "用户名为空");
                return;
            }
            String passwordText = mainController.password.getText();
            if (StringUtils.isEmpty(passwordText)) {
                ShowMessageUtil.showInfo(null, "密码为空");
                return;
            }
            String result = PasswordService.getInstance().login(usernameText, passwordText);
            if (StringUtils.isEmpty(result)) {
                ShowMessageUtil.showInfo(null, "账号或密码错误");
                return;
            }
            mainController.loginPane.setVisible(false);
            mainController.showPane.setVisible(true);
            List<Password> passwords = new ArrayList<>(JSON.parseArray(result, Password.class));
            List<TablePassword> list = new ArrayList<>();
            for (Password password1 : passwords) {
                list.add(password1.toTablePassword());
            }

            table.setItems(FXCollections.observableList(list));
            TableColumn<TablePassword, String> column = new TableColumn<>("id");
            TableColumn<TablePassword, String> column1 = new TableColumn<>("网站名称");
            TableColumn<TablePassword, String> column2 = new TableColumn<>("用户名");
            TableColumn<TablePassword, String> column3 = new TableColumn<>("密码");
            TableColumn<TablePassword, String> column4 = new TableColumn<>("描述");

            column.setCellValueFactory(new PropertyValueFactory<>("id"));
            column1.setCellValueFactory(new PropertyValueFactory<>("webName"));
            column2.setCellValueFactory(new PropertyValueFactory<>("username"));
            column3.setCellValueFactory(new PropertyValueFactory<>("password"));
            column4.setCellValueFactory(new PropertyValueFactory<>("des"));
            table.getColumns().addAll(column, column1, column2, column3, column4);
        }
    }

    public void addInfo(ActionEvent actionEvent) {
        PasswordService.getInstance().addInfo();
    }

    public void editInfo(ActionEvent actionEvent) {
        TablePassword selectedItem = table.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            return;
        }
        PasswordService.getInstance().editInfo(selectedItem);
    }

    public void deleteInfo(ActionEvent actionEvent) {
        TablePassword selectedItem = table.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            return;
        }
        PasswordService.getInstance().delete(selectedItem.getId());
    }
}