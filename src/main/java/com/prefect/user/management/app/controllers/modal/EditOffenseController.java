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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class EditOffenseController {
    @FXML
    private TextField offenseIdField;

    @FXML
    private TextField studentIdField;

    @FXML
    private TextField violationIdField;

    @FXML
    private DatePicker offenseDateField;

    private Offense offense;

    private OffenseFacade offenseFacade;

    private ViolationFacade violationFacade;

    private StudentFacade studentFacade;

    @FXML
    protected void saveUpdateClicked(ActionEvent event) {
        PrefectOfficeRecordMgtApplication app = new PrefectOfficeRecordMgtApplication();
        offenseFacade = app.getOffenseFacade();
        violationFacade = app.getViolationFacade();

        Violation violation = violationFacade.getViolationByID(Integer.parseInt(violationIdField.getText()));

        StudentInfoMgtApplication appl = new StudentInfoMgtApplication();
        studentFacade = appl.getStudentFacade();

        Student student = studentFacade.getStudentById(studentIdField.getText());

        Offense editOffense = new Offense();
        editOffense.setId(Integer.parseInt(offenseIdField.getText()));
        editOffense.setStudent(student);
        editOffense.setViolation(violation);

        LocalDate selectedDate = offenseDateField.getValue();
        if (selectedDate != null) {
            try {
                LocalDateTime localDateTime = selectedDate.atStartOfDay();
                Timestamp timestamp = Timestamp.valueOf(localDateTime);
                editOffense.setOffenseDate(timestamp);
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid date format: " + selectedDate);
                e.printStackTrace();
            }
        } else {
            System.err.println("No date selected.");
        }

        try {
            offenseFacade.updateOffense(editOffense);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        finally {
            //after save, go back to offense list
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

    public void setOffense(Offense offense) {
        this.offense = offense;
        offenseIdField.setText(String.valueOf(offense.getId()));

        studentIdField.setText(offense.getStudent().getStudentId());
        violationIdField.setText(String.valueOf(offense.getViolation().getId()));

        //date picker format setter
        Timestamp offenseTimestamp = offense.getOffenseDate();
        LocalDate offenseLocalDate = offenseTimestamp.toLocalDateTime().toLocalDate();
        offenseDateField.setValue(offenseLocalDate);
    }

    @FXML
    protected void handleCancelEditOffense(MouseEvent event) {
        try {
            Stage previousStage4 = (Stage) ((Node) event.getSource()).getScene().getWindow();
            previousStage4.close();

            Stage dashboardStage4 = new Stage();
            FXMLLoader loader4 = new FXMLLoader();
            loader4.setLocation(getClass().getResource("/views/OffenseList.fxml"));
            Parent root4 = loader4.load();
            Scene scene4 = new Scene(root4);
            dashboardStage4.setScene(scene4);
            dashboardStage4.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
