package com.example.homeworkwizzz;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class HomeworkWizz extends Application {
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HomeworkWizz.class.getResource("login.fxml"));
            stage.initStyle(StageStyle.UNDECORATED);
            Scene scene = new Scene(fxmlLoader.load(), 520, 400);
            stage.setScene(scene);
            stage.show();
        }catch (IOException e){
            System.out.println("IOException");
        }catch (RuntimeException e){
            System.out.println("RuntimeException");
        }
    }
    public static void main(String[] args) {
        launch();
    }
}