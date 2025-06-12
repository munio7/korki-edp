package org.example.korkiedp.controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.korkiedp.async.BackgroundWorker;
import org.example.korkiedp.dao.LessonDAO;
import org.example.korkiedp.dao.TutorStudentDAO;
import org.example.korkiedp.events.EventBus;
import org.example.korkiedp.events.LessonsForStudentEvent;
import org.example.korkiedp.model.Lesson;
import org.example.korkiedp.model.TutorStudent;
import org.example.korkiedp.session.CurrentSession;

import java.math.BigDecimal;
import java.util.List;

public class PaymentsController {

    @FXML public TableView<TutorStudent> studentsTable;
    @FXML public TableColumn<TutorStudent,String> studentNameColumn;
    @FXML public TableView<Lesson> lessonsTable;
    @FXML public TableColumn<Lesson,String> dateColumn;
    @FXML public TableColumn<Lesson,String> timeColumn;
    @FXML public TableColumn<Lesson,String> priceColumn;
    @FXML public TableColumn<Lesson,String> subjectColumn;
    @FXML public TableColumn<Lesson,String> statusColumn;
    @FXML  public TableColumn<Lesson,String> attendanceColumn;

    private final ObservableList<TutorStudent> studentList = FXCollections.observableArrayList();
    private final ObservableList<Lesson> lessonList = FXCollections.observableArrayList();
    public Label paidSumLabel;
    public Label toPaySumLabel;

    @FXML
    public void initialize() {
        lessonsTable.setPlaceholder(new Label("Wybierz ucznia, aby wyświetlić płatności"));
        studentsTable.setPlaceholder(new Label("Brak uczniów. Zawrzyj relację."));
        studentsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                EventBus.publish(new LessonsForStudentEvent(newSelection));
            }
        });
        EventBus.subscribe(LessonsForStudentEvent.class, (event) -> loadLessonsForStudent(event));
        setupStudentColumns();
        setupLessonColumns();
        BackgroundWorker.submit(() -> loadStudents());
    }

    private void setupStudentColumns() {
        studentNameColumn.setCellValueFactory(new PropertyValueFactory<>("default_name"));
    }

    private void setupLessonColumns() {

        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        subjectColumn.setCellValueFactory(new PropertyValueFactory<>("subject"));
        statusColumn.setCellValueFactory(cellData -> {
            Lesson lesson = cellData.getValue();
            Boolean canceled = lesson.isCanceled();
            Boolean paid = lesson.isPaid();
            String status;

            if (Boolean.TRUE.equals(canceled)) status = "Anulowana";
            else if (Boolean.TRUE.equals(paid))  status = "Opłacona";
            else status = "Nieopłacona";

            return new SimpleStringProperty(status);
        });
        attendanceColumn.setCellValueFactory(cellValue -> {
            if(Boolean.TRUE.equals(cellValue.getValue().getAttendance())){
                return new SimpleStringProperty("Tak");
            }
            return new SimpleStringProperty("Nie");
        });

        lessonsTable.setItems(lessonList);
    }

    private void loadStudents() {
        List<TutorStudent> result = TutorStudentDAO.findByTutorId(CurrentSession.getTutorId());
        Platform.runLater(() -> {
            studentList.setAll(result);
            studentsTable.setItems(studentList);
        });
    }
    public void loadLessonsForStudent(LessonsForStudentEvent event) {
        BackgroundWorker.submit(() -> {
            int tutorId = event.getStudent().getTutorId();
            int studentId = event.getStudent().getStudentId();

            List<Lesson> lessons = LessonDAO.findByTutorAndStudentIdFinished(tutorId, studentId);
            BigDecimal paidSum = LessonDAO.sumPaidFinishedLessons(tutorId, studentId);
            BigDecimal unpaidSum = LessonDAO.sumUnpaidFinishedLessons(tutorId, studentId);

            Platform.runLater(() -> {
                if(lessons.isEmpty())
                    lessonsTable.setPlaceholder(new Label("Brak zakończonych lekcji dla wybranego ucznia."));
                else
                    lessonsTable.setPlaceholder(new Label("Wybierz ucznia, aby wyświetlić płatności"));
                lessonList.setAll(lessons);
                paidSumLabel.setText("Suma wpływów: " + paidSum + " zł");
                toPaySumLabel.setText("Do opłacenia: " + unpaidSum + " zł");
            });
        });
    }
}
