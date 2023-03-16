package stijn.dev.resource.controllers;

import javafx.fxml.*;
import stijn.dev.datasource.objects.items.*;

public class EditController {
    private Game game;

    public static EditController create(FXMLLoader loader, Game game){
        EditController ec = loader.getController();
        ec.configure(game);
        return ec;
    }

    public void configure(Game game){
        this.game = game;
    }

}
