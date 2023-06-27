module com.example.homeworkwizzz {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.homeworkwizzz to javafx.fxml;
    exports com.example.homeworkwizzz;
}