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

    public TableView2 getAlternateNamesTable() {
        return alternateNamesTable;
    }

    public void setAlternateNamesTable(TableView2 alternateNamesTable) {
        this.alternateNamesTable = alternateNamesTable;
    }

    public TableView2 getRelatedGameTable() {
        return relatedGameTable;
    }

    public void setRelatedGameTable(TableView2 relatedGameTable) {
        this.relatedGameTable = relatedGameTable;
    }

    public TableView2 getAdditionalAppsTable() {
        return additionalAppsTable;
    }

    public void setAdditionalAppsTable(TableView2 additionalAppsTable) {
        this.additionalAppsTable = additionalAppsTable;
    }

    public TableView2 getTriviaTable() {
        return triviaTable;
    }

    public void setTriviaTable(TableView2 triviaTable) {
        this.triviaTable = triviaTable;
    }

    public TableView2 getReleaseDatesTable() {
        return releaseDatesTable;
    }

    public void setReleaseDatesTable(TableView2 releaseDatesTable) {
        this.releaseDatesTable = releaseDatesTable;
    }

    public TableView2 getStaffTable() {
        return staffTable;
    }

    public void setStaffTable(TableView2 staffTable) {
        this.staffTable = staffTable;
    }

    public TableView2 getCharacterTable() {
        return characterTable;
    }

    public void setCharacterTable(TableView2 characterTable) {
        this.characterTable = characterTable;
    }

    public TableView2 getTagTable() {
        return tagTable;
    }

    public void setTagTable(TableView2 tagTable) {
        this.tagTable = tagTable;
    }

    public BorderPane getWarningBorderpane() {
        return warningBorderpane;
    }

    public void setWarningBorderpane(BorderPane warningBorderpane) {
        this.warningBorderpane = warningBorderpane;
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public void setSaveButton(Button saveButton) {
        this.saveButton = saveButton;
    }

    public Button getExitButton() {
        return exitButton;
    }

    public void setExitButton(Button exitButton) {
        this.exitButton = exitButton;
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

    public void setOriginalGame(Game originalGame) {
        this.originalGame = originalGame;
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

    public void setEditService(EditService editService) {
        this.editService = editService;
    }

    public ComboBox<ComboBoxItemWrap<String>> getDeveloperComboCheckBox() {
        return developerComboCheckBox;
    }

    public void setDeveloperComboCheckBox(ComboBox<ComboBoxItemWrap<String>> developerComboCheckBox) {
        this.developerComboCheckBox = developerComboCheckBox;
    }

    public ComboBox<ComboBoxItemWrap<String>> getPublisherComboCheckBox() {
        return publisherComboCheckBox;
    }

    public void setPublisherComboCheckBox(ComboBox<ComboBoxItemWrap<String>> publisherComboCheckBox) {
        this.publisherComboCheckBox = publisherComboCheckBox;
    }

    public ComboBox<ComboBoxItemWrap<String>> getPlayModesComboCheckbox() {
        return playModesComboCheckbox;
    }

    public void setPlayModesComboCheckbox(ComboBox<ComboBoxItemWrap<String>> playModesComboCheckbox) {
        this.playModesComboCheckbox = playModesComboCheckbox;
    }

    public ComboBox<ComboBoxItemWrap<String>> getRatingComboCheckBox() {
        return ratingComboCheckBox;
    }

    public void setRatingComboCheckBox(ComboBox<ComboBoxItemWrap<String>> ratingComboCheckBox) {
        this.ratingComboCheckBox = ratingComboCheckBox;
    }

    public ComboBox<String> getPlatformComboBox() {
        return platformComboBox;
    }

    public void setPlatformComboBox(ComboBox<String> platformComboBox) {
        this.platformComboBox = platformComboBox;
    }

    public ComboBox<PriorityData> getPriorityComboBox() {
        return priorityComboBox;
    }

    public void setPriorityComboBox(ComboBox<PriorityData> priorityComboBox) {
        this.priorityComboBox = priorityComboBox;
    }

    public TextField getMaxPlayersField() {
        return maxPlayersField;
    }

    public void setMaxPlayersField(TextField maxPlayersField) {
        this.maxPlayersField = maxPlayersField;
    }

    public TextField getTitleTextField() {
        return titleTextField;
    }

    public void setTitleTextField(TextField titleTextField) {
        this.titleTextField = titleTextField;
    }

    public TextField getDefaultSortingTitleTextField() {
        return defaultSortingTitleTextField;
    }

    public void setDefaultSortingTitleTextField(TextField defaultSortingTitleTextField) {
        this.defaultSortingTitleTextField = defaultSortingTitleTextField;
    }

    public TextArea getDescriptionTextArea() {
        return descriptionTextArea;
    }

    public void setDescriptionTextArea(TextArea descriptionTextArea) {
        this.descriptionTextArea = descriptionTextArea;
    }
}
