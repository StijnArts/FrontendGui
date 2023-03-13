package stijn.dev.resource.controllers;

import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.stage.*;
import stijn.dev.resource.*;

import java.io.*;

public class SceneController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    public void switchToDemo(ActionEvent event) throws IOException {
        root = FXMLLoader.load(FrontEndApplication.class.getResource("../main.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToImport(ActionEvent event) throws IOException {
        System.out.println("switching to import scene");
        root = FXMLLoader.load(FrontEndApplication.class.getResource("importPlatformSelection.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
