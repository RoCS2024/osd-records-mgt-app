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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class EditViolationController{
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
    private Violation violation;
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
    protected void saveUpdateClicked(ActionEvent event) {
        PrefectOfficeRecordMgtApplication app = new PrefectOfficeRecordMgtApplication();
        violationFacade = app.getViolationFacade();

        offenseFacade = app.getOffenseFacade();
        String selectedOffense = (String) offenseComboBox.getValue();
        Offense offense = offenseFacade.getOffenseByName(selectedOffense);

        StudentInfoMgtApplication appl = new StudentInfoMgtApplication();
        studentFacade = appl.getStudentFacade();
        Student student = studentFacade.getStudentByNumber(studentIdField.getText());


        EmployeeInfoMgtApplication ap = new EmployeeInfoMgtApplication();
        employeeFacade = ap.getEmployeeFacade();
        Employee employee = employeeFacade.getEmployeeById(employeeIdField.getText());

        Violation editViolation = new Violation();
        editViolation.setId(violation.getId());
        editViolation.setStudent(student);
        editViolation.setOffense(offense);
        editViolation.setApprovedBy(employee);

        LocalDate selectedDate = dateField.getValue();
        if (selectedDate != null) {
            try {
                LocalDateTime localDateTime = selectedDate.atStartOfDay();
                Timestamp timestamp = Timestamp.valueOf(localDateTime);
                editViolation.setDateOfNotice(timestamp);
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid date format: " + selectedDate);
                e.printStackTrace();
            }
        } else {
            System.err.println("No date selected.");
        }

        editViolation.setWarningNum(Integer.parseInt(warningNumField.getText()));
        editViolation.setCommServHours(Integer.parseInt(csHoursField.getText()));
        editViolation.setDisciplinaryAction(disciplinaryField.getText());

        try {
//            System.out.println(editViolation.getId());
//            System.out.println(editViolation.getStudent().getFirstName());
//            System.out.println(editViolation.getOffense().getDescription());

            violationFacade.updateViolation(editViolation);
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

    public void setViolation(Violation violation) {
        this.violation = violation;
        violation.getId();
        studentIdField.setText(violation.getStudent().getStudentId());
        offenseComboBox.setValue(violation.getOffense().getDescription());

        //date picker format setter
        Timestamp offenseTimestamp = violation.getDateOfNotice();
        LocalDate offenseLocalDate = offenseTimestamp.toLocalDateTime().toLocalDate();
        dateField.setValue(offenseLocalDate);

        warningNumField.setText(String.valueOf(violation.getWarningNum()));
        csHoursField.setText(String.valueOf(violation.getCommServHours()));
        disciplinaryField.setText(violation.getDisciplinaryAction());
        employeeIdField.setText(violation.getApprovedBy().getEmployeeNo());
    }

    @FXML
    protected void handleCancelEditViolation(MouseEvent event) {
        try {
            Stage previousStage4 = (Stage) ((Node) event.getSource()).getScene().getWindow();
            previousStage4.close();

            Stage dashboardStage4 = new Stage();
            FXMLLoader loader4 = new FXMLLoader();
            loader4.setLocation(getClass().getResource("/views/ViolationList.fxml"));
            Parent root4 = loader4.load();
            Scene scene4 = new Scene(root4);
            dashboardStage4.setScene(scene4);
            dashboardStage4.show();
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