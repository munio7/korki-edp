package org.example.korkiedp.component;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;
import java.time.YearMonth;

public class CalendarController {

    @FXML
    private Label monthLabel;

    @FXML
    private GridPane calendarGrid;

    private YearMonth currentMonth;

    @FXML
    public void initialize() {
        currentMonth = YearMonth.now();
        drawCalendar(currentMonth);
    }

    private void drawCalendar(YearMonth month) {
        calendarGrid.getChildren().clear();
        monthLabel.setText(month.getMonth() + " " + month.getYear());

        LocalDate firstDay = month.atDay(1);
        int startDayOfWeek = firstDay.getDayOfWeek().getValue(); // 1 = Monday
        int daysInMonth = month.lengthOfMonth();

        int row = 0;
        int col = startDayOfWeek - 1;

        for (int day = 1; day <= daysInMonth; day++) {
            Button dayButton = new Button(String.valueOf(day));
            dayButton.setPrefSize(40, 40);

            int finalDay = day;
            dayButton.setOnAction(e -> {
                System.out.println("Clicked day: " + finalDay);
            });

            calendarGrid.add(dayButton, col, row);
            col++;
            if (col > 6) {
                col = 0;
                row++;
            }
        }
    }
}
