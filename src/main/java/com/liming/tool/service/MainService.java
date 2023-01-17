package com.liming.tool.service;

import com.liming.tool.bean.ChoiceItem;
import com.liming.tool.controller.MainController;
import com.liming.tool.manager.ObjectManager;
import com.liming.tool.utils.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


public class MainService {

    public static final String SEARCH_EXCEL_SHELL_AND_OPEN = "searchExcelShellAndOpen";

    static {
        instance = new MainService();
    }

    private MainService() {
    }

    private final static MainService instance;

    public static MainService getInstance() {
        return instance;
    }

    public void chooseDirectory() throws IOException {
        DirectoryAddService.getInstance().showStage();
    }

    public void saveExcelPath(String text, String path) {
        MainController controller = ObjectManager.get(Constant.MAIN_CONTROLLER, MainController.class);
        if (controller == null) {
            return;
        }
        ChoiceBox<ChoiceItem<String>> name = controller.name;
        ObservableList<ChoiceItem<String>> items = name.getItems();
        items.add(new ChoiceItem<>(text, path));
        name.getSelectionModel().select(items.size() - 1);
        save();
    }


    public void save() {
        MainController controller = ObjectManager.get(Constant.MAIN_CONTROLLER, MainController.class);
        if (controller == null) {
            return;
        }
        ObservableList<ChoiceItem<String>> items = controller.name.getItems();
        Path data = getPath();
        if (!Files.exists(data)) {
            try {
                Files.createFile(data);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try (ObjectOutputStream outputStream = new ObjectOutputStream(Files.newOutputStream(data, StandardOpenOption.WRITE))) {
            outputStream.writeObject(new ArrayList<>(items));
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public void init() {
        MainController controller = ObjectManager.get(Constant.MAIN_CONTROLLER, MainController.class);
        if (controller == null) {
            return;
        }
        ChoiceBox<ChoiceItem<String>> name = controller.name;
        //数据读取 其余线程执行
        Platform.runLater(() -> {
            Path data = getPath();
            if (Files.exists(data)) {
                try (ObjectInputStream inputStream = new ObjectInputStream(Files.newInputStream(data))) {
                    List<ChoiceItem<String>> readObject = (List<ChoiceItem<String>>) inputStream.readObject();
                    name.setItems(FXCollections.observableList(readObject));
                    //默认选中第一个
                    if (readObject.size() > 0) {
                        name.getSelectionModel().select(0);
                    }
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        name.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> fileScan(newValue.getValue()));
        TextField search = controller.search;
        initFiles(new ArrayList<>());
        search.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                ObservableList<String> list = controller.files.getItems();
                if (list instanceof FilteredList) {
                    FilteredList<String> filteredList = (FilteredList<String>) list;
                    filteredList.setPredicate((s) -> {
                        if (!"".equals(search.getText().trim())) {
                            return s.contains(search.getText());
                        }
                        return true;
                    });
                }
            }
        });
    }

    private Path getPath() {
        return FileSystems.getDefault().getPath("data");
    }

    private void initFiles(List<String> collect) {
        MainController controller = ObjectManager.get(Constant.MAIN_CONTROLLER, MainController.class);
        if (controller == null) {
            return;
        }
        controller.files.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        ObservableList<String> observableList = FXCollections.observableList(collect);
        FilteredList<String> strings = new FilteredList<>(observableList);
        strings.addListener((ListChangeListener<String>) c -> controller.files.getSelectionModel().selectFirst());
        controller.files.setItems(strings);
    }

    private void fileScan(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        File[] listFiles = file.listFiles(pathname -> pathname.getName().endsWith(".xlsx"));
        if (listFiles == null) {
            return;
        }
        List<String> collect = Arrays.stream(listFiles).map(File::getName).collect(Collectors.toList());
        initFiles(collect);
    }

    public void openExcel(String filesItem) {
//        final IOReactorConfig ioReactorConfig = IOReactorConfig.custom()
//                .setSoTimeout(Timeout.ofSeconds(3))
//                .build();
//
//        final CloseableHttpAsyncClient client = HttpAsyncClients.custom()
//                .setIOReactorConfig(ioReactorConfig)
//                .build();
//
//        client.start();
//
//        int count = 1000;
//        CountDownLatch countDownLatch = new CountDownLatch(count);
//        System.out.println("开始执行");
//        Instant now = Instant.now();
//        for (int i = 0; i< count; i++) {
//            final SimpleHttpRequest request = SimpleRequestBuilder.get()
//                    .setUri("http://10.10.30.196:10011/hqg/background_api/?command=recharge&platform=1&channel=100&sid=17777&puid="+i+"&uid=260026&orderno=2301031548271002102626FB&amount=30&status=1&extinfo=2$288360372933039113$10120$504445194148291567$260026&nonce=1672732107&pfid=100&vname=1.1.0&token=62f3dc8a74ba0abf5bbfc81e6c48d657")
//                    .build();
//            client.execute(
//                    SimpleRequestProducer.create(request),
//                    SimpleResponseConsumer.create(),
//                    new FutureCallback<SimpleHttpResponse>() {
//
//                        @Override
//                        public void completed(final SimpleHttpResponse response) {
//                            countDownLatch.countDown();
//                        }
//                        @Override
//                        public void failed(final Exception ex) {
//                            System.out.println(request + "->" + ex);
//                            countDownLatch.countDown();
//                        }
//                        @Override
//                        public void cancelled() {
//                            System.out.println(request + " cancelled");
//                            countDownLatch.countDown();
//                        }
//                    });
//        }
//        try {
//            System.out.println("请求发送时间："+ ChronoUnit.SECONDS.between(now,Instant.now()));
//            countDownLatch.await();
//            System.out.println("全部返回时间："+ ChronoUnit.SECONDS.between(now,Instant.now()));
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//
//        client.close(CloseMode.GRACEFUL);
        MainController controller = ObjectManager.get(Constant.MAIN_CONTROLLER, MainController.class);
        if (controller == null) {
            return;
        }
        String fileName = filesItem == null ? controller.files.getSelectionModel().getSelectedItem() : filesItem;
        ChoiceItem<String> selectedItem = controller.name.getSelectionModel().getSelectedItem();
        String path = selectedItem.getValue();
        RunTimeExec.openFile(path + File.separatorChar + fileName);
    }


    public void searchExcelShellAndOpen() {
        MainController controller = ObjectManager.get(Constant.MAIN_CONTROLLER, MainController.class);
        if (controller == null) {
            return;
        }
        String name = controller.search.getText();
        if (StringUtils.isEmpty(name.trim()) || RunStateUtil.getState(SEARCH_EXCEL_SHELL_AND_OPEN)) {
            return;
        }
        ObservableList<String> filesItems = controller.files.getItems();
        ChoiceItem<String> selectedItem = controller.name.getSelectionModel().getSelectedItem();
        String path = selectedItem.getValue();
        RunStateUtil.isRunning(SEARCH_EXCEL_SHELL_AND_OPEN);
        controller.searchButton.setText("搜索中...");
        controller.searchButton.setDisable(true);
        doSearch(name, new ArrayList<>(filesItems), path);
    }

    private void doSearch(String name, List<String> filesItems, String path) {

        CompletableFuture.runAsync(() -> {

            for (String filesItem : filesItems) {
                Path pathFile = Paths.get(path, filesItem);
                try (OPCPackage opcPackage = OPCPackage.open(pathFile.toFile(), PackageAccess.READ)) {
                    XSSFWorkbook sheets = new XSSFWorkbook(opcPackage);
                    XSSFSheet sheet = sheets.getSheet(name);
                    if (sheet != null) {
                        Platform.runLater(() -> {
                            MainController controller = ObjectManager.get(Constant.MAIN_CONTROLLER, MainController.class);
                            if (controller == null) {
                                return;
                            }
                            controller.searchButton.setText("搜索");
                            controller.searchButton.setDisable(false);
                            Stage stage = ObjectManager.get(Constant.MAIN_STAGE, Stage.class);
                            if (stage != null) {
                                ShowMessageUtil.showInfo(stage, "正在打开..");
                            }
                        });
                        openExcel(filesItem);
                        return;
                    }
                } catch (IOException | InvalidFormatException e) {
                    throw new RuntimeException(e);
                } finally {
                    RunStateUtil.isOver(SEARCH_EXCEL_SHELL_AND_OPEN);
                }
            }
            Platform.runLater(() -> ShowMessageUtil.showInfo(Objects.requireNonNull(ObjectManager.get(Constant.MAIN_STAGE, Stage.class)), "未找到该表"));
        }, ThreadUtil.pool);
    }

}
