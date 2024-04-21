package com.prefect.user.management.app.controllers.modal;

import com.user.management.appl.facade.user.UserFacade;
import com.user.management.appl.facade.user.impl.UserFacadeImpl;
import com.user.management.appl.model.user.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;

import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ForgotPswController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField nicknameField;

    @FXML
    private PasswordField newPswField;

    @FXML
    private TextField showNewPsw;

    @FXML
    private Button saveForgotPswButton;
    
    @FXML
    private ToggleButton toggleButton;

    @FXML
    private ToggleButton toggleButton2;

    private User user;

    private UserFacade userFacade = new UserFacadeImpl();

    @FXML
    protected void saveForgotPswClicked(ActionEvent event) {
        User forgotPsw = new User();
        forgotPsw.setUsername(usernameField.getText());
        forgotPsw.setPassword(newPswField.getText());

        String username = usernameField.getText();
        String nickname = nicknameField.getText();
        String newPassword  = newPswField.getText();

        try {
            userFacade.forgotPassword(username, nickname, newPassword);
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
    void changeVisibility(ActionEvent event){
        if(toggleButton.isSelected()){
            showNewPsw.setText(newPswField.getText());
            showNewPsw.setVisible(true);
            newPswField.setVisible(false);
            toggleButton.setVisible(false);
            return;
        }
        newPswField.setText(showNewPsw.getText());
        newPswField.setVisible(true);
        showNewPsw.setVisible(false);
        toggleButton.setVisible(true);
    }

    @FXML
    void changeVisibility2(ActionEvent event){
        if(toggleButton2.isSelected()){
            newPswField.setText(showNewPsw.getText());
            newPswField.setVisible(true);
            showNewPsw.setVisible(false);
            toggleButton.setVisible(true);
            return;
        }
        showNewPsw.setText(newPswField.getText());
        showNewPsw.setVisible(true);
        newPswField.setVisible(false);
        toggleButton.setVisible(false);
    }

    @FXML
    protected void handleCancelForgotPsw(MouseEvent event) {
        try {
            Stage previousStage2 = (Stage) ((Node) event.getSource()).getScene().getWindow();
            previousStage2.close();

            Stage dashboardStage2 = new Stage();
            FXMLLoader loader2 = new FXMLLoader();
            loader2.setLocation(getClass().getResource("/views/MainView.fxml"));
            Parent root2 = loader2.load();
            Scene scene2 = new Scene(root2);
            dashboardStage2.setScene(scene2);
            dashboardStage2.initStyle(StageStyle.UNDECORATED);
            dashboardStage2.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
