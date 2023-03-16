package stijn.dev.resource.controllers;

import javafx.fxml.*;
import stijn.dev.datasource.objects.items.*;

public class EditController {
    private GameImportItem gameImportItem;

    public static EditController create(FXMLLoader loader, GameImportItem gameImportItem){
        EditController ec = loader.getController();
        ec.configure(gameImportItem);
        return ec;
    }

    public void configure(GameImportItem gameImportItem){
        this.gameImportItem = gameImportItem;
    }

}
