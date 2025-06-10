package org.example.korkiedp.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import org.example.korkiedp.model.Lesson;

public class StartMenuController {
    @FXML private TableView<Lesson> lessonsTable;
    @FXML private VBox formBox;
    @FXML private Label formMessageLabel;

    @FXML private Label infoLabel1;
    @FXML private Label infoLabel2;
    @FXML private Label infoLabel3;

    @FXML private Button actionButton1;
    @FXML private Button actionButton2;
    @FXML private Button actionButton3;

    @FXML
    public void initialize() {
        // tu możesz ładować dane i reagować na kliknięcie w tabeli
    }
}
