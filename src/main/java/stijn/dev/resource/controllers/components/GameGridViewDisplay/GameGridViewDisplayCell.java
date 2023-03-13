package stijn.dev.resource.controllers.components.GameGridViewDisplay;

import org.controlsfx.control.*;
import stijn.dev.resource.controllers.*;

public class GameGridViewDisplayCell extends GridCell<GameGridViewDisplay> {
    public GameGridViewDisplayCell(){
        super();
    }

    @Override
    public void updateItem(GameGridViewDisplay gameGridViewDisplay, boolean empty){
        super.updateItem(gameGridViewDisplay, empty);
        if(empty || gameGridViewDisplay == null){
            setGraphic(null);

        } else {
            if(getWidth()!= MainController.cellWidth||getHeight()!= MainController.cellHeight){
                gameGridViewDisplay.setHeight(MainController.cellHeight);
                gameGridViewDisplay.setWidth(MainController.cellWidth);
            }
            setItem(gameGridViewDisplay);
            setText(gameGridViewDisplay.getGameTitle());
            setGraphic(gameGridViewDisplay);
        }
    }
}
