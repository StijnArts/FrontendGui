package stijn.dev.service;

import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.input.*;
import javafx.stage.*;
import javafx.util.*;
import stijn.dev.data.database.*;
import stijn.dev.resource.controllers.*;

import java.io.*;
import java.util.*;

public class RomImportDragAndDroppable {
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
                ArrayList<String> fileExtensions = FilenameService.extractFileExtensions(files);
                String platformSelection = String.valueOf(getClass().getResource("importPlatformSelection.fxml"));
                FXMLLoader loader = new FXMLLoader(getClass().getResource("importPlatformSelection.fxml"));
                try {
                    Parent root = loader.load();
                    Stage stage = new Stage();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                ImportPlatformSelectionController importPlatformSelectionController = loader.getController();
                importPlatformSelectionController.setFileExtensions(fileExtensions);
                files.forEach(file -> {
                    System.out.println(file.getAbsolutePath());
                    XMLParser.parseRoms(new Pair[]{new Pair<>(file.getName(), "Nintendo DS")});
                });

                dragEvent.setDropCompleted(success);
                dragEvent.consume();
            }
        });
    }


}
