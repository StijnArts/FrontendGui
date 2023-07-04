package stijn.dev.resource.controllers.components;

import javafx.scene.*;
import javafx.scene.input.*;
import stijn.dev.threads.runnables.*;

import java.util.function.*;

public class MediaImportDragAndDroppable {
    public static void createDragAndDropBehavior(Node node, int id, Supplier<String> category) {
        setOnDragOver(node);
        setOnDragDropped(node, id, category);
    }

    private static void setOnDragOver(Node node){
        node.setOnDragOver(dragEvent -> {
            if (dragEvent.getGestureSource() != node && dragEvent.getDragboard().hasFiles()){
                dragEvent.acceptTransferModes(TransferMode.LINK);
            }
            dragEvent.consume();
        });
    }

    private static void setOnDragDropped(Node node, int id, Supplier<String> category){
        node.setOnDragDropped(dragEvent -> {
            Dragboard dragboard = dragEvent.getDragboard();
            boolean success = dragboard.hasFiles();
            Thread thread = MediaImportProcess.createMediaImportProcess(dragboard, node, id, category);
            thread.run();
            dragEvent.setDropCompleted(success);
            dragEvent.consume();
        });
    }
}
