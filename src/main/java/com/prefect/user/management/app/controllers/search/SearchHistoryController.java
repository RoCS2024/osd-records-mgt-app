package com.prefect.user.management.app.controllers.search;

import com.prefect.office.record.management.PrefectOfficeRecordMgtApplication;
import com.prefect.office.record.management.appl.facade.prefect.communityservice.CommunityServiceFacade;
import com.prefect.office.record.management.appl.facade.prefect.communityservice.impl.CommunityServiceFacadeImpl;
import com.prefect.office.record.management.appl.model.communityservice.CommunityService;
import com.prefect.office.record.management.appl.model.offense.Offense;
import com.prefect.office.record.management.data.dao.prefect.communityservice.impl.CommunityServiceDaoImpl;
import com.student.information.management.appl.model.student.Student;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.control.Button;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SearchHistoryController implements Initializable {
    //for table id
    @FXML
    TableView table;

    @FXML
    private TextField totalField;

    @FXML
    private Button previousButton;

    private CommunityServiceFacade communityServiceFacade;

    private Student student;


    public void initData(Student student) {this.student = student; }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        PrefectOfficeRecordMgtApplication app = new PrefectOfficeRecordMgtApplication();
        communityServiceFacade = app.getCommunityserviceFacade();

        previousButton.setOnAction(event -> {handleBack2Previous((ActionEvent) event);});

        if (student != null) {
            table.getItems().clear();

            List<CommunityService> communityServiceByStudId = communityServiceFacade.getAllCsByStudent(student);

            int totalHoursRendered = computeTotalHoursRendered(communityServiceByStudId);
            totalField.setText(String.valueOf(totalHoursRendered));

            ObservableList<CommunityService> data = FXCollections.observableArrayList(communityServiceByStudId);
            table.setItems(data);
        } else {
            System.out.println("No student data passed.");
        }

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

    private int computeTotalHoursRendered(List<CommunityService> communityServiceByStudId) {
        int totalHoursRendered = 0;
        for (CommunityService communityService : communityServiceByStudId) {
            totalHoursRendered += communityService.getHours_rendered();
        }
        return totalHoursRendered;
    }


    @FXML
    protected void handleBack2Previous(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/CommunityService.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
