package com.liming.tool.bean;

import com.liming.tool.impl.DataInit;
import javafx.stage.Stage;

public class StageController {
    private final Stage stage;
    private final DataInit dataInit;

    public StageController(Stage stage, DataInit dataInit) {
        this.stage = stage;
        this.dataInit = dataInit;
    }

    public Stage getStage() {
        return stage;
    }

    public DataInit getDataInit() {
        return dataInit;
    }
}
