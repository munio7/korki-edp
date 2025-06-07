package org.example.korkiedp.app;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainStageHolder {
    private static Stage stage;
    private static AnchorPane popupLayer;
    private static AnchorPane contentLayer;
    private static StackPane mainPanelPane;

    public static void set(Stage s, AnchorPane popup, AnchorPane content) {
        stage = s;
        popupLayer = popup;
        contentLayer = content;
    }

    public static Stage get() { return stage; }
    public static AnchorPane getPopupLayer() { return popupLayer; }
    public static AnchorPane getContentLayer() { return contentLayer; }
    public static StackPane getMainPanelPane() { return mainPanelPane; }

    public static void setMainPanelPane(StackPane mainContent) {
        mainPanelPane = mainContent;
    }
}
