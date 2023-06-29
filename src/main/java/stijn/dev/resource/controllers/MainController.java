package stijn.dev.resource.controllers;

import javafx.beans.property.*;
import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import org.controlsfx.control.*;
import stijn.dev.datasource.database.dao.*;
import stijn.dev.datasource.objects.items.*;
import stijn.dev.resource.controllers.components.GameGridViewDisplay.*;
import stijn.dev.resource.controllers.components.*;
import stijn.dev.resource.controllers.shortcut.*;
import stijn.dev.threads.runnables.*;

import java.net.*;
import java.util.*;

import static stijn.dev.resource.FrontEndApplication.*;

public class MainController extends SceneController implements Initializable{
    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private BorderPane gamesBorderPane;
    private GridView<GameGridViewDisplay> gamesGridView;
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
    @FXML
    private Pagination gamesPaginator;
    @FXML
    private TextField searchBar;
    public static double cellWidth = 200;
    public static double cellHeight = 200;
    private RomImportDragAndDroppable romImportDragAndDroppable = new RomImportDragAndDroppable();
    private GameDAO gameDAO = new GameDAO();
    private Stage stage;
    private Scene scene;
    public static BooleanProperty gamesHaveBeenAdded = new SimpleBooleanProperty(false);
    public void configure(Stage stage, Scene scene){
        this.stage = stage;
        this.scene = scene;
        gamesHaveBeenAdded.addListener((observableValue, aBoolean, t1) -> {
            if(gamesHaveBeenAdded.get()){
                refreshTable();
                gamesHaveBeenAdded.set(false);
            }
        });
        searchBar.setOnKeyPressed(keyEvent -> {
            if(keyEvent.getCode() == KeyCode.ENTER){
                refreshTable();
            }
        });
        gamesGridView = new GridView<>();
        gamesGridView.setCellFactory(new GameGridViewDisplayCellFactory());
        gamesGridView.cellWidthProperty().set(MainController.cellWidth);
        gamesGridView.cellHeightProperty().set(MainController.cellHeight);
        gamesGridView.setVerticalCellSpacing(50);
        gamesPaginator.setPageCount(gameDAO.getGameCount("")/21+1);
        gamesPaginator.setPageFactory(this::createPage);
        MainShortcutManager.configureShortcuts(this);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        romImportDragAndDroppable.createDragAndDropBehavior(mainBorderPane);
    }

    public Node createPage(int pageNumber){
        this.tabNumber = pageNumber;
        gamesGridView.setItems(FXCollections.observableList(getGameGridDisplayItems()));
        //gamesBorderPane.setCenter(gamesGridView);
        return gamesGridView;
    }

    public void setRomImportDragAndDroppable(RomImportDragAndDroppable romImportDragAndDroppable) {
        this.romImportDragAndDroppable = romImportDragAndDroppable;
    }

    private int tabNumber = 1;
    private final int gamesPerPage = 21;
    public ArrayList<GameGridViewDisplay> getGameGridDisplayItems(){
        ArrayList<GameGridViewDisplay> gameGridViewDisplays = new ArrayList<>();
        ArrayList<Game> games = gameDAO.getGames((tabNumber*gamesPerPage), searchBar.getText());
        for (Game game : games) {
            gameGridViewDisplays.add(new GameGridViewDisplay(game,"image location", this));
        }
        return gameGridViewDisplays;
    }
    private int gameCount = 0;
    public void refreshTable() {
        gamesGridView.setItems(FXCollections.observableList(getGameGridDisplayItems()));
        int oldGameCount = gameCount;
        gameCount = gameDAO.getGameCount(searchBar.getText());
        if(gameCount!=oldGameCount){
            gamesPaginator.setPageCount(gameCount/gamesPerPage+1);
        }

    }

    public void importGames(){
        if(!importProcessIsRunning){
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select the roms you wish to import");
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Files", "*.*"));
            Thread thread = ImportProcess.createImportProcess(fileChooser.showOpenMultipleDialog(stage), mainBorderPane);
            thread.run();
        }
    }

    public void openEditTagsMenu(){
        TagConfigurationScreenController.openTagConfigurationScreen(stage);
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }
}

//    public void playGame(ActionEvent e){
//        Emulator emulator = new Emulator("bsnes", "D:\\Emulator Installs\\bsnes_v115-windows\\bsnes.exe");
//        Game game = new Game("", "L:\\Super Nintendo Entertainment System\\Super Mario World (U) [!].smc");
//        Process process = emulator.launchGame(game);
//    }