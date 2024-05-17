package com.prefect.user.management.app.controllers.modal;

import com.employee.info.mgmt.EmployeeInfoMgtApplication;
import com.employee.info.mgmt.appl.facade.employee.EmployeeFacade;
import com.employee.info.mgmt.appl.model.Employee;
import com.prefect.office.record.management.PrefectOfficeRecordMgtApplication;
import com.prefect.office.record.management.appl.facade.prefect.offense.OffenseFacade;
import com.prefect.office.record.management.appl.facade.prefect.violation.ViolationFacade;
import com.prefect.office.record.management.appl.model.offense.Offense;
import com.prefect.office.record.management.appl.model.violation.Violation;
import com.student.information.management.StudentInfoMgtApplication;
import com.student.information.management.appl.facade.student.StudentFacade;
import com.student.information.management.appl.model.student.Student;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javafx.scene.control.ComboBox;
import javafx.stage.StageStyle;

public class AddViolationController{
    @FXML
    private TextField studentIdField;
    @FXML
    private TextField studentNameField;
    @FXML
    private ComboBox offenseComboBox;
    @FXML
    private TextField warningNumField;
    @FXML
    private TextField csHoursField;
    @FXML
    private TextField disciplinaryField;
    @FXML
    private DatePicker dateField;
    @FXML
    private TextField employeeIdField;
    @FXML
    private TextField employeeNameField;

    private OffenseFacade offenseFacade;
    private ViolationFacade violationFacade;
    private StudentFacade studentFacade;
    private EmployeeFacade employeeFacade;

    @FXML
    public void initialize() {
        PrefectOfficeRecordMgtApplication appl = new PrefectOfficeRecordMgtApplication();
        offenseFacade = appl.getOffenseFacade();

        List<Offense> offenses = offenseFacade.getAllOffense();

        List<String> offenseName = offenses.stream()
                .map(Offense::getDescription)
                .collect(Collectors.toList());

        offenseComboBox.getItems().addAll(offenseName);
    }

    @FXML
    protected void saveAddClicked(ActionEvent event) {
        PrefectOfficeRecordMgtApplication app = new PrefectOfficeRecordMgtApplication();
        violationFacade = app.getViolationFacade();

        offenseFacade = app.getOffenseFacade();
        Offense offense = offenseFacade.getOffenseByName((String) offenseComboBox.getValue());

        StudentInfoMgtApplication appl = new StudentInfoMgtApplication();
        studentFacade = appl.getStudentFacade();
        Student student = studentFacade.getStudentByNumber(studentIdField.getText());

        EmployeeInfoMgtApplication ap = new EmployeeInfoMgtApplication();
        employeeFacade = ap.getEmployeeFacade();
        Employee employee = employeeFacade.getEmployeeById(employeeIdField.getText());

        Violation addViolation = new Violation();
        addViolation.setStudent(student);
        addViolation.setOffense(offense);
        addViolation.setApprovedBy(employee);

        LocalDate selectedDate = dateField.getValue();
        if (selectedDate != null) {
            try {
                LocalDateTime localDateTime = selectedDate.atStartOfDay();
                Timestamp timestamp = Timestamp.valueOf(localDateTime);
                addViolation.setDateOfNotice(timestamp);
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid date format: " + selectedDate);
                e.printStackTrace();
            }
        } else {
            System.err.println("No date selected.");
        }

        addViolation.setWarningNum(Integer.parseInt(warningNumField.getText()));
        addViolation.setCommServHours(Integer.parseInt(csHoursField.getText()));
        addViolation.setDisciplinaryAction(disciplinaryField.getText());

        try {
            violationFacade.addViolation(addViolation);
        } catch(Exception ex) {
            ex.printStackTrace();
        } finally {
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

    @FXML
    protected void handleCancelAddViolationClicked(MouseEvent event) {
        try {
            Stage previousStage2 = (Stage) ((Node) event.getSource()).getScene().getWindow();
            previousStage2.close();

            Stage dashboardStage2 = new Stage();
            FXMLLoader loader2 = new FXMLLoader();
            loader2.setLocation(getClass().getResource("/views/ViolationList.fxml"));
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
            Student student = studentFacade.getStudentByNumber(studentIdField.getText());
            if (student != null) {
                String fullName = student.getLastName() + ", " + student.getFirstName() + " " + student.getMiddleName();
                studentNameField.setText(fullName);
            } else {
                studentNameField.clear();
            }
        }
    }

    @FXML
    protected void handleEmployeeIdChanged(KeyEvent event) {

        if(!employeeIdField.getText().isEmpty()){
            EmployeeInfoMgtApplication appl = new EmployeeInfoMgtApplication();
            employeeFacade = appl.getEmployeeFacade();
            Employee employee = employeeFacade.getEmployeeById(employeeIdField.getText());
            if (employee != null) {
                String fullName = employee.getLastName() + ", " + employee.getFirstName() + " " + employee.getMiddleName();
                employeeNameField.setText(fullName);
            } else {
                employeeNameField.clear();
            }
        }
    }
}
