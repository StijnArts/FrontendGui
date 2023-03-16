package stijn.dev.resource.controllers.components.GameGridViewDisplay;

import org.controlsfx.control.*;
import stijn.dev.resource.controllers.*;

public class GameGridViewDisplayCell extends GridCell<GameGridViewDisplay> {
    public GameGridViewDisplayCell(){
        super();
    }

    private int previousWidth = 0;
    private int previousHeight = 0;
    @Override
    public void updateItem(GameGridViewDisplay gameGridViewDisplay, boolean empty){
        super.updateItem(gameGridViewDisplay, empty);
        if(empty || gameGridViewDisplay == null){
            setGraphic(null);

        } else {
            if(checkHeightWidth()){
                gameGridViewDisplay.setHeight(MainController.cellHeight);
                gameGridViewDisplay.setWidth(MainController.cellWidth);
            }
            setItem(gameGridViewDisplay);
            setText(gameGridViewDisplay.getGame().getName());
            setGraphic(gameGridViewDisplay);
        }
    }

    private boolean checkHeightWidth(){
        if(previousWidth!=MainController.cellWidth || previousHeight!=MainController.cellHeight){
            return true;
        }
        return false;
    }
}
