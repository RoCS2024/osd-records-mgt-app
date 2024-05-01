package com.prefect.user.management.app.controllers.search;

import com.prefect.office.record.management.PrefectOfficeRecordMgtApplication;
import com.prefect.office.record.management.appl.facade.prefect.violation.ViolationFacade;
import com.prefect.office.record.management.appl.facade.prefect.violation.impl.ViolationFacadeImpl;
import com.prefect.office.record.management.appl.model.violation.Violation;
import com.prefect.user.management.app.controllers.modal.EditViolationController;
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
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SearchViolationController implements Initializable {

    //for table id
    @FXML
    TableView table;

    @FXML
    private Button previousButton;
    private ViolationFacade violationFacade;

    private String violationName;

    public void initData(String violationName) {
        this.violationName = violationName;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        PrefectOfficeRecordMgtApplication app = new PrefectOfficeRecordMgtApplication();
        violationFacade = app.getViolationFacade();

        previousButton.setOnAction(event -> {handleBack2Previous((ActionEvent) event);});

        table.getItems().clear();
        Violation violationByName = violationFacade.getViolationByName(violationName);

        ObservableList<Violation> data = FXCollections.observableArrayList(violationByName);
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

        table.getColumns().addAll(violationColumn, violationTypeColumn, totalCsHoursColumn, actionColumn);
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
