package com.example.homeworkwizzz;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.awt.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class MainMenuController {
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
    @FXML
    private Button saveButton;
    @FXML
    private TextArea textBox;
    @FXML
    private ListView<CheckBox> todolistListView;
    @FXML
    private ListView<String> fileListView;
    @FXML
    private Button closeButton;
    public static Account account = new Account(LoginController.username);
    public static ArrayList currentDirectory = new ArrayList<>();
    private ArrayList<String> subjects;


    @FXML
    public void initialize() throws IOException {
        subjects = new ArrayList<>();
        account = new Account(LoginController.username);
        File file = new File(account.getUsername());
        File[] files = file.listFiles();
        assert files != null;
        for (File existingFile : files) {
            if (existingFile.isDirectory()) {
                subjects.add(existingFile.getName());
            }
        }

        String username = account.getUsername();
        welcomeMessage.setText("Welcome, " + username);
        ArrayList<Label> labels = new ArrayList<>(Arrays.asList(labelOne, labelTwo, labelThree, labelFour, labelFive, labelSix));
        for (int i = 0; i < subjects.size(); i++) {
            Label label = labels.get(i);
            String subject = subjects.get(i);
            label.setText(subject);
        }

        Platform.runLater(() -> {
            Scene scene = welcomeMessage.getScene();
            scene.setOnMouseClicked(event -> {
                if (!searchResults.getBoundsInParent().contains(event.getX(), event.getY())) {
                    searchResults.setVisible(false);
                    noFileFoundLabel.setVisible(false);
                    noUserInputLabel.setVisible(false);
                }
            });
            scene.getWindow().setOnCloseRequest(windowEvent -> {
                try {
                    saveText();
                    saveToDoList();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            Platform.runLater(() ->{
                labelOne.setOnMouseClicked(event -> onFileCLick(labelOne));
                labelTwo.setOnMouseClicked(event -> onFileCLick(labelTwo));
                labelThree.setOnMouseClicked(event -> onFileCLick(labelThree));
                labelFour.setOnMouseClicked(event -> onFileCLick(labelFour));
                labelFive.setOnMouseClicked(event -> onFileCLick(labelFive));
                labelSix.setOnMouseClicked(event -> onFileCLick(labelSix));
            });
        });

        File directory = new File(LoginController.username);
        File notes = new File(directory,"notes_section.txt");
        File toDoList = new File(directory, "to_do_list.txt");

        if (!notes.exists()){
            NotesSection.createNotesSection(directory);
        }else{
            String text = NotesSection.seeCurrentNotes(directory);
            textBox.setText(text);
        }

        if(toDoList.createNewFile()){
            ToDoList.createToDoList(directory);
        }else{
            ArrayList<String> tasks = ToDoList.returnToDoListItems(directory);

                for (int i=0; i<tasks.size(); i++){
                    String task = tasks.get(i);
                    String taskName = task.split(":")[0];
                    String isFinished = task.split(":")[1];
                    boolean isDone = Boolean.parseBoolean(isFinished);
                    CheckBox checkBox = new CheckBox(taskName);
                    todolistListView.getItems().add(checkBox);

                    if(isDone){
                    todolistListView.getItems().get(i).fire();
                }
            }
        }
    }

    @FXML
    public void searchConfirmButton() {
        noUserInputLabel.setVisible(false);
        noFileFoundLabel.setVisible(false);
        String userInput = searchBar.getText();
        searchResults.getItems().clear();
        searchResults.setVisible(false);
        fileListView.setVisible(false);
        closeButton.setVisible(false);

        if (userInput.length() != 0) {
            File directory = new File(account.getUsername());

            ArrayList<File> results = Search.find(directory, userInput);
            if(results.size() == 0){
                noFileFoundLabel.setVisible(true);
            }else {
                for (File file : results){
                    searchResults.getItems().add(file.getPath());
                    searchResults.setVisible(true);
                }
            }
        }else {
            noUserInputLabel.setVisible(true);
        }
    }

    public void saveText(){
        File directory = new File(LoginController.username);
        String content = textBox.getText();
        NotesSection.overWriteNotes(content, directory);
    }

    public void saveToDoList() throws IOException {
        ObservableList<CheckBox> toDoList = todolistListView.getItems();
        ArrayList<ToDoListObject> toDoListObjects = new ArrayList<>();

        File directory = new File(LoginController.username);
        File toDoListFile = new File(directory,"to_do_list.txt");

        if (!toDoListFile.exists()){
            ToDoList.createToDoList(directory);
        }

        for (CheckBox task : toDoList){
            String taskName = task.getText();
            boolean isChecked = task.isSelected();
            ToDoListObject newItem = new ToDoListObject(taskName, isChecked);
            toDoListObjects.add(newItem);

            ToDoList.addTasks(toDoListObjects, directory);
        }
    }

    @FXML
    public void addButton() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("addButtonPopUp.fxml"));
        VBox root = loader.load();

        // Create a new stage for the pop-up window
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL); // Prevent interactions with other windows
        popupStage.initOwner(textBox.getScene().getWindow()); // Set the main window as the owner
        popupStage.setScene(new Scene(root));
        popupStage.setUserData(this);
        popupStage.setResizable(false);
        popupStage.showAndWait();
    }

    public void addTask(String taskName){
        CheckBox task = new CheckBox(taskName);
        todolistListView.getItems().add(task);
    }

    public void onFileCLick(Label clickedLabel){
        fileListView.getItems().clear();
        searchResults.setVisible(false);
        File directory = new File(LoginController.username);
        String fileName = clickedLabel.getText();
        File file = new File(directory, fileName);
        ArrayList<File> files = Search.listFiles(file);

        if (files == null){
            fileListView.setVisible(true);
            closeButton.setVisible(true);
        }else{
            for (File file1 : files){
                fileListView.getItems().add(file1.getName());
                fileListView.setVisible(true);
                closeButton.setVisible(true);

                if (currentDirectory.size() != 0) {
                    currentDirectory.clear();
                }
            }
        }
        currentDirectory.add(LoginController.username);
        currentDirectory.add(fileName);
    }

    public void onFileCLick(String fileName){
        fileListView.getItems().clear();
        String filePath = "";

        for (int i=0; i<=currentDirectory.size()-2; i++){
            filePath = filePath + currentDirectory.get(i) + "\\";
        }
        filePath = filePath + currentDirectory.get(currentDirectory.size()-1);

        File file = new File(filePath, fileName);
        ArrayList<File> files = Search.listFiles(file);

        if (files.size() == 0){
            fileListView.setVisible(true);
            closeButton.setVisible(true);
        }else{
            for (File file1 : files){
                fileListView.getItems().add(file1.getName());
                fileListView.setVisible(true);
                closeButton.setVisible(true);
            }
            currentDirectory.add(fileName);
        }
    }

    public void closeButton(){
        fileListView.setVisible(false);
        closeButton.setVisible(false);
    }

    public void onItemClick() {
        String selectedItem = fileListView.getSelectionModel().getSelectedItem();
        searchResults.setVisible(false);
        if (selectedItem == null){
            return;
        }

        String filePath = "";

        for (int i=0; i<=currentDirectory.size()-2; i++){
            filePath = filePath + currentDirectory.get(i) + "\\";
        }
        filePath = filePath + currentDirectory.get(currentDirectory.size()-1);

        File directory = new File(filePath);
        File file = new File(directory, selectedItem);

        if (file.isDirectory()){
            onFileCLick(selectedItem);
        }else{
            Desktop desktop = Desktop.getDesktop();
            try{
                desktop.open(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public void onSearchItemClick(){
        String selectedFilePath = searchResults.getSelectionModel().getSelectedItem();
        if (selectedFilePath == null){
            return;
        }

        File file = new File(selectedFilePath);
        if (file.isDirectory()){
            onSearchFileCLick(selectedFilePath);
        }else{
            Desktop desktop = Desktop.getDesktop();
            try{
                desktop.open(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void onSearchFileCLick(String filePath){
        fileListView.getItems().clear();

        File file = new File(filePath);
        ArrayList<File> files = Search.listFiles(file);

        if (files.size() == 0){
            fileListView.setVisible(true);
            closeButton.setVisible(true);
        }else{
            for (File file1 : files){
                fileListView.getItems().add(file1.getName());
                fileListView.setVisible(true);
                closeButton.setVisible(true);
            }
            currentDirectory.clear();
            currentDirectory = new ArrayList<>(Arrays.asList(filePath.split("\\\\")));
            searchResults.setVisible(false);
        }
    }
}
