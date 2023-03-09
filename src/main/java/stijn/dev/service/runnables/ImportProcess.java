package stijn.dev.service.runnables;

import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.input.*;
import javafx.stage.*;
import stijn.dev.resource.*;
import stijn.dev.resource.controllers.*;

import java.io.*;
import java.util.*;

public class ImportProcess implements Runnable{
    private List<File> files;
    private Node node;
    private Parent root;
    public ImportProcess(List<File> files, Node node){
        this.files = files;
        this.node = node;
    }
    @Override
    public void run() {
        FXMLLoader loader = new FXMLLoader(FrontEndApplication.class.getResource("importPlatformSelection.fxml"));

        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ImportPlatformSelectionController importPlatformSelectionController = loader.getController();


        importPlatformSelectionController.setFiles(files);

        importPlatformSelectionController.processPlatforms();
        Stage stage = new Stage();
        Stage mainWindow = (Stage) node.getScene().getWindow();
        stage.initOwner(mainWindow);
        importPlatformSelectionController.setStage(stage);
        Scene scene = new Scene(root);

        importPlatformSelectionController.setScene(scene);
        importPlatformSelectionController.setKeyBehavior();
        stage.setScene(scene);
        stage.setOnCloseRequest(event->{
            FrontEndApplication.importProcessIsRunning = false;
        });
        stage.show();
    }
}
