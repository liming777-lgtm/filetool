package com.liming.tool.bean;

public class Password {

    private int id;
    private String webName;

    private String username;
    private String password;
    private String des;

    @Override
    public String toString() {
        return "Password{" +
                "id=" + id +
                ", webName='" + webName + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", des='" + des + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWebName() {
        return webName;
    }

    public void setWebName(String webName) {
        this.webName = webName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public Password() {
    }

    public TablePassword toTablePassword() {
        TablePassword tablePassword = new TablePassword();
        tablePassword.setPassword(this.password);
        tablePassword.setId(this.id);
        tablePassword.setDes(this.des);
        tablePassword.setUsername(this.username);
        tablePassword.setWebName(this.webName);
        return tablePassword;
    }
}
