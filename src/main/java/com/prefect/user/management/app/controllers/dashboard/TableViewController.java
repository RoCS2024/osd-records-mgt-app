package com.prefect.user.management.app.controllers.dashboard;

import com.prefect.user.management.app.controllers.modal.ChangePswController;
import com.prefect.user.management.app.controllers.search.SearchOffenseController;
import com.prefect.user.management.app.controllers.search.SearchUserController;
import com.student.information.management.appl.facade.student.StudentFacade;
import com.student.information.management.appl.facade.student.impl.StudentFacadeImpl;
import com.student.information.management.appl.model.student.Student;
import com.user.management.appl.facade.user.impl.UserFacadeImpl;
import com.user.management.appl.model.user.User;
import com.user.management.appl.facade.user.*;

import com.user.management.data.user.dao.UserDao;
import com.user.management.data.user.dao.impl.UserDaoImpl;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class TableViewController implements Initializable {

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

    UserDao userFacade = new UserDaoImpl();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        table.getItems().clear();
        List<User> users = userFacade.getAllUsers();
        ObservableList<User> data = FXCollections.observableArrayList(users);
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

    //button actions
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
        int userId = Integer.parseInt(searchField.getText());

        System.out.println("User ID: " + userId);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/SearchUser.fxml"));

            SearchUserController searchUserController = new SearchUserController();
            searchUserController.initData(userId);
            loader.setController(searchUserController);
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
