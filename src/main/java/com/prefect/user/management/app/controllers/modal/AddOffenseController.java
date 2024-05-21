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
    protected void saveAddOffenseClicked(ActionEvent event) {
        PrefectOfficeRecordMgtApplication app = new PrefectOfficeRecordMgtApplication();
        offenseFacade = app.getOffenseFacade();

        Offense addOffense = new Offense();
        addOffense.setDescription(offenseField.getText());
        addOffense.setType(comboBox.getValue());


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
}
