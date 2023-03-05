package stijn.dev.service;

import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.input.*;
import javafx.stage.*;
import stijn.dev.resource.*;
import stijn.dev.resource.controllers.*;

import java.io.*;
import java.util.*;

public class RomImportDragAndDroppable {
    private static Parent root;
    public static void createDragAndDropBehavior(Node pane) {
        pane.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent dragEvent) {
                if (dragEvent.getGestureSource() != pane && dragEvent.getDragboard().hasFiles()){
                    dragEvent.acceptTransferModes(TransferMode.LINK);
                }
                dragEvent.consume();
            }
        });
        pane.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent dragEvent) {
                Dragboard dragboard = dragEvent.getDragboard();
                boolean success = false;
                if(dragboard.hasFiles()){
                    success = true;
                }

                //Code Execution here
                List<File> files = dragboard.getFiles();
                FXMLLoader loader = new FXMLLoader(FrontEndApplication.class.getResource("importPlatformSelection.fxml"));

                try {
                    root = loader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                ImportPlatformSelectionController importPlatformSelectionController = loader.getController();
                importPlatformSelectionController.setFiles(files);

                importPlatformSelectionController.processRoms();
                Stage stage = new Stage();
                importPlatformSelectionController.setStage(stage);
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
                dragEvent.setDropCompleted(success);
                dragEvent.consume();
            }
        });
    }


}
