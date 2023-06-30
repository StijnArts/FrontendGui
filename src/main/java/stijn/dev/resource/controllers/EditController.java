package stijn.dev.resource.controllers;

import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.*;
import org.controlsfx.control.tableview2.*;
import stijn.dev.datasource.database.dao.*;
import stijn.dev.datasource.objects.data.*;
import stijn.dev.datasource.objects.items.*;
import stijn.dev.resource.controllers.components.*;
import stijn.dev.resource.service.*;
import stijn.dev.util.javafx.*;

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
    TableView2<Tag> tagTable;
    @FXML
    TextField maxPlayersField;
    @FXML
    TextField titleField;
    @FXML
    TextField defaultSortingTitleTextField;
    @FXML
    TextArea descriptionTextArea;
    @FXML
    TextArea summaryTextArea;
    @FXML
    TextField unitsSoldField;
    @FXML
    Button saveButton;
    @FXML
    Button exitButton;
    private Game game;
    private Game originalGame;
    private Stage stage;
    private EditService editService = new EditService();
    private EditPrepper editPrepper = new EditPrepper();
    private TagDAO tagDAO = new TagDAO();
    private ObservableList<String> tagOptions;
    private ObservableList<String> platformOptions;
    private static MainController mainController;
    public static EditController create(FXMLLoader loader, Game gameImportItem, Stage stage, MainController mainControllerIn){
        EditController ec = loader.getController();
        ec.configure(gameImportItem, stage);
        mainController = mainControllerIn;
        return ec;
    }

    public void save(){
        editService.saveChanges(originalGame, this);
        mainController.refreshTable();
        stage.close();
    }

    public void openPlatformConfigurationScreen(){
        FXMLLoader loader = FXMLLoaderUtil.createFMXLLoader("platformConfigurationScreen.fxml");
        Parent root = RootUtil.createRoot(loader);
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.initOwner(this.stage);
        PlatformConfigurationScreenController.create(loader, stage, this);
        stage.setScene(scene);
        stage.show();
    }

    public void openPriorityConfigScreen(){
        //TODO open the priority configuration screen
    }

    public void openRatingSettingsScreen(){
        //TODO open rating edit screen
    }

    public void openGameRelationshipTypeScreen(){
        //TODO open relationship Type screen
    }

    public void openStaffEditScreen(){
        //TODO open staff edit screen
    }

    public void openStaffRoleConfigurationScreen(){
        //TODO open role configurationScreen
    }

    public void openCharacterEditScreen(){
        //TODO open character edit screen
    }

    public void openCharacterRoleConfigurationScreen(){
        //TODO open character role configuration screen
    }

    public void openGameMediaTypeManagementScreen(){
        //TODO open GameMediaTypeManagementScreen
    }

    public void openGameSupplementaryMaterialTypeManagementScreen(){
        //TODO open GameSupplementaryMaterialTypeManagementScreen
    }

    public void openTagConfigurationScreen(){
        FXMLLoader loader = FXMLLoaderUtil.createFMXLLoader("tagConfigurationScreen.fxml");
        Parent root = RootUtil.createRoot(loader);
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.initOwner(this.stage);
        TagConfigurationScreenController.create(loader, stage, this);
        stage.setScene(scene);
        stage.show();
    }

    public void configure(Game gameImportItem, Stage stage){
        this.game = gameImportItem;
        this.originalGame = new Game(gameImportItem);
        this.stage = stage;
        editPrepper.configure(this);
    }

    public void updateTags(){
        tagOptions.removeAll();
        tagOptions.setAll(FXCollections.observableList(tagDAO.getTags()));
        game.setTags(tagDAO.getTags(Integer.valueOf(game.getDatabaseId())));
        tagTable.setItems(FXCollections.observableList(tagDAO.getTags(Integer.valueOf(game.getDatabaseId()))));
        tagTable.getItems().add(new Tag(""));
    }

    public void updatePlatforms() {
        platformOptions.removeAll();
        platformOptions.setAll(FXCollections.observableList(queryPlatformOptions()));
        PlatformDAO platformDAO = new PlatformDAO();
        game.setPlatform(platformDAO.getPlatform(Integer.valueOf(game.getDatabaseId())));
    }

    public List<String> queryPlatformOptions() {
        PlatformDAO platformDAO = new PlatformDAO();
        List<String> options = new ArrayList<>();
        for (Platform platform : platformDAO.getPlatforms()){
            options.add(platform.getPlatformName());
        }
        return options;
    }

    public ObservableList<String> getPlatformOptions() {
        return platformOptions;
    }

    public void setPlatformOptions(ObservableList<String> platformOptions) {
        this.platformOptions = platformOptions;
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

    public ObservableList<String> getTagOptions() {
        return tagOptions;
    }

    public void setTagOptions(ObservableList<String> tagOptions) {
        this.tagOptions = tagOptions;
    }

    public void setDeveloperComboCheckBox(ComboBox<ComboBoxItemWrap<String>> developerComboCheckBox) {
        this.developerComboCheckBox = developerComboCheckBox;
    }

    public void setPublisherComboCheckBox(ComboBox<ComboBoxItemWrap<String>> publisherComboCheckBox) {
        this.publisherComboCheckBox = publisherComboCheckBox;
    }

    public void setPlayModesComboCheckbox(ComboBox<ComboBoxItemWrap<String>> playModesComboCheckbox) {
        this.playModesComboCheckbox = playModesComboCheckbox;
    }

    public void setRatingComboCheckBox(ComboBox<ComboBoxItemWrap<String>> ratingComboCheckBox) {
        this.ratingComboCheckBox = ratingComboCheckBox;
    }

    public void setPlatformComboBox(ComboBox<String> platformComboBox) {
        this.platformComboBox = platformComboBox;
    }

    public void setPriorityComboBox(ComboBox<PriorityData> priorityComboBox) {
        this.priorityComboBox = priorityComboBox;
    }

    public void setAlternateNamesTable(TableView2 alternateNamesTable) {
        this.alternateNamesTable = alternateNamesTable;
    }

    public void setRelatedGameTable(TableView2 relatedGameTable) {
        this.relatedGameTable = relatedGameTable;
    }

    public void setAdditionalAppsTable(TableView2 additionalAppsTable) {
        this.additionalAppsTable = additionalAppsTable;
    }

    public void setTriviaTable(TableView2 triviaTable) {
        this.triviaTable = triviaTable;
    }

    public void setReleaseDatesTable(TableView2 releaseDatesTable) {
        this.releaseDatesTable = releaseDatesTable;
    }

    public void setStaffTable(TableView2 staffTable) {
        this.staffTable = staffTable;
    }

    public void setCharacterTable(TableView2 characterTable) {
        this.characterTable = characterTable;
    }

    public void setTagTable(TableView2<Tag> tagTable) {
        this.tagTable = tagTable;
    }

    public void setMaxPlayersField(TextField maxPlayersField) {
        this.maxPlayersField = maxPlayersField;
    }

    public void setTitleField(TextField titleField) {
        this.titleField = titleField;
    }

    public void setDefaultSortingTitleTextField(TextField defaultSortingTitleTextField) {
        this.defaultSortingTitleTextField = defaultSortingTitleTextField;
    }

    public void setDescriptionTextArea(TextArea descriptionTextArea) {
        this.descriptionTextArea = descriptionTextArea;
    }

    public void setSummaryTextArea(TextArea summaryTextArea) {
        this.summaryTextArea = summaryTextArea;
    }

    public TextField getUnitsSoldField() {
        return unitsSoldField;
    }

    public void setUnitsSoldField(TextField unitsSoldField) {
        this.unitsSoldField = unitsSoldField;
    }

    public void setSaveButton(Button saveButton) {
        this.saveButton = saveButton;
    }

    public void setExitButton(Button exitButton) {
        this.exitButton = exitButton;
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
    public TextField getTitleField() {
        return titleField;
    }
    public TextField getDefaultSortingTitleTextField() {
        return defaultSortingTitleTextField;
    }
    public TextArea getDescriptionTextArea() {
        return descriptionTextArea;
    }

    public void exit() {
        stage.close();
    }

}
