package com.example.homeworkwizzz;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class MainMenuController {
    public static Account account;

    @FXML
    private Label labelOne;
    @FXML
    private Label labelTwo;
    @FXML
    private Label labelThree;
    @FXML
    private Label labelFour;
    @FXML
    private Label labelFive;
    @FXML
    private Label labelSix;
    @FXML
    private Label welcomeMessage;
    @FXML
    private TextField searchBar;
    @FXML
    private Button searchButton;
    @FXML
    private ListView<String> searchResults;
    @FXML
    private Label noFileFoundLabel;
    @FXML
    private Label noUserInputLabel;
    private ArrayList<String> subjects;


    @FXML
    public void initialize(){
        account = SubjectChoiceController.account;
        subjects = account.getSubjects();
        searchResults.getItems().add("Task1");

        if (subjects == null){
            subjects = new ArrayList<>();
            account = new Account(LoginController.username);
            File file = new File(account.getUsername());
            File[] files = file.listFiles();
            assert files != null;
            for (File existingFile : files){
                assert false;
                subjects.add(existingFile.getName());
            }
        }
        String username = account.getUsername();
        welcomeMessage.setText("Welcome, " + username);
        ArrayList<Label> labels = new ArrayList<>(Arrays.asList(labelOne, labelTwo, labelThree, labelFour, labelFive, labelSix));
        for (int i=0; i<subjects.size(); i++){
            Label label = labels.get(i);
            String subject = subjects.get(i);
            label.setText(subject);
        }
    }

    @FXML
    public void searchConfirmButton(ActionEvent event) {
        noUserInputLabel.setVisible(false);
        noFileFoundLabel.setVisible(false);
        String userInput = searchBar.getText();
        searchResults.getItems().clear();
        searchResults.setVisible(false);
        boolean fileFound = false; // Flag to track if a file is found

        if (userInput.length() != 0) {
            File directory = new File(account.getUsername());
            File[] files = directory.listFiles();

            if (files != null) {
                for (File file : files) {
                    if (userInput.equalsIgnoreCase(file.getName())) {
                        searchResults.getItems().add(file.getName());
                        searchResults.setVisible(true);
                        fileFound = true; // File is found
                    } else {
                        File[] innerFiles = file.listFiles();
                        assert innerFiles != null;
                        for (File innerFile : innerFiles) {
                            if (userInput.equalsIgnoreCase(innerFile.getName())) {
                                searchResults.getItems().add(innerFile.getName());
                                searchResults.setVisible(true);
                                fileFound = true; // File is found
                            }
                        }
                    }
                }

                if (!fileFound) {
                    noFileFoundLabel.setVisible(true);
                }
            }
        } else {
            noUserInputLabel.setVisible(true);
        }
    }
}
