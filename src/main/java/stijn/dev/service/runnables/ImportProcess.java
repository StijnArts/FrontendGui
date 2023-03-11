package stijn.dev.service.runnables;

import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.input.*;
import javafx.stage.*;
import stijn.dev.resource.*;
import stijn.dev.resource.controllers.*;
import stijn.dev.service.javafx.*;

import java.io.*;
import java.util.*;

import static stijn.dev.resource.FrontEndApplication.importProcessIsRunning;

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
        FXMLLoader loader = FXMLLoaderUtil.createFMXLLoader("importPlatformSelection.fxml");
        root = RootUtil.createRoot(loader);
        Stage stage = StageUtil.createStageFromExistingNode(node);
        Scene scene = new Scene(root);
        ImportPlatformSelectionController.create(loader, files, stage, scene);
        stage.setScene(scene);
        stage.setOnHidden(event->{FrontEndApplication.importProcessIsRunning = false;});
        stage.show();
    }

    public static Thread createImportProcess(Dragboard dragboard, Node node){
        List<File> files = dragboard.getFiles();
        ImportProcess importProcess = new ImportProcess(files, node);
        Thread thread = new Thread(importProcess);
        importProcessIsRunning =true;
        return thread;
    }
}
