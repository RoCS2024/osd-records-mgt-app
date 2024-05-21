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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class EditOffenseController implements Initializable {
    @FXML
    private TextField offenseField;

    @FXML
    private ComboBox<String> comboBox;

    private Offense offense;

    private OffenseFacade offenseFacade;

    @FXML
    protected void saveEditOffenseClicked(ActionEvent event) {
        PrefectOfficeRecordMgtApplication app = new PrefectOfficeRecordMgtApplication();
        offenseFacade = app.getOffenseFacade();

        Offense editOffense = new Offense();
        editOffense.setId(offense.getId());
        editOffense.setType(comboBox.getValue());
        editOffense.setDescription(offenseField.getText());

//        System.out.println(offense.getId());
//        System.out.println(offense.getType());
//        System.out.println(offense.getDescription());

        try {
//            System.out.println(editOffense.getId());
//            System.out.println(editOffense.getType());
//            System.out.println(editOffense.getDescription());
            offenseFacade.updateOffense(editOffense);
        } catch(Exception ex) {
            ex.printStackTrace();;
        }
        finally {
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        comboBox.getItems().addAll("Minor", "Major");
    }

    public void setOffense(Offense offense) {
        this.offense = offense;
        offense.getId();
        comboBox.setValue(offense.getType());
        offenseField.setText(offense.getDescription());
    }

    @FXML
    protected void handleCancelEditOffenseClicked(MouseEvent event) {
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
