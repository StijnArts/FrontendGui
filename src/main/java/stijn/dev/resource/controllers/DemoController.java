package stijn.dev.resource.controllers;

import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.layout.*;
import stijn.dev.service.*;
import stijn.dev.data.objects.items.*;

import java.io.*;
import java.net.*;
import java.util.*;

public class DemoController extends SceneController implements Initializable{

    @FXML
    private AnchorPane pane;
    public void playGame(ActionEvent e){
        Emulator emulator = new Emulator("bsnes", "D:\\Emulator Installs\\bsnes_v115-windows\\bsnes.exe");
        Game game = new Game("", "L:\\Super Nintendo Entertainment System\\Super Mario World (U) [!].smc");
        Process process = emulator.launchGame(game);
    }

    public void switchToImportScreen(ActionEvent event) throws IOException {
        super.switchToImport(event);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        RomImportDragAndDroppable.createDragAndDropBehavior(pane);
    }
}