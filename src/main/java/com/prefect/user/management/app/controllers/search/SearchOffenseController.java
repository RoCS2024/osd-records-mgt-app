package com.prefect.user.management.app.controllers.search;

import com.prefect.office.record.management.PrefectOfficeRecordMgtApplication;
import com.prefect.office.record.management.appl.facade.prefect.communityservice.CommunityServiceFacade;
import com.prefect.office.record.management.appl.facade.prefect.offense.OffenseFacade;
import com.prefect.office.record.management.appl.facade.prefect.offense.impl.OffenseFacadeImpl;
import com.prefect.office.record.management.appl.model.communityservice.CommunityService;
import com.prefect.office.record.management.appl.model.offense.Offense;
import com.prefect.office.record.management.data.dao.prefect.offense.OffenseDao;
import com.prefect.office.record.management.data.dao.prefect.offense.impl.OffenseDaoImpl;
import com.prefect.user.management.app.controllers.modal.EditOffenseController;
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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SearchOffenseController implements Initializable {

    @FXML
    private TextField totalField;

    @FXML
    private TextField remainingField;
    @FXML
    private TableView<Offense> tableView;

    @FXML
    private Button previousButton;
    @FXML
    private Button renderBtn;
    private OffenseFacade offenseFacade;
    private CommunityServiceFacade communityServiceFacade;
    private Student student;

    public void initData(Student student) {
        this.student = student;
        System.out.println("student data passed: " + student.getStudentId());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        PrefectOfficeRecordMgtApplication app = new PrefectOfficeRecordMgtApplication();
        offenseFacade = app.getOffenseFacade();
        communityServiceFacade = app.getCommunityserviceFacade();

        previousButton.setOnAction(event -> {handleBack2Previous((ActionEvent) event);});
        renderBtn.setOnAction(event -> {handleSubmitRenderCSButton((ActionEvent) event);});
        System.out.println("student data passed: " + student.getStudentId());

        tableView.getItems().clear();
        if (student != null) {

            List<Offense> studentOffenses = offenseFacade.getAllOffenseByStudentId(student);

            // Compute total comm serv hours
            int totalCommServHours = computeTotalCommServHours(studentOffenses);
            totalField.setText(String.valueOf(totalCommServHours));

            List<CommunityService> communityServiceByStudId = communityServiceFacade.getAllCsByStudentId(student);

            int totalHoursRendered = computeTotalHoursRendered(communityServiceByStudId);
            int remainingHours = totalCommServHours - totalHoursRendered;
            remainingField.setText(String.valueOf(remainingHours));

            if (remainingHours <= 0) {
                remainingField.setText(String.valueOf("0"));
                renderBtn.setDisable(true);
            }
            // Populate the TableView
            ObservableList<Offense> data = FXCollections.observableArrayList(studentOffenses);
            tableView.setItems(data);
        } else {
            System.out.println("No student data passed.");
        }

        TableColumn<Offense, String> offenseIdColumn = new TableColumn<>("OFFENSE ID");
        offenseIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        offenseIdColumn.getStyleClass().addAll("offense-id-column");

        TableColumn<Offense, String> violationIdColumn = new TableColumn<>("VIOLATION");
        violationIdColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getViolation().getViolation()));
        violationIdColumn.getStyleClass().addAll("violation-id-column");

        TableColumn<Offense, String> studIdColumn = new TableColumn<>("STUDENT ID");
        studIdColumn.setCellValueFactory(cellData -> {
            String studentId = cellData.getValue().getStudent().getStudentId();
            return new SimpleStringProperty(studentId);
        });
        studIdColumn.getStyleClass().addAll("student-column");

        TableColumn<Offense, String> studColumn = new TableColumn<>("NAME");
        studColumn.setCellValueFactory(cellData -> {
            String firstName = cellData.getValue().getStudent().getFirstName();
            String lastName = cellData.getValue().getStudent().getLastName();
            return new SimpleStringProperty(firstName + " " + lastName);
        });
        studColumn.getStyleClass().addAll("student-column");

        TableColumn<Offense, Timestamp> offenseDateColumn = new TableColumn<>("OFFENSE DATE");
        offenseDateColumn.setCellValueFactory(new PropertyValueFactory<>("offenseDate"));
        offenseDateColumn.getStyleClass().addAll("date-column");
        offenseDateColumn.setCellFactory(getDateCellFactory());

        TableColumn<Offense, Integer> csHoursColumn = new TableColumn<>("CS HOURS");
        csHoursColumn.setCellValueFactory(new PropertyValueFactory<>("commServHours"));
        csHoursColumn.getStyleClass().addAll("cs-hours-column");

        TableColumn<Offense, String> actionColumn = new TableColumn<>("ACTION");
        actionColumn.setCellValueFactory(new PropertyValueFactory<>(""));
        actionColumn.getStyleClass().addAll("action-column");
        actionColumn.setCellFactory(cell -> {
            final Button editButton_2 = new Button();
            TableCell<Offense, String> cellInstance = new TableCell<>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        editButton_2.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/assets/pencil.png"))));
                        editButton_2.setOnAction(event -> {
                            Offense offense = getTableView().getItems().get(getIndex());
                            showEditOffense(offense, (ActionEvent) event);
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

        tableView.getColumns().addAll(offenseIdColumn, violationIdColumn, studIdColumn, studColumn, offenseDateColumn, csHoursColumn, actionColumn);
    }


    private int computeTotalCommServHours(List<Offense> studentOffenses) {
        int totalCommServHours = 0;
        for (Offense offense : studentOffenses) {
            totalCommServHours += offense.getCommServHours();
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
    private Callback<TableColumn<Offense, Timestamp>, TableCell<Offense, Timestamp>> getDateCellFactory() {
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

    private void showEditOffense(Offense offense, ActionEvent event) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();

            Stage editOffense = new Stage();
            editOffense.initStyle(StageStyle.UNDECORATED);

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/EditOffense.fxml"));
            AnchorPane editOffenseLayout = new AnchorPane();
            editOffenseLayout = loader.load();
            EditOffenseController editOffenseController = loader.getController();
            editOffenseController.setOffense(offense);
            Scene scene = new Scene(editOffenseLayout);
            editOffense.setScene(scene);
            editOffense.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void handleBack2Previous(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/OffenseList.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
