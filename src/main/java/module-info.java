module com.luthors.binarytreeapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.luthors.binarytreeapp to javafx.fxml;
    exports com.luthors.binarytreeapp;
}