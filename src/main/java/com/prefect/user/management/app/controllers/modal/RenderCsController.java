package com.prefect.user.management.app.controllers.modal;

import com.prefect.office.record.management.appl.facade.prefect.communityservice.CommunityServiceFacade;
import com.prefect.office.record.management.appl.facade.prefect.communityservice.impl.CommunityServiceFacadeImpl;
import com.prefect.office.record.management.appl.facade.prefect.offense.OffenseFacade;
import com.prefect.office.record.management.appl.facade.prefect.offense.impl.OffenseFacadeImpl;
import com.prefect.office.record.management.appl.model.communityservice.CommunityService;
import com.prefect.office.record.management.appl.model.offense.Offense;
import com.prefect.office.record.management.data.dao.prefect.communityservice.CommunityServiceDao;
import com.prefect.office.record.management.data.dao.prefect.communityservice.impl.CommunityServiceDaoImpl;
import com.prefect.office.record.management.data.dao.prefect.offense.OffenseDao;
import com.prefect.office.record.management.data.dao.prefect.offense.impl.OffenseDaoImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class RenderCsController {
    @FXML
    private TextField offenseIdField;

    @FXML
    private TextField studentIdField;

    @FXML
    private DatePicker dateRenderedField;

    @FXML
    private TextField hoursRenderedField;

    @FXML
    private Button saveRenderButton;
    private CommunityServiceFacade communityServiceFacade;

    private OffenseFacade offenseFacade;

    //click save button to save rendered cs to database
    @FXML
    protected void saveRenderClicked(ActionEvent event) {
        OffenseDao offenseDao = new OffenseDaoImpl();
        offenseFacade = new OffenseFacadeImpl(offenseDao);

        CommunityServiceDao communityServiceDao = new CommunityServiceDaoImpl();
        communityServiceFacade = new CommunityServiceFacadeImpl(communityServiceDao);

        Offense existingOffense = offenseFacade.getOffenseByID(Integer.parseInt(offenseIdField.getText()));
        if (existingOffense != null) {
            CommunityService renderCs = new CommunityService();
            renderCs.setStudent_id(studentIdField.getText());

            LocalDate selectedDate = dateRenderedField.getValue();
            if (selectedDate != null) {
                try {
                    LocalDateTime localDateTime = selectedDate.atStartOfDay();
                    Timestamp timestamp = Timestamp.valueOf(localDateTime);
                    renderCs.setDate_rendered(timestamp);
                } catch (IllegalArgumentException e) {
                    System.err.println("Invalid date format: " + selectedDate);
                    e.printStackTrace();
                }
            } else {
                System.err.println("No date selected.");
            }

            renderCs.setHours_rendered(Integer.parseInt(hoursRenderedField.getText()));

            try {
                communityServiceFacade.renderCs(renderCs);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Rendering Success", "Community service rendered successfully.");
            } catch(Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "Rendering Error", "An error occurred while rendering community service.");
                ex.printStackTrace();
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
        } else {
            showAlert(Alert.AlertType.WARNING, "Warning", "Offense ID Not Found", "Offense with ID " + Integer.parseInt(offenseIdField.getText()) + " does not exist.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    @FXML
    protected void handleCancelRenderCSClicked(MouseEvent event) {
        try {
            Stage previousStage3 = (Stage) ((Node) event.getSource()).getScene().getWindow();
            previousStage3.close();

            Stage dashboardStage3 = new Stage();
            FXMLLoader loader3 = new FXMLLoader();
            loader3.setLocation(getClass().getResource("/views/OffenseList.fxml"));
            Parent root3 = loader3.load();
            Scene scene3 = new Scene(root3);
            dashboardStage3.setScene(scene3);
            dashboardStage3.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
