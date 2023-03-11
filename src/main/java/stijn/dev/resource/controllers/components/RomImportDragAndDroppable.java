package stijn.dev.resource.controllers.components;

import javafx.scene.*;
import javafx.scene.input.*;
import stijn.dev.threads.runnables.*;

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
