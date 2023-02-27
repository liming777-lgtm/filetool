package com.liming.tool.service;

import com.liming.tool.bean.ChoiceItem;
import com.liming.tool.bean.StageController;
import com.liming.tool.controller.MainController;
import com.liming.tool.impl.DataInit;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


public class MainService {

    public static final String SEARCH_EXCEL_SHELL_AND_OPEN = "searchExcelShellAndOpen";
    private static final Logger LOGGER = LoggerFactory.getLogger(MainService.class);

    static {
        instance = new MainService();
    }

    private MainService() {
    }

    private final static MainService instance;

    public static MainService getInstance() {
        return instance;
    }

    public void chooseDirectory() {
        DirectoryAddService.getInstance().showStage(DirectoryAddService.class.getSimpleName(), AddPath.EXCEL, "excel目录选择");
    }

    public void saveExcelPath(String text, String path) {
        StageController stageController = ObjectManager.get(MainService.class.getSimpleName(), StageController.class);
        if (stageController == null) {
            return;
        }
        DataInit dataInit = stageController.getDataInit();
        if (dataInit instanceof MainController) {
            MainController controller = (MainController) dataInit;
            ChoiceBox<ChoiceItem<String>> name = controller.name;
            ObservableList<ChoiceItem<String>> items = name.getItems();
            items.add(new ChoiceItem<>(text, path));
            name.getSelectionModel().select(items.size() - 1);
        }
    }


