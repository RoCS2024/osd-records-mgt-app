package com.prefect.user.management.app.controllers.dashboard;

import com.prefect.office.record.management.PrefectOfficeRecordMgtApplication;
import com.prefect.office.record.management.appl.facade.prefect.violation.impl.ViolationFacadeImpl;
import com.prefect.office.record.management.appl.model.violation.Violation;
import com.prefect.office.record.management.appl.facade.prefect.violation.*;

import com.prefect.user.management.app.controllers.modal.ChangePswController;
import com.prefect.user.management.app.controllers.modal.EditViolationController;
import com.prefect.user.management.app.controllers.search.SearchOffenseController;
import com.prefect.user.management.app.controllers.search.SearchViolationController;
import com.student.information.management.appl.facade.student.StudentFacade;
import com.student.information.management.appl.facade.student.impl.StudentFacadeImpl;
import com.student.information.management.appl.model.student.Student;
import com.user.management.appl.model.user.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ViolationController implements Initializable {
    //for sidebar uses
    @FXML
    private Button burgerButton;

    @FXML
    private ImageView burgerIcon;

    @FXML
    private AnchorPane sidebarPane;

    private boolean sidebarVisible = false;

    @FXML
    private ComboBox<String> filterBox;

    //for search
    @FXML
    private TextField searchField;

    //for table id
    @FXML
    TableView table;

    private ViolationFacade violationFacade;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        filterBox.getItems().addAll("All", "Minor", "Major");
        filterBox.setValue("All");

        PrefectOfficeRecordMgtApplication app = new PrefectOfficeRecordMgtApplication();
        violationFacade = app.getViolationFacade();

        List<Violation> violations = violationFacade.getAllViolation();
        ObservableList<Violation> data = FXCollections.observableArrayList(violations);

        setupTable(data);

        filterBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            List<Violation> filteredViolations = filterViolations(newValue);
            ObservableList<Violation> filteredData = FXCollections.observableArrayList(filteredViolations);

            setupTable(filteredData);
        });
    }

    private void setupTable(ObservableList<Violation> data) {
        table.getItems().clear();
        table.setItems(data);

        TableColumn violationColumn = new TableColumn("VIOLATION");
        violationColumn.setCellValueFactory(new PropertyValueFactory<>("violation"));
        violationColumn.getStyleClass().addAll("violation-column");

        TableColumn violationTypeColumn = new TableColumn("VIOLATION TYPE");
        violationTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        violationTypeColumn.getStyleClass().addAll("type-column");

        TableColumn totalCsHoursColumn = new TableColumn("CS HOURS");
        totalCsHoursColumn.setCellValueFactory(new PropertyValueFactory<>("commServHours"));
        totalCsHoursColumn.getStyleClass().addAll("cs-hours-column");

        TableColumn<Violation, String> actionColumn = new TableColumn<>("ACTION");
        actionColumn.setCellValueFactory(new PropertyValueFactory<>(""));
        actionColumn.getStyleClass().addAll("action-column");
        actionColumn.setCellFactory(cell -> {
            final Button editButton = new Button();
            TableCell<Violation, String> cellInstance = new TableCell<>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        editButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/assets/pencil.png"))));
                        editButton.setOnAction(event -> {
                            Violation violation = getTableView().getItems().get(getIndex());
                            showEditViolation(violation, (ActionEvent) event);
                        });
                        HBox hbox = new HBox(editButton);
                        hbox.setSpacing(10);
                        hbox.setAlignment(Pos.BASELINE_CENTER);
                        setGraphic(hbox);
                        setText(null);
                    }
                }
            };
            return cellInstance;
        });

        table.getColumns().setAll(violationColumn, violationTypeColumn, totalCsHoursColumn, actionColumn);
    }

    private List<Violation> filterViolations(String filter) {
        if (filter.equals("All")) {
            return violationFacade.getAllViolation();
        } else if (filter.equals("Minor")) {
            return violationFacade.getAllViolationByType("Minor");
        } else if (filter.equals("Major")) {
            return violationFacade.getAllViolationByType("Major");
        }
        //empty return
        return new ArrayList<>();
    }


    @FXML
    protected void handleSubmitAddViolationButton(ActionEvent event) {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.hide();
        showDashboard();
    }

    private void showDashboard() {
        try {
            Stage dashboardStage = new Stage();
            dashboardStage.initStyle(StageStyle.UNDECORATED);

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/AddViolation.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            dashboardStage.setScene(scene);
            dashboardStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    //show details in edit button
    private void showEditViolation(Violation violation, ActionEvent event) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();

            Stage editViolationStage = new Stage();
            editViolationStage.initStyle(StageStyle.UNDECORATED);

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/EditViolation.fxml"));
            AnchorPane editViolationLayout = new AnchorPane();
            editViolationLayout = loader.load();
            EditViolationController editViolationController = loader.getController();
            editViolationController.setViolation(violation);
            Scene scene = new Scene(editViolationLayout);
            editViolationStage.setScene(scene);
            editViolationStage.show();

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
        String violationName = searchField.getText();

        System.out.println("Violation Name: " + violationName);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/SearchViolation.fxml"));

            SearchViolationController searchViolationController = new SearchViolationController();
            searchViolationController.initData(violationName);
            loader.setController(searchViolationController);
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
