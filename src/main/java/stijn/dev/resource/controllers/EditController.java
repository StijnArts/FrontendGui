package stijn.dev.resource.controllers;

import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import org.controlsfx.control.tableview2.*;
import stijn.dev.datasource.objects.data.*;
import stijn.dev.datasource.objects.items.*;
import stijn.dev.resource.controllers.components.*;
import stijn.dev.resource.service.*;

import java.util.*;

public class EditController {
    @FXML
    ComboBox<ComboBoxItemWrap<String>> developerComboCheckBox;
    @FXML
    ComboBox<ComboBoxItemWrap<String>> publisherComboCheckBox;
    @FXML
    ComboBox<ComboBoxItemWrap<String>> playModesComboCheckbox;
    @FXML
    ComboBox<ComboBoxItemWrap<String>> ratingComboCheckBox;
    @FXML
    ComboBox<String> platformComboBox;
    @FXML
    ComboBox<PriorityData> priorityComboBox;
    @FXML
    TableView2 alternateNamesTable;
    @FXML
    TableView2 relatedGameTable;
    @FXML
    TableView2 additionalAppsTable;
    @FXML
    TableView2 triviaTable;
    @FXML
    TableView2 releaseDatesTable;
    @FXML
    TableView2 staffTable;
    @FXML
    TableView2 characterTable;
    @FXML
    TableView2 tagTable;
    @FXML
    BorderPane warningBorderpane;
    @FXML
    TextField maxPlayersField;
    @FXML
    TextField titleTextField;
    @FXML
    TextField defaultSortingTitleTextField;
    @FXML
    TextArea descriptionTextArea;
    @FXML
    TextArea summaryTextArea;
    @FXML
    Button saveButton;
    @FXML
    Button exitButton;
    private Game game;
    private Game originalGame;
    private Stage stage;
    private EditService editService = new EditService();
    private EditPrepper editPrepper = new EditPrepper();
    public static EditController create(FXMLLoader loader, Game gameImportItem, Stage stage){
        EditController ec = loader.getController();
        ec.configure(gameImportItem, stage);
        return ec;
    }

    public void save(){
        editService.saveChanges(game,originalGame,stage, this);
    }

    public void configure(Game gameImportItem, Stage stage){
        warningBorderpane.setVisible(false);
        this.game = gameImportItem;
        this.originalGame = new Game(gameImportItem);
        this.stage = stage;
        editPrepper.configure(this);
    }
    public TextArea getSummaryTextArea() {
        return summaryTextArea;
    }
    public TableView2 getAlternateNamesTable() {
        return alternateNamesTable;
    }
    public TableView2 getRelatedGameTable() {
        return relatedGameTable;
    }
    public TableView2 getAdditionalAppsTable() {
        return additionalAppsTable;
    }
    public TableView2 getTriviaTable() {
        return triviaTable;
    }
    public TableView2 getReleaseDatesTable() {
        return releaseDatesTable;
    }
    public TableView2 getStaffTable() {
        return staffTable;
    }
    public TableView2 getCharacterTable() {
        return characterTable;
    }
    public TableView2 getTagTable() {
        return tagTable;
    }
    public BorderPane getWarningBorderpane() {
        return warningBorderpane;
    }
    public Button getSaveButton() {
        return saveButton;
    }
    public Button getExitButton() {
        return exitButton;
    }
    public Game getGame() {
        return game;
    }
    public void setGame(Game game) {
        this.game = game;
    }
    public Game getOriginalGame() {
        return originalGame;
    }
    public Stage getStage() {
        return stage;
    }
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    public EditService getEditService() {
        return editService;
    }
    public ComboBox<ComboBoxItemWrap<String>> getDeveloperComboCheckBox() {
        return developerComboCheckBox;
    }
    public ComboBox<ComboBoxItemWrap<String>> getPublisherComboCheckBox() {
        return publisherComboCheckBox;
    }
    public ComboBox<ComboBoxItemWrap<String>> getPlayModesComboCheckbox() {
        return playModesComboCheckbox;
    }
    public ComboBox<ComboBoxItemWrap<String>> getRatingComboCheckBox() {
        return ratingComboCheckBox;
    }
    public ComboBox<String> getPlatformComboBox() {
        return platformComboBox;
    }
    public ComboBox<PriorityData> getPriorityComboBox() {
        return priorityComboBox;
    }
    public TextField getMaxPlayersField() {
        return maxPlayersField;
    }
    public TextField getTitleTextField() {
        return titleTextField;
    }
    public TextField getDefaultSortingTitleTextField() {
        return defaultSortingTitleTextField;
    }
    public TextArea getDescriptionTextArea() {
        return descriptionTextArea;
    }
}
