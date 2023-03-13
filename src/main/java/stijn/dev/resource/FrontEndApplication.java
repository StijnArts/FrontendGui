package stijn.dev.resource;

import javafx.application.Application;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.image.*;
import javafx.scene.text.*;
import javafx.stage.*;
import stijn.dev.resource.controllers.*;
import stijn.dev.util.javafx.*;

import java.io.IOException;
//TODO add dependency Injection
public class FrontEndApplication extends Application {
    public static boolean importProcessIsRunning;
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = FXMLLoaderUtil.createFMXLLoader(("main.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        String css = this.getClass().getResource("demo.css").toExternalForm();
        scene.getStylesheets().add(css);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);

        primaryStage.show();
        MainController mainController = loader.getController();
        mainController.configure();

    }

    public static void main(String[] args) {
        launch(args);
    }
}