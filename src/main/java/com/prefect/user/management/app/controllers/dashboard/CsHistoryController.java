package com.prefect.user.management.app.controllers.dashboard;

import com.prefect.office.record.management.PrefectOfficeRecordMgtApplication;
import com.prefect.office.record.management.appl.model.communityservice.CommunityService;
import com.prefect.office.record.management.appl.facade.prefect.communityservice.CommunityServiceFacade;
import com.prefect.office.record.management.appl.facade.prefect.communityservice.impl.CommunityServiceFacadeImpl;
import com.prefect.office.record.management.appl.model.offense.Offense;
import com.prefect.office.record.management.data.dao.prefect.communityservice.impl.CommunityServiceDaoImpl;

import com.prefect.user.management.app.controllers.search.SearchHistoryController;
import com.prefect.user.management.app.controllers.search.SearchOffenseController;
import com.student.information.management.StudentInfoMgtApplication;
import com.student.information.management.appl.facade.student.StudentFacade;
import com.student.information.management.appl.facade.student.impl.StudentFacadeImpl;
import com.student.information.management.appl.model.student.Student;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
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

public class CsHistoryController implements Initializable{
    //for sidebar uses
    @FXML
    private Button burgerButton;

    @FXML
    private ImageView burgerIcon;

    @FXML
    private AnchorPane sidebarPane;

    private boolean sidebarVisible = false;

    //for search
    @FXML
    private TextField searchField;

    //for table id
    @FXML
    TableView table;

    private CommunityServiceFacade communityServiceFacade;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        PrefectOfficeRecordMgtApplication app = new PrefectOfficeRecordMgtApplication();

        communityServiceFacade = app.getCommunityserviceFacade();

        table.getItems().clear();
        List<CommunityService> communityServices = communityServiceFacade.getAllCs();
        ObservableList<CommunityService> data = FXCollections.observableArrayList(communityServices);
        table.setItems(data);

        TableColumn<CommunityService, String> studIdColumn = new TableColumn<>("STUDENT ID");
        studIdColumn.setCellValueFactory(cellData -> {
            String studentId = cellData.getValue().getStudent().getStudentId();
            return new SimpleStringProperty(studentId);
        });
        studIdColumn.getStyleClass().addAll("student-column");

        TableColumn<CommunityService, String> studColumn = new TableColumn<>("NAME");
        studColumn.setCellValueFactory(cellData -> {
            String firstName = cellData.getValue().getStudent().getFirstName();
            String lastName = cellData.getValue().getStudent().getLastName();
            return new SimpleStringProperty(firstName + " " + lastName);
        });
        studColumn.getStyleClass().addAll("student-column");

        TableColumn<CommunityService, Timestamp> dateRenderedColumn = new TableColumn<>("DATE RENDERED");
        dateRenderedColumn.setCellValueFactory(new PropertyValueFactory<>("date_rendered"));
        dateRenderedColumn.getStyleClass().addAll("date-column");
        dateRenderedColumn.setCellFactory(getDateCellFactory());

        TableColumn<CommunityService, Integer> hoursRendered = new TableColumn<>("HOURS RENDERED");
        hoursRendered.setCellValueFactory(new PropertyValueFactory<>("hours_rendered"));
        hoursRendered.getStyleClass().addAll("hours-column");

        table.getColumns().addAll(studIdColumn, studColumn, dateRenderedColumn, hoursRendered);
    }

    private Callback<TableColumn<CommunityService, Timestamp>, TableCell<CommunityService, Timestamp>> getDateCellFactory() {
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
    protected void handleIconUserList(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/UserList.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void handleIconOffense(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/OffenseList.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void handleIconViolationList(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ViolationList.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void handleIconCommunityService(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/CommunityService.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void handleIconLogout (MouseEvent event) {
        try {
            Stage previousStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            previousStage.close();

            Stage dashboardStage = new Stage();
            dashboardStage.initStyle(StageStyle.UNDECORATED);

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/MainView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            dashboardStage.setScene(scene);
            dashboardStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //for sidebar actions
    @FXML
    private void toggleSidebarVisibility(ActionEvent event) {
        sidebarVisible = !sidebarVisible;
        sidebarPane.setVisible(sidebarVisible);

        if (sidebarVisible) {
            BorderPane.setMargin(sidebarPane, new Insets(0));
        } else {
            BorderPane.setMargin(sidebarPane, new Insets(0, -125.0, 0, 0));
        }
    }

    //for search
    @FXML
    private void handleSearchButton(ActionEvent event) {
        String studentId = searchField.getText();

        StudentInfoMgtApplication app = new StudentInfoMgtApplication();
        StudentFacade studentFacade = app.getStudentFacade();
        Student student = studentFacade.getStudentById(studentId);

        if(student != null){
            System.out.println("Student ID: " + student);
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/SearchHistory.fxml"));

                SearchHistoryController searchHistoryController = new SearchHistoryController();
                searchHistoryController.initData(student);
                loader.setController(searchHistoryController);
                Parent root = loader.load();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            showAlert(Alert.AlertType.WARNING, "Warning", "Student ID Not Found", "Student with ID " + searchField.getText() + " does not exist.");

        }
    }
    private void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
}
