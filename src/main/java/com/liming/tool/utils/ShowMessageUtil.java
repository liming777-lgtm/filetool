package com.liming.tool.utils;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.stage.Popup;
import javafx.stage.Window;
import javafx.util.Duration;

public class ShowMessageUtil {
    private final static Timeline timeline = new Timeline();
    private final static Popup popup = new Popup();
    private final static Label label = new Label();

    static {
        ObservableList<KeyFrame> keyFrames = timeline.getKeyFrames();
        keyFrames.add(new KeyFrame(Duration.seconds(1), event -> popup.hide()));
        popup.getContent().add(label);
    }
    public static void showInfo(Window window, String message) {
        label.getStyleClass().add("message");
        label.setText(message);
        //计算显示位置
        double width = window.getWidth();
        double height = window.getHeight();
        label.setPrefWidth(width / 2);
        label.setPrefHeight(height / 10);
        popup.setAnchorX(window.getX() + label.getPrefWidth() / 2);
        popup.setAnchorY(window.getY() + height / 20);
        popup.show(window);
        timeline.play();
    }

}
