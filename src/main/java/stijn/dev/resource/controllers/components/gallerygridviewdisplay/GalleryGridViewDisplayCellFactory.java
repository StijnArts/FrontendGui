package stijn.dev.resource.controllers.components.gallerygridviewdisplay;

import javafx.util.*;
import org.controlsfx.control.*;

public class GalleryGridViewDisplayCellFactory implements Callback<GridView<GalleryGridViewDisplay>,GridCell<GalleryGridViewDisplay>> {
    @Override
    public GridCell<GalleryGridViewDisplay> call(GridView<GalleryGridViewDisplay> mediaGridViewDisplayGridView) {
        return new GalleryGridViewDisplayCell();
    }
}
