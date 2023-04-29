package com.example.homeworkwizzz;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.file.NoSuchFileException;
import java.util.EventObject;
import java.util.ResourceBundle;
import java.util.Scanner;

public class SignupController {
    @FXML
    private Button signupBackButton;
    @FXML
    private Button createAccountButton;
    @FXML
    private TextField signupUsername;
    @FXML
    private PasswordField signupPassword;
    @FXML
    private PasswordField signupConfirmPassword;
    @FXML
    private Label passwordLengthLabel;
    @FXML
    private Label accountAlreadyExistsLabel;
    @FXML
    private Label confirmPasswordLabel;
    @FXML
    private Label accountSuccessfullyCreatedLabel;
    @FXML
    private Label usernameLengthLabel;


    private static boolean loggedIn; //checks if a user is logged in

    public void initialize() {
        signupConfirmPassword.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                try {
                    createAccountButton(new ActionEvent());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        signupUsername.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                try {
                    createAccountButton(new ActionEvent());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        signupPassword.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                try {
                    createAccountButton(new ActionEvent());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        passwordLengthLabel.setVisible(false);
        accountAlreadyExistsLabel.setVisible(false);
        confirmPasswordLabel.setVisible(false);
        accountSuccessfullyCreatedLabel.setVisible(false);
        usernameLengthLabel.setVisible(false);
    }

    public void signupBackButton(ActionEvent event){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }catch (IOException e){
            System.out.println("Couldn't load screen: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("Runtime Exception");
        }catch (Exception e){
            System.out.println("Exception");
        }
    }

    public void createAccountButton(ActionEvent e) throws Exception {
        File file = new File("users.txt");


        if (!file.exists()){
            FileUtilities.writeToFile("users.txt", "", false);
        }

        FileDecryption.decryptFile();

        String username = signupUsername.getText();
        String password = signupPassword.getText();
        String confirmedPassword = signupConfirmPassword.getText();


        if (Database.checkUsername("users.txt", username)) {
            passwordLengthLabel.setVisible(false);
            confirmPasswordLabel.setVisible(false);
            accountSuccessfullyCreatedLabel.setVisible(false);
            usernameLengthLabel.setVisible(false);
            accountAlreadyExistsLabel.setVisible(true);
            FileEncryption.encryptFile();
        } else if (username.length() == 0) {
            accountAlreadyExistsLabel.setVisible(false);
            passwordLengthLabel.setVisible(false);
            confirmPasswordLabel.setVisible(false);
            accountSuccessfullyCreatedLabel.setVisible(false);
            usernameLengthLabel.setVisible(true);
            FileEncryption.encryptFile();
        } else if (password.length() < 8) {
            accountAlreadyExistsLabel.setVisible(false);
            confirmPasswordLabel.setVisible(false);
            accountSuccessfullyCreatedLabel.setVisible(false);
            usernameLengthLabel.setVisible(false);
            passwordLengthLabel.setVisible(true);
            FileEncryption.encryptFile();
        } else if (!password.equals(confirmedPassword)) {
            accountAlreadyExistsLabel.setVisible(false);
            passwordLengthLabel.setVisible(false);
            accountSuccessfullyCreatedLabel.setVisible(false);
            usernameLengthLabel.setVisible(false);
            confirmPasswordLabel.setVisible(true);
            FileEncryption.encryptFile();
        } else {
            accountAlreadyExistsLabel.setVisible(false);
            passwordLengthLabel.setVisible(false);
            confirmPasswordLabel.setVisible(false);
            usernameLengthLabel.setVisible(false);
            Account newAccount = new Account(username, password);
            Database.addToTextDatabase("users.txt", newAccount, true);
            FileEncryption.encryptFile();
            accountSuccessfullyCreatedLabel.setVisible(true);
        }
    }
}
