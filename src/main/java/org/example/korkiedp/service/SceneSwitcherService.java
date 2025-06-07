package org.example.korkiedp.service;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import org.example.korkiedp.app.MainStageHolder;


import java.io.IOException;
import java.util.Objects;

public class SceneSwitcherService {

    public static void loadView(String fxmlPath, Pane container) {
        try {
            Parent view = FXMLLoader.load(Objects.requireNonNull(SceneSwitcherService.class.getResource(fxmlPath)));
            if (container instanceof AnchorPane) {
                AnchorPane.setTopAnchor(view, 0.0);
                AnchorPane.setBottomAnchor(view, 0.0);
                AnchorPane.setLeftAnchor(view, 0.0);
                AnchorPane.setRightAnchor(view, 0.0);
            }

            container.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadMainPanel(String fxmlPath) {
        loadView(fxmlPath, MainStageHolder.getMainPanelPane());
    }
    public static void loadContentLayer(String fxmlPath) {
        loadView(fxmlPath, MainStageHolder.getContentLayer());
    }
}

