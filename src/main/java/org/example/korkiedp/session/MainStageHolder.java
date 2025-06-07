package org.example.korkiedp.session;

import javafx.stage.Stage;

public class MainStageHolder {
    private static Stage stage;

    public static void set(Stage s) {
        stage = s;
    }

    public static Stage get() {
        return stage;
    }
}