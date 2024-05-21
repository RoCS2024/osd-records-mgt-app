package com.prefect.user.management.app.controllers.dashboard;

import com.prefect.office.record.management.PrefectOfficeRecordMgtApplication;
import com.prefect.office.record.management.appl.model.offense.Offense;
import com.prefect.office.record.management.appl.facade.prefect.offense.*;

import com.prefect.user.management.app.controllers.modal.EditOffenseController;
import com.prefect.user.management.app.controllers.search.SearchOffenseController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import javafx.stage.StageStyle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class OffenseController implements Initializable {
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

    private OffenseFacade offenseFacade;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        filterBox.getItems().addAll("All", "Minor", "Major");
        filterBox.setValue("All");

        PrefectOfficeRecordMgtApplication app = new PrefectOfficeRecordMgtApplication();
        offenseFacade = app.getOffenseFacade();

        List<Offense> offenses = offenseFacade.getAllOffense();
        ObservableList<Offense> data = FXCollections.observableArrayList(offenses);

        setupTable(data);

        filterBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            List<Offense> filteredOffenses = filterOffenses(newValue);
            ObservableList<Offense> filteredData = FXCollections.observableArrayList(filteredOffenses);

            setupTable(filteredData);
        });
    }

    private void setupTable(ObservableList<Offense> data) {
        table.getItems().clear();
        table.setItems(data);

        TableColumn offenseTypeColumn = new TableColumn("OFFENSE TYPE");
        offenseTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        offenseTypeColumn.getStyleClass().addAll("type-column");

        TableColumn offenseDescriptionColumn = new TableColumn("OFFENSE DESCRIPTION");
        offenseDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        offenseDescriptionColumn.getStyleClass().addAll("description-column");

        TableColumn<Offense, String> actionColumn = new TableColumn<>("ACTION");
        actionColumn.setCellValueFactory(new PropertyValueFactory<>(""));
        actionColumn.getStyleClass().addAll("action-column");
        actionColumn.setCellFactory(cell -> {
            final Button editButton = new Button();
            TableCell<Offense, String> cellInstance = new TableCell<>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        editButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/assets/pencil.png"))));
                        editButton.setOnAction(event -> {
                            Offense offense = getTableView().getItems().get(getIndex());
                            showEditOffense(offense, (ActionEvent) event);
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

        table.getColumns().setAll(offenseTypeColumn, offenseDescriptionColumn, actionColumn);
    }

    private List<Offense> filterOffenses(String filter) {
        if (filter.equals("All")) {
            return offenseFacade.getAllOffense();
        } else if (filter.equals("Minor")) {
            return offenseFacade.getAllOffenseByType("Minor");
        } else if (filter.equals("Major")) {
            return offenseFacade.getAllOffenseByType("Major");
        }
        //empty return
        return new ArrayList<>();
    }


    @FXML
    protected void handleSubmitAddOffenseButton(ActionEvent event) {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.hide();
        showDashboard();
    }

    private void showDashboard() {
        try {
            Stage dashboardStage = new Stage();
            dashboardStage.initStyle(StageStyle.UNDECORATED);

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/AddOffense.fxml"));
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
    private void showEditOffense(Offense offense, ActionEvent event) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();

            Stage editStage = new Stage();
            editStage.initStyle(StageStyle.UNDECORATED);

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/EditOffense.fxml"));
            AnchorPane editLayout = new AnchorPane();
            editLayout = loader.load();
            EditOffenseController editOffenseController = loader.getController();
            editOffenseController.setOffense(offense);
            Scene scene = new Scene(editLayout);
            editStage.setScene(scene);
            editStage.show();

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
        String offenseName = searchField.getText();

        System.out.println("Offense Name: " + offenseName);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/SearchStudentOffense.fxml"));

            SearchOffenseController searchOffenseController = new SearchOffenseController();
            searchOffenseController.initData(offenseName);
            loader.setController(searchOffenseController);
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
