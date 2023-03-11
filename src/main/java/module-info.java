module stijn.dev.frontendgui {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.neo4j.driver;
    requires java.xml;
    requires nu.xom;
    requires com.google.common;
    requires org.controlsfx.controls;
    requires org.apache.commons.lang3;
    requires htmlunit;
    requires org.jsoup;
    requires com.google.guice;

    opens stijn.dev.resource to javafx.fxml;
    exports stijn.dev.resource;
    exports stijn.dev.resource.controllers;
    opens stijn.dev.resource.controllers to javafx.fxml;
    exports stijn.dev.resource.controllers.interfaces;
    opens stijn.dev.resource.controllers.interfaces to javafx.fxml;
}