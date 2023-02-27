package com.liming.tool.controller;

import com.liming.tool.bean.StageController;
import com.liming.tool.bean.TablePassword;
import com.liming.tool.impl.DataInit;
import com.liming.tool.manager.ObjectManager;
import com.liming.tool.service.PasswordService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class PasswordController implements DataInit {
    @FXML
    public TextField username;
    @FXML
    public TextField webName;
    @FXML
    public TextField password;
    @FXML
    public TextField des;

    private int openId;

    public void save(ActionEvent actionEvent) {
        String usernameStr = username.getText();
        String webNameStr = webName.getText();
        String passwordStr = password.getText();
        String desStr = des.getText();
        PasswordService.getInstance().save(usernameStr, webNameStr, passwordStr, desStr, openId);
        StageController stageController = ObjectManager.get(PasswordService.class.getSimpleName(), StageController.class);
        if (stageController != null) {
            stageController.getStage().close();
        }
    }

    public void update(String username, String webName, String password, String des, int id) {
        this.username.setText(username);
        this.webName.setText(webName);
        this.password.setText(password);
        this.des.setText(des);
        this.openId = id;
    }

    public void add() {
        this.username.setText("");
        this.webName.setText("");
        this.password.setText("");
        this.des.setText("");
        this.openId = 0;
    }

    @Override
    public void init(Object obj) {
        if (obj instanceof TablePassword) {
            TablePassword tablePassword = (TablePassword) obj;
            update(tablePassword.getUsername(), tablePassword.getWebName(), tablePassword.getPassword()
                    , tablePassword.getDes(), tablePassword.getId());
        }else {
            add();
        }
    }

    public void createPassword(ActionEvent actionEvent) {
            PasswordService.getInstance().createPassword();
    }
}
