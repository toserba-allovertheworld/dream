module com.example.breakout {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.javafx1 to javafx.fxml;
    exports com.example.javafx1;
}