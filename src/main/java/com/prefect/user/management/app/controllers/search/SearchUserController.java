package com.prefect.user.management.app.controllers.search;

import com.prefect.user.management.app.controllers.modal.ChangePswController;
import com.user.management.appl.facade.user.UserFacade;
import com.user.management.appl.facade.user.impl.UserFacadeImpl;
import com.user.management.appl.model.user.User;
import com.user.management.data.user.dao.UserDao;
import com.user.management.data.user.dao.impl.UserDaoImpl;
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

public class SearchUserController implements Initializable {

    //for table id
    @FXML
    TableView table;
    @FXML
    private TextField entityIdField;
    private int userId;
    @FXML
    private Button previousButton;
    UserDao userFacade = new UserDaoImpl();
    public void initData(int userId) {
        this.userId = userId;

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        previousButton.setOnAction(event -> {handleBack2Previous((ActionEvent) event);});

        System.out.println("User ID: " + this.userId);
        table.getItems().clear();
        List<User> users = userFacade.getAllUsers();
        User userData = userFacade.getUserById(userId);
        System.out.println(userId);

        ObservableList<User> data = FXCollections.observableArrayList(userData);
        table.setItems(data);

        TableColumn<User, Integer> userIdColumn = new TableColumn<>("USER ID");
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        userIdColumn.getStyleClass().addAll("user-id-column");

        TableColumn<User, String> usernameColumn = new TableColumn<>("USERNAME");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        usernameColumn.getStyleClass().addAll("username-column");

        TableColumn<User, Integer> entityIdColumn = new TableColumn<>("ENTITY ID");
        entityIdColumn.setCellValueFactory(new PropertyValueFactory<>("entity_id"));
        entityIdColumn.getStyleClass().addAll("entity-id-column");
        entityIdField.setOnAction(event -> handleSearchByEntityId());

        TableColumn<User, Timestamp> dateCreatedColumn = new TableColumn<>("DATE CREATED");
        dateCreatedColumn.setCellValueFactory(new PropertyValueFactory<>("date_created"));
        dateCreatedColumn.getStyleClass().addAll("date-created-column");
        dateCreatedColumn.setCellFactory(getDateCellFactory());

        TableColumn<User, Timestamp> dateModifiedColumn = new TableColumn<>("DATE MODIFIED");
        dateModifiedColumn.setCellValueFactory(new PropertyValueFactory<>("date_modified"));
        dateModifiedColumn.getStyleClass().addAll("date-modified-column");
        dateModifiedColumn.setCellFactory(getDateCellFactory());

        TableColumn<User, String> actionColumn = new TableColumn<>("ACTION");
        actionColumn.setCellValueFactory(new PropertyValueFactory<>(""));
        actionColumn.getStyleClass().addAll("action-column");
        actionColumn.setCellFactory(cell -> {
            final Button editButton = new Button();
            TableCell<User, String> cellInstance = new TableCell<>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        editButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/assets/pencil.png"))));
                        editButton.setOnAction(event -> {
                            User user = getTableView().getItems().get(getIndex());
                            showEditUser(user, (ActionEvent) event);
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

        table.getColumns().addAll(userIdColumn, usernameColumn, entityIdColumn, dateCreatedColumn, dateModifiedColumn, actionColumn);
    }

    private Callback<TableColumn<User, Timestamp>, TableCell<User, Timestamp>> getDateCellFactory() {
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

    private void handleSearchByEntityId() {
        String entityIdStr = entityIdField.getText();
        if (!entityIdStr.isEmpty()) {
            int entityId = Integer.parseInt(entityIdStr);
            User user = userFacade.getUserByEntityId(entityId); // Assuming this method exists in UserDao
            if (user != null) {
                ObservableList<User> data = FXCollections.observableArrayList(user);
                table.setItems(data);
            } else {
                table.getItems().clear();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("No User Found");
                alert.setHeaderText(null);
                alert.setContentText("No user found with the specified entity ID.");
                alert.showAndWait();
            }
        }
    }
    //show details in edit button
    private void showEditUser(User user, ActionEvent event) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();

            Stage changePsw = new Stage();
            changePsw.initStyle(StageStyle.UNDECORATED);

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/ChangePsw.fxml"));
            AnchorPane changePswLayout = new AnchorPane();
            changePswLayout = loader.load();
            ChangePswController changePswController = loader.getController();
            changePswController.setUser(user);
            Scene scene = new Scene(changePswLayout);
            changePsw.setScene(scene);
            changePsw.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void handleBack2Previous(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/UserList.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


