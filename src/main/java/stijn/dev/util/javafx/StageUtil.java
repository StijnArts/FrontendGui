package stijn.dev.util.javafx;

import javafx.scene.*;
import javafx.stage.*;

public class StageUtil {

    public static Stage createStageFromExistingNode(Node node){
        Stage stage = new Stage();
        setOwnership(stage, node);
        return stage;
    }

    public static void setOwnership(Stage stage, Node node){
        Stage mainWindow = (Stage) node.getScene().getWindow();
        stage.initOwner(mainWindow);
    }
}
