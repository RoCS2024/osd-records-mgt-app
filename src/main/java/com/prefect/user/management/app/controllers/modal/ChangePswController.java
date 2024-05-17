package com.prefect.user.management.app.controllers.modal;

import com.user.management.appl.facade.user.impl.UserFacadeImpl;
import com.user.management.appl.model.user.User;
import com.user.management.appl.facade.user.*;
import com.user.management.data.user.dao.UserDao;
import com.user.management.data.user.dao.impl.UserDaoImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.Timestamp;

public class ChangePswController {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField currentPswField;

    @FXML
    private TextField showCurrentPsw;

    @FXML
    private PasswordField newPswField;

    @FXML
    private TextField showNewPsw;

    @FXML
    private PasswordField confirmPswField;

    @FXML
    private TextField showConfirmPsw;

    @FXML
    private Button saveChangePswButton;

    @FXML
    private ToggleButton toggleButton;

    @FXML
    private ToggleButton toggleButton2;

    @FXML
    private ToggleButton toggleButton3;

    @FXML
    private ToggleButton toggleButton4;

    @FXML
    private ToggleButton toggleButton5;

    @FXML
    private ToggleButton toggleButton6;

    private User user;

    UserDao userFacade = new UserDaoImpl();

    @FXML
    protected void setSaveChangePswClicked(ActionEvent event) {
        if (!validateFields()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("All fields are important. Please enter valid input.");
            alert.showAndWait();
            return;
        }
        String username = usernameField.getText();
        String currentPassword = currentPswField.getText();

        if (user != null && user.getPassword().equals(currentPassword)) {

            User updatePsw = new User();
            updatePsw.setUsername(username);
            updatePsw.setPassword(newPswField.getText());

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            updatePsw.setDate_modified(timestamp);

            try {
                userFacade.updatePassword(updatePsw);


                Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                currentStage.close();


                Stage userListStage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/UserList.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                userListStage.setScene(scene);
                userListStage.initStyle(StageStyle.UNDECORATED);
                userListStage.show();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Password does not match with your current password.");
            alert.showAndWait();
        }
    }
    private boolean validateFields() {
        return !usernameField.getText().isEmpty() &&
                !currentPswField.getText().isEmpty() &&
                !newPswField.getText().isEmpty() &&
                !confirmPswField.getText().isEmpty();
    }

    @FXML
    void changeVisibility(ActionEvent event){
        if(toggleButton.isSelected()){
            showCurrentPsw.setText(currentPswField.getText());
            showCurrentPsw.setVisible(true);
            currentPswField.setVisible(false);
            toggleButton.setVisible(false);
            return;
        }
        currentPswField.setText(showCurrentPsw.getText());
        currentPswField.setVisible(true);
        showCurrentPsw.setVisible(false);
        toggleButton.setVisible(true);
    }

    @FXML
    void changeVisibility2(ActionEvent event){
        if(toggleButton2.isSelected()){
            currentPswField.setText(showCurrentPsw.getText());
            currentPswField.setVisible(true);
            showCurrentPsw.setVisible(false);
            toggleButton.setVisible(true);
            return;
        }
        showCurrentPsw.setText(currentPswField.getText());
        showCurrentPsw.setVisible(true);
        currentPswField.setVisible(false);
        toggleButton.setVisible(false);
    }

    @FXML
    void changeVisibility3(ActionEvent event){
        if(toggleButton3.isSelected()){
            showNewPsw.setText(newPswField.getText());
            showNewPsw.setVisible(true);
            newPswField.setVisible(false);
            toggleButton3.setVisible(false);
            return;
        }
        newPswField.setText(showNewPsw.getText());
        newPswField.setVisible(true);
        showNewPsw.setVisible(false);
        toggleButton3.setVisible(true);
    }

    @FXML
    void changeVisibility4(ActionEvent event){
        if(toggleButton4.isSelected()){
            newPswField.setText(showNewPsw.getText());
            newPswField.setVisible(true);
            showNewPsw.setVisible(false);
            toggleButton3.setVisible(true);
            return;
        }
        showNewPsw.setText(newPswField.getText());
        showNewPsw.setVisible(true);
        newPswField.setVisible(false);
        toggleButton3.setVisible(false);
    }

    @FXML
    void changeVisibility5(ActionEvent event){
        if(toggleButton5.isSelected()){
            showConfirmPsw.setText(confirmPswField.getText());
            showConfirmPsw.setVisible(true);
            confirmPswField.setVisible(false);
            toggleButton5.setVisible(false);
            return;
        }
        confirmPswField.setText(showConfirmPsw.getText());
        confirmPswField.setVisible(true);
        showConfirmPsw.setVisible(false);
        toggleButton5.setVisible(true);
    }

    @FXML
    void changeVisibility6(ActionEvent event){
        if(toggleButton6.isSelected()){
            confirmPswField.setText(showConfirmPsw.getText());
            confirmPswField.setVisible(true);
            showConfirmPsw.setVisible(false);
            toggleButton5.setVisible(true);
            return;
        }
        showConfirmPsw.setText(confirmPswField.getText());
        showConfirmPsw.setVisible(true);
        confirmPswField.setVisible(false);
        toggleButton5.setVisible(false);
    }


    public void setUser(User user) {
        this.user = user;
        usernameField.setText(user.getUsername());
    }

    @FXML
    protected void handleCancelChangePsw(MouseEvent event) {
        try {
            Stage previousStage2 = (Stage) ((Node) event.getSource()).getScene().getWindow();
            previousStage2.close();

            Stage dashboardStage2 = new Stage();
            FXMLLoader loader2 = new FXMLLoader();
            loader2.setLocation(getClass().getResource("/views/UserList.fxml"));
            Parent root2 = loader2.load();
            Scene scene2 = new Scene(root2);
            dashboardStage2.setScene(scene2);
            dashboardStage2.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
