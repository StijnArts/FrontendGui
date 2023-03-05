module stijn.dev.frontendgui {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.neo4j.driver;
    requires java.xml;
    requires nu.xom;
    requires com.google.common;
    requires org.controlsfx.controls;

    opens stijn.dev.resource to javafx.fxml;
    exports stijn.dev.resource;
    exports stijn.dev.resource.controllers;
    opens stijn.dev.resource.controllers to javafx.fxml;
}