package org.example.korkiedp.service;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import org.example.korkiedp.app.MainStageHolder;


import java.io.IOException;
import java.util.Objects;
import java.util.function.Consumer;

public class SceneSwitcherService {

//    public static void loadView(String fxmlPath, Pane container) {
//        try {
//            Parent view = FXMLLoader.load(Objects.requireNonNull(SceneSwitcherService.class.getResource(fxmlPath)));
//            if (container instanceof AnchorPane) {
//                AnchorPane.setTopAnchor(view, 0.0);
//                AnchorPane.setBottomAnchor(view, 0.0);
//                AnchorPane.setLeftAnchor(view, 0.0);
//                AnchorPane.setRightAnchor(view, 0.0);
//            }
//
//            container.getChildren().setAll(view);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void loadMainPanel(String fxmlPath) {
//        loadView(fxmlPath, MainStageHolder.getMainPanelPane());
//    }
//    public static void loadContentLayer(String fxmlPath) {
//        loadView(fxmlPath, MainStageHolder.getContentLayer());
//    }
//}

    public static <T> void loadView(String fxmlPath, Pane container, Consumer<T> controllerInitializer) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneSwitcherService.class.getResource(fxmlPath));
            Parent view = loader.load();

            if (container instanceof AnchorPane) {
                AnchorPane.setTopAnchor(view, 0.0);
                AnchorPane.setBottomAnchor(view, 0.0);
                AnchorPane.setLeftAnchor(view, 0.0);
                AnchorPane.setRightAnchor(view, 0.0);
            }

            T controller = loader.getController();
            controllerInitializer.accept(controller);

            container.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadMainPanel(String fxmlPath) {
        Platform.runLater(() -> {
            loadView(fxmlPath, MainStageHolder.getMainPanelPane(), c -> {});
        });
    }

    public static <T> void loadMainPanel(String fxmlPath, Consumer<T> controllerInitializer) {
        Platform.runLater(() -> {
            loadView(fxmlPath, MainStageHolder.getMainPanelPane(), controllerInitializer);
        });
    }

    public static void loadContentLayer(String fxmlPath) {
        Platform.runLater(() -> {
            loadView(fxmlPath, MainStageHolder.getContentLayer(), c -> {});
        });
    }
}

