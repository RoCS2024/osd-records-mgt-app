package com.prefect.user.management.app.controllers.modal;

import com.prefect.office.record.management.PrefectOfficeRecordMgtApplication;
import com.prefect.office.record.management.appl.facade.prefect.offense.OffenseFacade;
import com.prefect.office.record.management.appl.model.offense.Offense;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.ResourceBundle;

public class AddOffenseController  implements Initializable {
    @FXML
    private TextField offenseField;

    @FXML
    private ComboBox<String> comboBox;

    private OffenseFacade offenseFacade;

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
    protected void saveAddOffenseClicked(ActionEvent event) {
        PrefectOfficeRecordMgtApplication app = new PrefectOfficeRecordMgtApplication();
        offenseFacade = app.getOffenseFacade();

        Offense addOffense = new Offense();
        addOffense.setDescription(offenseField.getText());
        addOffense.setType(comboBox.getValue());

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
        }
        finally {
            try {
                //back to list after adding
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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        comboBox.getItems().addAll("Minor", "Major");
    }


    @FXML
    protected void handleCancelAddOffenseClicked(MouseEvent event) {
        try {
            Stage previousStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            previousStage.close();

            Stage dashboardStage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/OffenseList.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            dashboardStage.setScene(scene);
            dashboardStage.show();
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

