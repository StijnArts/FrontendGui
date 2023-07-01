package stijn.dev.resource.controllers;

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

public class PlatformEditScreenController {

    @FXML
    private TextField platformNameField;
    @FXML
    private ComboBox<ComboBoxItemWrap<String>> publisherComboBox;
    @FXML
    private ComboBox<ComboBoxItemWrap<String>> manufacturerComboBox;
    @FXML
    private ComboBox<ComboBoxItemWrap<String>> mediaComboBox;
    @FXML
    private ComboBox<ComboBoxItemWrap<String>> mediaTypeComboBox;
    @FXML
    private ComboBox<String> productFamilyComboBox;
    @FXML
    private TextField platformGenerationField;
    @FXML
    private TextField unitsSoldField;
    @FXML
    private DatePicker discontinuedDatePicker;
    @FXML
    private TextArea defaultSummaryTextArea;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private TableView2<AlternateName> alternateNamesTable;
    @FXML
    private TableView2<RelatedPlatform> relatedPlatformsTable;
    @FXML
    private TableView2<PlatformSpecification> specificationTable;
    @FXML
    private TableView2<Trivia> triviaTable;
    @FXML
    private TableView2<ReleaseDate> releaseDatesTable;

    @FXML
    private TableView2<Emulator> emulatorTable;
    private static PlatformDAO platformDAO = new PlatformDAO();

    //TODO double clicking on a media item should open a zoomed in gallery view with details and a full size view of the image
    //TODO double clicking on a supplementary material item should open the supplementary media group it belongs to (for example an anime or tv show overview)

    private Stage stage;
    private Platform platform;
    private PlatformEditPrepper platformEditPrepper = new PlatformEditPrepper();
    private PlatformConfigurationScreenController platformConfigurationScreenController;

    public static PlatformEditScreenController create(Platform platform, FXMLLoader loader, Stage stage, PlatformConfigurationScreenController platformConfigurationScreenController){
        PlatformEditScreenController controller = loader.getController();
        controller.configure(platform, stage, platformConfigurationScreenController);
        return controller;
    }

    public static PlatformEditScreenController create(Platform platform, FXMLLoader loader, Stage stage){
        PlatformEditScreenController controller = loader.getController();
        controller.configure(platform, stage);
        return controller;
    }

    public static void openPlatformEditScreen(Platform platform, Stage stage){
        FXMLLoader loader = FXMLLoaderUtil.createFMXLLoader("platformEditScreen.fxml");
        Parent root = RootUtil.createRoot(loader);
        Stage platformStage = new Stage();
        platformStage.initOwner(stage);
        Scene scene = new Scene(root);
        PlatformEditScreenController.create(platform, loader, platformStage);
        platformStage.setScene(scene);
        platformStage.show();
    }

    public static PlatformConfigurationScreenController create(FXMLLoader loader, Stage stage){
        PlatformConfigurationScreenController controller = loader.getController();
        controller.configure(platformDAO.getPlatforms(), stage);
        return controller;
    }

    public void configure(Platform platform, Stage stage) {
        this.platform = platform;
        this.stage = stage;
    }

    public void configure(Platform platform, Stage stage, PlatformConfigurationScreenController platformConfigurationScreenController){
        configure(platform, stage);
        this.platformConfigurationScreenController = platformConfigurationScreenController;
        platformEditPrepper.configure(this);
    }

    public void savePlatform() {
        //TODO implement saving platform and signalling to config screen to refresh
    }

    public void openPlatformMediaTypeManagementScreen() {
        //TODO open PlatformMediaTypeManagementScreen
    }

    public void openPlatformSupplementaryMaterialTypeManagementScreen() {
        //TODO open PlatformSupplementaryMaterialTypeManagementScreen
    }

    public void openCompanyEditScreen() {
        //TODO open CompanyEditScreen
    }

    public void close() {
        stage.close();
    }

    public TextField getPlatformNameField() {
        return platformNameField;
    }

    public void setPlatformNameField(TextField platformNameField) {
        this.platformNameField = platformNameField;
    }

    public ComboBox<ComboBoxItemWrap<String>> getPublisherComboBox() {
        return publisherComboBox;
    }

    public void setPublisherComboBox(ComboBox<ComboBoxItemWrap<String>> publisherComboBox) {
        this.publisherComboBox = publisherComboBox;
    }

    public ComboBox<ComboBoxItemWrap<String>> getManufacturerComboBox() {
        return manufacturerComboBox;
    }

