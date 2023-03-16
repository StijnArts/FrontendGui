package stijn.dev.resource.controllers;

import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.controlsfx.control.*;
import stijn.dev.datasource.objects.items.*;
import stijn.dev.resource.controllers.components.*;
import stijn.dev.resource.controllers.components.GameGridViewDisplay.*;

import java.net.*;
import java.util.*;

public class MainController extends SceneController implements Initializable{
    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private GridView gamesGridView;
    @FXML
    private Accordion overviewAccordion;
    @FXML
    private HBox topControlBar;
    @FXML
    private VBox detailsPanel;
    @FXML
    private VBox bottomBar;
    @FXML
    private HBox progressBar;
    @FXML
    private HBox bottomControlBar;
    public static double cellWidth = 200;
    public static double cellHeight = 200;
    private RomImportDragAndDroppable romImportDragAndDroppable = new RomImportDragAndDroppable();

    public void configure(){
        ArrayList<GameGridViewDisplay> gameGridViewDisplays = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            gameGridViewDisplays.add(new GameGridViewDisplay(new GameImportItem("test game"),"image location"));
        }
        ObservableList games = FXCollections.observableList(gameGridViewDisplays);
        gamesGridView.setItems(games);
        gamesGridView.setCellFactory(new GameGridViewDisplayCellFactory());
        gamesGridView.cellWidthProperty().set(MainController.cellWidth);
        gamesGridView.cellHeightProperty().set(MainController.cellHeight);
        gamesGridView.setVerticalCellSpacing(50);

    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        romImportDragAndDroppable.createDragAndDropBehavior(mainBorderPane);
    }

    public void setRomImportDragAndDroppable(RomImportDragAndDroppable romImportDragAndDroppable) {
        this.romImportDragAndDroppable = romImportDragAndDroppable;
    }
}

//    public void playGame(ActionEvent e){
////        Emulator emulator = new Emulator("bsnes", "D:\\Emulator Installs\\bsnes_v115-windows\\bsnes.exe");
////        Game game = new Game("", "L:\\Super Nintendo Entertainment System\\Super Mario World (U) [!].smc");
////        Process process = emulator.launchGame(game);
//    }