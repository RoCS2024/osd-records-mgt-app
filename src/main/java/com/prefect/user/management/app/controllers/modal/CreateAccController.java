package com.prefect.user.management.app.controllers.modal;

import com.user.management.appl.facade.user.UserFacade;
import com.user.management.appl.facade.user.impl.UserFacadeImpl;
import com.user.management.appl.model.user.User;
import com.user.management.data.user.dao.UserDao;
import com.user.management.data.user.dao.impl.UserDaoImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.Timestamp;

public class CreateAccController {

    @FXML
    private TextField idField;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField entityIdField;

    @FXML
    private Button registerButton;

    private User user;

    UserDao userFacade = new UserDaoImpl();

    @FXML
    protected void saveRegisterClicked(ActionEvent event) {
        String username = usernameField.getText();
        String entityId = entityIdField.getText();

        if (!isValidInput(username, entityId)) {
            showAlert("Invalid Input", "Please fill in all fields.");
            return;
        }
        if (!isAlphaNumeric(username)) {
            showAlert("Invalid Input", "Username must contain alphanumeric characters only.");
            return;
        }
        if (!isNumeric(entityId)) {
            showAlert("Invalid Input", "Entity ID must contain numbers only.");
            return;
        }
        User addUser = new User();
        addUser.setUsername(usernameField.getText());
        addUser.setEntity_id(entityIdField.getText());

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        addUser.setDate_created(timestamp);
        addUser.setDate_modified(timestamp);

        try {
            userFacade.saveUser(addUser);
        } catch(Exception ex) {
            ex.printStackTrace();;
        }
        finally {
            try {
                Stage previousStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                previousStage.close();

                Stage dashboardStage = new Stage();
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/views/MainView.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                dashboardStage.setScene(scene);

                dashboardStage.initStyle(StageStyle.UNDECORATED);

                dashboardStage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    protected void handleHaveAccount(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/MainView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.UNDECORATED);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void handleRegisterNow(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/MainView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.UNDECORATED);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private boolean isValidInput(String username, String entityId) {
        return !username.isEmpty() && !entityId.isEmpty();
    }
    private boolean isAlphaNumeric(String str) {
        return str.matches("^[a-zA-Z0-9]*$");
    }

    private boolean isNumeric(String str) {
        return str.matches("[0-9]+");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
