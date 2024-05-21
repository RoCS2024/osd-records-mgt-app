package com.prefect.user.management.app.controllers.search;

import com.prefect.office.record.management.PrefectOfficeRecordMgtApplication;
import com.prefect.office.record.management.appl.facade.prefect.communityservice.CommunityServiceFacade;
import com.prefect.office.record.management.appl.facade.prefect.violation.ViolationFacade;
import com.prefect.office.record.management.appl.facade.prefect.violation.impl.ViolationFacadeImpl;
import com.prefect.office.record.management.appl.model.communityservice.CommunityService;
import com.prefect.office.record.management.appl.model.violation.Violation;
import com.prefect.user.management.app.controllers.modal.EditViolationController;
import com.student.information.management.appl.model.student.Student;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class SearchViolationController implements Initializable {
    @FXML
    private TextField totalField;

    @FXML
    private TextField remainingField;
    @FXML
    private TableView<Violation> tableView;

    @FXML
    private Button previousButton;
    @FXML
    private Button renderBtn;
    private ViolationFacade violationFacade;
    private CommunityServiceFacade communityServiceFacade;
    private Student student;

    public void initData(Student student) {
        this.student = student;
        System.out.println("student data passed: " + student.getStudentId());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        PrefectOfficeRecordMgtApplication app = new PrefectOfficeRecordMgtApplication();
        violationFacade = app.getViolationFacade();
        communityServiceFacade = app.getCommunityserviceFacade();

        previousButton.setOnAction(event -> {handleBack2Previous((ActionEvent) event);});
        renderBtn.setOnAction(event -> {handleSubmitRenderCSButton((ActionEvent) event);});
        System.out.println("student data passed: " + student.getStudentId());

        tableView.getItems().clear();
        if (student != null) {

            List<Violation> studentViolations = violationFacade.getAllViolationByStudent(student);

            // Compute total comm serv hours
            int totalCommServHours = computeTotalCommServHours(studentViolations);
            totalField.setText(String.valueOf(totalCommServHours));

            List<CommunityService> communityServiceByStudId = communityServiceFacade.getAllCsByStudent(student);

            int totalHoursRendered = computeTotalHoursRendered(communityServiceByStudId);
            int remainingHours = totalCommServHours - totalHoursRendered;
            remainingField.setText(String.valueOf(remainingHours));

            if (remainingHours <= 0) {
                remainingField.setText(String.valueOf("0"));
                renderBtn.setDisable(true);
            }
            // Populate the TableView
            ObservableList<Violation> data = FXCollections.observableArrayList(studentViolations);
            tableView.setItems(data);
        } else {
            System.out.println("No student data passed.");
        }

        TableColumn<Violation, String> studIdColumn = new TableColumn<>("STUDENT ID");
        studIdColumn.setCellValueFactory(cellData -> {
            String studentId = cellData.getValue().getStudent().getStudentId();
            return new SimpleStringProperty(studentId);
        });
        studIdColumn.getStyleClass().addAll("student-column");

        TableColumn<Violation, String> studColumn = new TableColumn<>("NAME");
        studColumn.setCellValueFactory(cellData -> {
            String firstName = cellData.getValue().getStudent().getFirstName();
            String lastName = cellData.getValue().getStudent().getLastName();
            return new SimpleStringProperty(firstName + " " + lastName);
        });
        studColumn.getStyleClass().addAll("student-column");

        TableColumn<Violation, String> offenseColumn = new TableColumn<>("OFFENSE");
        offenseColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOffense().getDescription()));
        offenseColumn.getStyleClass().addAll("offense-id-column");

        TableColumn<Violation, Integer> warningColumn = new TableColumn<>("WARNING NO.");
        warningColumn.setCellValueFactory(new PropertyValueFactory<>("warningNum"));
        warningColumn.getStyleClass().addAll("warning-column");

        TableColumn<Violation, Integer> csHoursColumn = new TableColumn<>("CS HOURS");
        csHoursColumn.setCellValueFactory(new PropertyValueFactory<>("commServHours"));
        csHoursColumn.getStyleClass().addAll("cs-hours-column");

        TableColumn<Violation, String> disciplinaryColumn = new TableColumn<>("DISCIPLINARY ACTION");
        disciplinaryColumn.setCellValueFactory(new PropertyValueFactory<>("disciplinaryAction"));
        disciplinaryColumn.getStyleClass().addAll("disciplinary-column");

        TableColumn<Violation, Timestamp> dateColumn = new TableColumn<>("DATE OF NOTICE");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateOfNotice"));
        dateColumn.getStyleClass().addAll("date-column");
        dateColumn.setCellFactory(getDateCellFactory());

        TableColumn<Violation, String> approvedByColumn = new TableColumn<>("APPROVED BY");
        approvedByColumn.setCellValueFactory(cellData -> {
            String firstName = cellData.getValue().getApprovedBy().getFirstName();
            String lastName = cellData.getValue().getApprovedBy().getLastName();
            return new SimpleStringProperty(firstName + " " + lastName);
        });
        approvedByColumn.getStyleClass().addAll("approvedBy-column");

        TableColumn<Violation, String> actionColumn = new TableColumn<>("ACTION");
        actionColumn.setCellValueFactory(new PropertyValueFactory<>(""));
        actionColumn.getStyleClass().addAll("action-column");
        actionColumn.setCellFactory(cell -> {
            final Button editButton_2 = new Button();
            TableCell<Violation, String> cellInstance = new TableCell<>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        editButton_2.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/assets/pencil.png"))));
                        editButton_2.setOnAction(event -> {
                            Violation violation = getTableView().getItems().get(getIndex());
                            showEditViolation(violation, (ActionEvent) event);
                        });
                        HBox hbox = new HBox(editButton_2);
                        hbox.setSpacing(10);
                        hbox.setAlignment(Pos.BASELINE_CENTER);
                        setGraphic(hbox);
                        setText(null);
                    }
                }
            };
            return cellInstance;
        });

        tableView.getColumns().addAll(studIdColumn, studColumn, offenseColumn , warningColumn, csHoursColumn, disciplinaryColumn, dateColumn, approvedByColumn, actionColumn);
    }


    private int computeTotalCommServHours(List<Violation> studentViolations) {
        int totalCommServHours = 0;
        for (Violation violation : studentViolations) {
            totalCommServHours += violation.getCommServHours();
        }
        return totalCommServHours;
    }

    private int computeTotalHoursRendered(List<CommunityService> communityServiceByStudId) {
        int totalHoursRendered = 0;
        for (CommunityService communityService : communityServiceByStudId) {
            totalHoursRendered += communityService.getHours_rendered();
        }
        return totalHoursRendered;
    }


    //change the offense date column
    private Callback<TableColumn<Violation, Timestamp>, TableCell<Violation, Timestamp>> getDateCellFactory() {
        return column -> new TableCell<>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            @Override
            protected void updateItem(Timestamp item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    LocalDate date = item.toLocalDateTime().toLocalDate();
                    setText(formatter.format(date));
                }
            }
        };
    }

    @FXML
    protected void handleSubmitRenderCSButton(ActionEvent event) {
        Stage stage3 = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage3.hide();
        showDashboard3();
    }

    private void showDashboard3() {
        try {
            Stage dashboardStage3 = new Stage();
            dashboardStage3.initStyle(StageStyle.UNDECORATED);

            FXMLLoader loader3 = new FXMLLoader();
            loader3.setLocation(getClass().getResource("/views/RenderCsByStudent.fxml"));
            Parent root3 = loader3.load();
            Scene scene3 = new Scene(root3);
            dashboardStage3.setScene(scene3);
            dashboardStage3.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showEditViolation(Violation violation, ActionEvent event) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();

            Stage editStage = new Stage();
            editStage.initStyle(StageStyle.UNDECORATED);

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/EditViolation.fxml"));
            AnchorPane editLayout = new AnchorPane();
            editLayout = loader.load();
            EditViolationController editViolationController = loader.getController();
            editViolationController.setViolation(violation);
            Scene scene = new Scene(editLayout);
            editStage.setScene(scene);
            editStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void handleBack2Previous(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ViolationList.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
