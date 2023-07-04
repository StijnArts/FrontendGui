package stijn.dev.resource.controllers.components.gamegridviewdisplay;

import javafx.util.*;
import org.controlsfx.control.*;

public class GameGridViewDisplayCellFactory implements Callback<GridView<GameGridViewDisplay>,GridCell<GameGridViewDisplay>> {
    @Override
    public GridCell<GameGridViewDisplay> call(GridView<GameGridViewDisplay> gameGridViewDisplayGridView) {
        return new GameGridViewDisplayCell();
    }
}