    public void setManufacturerComboBox(ComboBox<ComboBoxItemWrap<String>> manufacturerComboBox) {
        this.manufacturerComboBox = manufacturerComboBox;
    }

    public ComboBox<ComboBoxItemWrap<String>> getMediaComboBox() {
        return mediaComboBox;
    }

    public void setMediaComboBox(ComboBox<ComboBoxItemWrap<String>> mediaComboBox) {
        this.mediaComboBox = mediaComboBox;
    }

    public ComboBox<ComboBoxItemWrap<String>> getMediaTypeComboBox() {
        return mediaTypeComboBox;
    }

    public void setMediaTypeComboBox(ComboBox<ComboBoxItemWrap<String>> mediaTypeComboBox) {
        this.mediaTypeComboBox = mediaTypeComboBox;
    }

    public ComboBox<String> getProductFamilyComboBox() {
        return productFamilyComboBox;
    }

    public void setProductFamilyComboBox(ComboBox<String> productFamilyComboBox) {
        this.productFamilyComboBox = productFamilyComboBox;
    }

    public TextField getPlatformGenerationField() {
        return platformGenerationField;
    }

    public void setPlatformGenerationField(TextField platformGenerationField) {
        this.platformGenerationField = platformGenerationField;
    }

    public TextField getUnitsSoldField() {
        return unitsSoldField;
    }

    public void setUnitsSoldField(TextField unitsSoldField) {
        this.unitsSoldField = unitsSoldField;
    }

    public DatePicker getDiscontinuedDatePicker() {
        return discontinuedDatePicker;
    }

    public void setDiscontinuedDatePicker(DatePicker discontinuedDatePicker) {
        this.discontinuedDatePicker = discontinuedDatePicker;
    }

    public TextArea getDefaultSummaryTextArea() {
        return defaultSummaryTextArea;
    }

    public void setDefaultSummaryTextArea(TextArea defaultSummaryTextArea) {
        this.defaultSummaryTextArea = defaultSummaryTextArea;
    }

    public TextArea getDescriptionTextArea() {
        return descriptionTextArea;
    }

    public void setDescriptionTextArea(TextArea descriptionTextArea) {
        this.descriptionTextArea = descriptionTextArea;
    }

    public TableView2<AlternateName> getAlternateNamesTable() {
        return alternateNamesTable;
    }

    public void setAlternateNamesTable(TableView2<AlternateName> alternateNamesTable) {
        this.alternateNamesTable = alternateNamesTable;
    }

    public TableView2<RelatedPlatform> getRelatedPlatformsTable() {
        return relatedPlatformsTable;
    }

    public void setRelatedPlatformsTable(TableView2<RelatedPlatform> relatedPlatformsTable) {
        this.relatedPlatformsTable = relatedPlatformsTable;
    }

    public TableView2<PlatformSpecification> getSpecificationTable() {
        return specificationTable;
    }

    public void setSpecificationTable(TableView2<PlatformSpecification> specificationTable) {
        this.specificationTable = specificationTable;
    }

    public TableView2<Trivia> getTriviaTable() {
        return triviaTable;
    }

    public void setTriviaTable(TableView2<Trivia> triviaTable) {
        this.triviaTable = triviaTable;
    }

    public TableView2<ReleaseDate> getReleaseDatesTable() {
        return releaseDatesTable;
    }

    public void setReleaseDatesTable(TableView2<ReleaseDate> releaseDatesTable) {
        this.releaseDatesTable = releaseDatesTable;
    }

    public TableView2<Emulator> getEmulatorTable() {
        return emulatorTable;
    }

    public void setEmulatorTable(TableView2<Emulator> emulatorTable) {
        this.emulatorTable = emulatorTable;
    }

    public static PlatformDAO getPlatformDAO() {
        return platformDAO;
    }

    public static void setPlatformDAO(PlatformDAO platformDAO) {
        PlatformEditScreenController.platformDAO = platformDAO;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public PlatformEditPrepper getPlatformEditPrepper() {
        return platformEditPrepper;
    }

    public void setPlatformEditPrepper(PlatformEditPrepper platformEditPrepper) {
        this.platformEditPrepper = platformEditPrepper;
    }

    public PlatformConfigurationScreenController getPlatformConfigurationScreenController() {
        return platformConfigurationScreenController;
    }

    public void setPlatformConfigurationScreenController(PlatformConfigurationScreenController platformConfigurationScreenController) {
        this.platformConfigurationScreenController = platformConfigurationScreenController;
    }
}
