package org.example.korkiedp.controller;

import javafx.event.ActionEvent;
import org.example.korkiedp.service.SceneSwitcherService;

public class StudentsController {


    public void handleGoBack(ActionEvent event) {
        SceneSwitcherService.loadContentLayer("/main_panel.fxml");
    }


}
