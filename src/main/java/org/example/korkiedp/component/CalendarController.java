package org.example.korkiedp.component;

import javafx.event.ActionEvent;
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

    private LocalDate currentDate;

    private Button selectedDayButton = null;

    @FXML
    private Button prevButton;

    @FXML
    private Button nextButton;


    @FXML
    public void initialize() {
        currentDate = LocalDate.now(); // Today
        drawCalendar(currentDate);
    }

    private void drawCalendar(LocalDate date) {
        calendarGrid.getChildren().clear();

        YearMonth yearMonth = YearMonth.from(date);
        monthLabel.setText(yearMonth.getMonth() + " " + yearMonth.getYear());

        LocalDate firstOfMonth = yearMonth.atDay(1);
        int startDayOfWeek = firstOfMonth.getDayOfWeek().getValue(); // Monday = 1
        int daysInMonth = yearMonth.lengthOfMonth();

        int row = 0;
        int col = startDayOfWeek - 1;

        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate dayDate = yearMonth.atDay(day);
            Button dayButton = createDayButton(dayDate);

            calendarGrid.add(dayButton, col, row);
            col++;
            if (col > 6) {
                col = 0;
                row++;
            }
        }
    }

    private Button createDayButton(LocalDate date) {
        Button dayButton = new Button(String.valueOf(date.getDayOfMonth()));
        dayButton.setPrefSize(40, 15);

        if (date.equals(LocalDate.now())) {
            selectedDayButton = dayButton;
            dayButton.getStyleClass().add("today");
            dayButton.getStyleClass().add("today-selected");
        }

        dayButton.setOnAction(e -> {
            if (selectedDayButton != null) {
                selectedDayButton.getStyleClass().remove("selected-day");
                selectedDayButton.getStyleClass().remove("today-selected");

            }
            selectedDayButton = dayButton;
            if (!selectedDayButton.getStyleClass().contains("selected-day")) {
                selectedDayButton.getStyleClass().add("selected-day");
            }
            if (selectedDayButton.getStyleClass().contains("selected-day") && selectedDayButton.getStyleClass().contains("today")) {
                selectedDayButton.getStyleClass().add("today-selected");
            }


            System.out.println("Clicked day: " + date);
//            loadLessonsForDate(date); // <- NEW
        });

        return dayButton;
    }


    @FXML
    private void handlePrevMonth() {
        currentDate = currentDate.minusMonths(1);
        drawCalendar(currentDate);
    }

    @FXML
    private void handleNextMonth() {
        currentDate = currentDate.plusMonths(1);
        drawCalendar(currentDate);
    }

    @FXML
    private void handleGoToToday() {
        if (currentDate.equals(LocalDate.now())) {
            return;
        }
        currentDate = LocalDate.now();
        drawCalendar(currentDate);
    }

//    private void loadLessonsForDate(LocalDate date) {
//        // Example only — replace with actual DAO call
//        List<Lesson> lessons = LessonDAO.findByTutorAndDate(CurrentSession.getCurrentTutorId(), date);
//
//        lessonsTable.getItems().clear();
//        lessonsTable.getItems().addAll(lessons);
//    }

}
