package com.example.homeworkwizzz;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class mainMenuController {
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
    private ArrayList<String> subjects;

    @FXML
    public void initialize(){
        account = SubjectChoiceController.account;
        subjects = account.getSubjects();
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
        ArrayList<Label> labels = new ArrayList<>(Arrays.asList(labelOne, labelTwo, labelThree, labelFour, labelFive, labelSix));
        for (int i=0; i<subjects.size(); i++){
            Label label = labels.get(i);
            String subject = subjects.get(i);
            label.setText(subject);
        }
    }
}
