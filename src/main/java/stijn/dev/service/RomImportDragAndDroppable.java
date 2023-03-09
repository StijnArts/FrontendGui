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
    public void createDragAndDropBehavior(Node node) {
        setOnDragOver(node);
        setOnDragDropped(node);
    }

    private void setOnDragOver(Node node){
        node.setOnDragOver(dragEvent -> {
            if (dragEvent.getGestureSource() != node && dragEvent.getDragboard().hasFiles()){
                dragEvent.acceptTransferModes(TransferMode.LINK);
            }
            dragEvent.consume();
        });
    }

    private void setOnDragDropped(Node node){
        node.setOnDragDropped(dragEvent -> {
            Dragboard dragboard = dragEvent.getDragboard();
            boolean success = dragboard.hasFiles();
            if(!importProcessIsRunning){
                Thread thread = ImportProcess.createImportProcess(dragboard, node);
                thread.run();
            }
            dragEvent.setDropCompleted(success);
            dragEvent.consume();
        });
    }
}
