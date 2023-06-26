package com.example.homeworkwizzz;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class AddButtonPopUpController {
    @FXML
    private TextField taskNameField;
    @FXML
    private Button confirmButton;

    public void setTask() {
        Platform.runLater(() -> {
           MainMenuController controller = ((MainMenuController) taskNameField.getScene().getWindow().getUserData());
           String taskName = taskNameField.getText();
           controller.addTask(taskName);
        });
        Stage stage = (Stage) confirmButton.getScene().getWindow();
        stage.close();
    }
}
