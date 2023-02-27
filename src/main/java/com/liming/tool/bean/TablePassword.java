package com.liming.tool.bean;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TablePassword {
    private final IntegerProperty id;
    private final StringProperty webName;

    private final StringProperty username;
    private final StringProperty password;
    private final StringProperty des;

    public void setId(int id) {
        this.id.set(id);
    }

    public void setWebName(String webName) {
        this.webName.set(webName);
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public void setDes(String des) {
        this.des.set(des);
    }

    public int getId() {
        return id.get();
    }

    public String getWebName() {
        return webName.get();
    }

    public String getUsername() {
        return username.get();
    }

    public String getPassword() {
        return password.get();
    }

    public String getDes() {
        return des.get();
    }

    public TablePassword() {
        id = new SimpleIntegerProperty();
        webName = new SimpleStringProperty();
        username = new SimpleStringProperty();
        password = new SimpleStringProperty();
        des = new SimpleStringProperty();
    }
}
