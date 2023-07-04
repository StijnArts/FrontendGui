package stijn.dev.resource.controllers.components.gallerygridviewdisplay;

import org.controlsfx.control.*;
import stijn.dev.resource.controllers.*;

public class GalleryGridViewDisplayCell extends GridCell<GalleryGridViewDisplay> {
    public GalleryGridViewDisplayCell(){
        super();
    }

    private int previousWidth = 0;
    private int previousHeight = 0;
    @Override
    public void updateItem(GalleryGridViewDisplay gameGridViewDisplay, boolean empty){
        super.updateItem(gameGridViewDisplay, empty);
        if(empty || gameGridViewDisplay == null){
            setGraphic(null);

        } else {
            if(checkHeightWidth()){
                gameGridViewDisplay.setHeight(ImageDetailScreenController.getCellHeight());
                gameGridViewDisplay.setWidth(ImageDetailScreenController.getCellWidth());
            }
            setItem(gameGridViewDisplay);
            setText(gameGridViewDisplay.getMedia().getFile().getName());
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
