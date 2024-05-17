package com.prefect.user.management.app.controllers.modal;

import com.prefect.office.record.management.PrefectOfficeRecordMgtApplication;
import com.prefect.office.record.management.appl.facade.prefect.offense.OffenseFacade;
import com.prefect.office.record.management.appl.facade.prefect.offense.impl.OffenseFacadeImpl;
import com.prefect.office.record.management.appl.facade.prefect.violation.ViolationFacade;
import com.prefect.office.record.management.appl.facade.prefect.violation.impl.ViolationFacadeImpl;
import com.prefect.office.record.management.appl.model.offense.Offense;
import com.prefect.office.record.management.appl.model.violation.Violation;
import com.prefect.office.record.management.data.dao.prefect.offense.OffenseDao;
import com.prefect.office.record.management.data.dao.prefect.offense.impl.OffenseDaoImpl;
import com.student.information.management.StudentInfoMgtApplication;
import com.student.information.management.appl.facade.student.StudentFacade;
import com.student.information.management.appl.facade.student.impl.StudentFacadeImpl;
import com.student.information.management.appl.model.student.Student;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AddOffenseController {
    @FXML
    private TextField studentIdField;

    @FXML
    private TextField violationField;

    @FXML
    private DatePicker offenseDateField;

    @FXML
    private TextField studentNameField;

    @FXML
    private ComboBox violationComboBox;

    private OffenseFacade offenseFacade;

    private ViolationFacade violationFacade;

    private StudentFacade studentFacade;
    @FXML
    public void initialize() {
        PrefectOfficeRecordMgtApplication appl = new PrefectOfficeRecordMgtApplication();
        violationFacade = appl.getViolationFacade();

        // Retrieve all violations from the database
        List<Violation> violations = violationFacade.getAllViolation();

        // Extract violation names from violations
        List<String> violationNames = violations.stream()
                .map(Violation::getViolation)
                .collect(Collectors.toList());

        // Populate the ComboBox with violation names
        violationComboBox.getItems().addAll(violationNames);

        saveButton.disableProperty().bind(studentIdField.textProperty().isEmpty()
                .or(violationComboBox.valueProperty().isNull())
                .or(offenseDateField.valueProperty().isNull())
        );
    }
    }


    @FXML
    protected void saveAddClicked(ActionEvent event) {
        PrefectOfficeRecordMgtApplication app = new PrefectOfficeRecordMgtApplication();
        offenseFacade = app.getOffenseFacade();
        violationFacade = app.getViolationFacade();
        Violation violation = violationFacade.getViolationByName((String) violationComboBox.getValue());

        StudentInfoMgtApplication appl = new StudentInfoMgtApplication();
        studentFacade = appl.getStudentFacade();

        Student student = studentFacade.getStudentById(studentIdField.getText());

        Offense addOffense = new Offense();
        addOffense.setStudent(student);
        addOffense.setViolation(violation);

        if (!validateInput()) {
            showAlert("Error", "All fields are important. Please enter valid input.", Alert.AlertType.ERROR);
            return;
        }

        LocalDate selectedDate = offenseDateField.getValue();
        if (selectedDate != null) {
            try {
                LocalDateTime localDateTime = selectedDate.atStartOfDay();
                Timestamp timestamp = Timestamp.valueOf(localDateTime);
                addOffense.setOffenseDate(timestamp);
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid date format: " + selectedDate);
                e.printStackTrace();
            }
        } else {
            System.err.println("No date selected.");
        }

        try {
            offenseFacade.addOffense(addOffense);
        } catch(Exception ex) {
            ex.printStackTrace();;
        } finally {
            try {
                Stage previousStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                previousStage.close();

                Stage dashboardStage = new Stage();
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/views/OffenseList.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                dashboardStage.setScene(scene);

                dashboardStage.initStyle(StageStyle.UNDECORATED);

                dashboardStage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private boolean validateInput() {
        return !studentIdField.getText().isEmpty()
            && violationComboBox.getValue() != null
            && offenseDateField.getValue() != null;
}


    @FXML
    protected void handleCancelAddOffenseClicked(MouseEvent event) {
        try {
            Stage previousStage2 = (Stage) ((Node) event.getSource()).getScene().getWindow();
            previousStage2.close();

            Stage dashboardStage2 = new Stage();
            FXMLLoader loader2 = new FXMLLoader();
            loader2.setLocation(getClass().getResource("/views/OffenseList.fxml"));
            Parent root2 = loader2.load();
            Scene scene2 = new Scene(root2);
            dashboardStage2.setScene(scene2);
            dashboardStage2.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    protected void handleStudentIdChanged(KeyEvent event) {

        if(!studentIdField.getText().isEmpty()){
            StudentInfoMgtApplication appl = new StudentInfoMgtApplication();
            studentFacade = appl.getStudentFacade();
            Student student = studentFacade.getStudentById(studentIdField.getText());
            if (student != null) {
                String fullName = student.getLastName() + ", " + student.getFirstName() + " " + student.getMiddleName();
                studentNameField.setText(fullName);
            } else {
                studentNameField.clear();
            }
        }
    }


