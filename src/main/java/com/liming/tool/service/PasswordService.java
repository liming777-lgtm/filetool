package com.liming.tool.service;

import com.alibaba.fastjson2.JSONObject;
import com.liming.tool.bean.StageController;
import com.liming.tool.bean.TablePassword;
import com.liming.tool.controller.MainController;
import com.liming.tool.controller.PasswordController;
import com.liming.tool.impl.DataInit;
import com.liming.tool.manager.ObjectManager;
import com.liming.tool.utils.HttpUtil;
import com.liming.tool.utils.RunStateUtil;
import javafx.collections.ObservableList;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class PasswordService {
    private PasswordService() {
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(PasswordService.class);
    private static final PasswordService service;
    private static final Random RANDOM = new Random();

    static {
        service = new PasswordService();
    }

    public static PasswordService getInstance() {
        return service;
    }

    public String login(String username, String password) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", username);
        jsonObject.put("password", password);
        return HttpUtil.request(jsonObject, new HttpPost(HttpUtil.BASE_URL + "/login"));

    }

    public void save(String usernameStr, String webNameStr, String passwordStr, String desStr, int id) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", usernameStr);
        jsonObject.put("webName", webNameStr);
        jsonObject.put("des", desStr);
        jsonObject.put("password", passwordStr);
        if (id != 0) {
            jsonObject.put("id", id);
            String url = HttpUtil.BASE_URL + "/updatePwdInfo";
            String request = HttpUtil.request(jsonObject, new HttpPost(url));
            if ("1".equals(request)) {
                StageController stageController = ObjectManager.get(MainService.class.getSimpleName(), StageController.class);
                if (stageController != null && stageController.getDataInit() instanceof MainController) {
                    MainController mainController = (MainController) stageController.getDataInit();
                    TablePassword selectedItem = mainController.table.getSelectionModel().getSelectedItem();
                    selectedItem.setUsername(usernameStr);
                    selectedItem.setWebName(webNameStr);
                    selectedItem.setDes(desStr);
                    selectedItem.setPassword(passwordStr);
                    mainController.table.refresh();
                }
            }
        } else {
            String url = HttpUtil.BASE_URL + "/addPwdInfo";
            String request = HttpUtil.request(jsonObject, new HttpPost(url));
            if (request == null || request.isEmpty()) {
                return;
            }
            int lastId = Integer.parseInt(request);
            StageController stageController = ObjectManager.get(MainService.class.getSimpleName(), StageController.class);
            if (stageController != null && stageController.getDataInit() instanceof MainController) {
                MainController mainController = (MainController) stageController.getDataInit();
                ObservableList<TablePassword> items = mainController.table.getItems();
                TablePassword tablePassword1 = new TablePassword();
                tablePassword1.setUsername(usernameStr);
                tablePassword1.setWebName(webNameStr);
                tablePassword1.setDes(desStr);
                tablePassword1.setPassword(passwordStr);
                tablePassword1.setId(lastId);
                items.add(tablePassword1);
            }
        }
    }


    public void addInfo() {
        RunStateUtil.showStage(PasswordService.class.getSimpleName(), "添加密码信息", "fxml/password-update.fxml", null);
    }

    public void editInfo(TablePassword selectedItem) {
        RunStateUtil.showStage(PasswordService.class.getSimpleName(), "修改密码信息", "fxml/password-update.fxml", selectedItem);

    }

    public void delete(int id) {
        String url = HttpUtil.BASE_URL + "/deletePwdInfo";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        String request = HttpUtil.request(jsonObject, new HttpPost(url));
        if (request.equals("1")) {
            StageController stageController = ObjectManager.get(MainService.class.getSimpleName(), StageController.class);
            if (stageController != null && stageController.getDataInit() instanceof MainController) {
                MainController mainController = (MainController) stageController.getDataInit();
                ObservableList<TablePassword> items = mainController.table.getItems();
                items.remove(mainController.table.getSelectionModel().getSelectedIndex());
            }
        }
    }

    public void createPassword() {
        //密码长度为16,4个大写4个写字母,8个数字
        char[] password = new char[16];
        int numberCount = 0;
        int lowerCaseCount = 0;
        for (int i = 0; i < password.length; i++) {
            //数字
            if (Math.random() < 0.5d && numberCount <= 8) {
                password[i] = (char) ('0' + (RANDOM.nextInt(10)));
                numberCount++;
            } else if (Math.random() < 0.5d && lowerCaseCount <= 4) {
                password[i] = (char) ('a' + (RANDOM.nextInt(26)));
                lowerCaseCount++;
            } else {
                password[i] = (char) ('A' + (RANDOM.nextInt(26)));
            }
        }
        System.out.println(new String(password));
        StageController stageController = ObjectManager.get(PasswordService.class.getSimpleName(), StageController.class);
        if (stageController != null) {
            DataInit dataInit = stageController.getDataInit();
            if (dataInit instanceof PasswordController) {
                PasswordController passwordController = (PasswordController) dataInit;
                passwordController.password.setText(new String(password));
            }
        }
    }
}
