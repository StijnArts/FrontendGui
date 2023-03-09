package stijn.dev.service;

import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.input.*;
import javafx.stage.*;
import stijn.dev.resource.*;
import stijn.dev.resource.controllers.*;
import stijn.dev.service.runnables.*;

import java.io.*;
import java.util.*;

import static stijn.dev.resource.FrontEndApplication.importProcessIsRunning;

public class RomImportDragAndDroppable {
    private static Parent root;
    public static void createDragAndDropBehavior(Node pane) {
        pane.setOnDragOver(dragEvent -> {
            if (dragEvent.getGestureSource() != pane && dragEvent.getDragboard().hasFiles()){
                dragEvent.acceptTransferModes(TransferMode.LINK);
            }
            dragEvent.consume();
        });
        pane.setOnDragDropped(dragEvent -> {
            Dragboard dragboard = dragEvent.getDragboard();
            boolean success = dragboard.hasFiles();

            //Code Execution here
            if(!importProcessIsRunning){
                List<File> files = dragboard.getFiles();
                ImportProcess importProcess = new ImportProcess(files, pane);
                Thread thread = new Thread(importProcess);
                thread.run();
                importProcessIsRunning =true;
            }

//            FXMLLoader loader = new FXMLLoader(FrontEndApplication.class.getResource("importPlatformSelection.fxml"));
//
//            try {
//                root = loader.load();
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//            ImportPlatformSelectionController importPlatformSelectionController = loader.getController();
//
//
//            importPlatformSelectionController.setFiles(files);
//
//            importPlatformSelectionController.processPlatforms();
//            Stage stage = new Stage();
//            Stage mainWindow = (Stage) pane.getScene().getWindow();
//            stage.initOwner(mainWindow);
//            importPlatformSelectionController.setStage(stage);
//            Scene scene = new Scene(root);
//            stage.setScene(scene);
//            stage.show();
            dragEvent.setDropCompleted(success);
            dragEvent.consume();
        });
    }


}
