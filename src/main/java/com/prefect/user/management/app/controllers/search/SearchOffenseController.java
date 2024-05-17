package com.prefect.user.management.app.controllers.search;

import com.prefect.office.record.management.PrefectOfficeRecordMgtApplication;
import com.prefect.office.record.management.appl.facade.prefect.communityservice.CommunityServiceFacade;
import com.prefect.office.record.management.appl.facade.prefect.offense.OffenseFacade;
import com.prefect.office.record.management.appl.facade.prefect.offense.impl.OffenseFacadeImpl;
import com.prefect.office.record.management.appl.facade.prefect.violation.ViolationFacade;
import com.prefect.office.record.management.appl.model.communityservice.CommunityService;
import com.prefect.office.record.management.appl.model.offense.Offense;
import com.prefect.office.record.management.appl.model.violation.Violation;
import com.prefect.office.record.management.data.dao.prefect.offense.OffenseDao;
import com.prefect.office.record.management.data.dao.prefect.offense.impl.OffenseDaoImpl;
import com.prefect.user.management.app.controllers.modal.EditOffenseController;
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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SearchOffenseController implements Initializable {
    //for table id
    @FXML
    TableView table;

    @FXML
    private Button previousButton;
    private OffenseFacade offenseFacade;

    private String offenseName;

    public void initData(String offenseName) {
        this.offenseName = offenseName;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        PrefectOfficeRecordMgtApplication app = new PrefectOfficeRecordMgtApplication();
        offenseFacade = app.getOffenseFacade();

        previousButton.setOnAction(event -> {handleBack2Previous((ActionEvent) event);});

        table.getItems().clear();
        Offense offenseByName = offenseFacade.getOffenseByName(offenseName);

        ObservableList<Offense> data = FXCollections.observableArrayList(offenseByName);
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
