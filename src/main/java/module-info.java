module com.demo.lab5 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;


    opens com.demo.lab5 to javafx.fxml;
    exports com.demo.lab5;
    exports com.demo.lab5.shapes;
    opens com.demo.lab5.shapes to javafx.fxml;
}