package org.example.korkiedp.controller;

import javafx.event.ActionEvent;
import org.example.korkiedp.service.SceneSwitcherService;

public class CalendarController {
    public void handleGoBack(ActionEvent event) {
        SceneSwitcherService.loadContentLayer("/main_panel.fxml");
    }
}
