package stijn.dev.resource.controllers;

import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.layout.*;
import stijn.dev.resource.controllers.components.*;

import java.io.*;
import java.net.*;
import java.util.*;

public class DemoController extends SceneController implements Initializable{

    @FXML
    private AnchorPane pane;

    //TODO @Inject
    private RomImportDragAndDroppable romImportDragAndDroppable = new RomImportDragAndDroppable();

    public void playGame(ActionEvent e){
//        Emulator emulator = new Emulator("bsnes", "D:\\Emulator Installs\\bsnes_v115-windows\\bsnes.exe");
//        Game game = new Game("", "L:\\Super Nintendo Entertainment System\\Super Mario World (U) [!].smc");
//        Process process = emulator.launchGame(game);
    }

    public void switchToImportScreen(ActionEvent event) throws IOException {
        //super.switchToImport(event);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        romImportDragAndDroppable.createDragAndDropBehavior(pane);
    }


    public void setRomImportDragAndDroppable(RomImportDragAndDroppable romImportDragAndDroppable) {
        this.romImportDragAndDroppable = romImportDragAndDroppable;
    }
}