package com.liming.tool.service;

import com.liming.tool.ChoiceItem;
import com.liming.tool.controller.MainController;
import com.liming.tool.utils.RunTimeExec;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.apache.hc.client5.http.async.methods.*;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.message.StatusLine;
import org.apache.hc.core5.io.CloseMode;
import org.apache.hc.core5.reactor.IOReactorConfig;
import org.apache.hc.core5.util.Timeout;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class MainService {
    static {
        instance = new MainService();
    }

    private MainService() {
    }

    private final static MainService instance;
    public static final String MAIN_STAGE = "mainStage";

    public static MainService getInstance() {
        return instance;
    }

    private MainController controller;

    public void chooseDirectory() throws IOException {
        DirectoryAddService.getInstance().showStage();
    }

    public void saveExcelPath(String text, String path) {
        ChoiceBox<ChoiceItem<String>> name = controller.name;
        ObservableList<ChoiceItem<String>> items = name.getItems();
        items.add(new ChoiceItem<>(text, path));
        name.getSelectionModel().select(items.size() - 1);
        save();
    }


    public void save() {
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
    public void init(MainController controller) {
        this.controller = controller;
        ChoiceBox<ChoiceItem<String>> name = controller.name;
        //数据读取 其余线程执行
        Platform.runLater(() -> {
            Path data = getPath();
            if (Files.exists(data)) {
                try (ObjectInputStream inputStream = new ObjectInputStream(Files.newInputStream(data));) {
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
        name.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ChoiceItem<String>>() {
            @Override
            public void changed(ObservableValue<? extends ChoiceItem<String>> observable, ChoiceItem<String> oldValue, ChoiceItem<String> newValue) {
                fileScan(newValue.getValue());
            }
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
    }

    private Path getPath() {
        return FileSystems.getDefault().getPath("data");
    }

    private void initFiles(List<String> collect) {
        controller.files.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        ObservableList<String> observableList = FXCollections.observableList(collect);
        FilteredList<String> strings = new FilteredList<>(observableList);
        strings.addListener(new ListChangeListener<String>() {
            @Override
                public void onChanged(Change<? extends String> c) {
                controller.files.getSelectionModel().selectFirst();
                }
        });
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

    public void openExcel() throws ExecutionException, InterruptedException {
        String fileName = controller.files.getSelectionModel().getSelectedItem();
        ChoiceItem<String> selectedItem = controller.name.getSelectionModel().getSelectedItem();
        String path = selectedItem.getValue();
        RunTimeExec.exec(path + File.separatorChar + fileName);
    }


}