    public void save() {
        StageController stageController = ObjectManager.get(MainService.class.getSimpleName(), StageController.class);
        if (stageController == null) {
            return;
        }
        DataInit dataInit = stageController.getDataInit();
        if (dataInit instanceof MainController) {
            MainController controller = (MainController) dataInit;
            Path data = getPath();
            if (!Files.exists(data)) {
                try {
                    Files.createFile(data);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            ObservableList<ChoiceItem<String>> items = controller.name.getItems();
            ObservableList<ChoiceItem<String>> gitPathChoiceItems = controller.GITPathChoice.getItems();
            ObservableList<ChoiceItem<String>> svnPathChoiceItems = controller.SVNPathChoice.getItems();

            try (ObjectOutputStream outputStream = new ObjectOutputStream(Files.newOutputStream(data, StandardOpenOption.WRITE))) {
                //excel
                outputStream.writeObject(new ArrayList<>(items));
                outputStream.flush();
                //git
                outputStream.writeObject(new ArrayList<>(gitPathChoiceItems));
                outputStream.flush();
                //svn
                outputStream.writeObject(new ArrayList<>(svnPathChoiceItems));
                outputStream.flush();

            } catch (IOException e) {
                LOGGER.error("数据保存出错", e);
            }
        }
    }

    public void init() {
        StageController stageController = ObjectManager.get(MainService.class.getSimpleName(), StageController.class);
        if (stageController == null) {
            return;
        }
        DataInit dataInit = stageController.getDataInit();
        if (dataInit instanceof MainController) {
            MainController controller = (MainController) dataInit;
            ChoiceBox<ChoiceItem<String>> name = controller.name;
            ChoiceBox<ChoiceItem<String>> gitPathChoice = controller.GITPathChoice;
            ChoiceBox<ChoiceItem<String>> svnPathChoice = controller.SVNPathChoice;
            //数据读取 其余线程执行
            Platform.runLater(() -> {
                Path data = getPath();
                if (Files.exists(data)) {
                    try (ObjectInputStream inputStream = new ObjectInputStream(Files.newInputStream(data))) {
                        //excel文件路径
                        loadDataList(name, inputStream);
                        //git路径
                        loadDataList(gitPathChoice, inputStream);
                        //svn路径
                        loadDataList(svnPathChoice, inputStream);
                    } catch (IOException | ClassNotFoundException e) {
                        LOGGER.error("数据读取错误", e);
                    }

                }
            });
            controller.files.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            name.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == null) {
                    nullFilePath();
                    return;
                }
                fileScan(newValue.getValue());
            });
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

            //git操作初始化
            ChoiceBox<ChoiceItem<String>> gitOperation = controller.GITOperation;
            List<ChoiceItem<String>> gitList = new ArrayList<>();
            gitList.add(new ChoiceItem<>("拉取", "TortoiseGitProc.exe /command:pull /path:\"%s"));
            gitList.add(new ChoiceItem<>("还原", "TortoiseGitProc.exe /command:revert /path:\"%s"));
            gitList.add(new ChoiceItem<>("提交", "TortoiseGitProc.exe /command:commit /path:\"%s"));
            gitList.add(new ChoiceItem<>("推送", "TortoiseGitProc.exe /command:push /path:\"%s"));
            gitList.add(new ChoiceItem<>("删除", ""));
            gitOperation.setItems(FXCollections.observableList(gitList));
            gitOperation.getSelectionModel().selectFirst();

            //svn操作初始化
            ChoiceBox<ChoiceItem<String>> svnOperation = controller.SVNOperation;
            List<ChoiceItem<String>> svnList = new ArrayList<>();
            svnList.add(new ChoiceItem<>("拉取", "TortoiseProc.exe /command:update /path:\"%s"));
            svnList.add(new ChoiceItem<>("还原", "TortoiseProc.exe /command:revert /path:\"%s"));
            svnList.add(new ChoiceItem<>("提交", "TortoiseProc.exe /command:commit /path:\"%s"));
            svnList.add(new ChoiceItem<>("删除", ""));
            svnOperation.setItems(FXCollections.observableList(svnList));
            svnOperation.getSelectionModel().selectFirst();
        }
    }

    @SuppressWarnings("unchecked")
    private void loadDataList(ChoiceBox<ChoiceItem<String>> name, ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
        List<ChoiceItem<String>> readObject = (List<ChoiceItem<String>>) inputStream.readObject();
        if (readObject != null) {
            name.setItems(FXCollections.observableList(readObject));
            //默认选中第一个
            if (readObject.size() > 0) {
                name.getSelectionModel().select(0);
            }
        }
    }

    private Path getPath() {
        return FileSystems.getDefault().getPath("data");
    }

    private void initFiles(List<String> collect) {
        StageController stageController = ObjectManager.get(MainService.class.getSimpleName(), StageController.class);
        if (stageController == null) {
            return;
        }
        DataInit dataInit = stageController.getDataInit();
        if (dataInit instanceof MainController) {
            MainController controller = (MainController) dataInit;
            ObservableList<String> observableList = FXCollections.observableList(collect);
            FilteredList<String> strings = new FilteredList<>(observableList);
            strings.addListener((ListChangeListener<String>) c -> controller.files.getSelectionModel().selectFirst());
            controller.files.setItems(strings);
        }
    }

    private void fileScan(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        File[] listFiles = file.listFiles(pathname -> pathname.getName().endsWith(".xlsx"));
        if (listFiles == null) {
            nullFilePath();
            return;
        }
        List<String> collect = Arrays.stream(listFiles).map(File::getName).collect(Collectors.toList());
        initFiles(collect);
    }

    private void nullFilePath() {
        StageController stageController = ObjectManager.get(MainService.class.getSimpleName(), StageController.class);
        if (stageController == null) {
            return;
        }
        DataInit dataInit = stageController.getDataInit();
        if (dataInit instanceof MainController) {
            MainController controller = (MainController) dataInit;
            controller.files.setItems(new FilteredList<>(FXCollections.emptyObservableList()));
        } ;
    }

    public void openExcel(String filesItem) {
        StageController stageController = ObjectManager.get(MainService.class.getSimpleName(), StageController.class);
        if (stageController == null) {
            return;
        }
        DataInit dataInit = stageController.getDataInit();
        if (dataInit instanceof MainController) {
            MainController controller = (MainController) dataInit;
            String fileName = filesItem == null ? controller.files.getSelectionModel().getSelectedItem() : filesItem;
            ChoiceItem<String> selectedItem = controller.name.getSelectionModel().getSelectedItem();
            String path = selectedItem.getValue();
            RunTimeExec.openFile(path + File.separatorChar + fileName);
        } ;
    }


    public void searchExcelShellAndOpen() {
        StageController stageController = ObjectManager.get(MainService.class.getSimpleName(), StageController.class);
        if (stageController == null) {
            return;
        }
        DataInit dataInit = stageController.getDataInit();
        if (dataInit instanceof MainController) {
            MainController controller = (MainController) dataInit;
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
                            StageController stageController = ObjectManager.get(MainService.class.getSimpleName(), StageController.class);
                            if (stageController == null) {
                                return;
                            }
                            DataInit dataInit = stageController.getDataInit();
                            if (dataInit instanceof MainController) {
                                MainController controller = (MainController) dataInit;
                                controller.searchButton.setText("搜索");
                                controller.searchButton.setDisable(false);
                                Stage stage = stageController.getStage();
                                if (stage != null) {
                                    ShowMessageUtil.showInfo(stage, "正在打开..");
                                }
                            }
                        });
                        openExcel(filesItem);
                        return;
                    }
                } catch (IOException | InvalidFormatException e) {
                    LOGGER.error("搜索表出错", e);
                } finally {
                    RunStateUtil.isOver(SEARCH_EXCEL_SHELL_AND_OPEN);
                }
            }
            Platform.runLater(() -> ShowMessageUtil.showInfo(null, "未找到该表"));
        }, ThreadUtil.pool);
    }

    public void deleteFilePath() {
        StageController stageController = ObjectManager.get(MainService.class.getSimpleName(), StageController.class);
        if (stageController == null) {
            return;
        }
        DataInit dataInit = stageController.getDataInit();
        if (dataInit instanceof MainController) {
            MainController mainController = (MainController) dataInit;
            ChoiceBox<ChoiceItem<String>> name = mainController.name;
            ChoiceItem<String> item = name.getSelectionModel().getSelectedItem();
            if (item == null) {
                return;
            }
            name.getItems().remove(item);
            name.getSelectionModel().selectFirst();
        }
    }
}
