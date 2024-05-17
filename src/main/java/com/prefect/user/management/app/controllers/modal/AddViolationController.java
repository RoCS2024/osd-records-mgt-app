package com.prefect.user.management.app.controllers.modal;

import com.prefect.office.record.management.PrefectOfficeRecordMgtApplication;
import com.prefect.office.record.management.appl.facade.prefect.violation.ViolationFacade;
import com.prefect.office.record.management.appl.facade.prefect.violation.impl.ViolationFacadeImpl;
import com.prefect.office.record.management.appl.model.violation.Violation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import javafx.scene.control.ComboBox;
import javafx.stage.StageStyle;

public class AddViolationController implements Initializable {
    @FXML
    private TextField violationField;

    @FXML
    private TextField commServHours;

    @FXML
    private ComboBox<String> comboBox;

    private ViolationFacade violationFacade;

    @FXML
    protected void saveAddViolationClicked(ActionEvent event) {
        String violationName = violationField.getText().trim();
        String violationType = comboBox.getValue();
        String commServHoursText = commServHours.getText().trim();

        if (violationName.isEmpty() || violationType == null || commServHoursText.isEmpty()) {
            showAlert("Error", "All fields are important. Please enter valid input.", Alert.AlertType.ERROR);
            return;
        }

        if (!validateNumericInput(commServHoursText)) {
            showAlert("Error", "Community service hours must be a numeric value.", Alert.AlertType.ERROR);
            return;
        }

        int violationCsHour = Integer.parseInt(commServHoursText);

        PrefectOfficeRecordMgtApplication app = new PrefectOfficeRecordMgtApplication();
        violationFacade = app.getViolationFacade();

        Violation addViolation = new Violation();
        addViolation.setViolation(violationName);
        addViolation.setType(violationType);
        addViolation.setCommServHours(violationCsHour);

        try {
            violationFacade.addViolation(violationName, violationType, violationCsHour);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        finally {
            try {
                //back to list after adding
                Stage previousStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                previousStage.close();

                Stage dashboardStage = new Stage();
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/views/ViolationList.fxml"));
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
    private boolean validateInput(String input) {
        return Pattern.matches("[a-zA-Z0-9]+", input);
    }

    private boolean validateNumericInput(String input) {
        return Pattern.matches("\\d+", input);
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        comboBox.getItems().addAll("Minor", "Major");
    }


    @FXML
    protected void handleCancelAddViolationClicked(MouseEvent event) {
        try {
            Stage previousStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            previousStage.close();

            Stage dashboardStage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/ViolationList.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            dashboardStage.setScene(scene);
            dashboardStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

