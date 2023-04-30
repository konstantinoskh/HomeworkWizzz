package com.example.homeworkwizzz;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;

public class LoginController {
    @FXML
    private Button exitButton;
    @FXML
    private Button loginButton;
    @FXML
    private Button signupButton;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label accountDoesntExistLabel;
    @FXML
    private Label passwordIsIncorrectLabel;
    @FXML
    private Label enterUsernameLabel;

    public void exitButton(ActionEvent e){
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    public void initialize(){
        usernameTextField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER){
                try{
                    handleEnterKey();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        passwordField.setOnKeyPressed( event -> {
            if (event.getCode() == KeyCode.ENTER){
                try{
                    handleEnterKey();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }



    public void loginButton(ActionEvent e) throws Exception {
        Platform.runLater(() -> {
            try {
                File file = new File("users.txt");

                if (!file.exists()) {
                    FileUtilities.writeToFile("users.txt", "", true);
                }

                FileDecryption.decryptFile();

                String username = usernameTextField.getText();
                String password = passwordField.getText();

                if (username.length() == 0) {
                    accountDoesntExistLabel.setVisible(false);
                    passwordIsIncorrectLabel.setVisible(false);
                    enterUsernameLabel.setVisible(true);
                    FileEncryption.encryptFile();
                } else if (!Database.checkUsername("users.txt", username)) {
                    accountDoesntExistLabel.setVisible(true);
                    passwordIsIncorrectLabel.setVisible(false);
                    enterUsernameLabel.setVisible(false);
                    FileEncryption.encryptFile();
                } else if (!Database.checkUsernameAndPassword("users.txt", username, password)) {
                    accountDoesntExistLabel.setVisible(false);
                    passwordIsIncorrectLabel.setVisible(true);
                    enterUsernameLabel.setVisible(false);
                    FileEncryption.encryptFile();
                } else {
                    FileEncryption.encryptFile();
                    Folder userFolder = new Folder(username);
                    if (!userFolder.folderExists()) {
                        userFolder.createFolder();
                    }

                    File newFolder = new File(username, "English");

                    if (!newFolder.exists()) {
                        try {
                            Stage stage = new Stage();
                            FXMLLoader fxmlLoader = new FXMLLoader(HomeworkWizz.class.getResource("subjectChoice.fxml"));
                            stage.initStyle(StageStyle.UNDECORATED);
                            Scene scene = new Scene(fxmlLoader.load(), 520, 400);
                            stage.setScene(scene);
                            stage.show();
                        } catch (IOException exception) {
                            System.out.println("IOException");
                        } catch (RuntimeException r) {
                            r.printStackTrace();
                        } catch (Exception exception) {
                            System.out.println("Exception");
                        }
                    } else {
                        try {
                            Stage stage = new Stage();
                            FXMLLoader fxmlLoader = new FXMLLoader(HomeworkWizz.class.getResource("mainMenu.fxml"));
                            stage.initStyle(StageStyle.UNDECORATED);
                            Scene scene = new Scene(fxmlLoader.load(), 520, 400);
                            stage.setScene(scene);
                            stage.show();
                        } catch (IOException exception) {
                            System.out.println("IOException");
                        } catch (RuntimeException r) {
                            System.out.println("Runtime Exception");
                        } catch (Exception exception) {
                            System.out.println("Exception");
                        }
                    }
                }
            }catch (Exception exception){
                exception.printStackTrace();
            }
        });
    }

    private void handleEnterKey() {
        try {
            loginButton(new ActionEvent());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void signupButton(ActionEvent event) {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(HomeworkWizz.class.getResource("signup.fxml"));
            stage.initStyle(StageStyle.UNDECORATED);
            Scene scene = new Scene(fxmlLoader.load(), 520, 400);
            stage.setScene(scene);
            stage.show();
        }catch (IOException e){
            System.out.println("Error loading screen: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}