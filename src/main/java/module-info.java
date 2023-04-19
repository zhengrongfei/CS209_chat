module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.google.gson;
    requires Java.WebSocket;


    opens com.example.demo to javafx.fxml;
    opens com.example.Controller to javafx.fxml;

    opens com.example.Json to com.google.gson;
    exports com.example.demo;
    exports com.example.Controller;

}