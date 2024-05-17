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
        if (!areFieldsValid()) {
            showAlert("All fields are important. Please enter valid input.");
            return;
        }
        PrefectOfficeRecordMgtApplication app = new PrefectOfficeRecordMgtApplication();
        violationFacade = app.getViolationFacade();

        Violation addViolation = new Violation();
        addViolation.setViolation(violationField.getText());
        addViolation.setType(comboBox.getValue());

        addViolation.setCommServHours(Integer.parseInt(commServHours.getText()));

        String violationName = violationField.getText();
        String violationType = comboBox.getValue();
        int violationCsHour = Integer.parseInt(commServHours.getText());

        try {
            violationFacade.addViolation(violationName, violationType, violationCsHour);
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
    private boolean areFieldsValid() {
        return violationField.getText() != null && !violationField.getText().isEmpty()
                && commServHours.getText() != null && !commServHours.getText().isEmpty()
                && comboBox.getValue() != null && !comboBox.getValue().isEmpty();
    }
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid Input");
        alert.setHeaderText(null);
        alert.setContentText(message);
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
