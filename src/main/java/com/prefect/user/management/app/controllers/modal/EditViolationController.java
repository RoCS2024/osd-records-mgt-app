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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.ResourceBundle;

public class EditViolationController implements Initializable {
    @FXML
    private TextField violationIdField;

    @FXML
    private TextField violationField;

    @FXML
    private TextField commServHours;

    @FXML
    private ComboBox<String> comboBox;

    private Violation violation;

    private ViolationFacade violationFacade;

    @FXML
    protected void saveEditViolationClicked(ActionEvent event) {
        String violationId = violationIdField.getText().trim();
        String violationText = violationField.getText().trim();
        String commServHoursText = commServHours.getText().trim();
        String comboBoxValue = comboBox.getValue();

        if (violationId.isEmpty() || violationText.isEmpty() || commServHoursText.isEmpty() || comboBoxValue == null) {
            showAlert("Invalid Input", "All fields are important. Please enter valid input.");
            return;
        }

        PrefectOfficeRecordMgtApplication app = new PrefectOfficeRecordMgtApplication();
        violationFacade = app.getViolationFacade();

        Violation editViolation = new Violation();

        editViolation.setId(Integer.parseInt(violationIdField.getText()));
        editViolation.setViolation(violationField.getText());
        editViolation.setType(comboBox.getValue());
        editViolation.setCommServHours(Integer.parseInt(commServHours.getText()));

        try {
            violationFacade.updateViolation(editViolation);
        } catch(Exception ex) {
            ex.printStackTrace();;
        }
        finally {
            try {
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        comboBox.getItems().addAll("Minor", "Major");
    }

    public void setViolation(Violation violation) {
        this.violation = violation;
        violationIdField.setText(String.valueOf(violation.getId()));
        violationField.setText(violation.getViolation());
        commServHours.setText(String.valueOf(violation.getCommServHours()));
    }

    @FXML
    protected void handleCancelEditViolationClicked(MouseEvent event) {
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
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}