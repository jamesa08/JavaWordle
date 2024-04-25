module com.csdemo {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;

    opens com.csdemo to javafx.fxml;
    
    exports com.csdemo;
    exports com.csdemo.model;
}