package stijn.dev.resource.service;

import javafx.beans.binding.*;
import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.util.*;
import org.controlsfx.control.*;
import org.controlsfx.control.tableview2.*;
import org.controlsfx.control.tableview2.cell.*;
import stijn.dev.datasource.database.dao.*;
import stijn.dev.datasource.objects.data.*;
import stijn.dev.datasource.objects.items.*;
import stijn.dev.resource.controllers.*;
import stijn.dev.resource.controllers.components.*;
import stijn.dev.resource.controllers.components.gallerygridviewdisplay.*;
import stijn.dev.util.javafx.*;

import java.time.*;
import java.util.*;

import static stijn.dev.resource.controllers.components.ComboBoxItemWrap.*;

public class PlatformEditPrepper {
    private int mediaPerPage = 12;
    private int mediaPageNumber = 1;
    private PlatformEditScreenController platformEditScreenController;
    public static BooleanProperty galleryNeedsRefresh = new SimpleBooleanProperty(false);
    private GalleryDAO galleryDAO = new GalleryDAO();
    public void configure(PlatformEditScreenController platformEditScreenController) {
        this.platformEditScreenController = platformEditScreenController;
        //General
        configurePlatformNameField(platformEditScreenController);
        configurePublisherCheckComboBox(platformEditScreenController);
        configureManufacturerCheckComboBox(platformEditScreenController);
        configureMediaCheckComboBox(platformEditScreenController);
        configureMediaTypeCheckComboBox(platformEditScreenController);
        configureProductFamilyComboBox(platformEditScreenController);
        configureGenerationField(platformEditScreenController);
        configureUnitsSoldField(platformEditScreenController);
        configureDiscontinuedOnDatePicker(platformEditScreenController);
        configureDefaultSummaryTextArea(platformEditScreenController);

        configureDescriptionTextArea(platformEditScreenController);
        configureAlternateNamesTable(platformEditScreenController);
        configureRelatedPlatformsTable(platformEditScreenController);
        configureSpecificationsTable(platformEditScreenController);
        configureTriviaTable(platformEditScreenController);
        configureReleaseDatesTable(platformEditScreenController);

        configureMediaGridView(platformEditScreenController);
        configureMediaListView(platformEditScreenController);
        configureMediaPaginator(platformEditScreenController);

//        configureSupplementaryMaterialListView(platformEditScreenController);
//        configureSupplementaryMaterialPaginator(platformEditScreenController);

        configureEmulationTable(platformEditScreenController);
    }

    private void configureMediaGridView(PlatformEditScreenController platformEditScreenController) {
        platformEditScreenController.setMediaGridView(new GridView<>());
        GridView<GalleryGridViewDisplay> gamesGridView = platformEditScreenController.getMediaGridView();
        gamesGridView.setCellFactory(new GalleryGridViewDisplayCellFactory());
        gamesGridView.cellWidthProperty().set(MainController.cellWidth);
        gamesGridView.cellHeightProperty().set(MainController.cellHeight);
        gamesGridView.setVerticalCellSpacing(30);
    }

    private void configureMediaPaginator(PlatformEditScreenController platformEditScreenController) {
        platformEditScreenController.getMediaPaginator().setPageFactory(this::createMediaPage);
        MediaImportDragAndDroppable.createDragAndDropBehavior(platformEditScreenController.getMediaPaginator(),
                platformEditScreenController.getPlatform().getId(), ()->platformEditScreenController.getPlatformMediaTypeList().getSelectionModel().getSelectedItem());
        galleryNeedsRefresh.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if(galleryNeedsRefresh.get()){
                    selectNewMediaItemPaginator(platformEditScreenController);
                    galleryNeedsRefresh.set(false);
                }
            }
        });
    }

    private Node createMediaPage(int pageNumber) {
        this.mediaPageNumber = pageNumber;
        platformEditScreenController.getMediaGridView().setItems(FXCollections.observableList(getGalleryGridViewDisplays(platformEditScreenController)));
        return platformEditScreenController.getMediaGridView();
    }
    private void selectNewMediaItemPaginator(PlatformEditScreenController platformEditScreenController) {
        ArrayList<GalleryGridViewDisplay> galleryGridViewDisplays = getGalleryGridViewDisplays(platformEditScreenController);
        platformEditScreenController.getMediaGridView().setItems(FXCollections.observableList(galleryGridViewDisplays));
        int mediaCount = galleryDAO.getGalleryCount(platformEditScreenController.getPlatformMediaTypeList().getSelectionModel().getSelectedItem(),
                platformEditScreenController.getPlatform().getId());
        platformEditScreenController.getMediaPaginator().setPageCount(mediaCount/mediaPerPage+1);
    }

    private ArrayList<GalleryGridViewDisplay> getGalleryGridViewDisplays(PlatformEditScreenController platformEditScreenController) {
        ArrayList<GalleryGridViewDisplay> galleryGridViewDisplays = new ArrayList<>();
        List<GalleryItem> games = galleryDAO.getMedia((mediaPageNumber*mediaPerPage), platformEditScreenController.getPlatform().getId(),
                platformEditScreenController.getPlatformMediaTypeList().getSelectionModel().getSelectedItem());
        for (GalleryItem galleryItem : games) {
            galleryGridViewDisplays.add(new GalleryGridViewDisplay(galleryItem,()->{}, platformEditScreenController.getPlatform().getId(), galleryNeedsRefresh));
        }
        return galleryGridViewDisplays;
    }

    private void configureMediaListView(PlatformEditScreenController platformEditScreenController) {
        platformEditScreenController.getPlatformMediaTypeList().setItems(FXCollections.observableList(galleryDAO.getPlatformGalleryTypes()));
        platformEditScreenController.getPlatformMediaTypeList().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                selectNewMediaItemPaginator(platformEditScreenController);
            }
        });
        platformEditScreenController.getPlatformMediaTypeList().getSelectionModel().select(0);
    }

    private void configureEmulationTable(PlatformEditScreenController platformEditScreenController) {
        EmulatorDAO emulatorDAO = new EmulatorDAO();
        platformEditScreenController.getEmulatorTable().setEditable(true);
        platformEditScreenController.getEmulatorTable().setItems(FXCollections.observableList(platformEditScreenController.getPlatform().getEmulators()));
        platformEditScreenController.getEmulatorTable().getItems().add(new Emulator("","","",""));
        HashMap<String, Emulator> emulatorHashMap = emulatorDAO.getEmulators();
        ObservableList<String> emulators = FXCollections.observableList(emulatorHashMap.keySet().stream().toList());
        TableColumn2<Emulator,String> argumentsColumn = new FilteredTableColumn<>("Launch Arguments");
        argumentsColumn.setEditable(true);
        argumentsColumn.setCellValueFactory(p -> p.getValue().argsProperty());
        argumentsColumn.setCellFactory(TextField2TableCell.forTableColumn());
        argumentsColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Emulator, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Emulator, String> event) {
                if(!event.getNewValue().isBlank()){
                    if(event.getTableView().getItems().size()-1==event.getTablePosition().getRow()){
                        event.getTableView().getItems().add(new Emulator("","","",""));
                    }
                }
                event.getTableView().getItems().get(event.getTablePosition().getRow()).setArgs(event.getNewValue());
            }
        });
        TableColumn2<Emulator, StringProperty> emulatorColumn = new FilteredTableColumn<>("Emulator");
        emulatorColumn.setCellValueFactory(i-> {
            final StringProperty value = i.getValue().nameProperty();
            return Bindings.createObjectBinding(()->value);
        });
        emulatorColumn.setCellFactory(col -> {
            TableCell<Emulator,StringProperty> c = new TableCell<>();
            final BorderPane borderPane = new BorderPane();
            final ComboBox<String> comboBox = new ComboBox<>(emulators);
            borderPane.centerProperty().setValue(comboBox);
            comboBox.setEditable(true);
            c.itemProperty().addListener(((observableValue, oldValue, newValue) -> {
                if(oldValue != null){
                    comboBox.valueProperty().unbindBidirectional(oldValue);
                }
                if(newValue != null){
                    comboBox.valueProperty().bindBidirectional(newValue);
                }
            }));
            ComboBoxAutocompleteUtil.autoCompleteComboBoxPlus(comboBox, (typedText, itemToCompare) -> itemToCompare.toLowerCase().contains(typedText.toLowerCase()));
            comboBox.valueProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                    if(comboBox.getValue()!=null&&!comboBox.getValue().isBlank()) {
                        c.getTableView().getItems().get(c.getIndex()).setName(comboBox.getValue());
                        c.getTableView().getItems().get(c.getIndex()).setArgs(emulatorHashMap.get(comboBox.valueProperty().get()).getArgs());
                        if(c.getTableView().getItems().size()-1==c.getIndex()){
                            c.getTableView().getItems().add(new Emulator("","","",""));
                        }
                    } else {
                        c.getTableView().getItems().get(c.getIndex()).setName(null);
                    }
                }
            });
            c.graphicProperty().bind(Bindings.when(c.emptyProperty()).then((Node)null).otherwise(comboBox));
            return c;
        });
        emulatorColumn.setEditable(true);
        platformEditScreenController.getEmulatorTable().getColumns().setAll(emulatorColumn,argumentsColumn);
    }

    private void configureReleaseDatesTable(PlatformEditScreenController platformEditScreenController) {
        TerritoryDAO territoryDAO = new TerritoryDAO();
        platformEditScreenController.getReleaseDatesTable().setEditable(true);
        ObservableList<String> territories = FXCollections.observableList(territoryDAO.getTerritories());
        ObservableList<ReleaseDate> releaseDates = FXCollections.observableList(platformEditScreenController.getPlatform().getReleaseDates());
        platformEditScreenController.getReleaseDatesTable().setItems(releaseDates);
        platformEditScreenController.getReleaseDatesTable().getItems().add(new ReleaseDate("",null));
        TableColumn2<ReleaseDate, StringProperty> territoryColumn = new FilteredTableColumn<>("Territory");
        territoryColumn.setCellValueFactory(i-> {
            final StringProperty value = i.getValue().getTerritory();
            return Bindings.createObjectBinding(()->value);
        });
        territoryColumn.setCellFactory(col -> {
            TableCell<ReleaseDate,StringProperty> c = new TableCell<>();
            final BorderPane borderPane = new BorderPane();
            final ComboBox<String> comboBox = new ComboBox<>(territories);
            borderPane.centerProperty().setValue(comboBox);
            comboBox.setEditable(true);
            c.itemProperty().addListener(((observableValue, oldValue, newValue) -> {
                if(oldValue != null){
                    comboBox.valueProperty().unbindBidirectional(oldValue);
                }
                if(newValue != null){
                    comboBox.valueProperty().bindBidirectional(newValue);
                }
            }));
            ComboBoxAutocompleteUtil.autoCompleteComboBoxPlus(comboBox, (typedText, itemToCompare) -> itemToCompare.toLowerCase().contains(typedText.toLowerCase()));
            comboBox.valueProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                    if(comboBox.getValue()!=null&&!comboBox.getValue().isBlank()) {
                        c.getTableView().getItems().get(c.getIndex()).setTerritory(new SimpleStringProperty(comboBox.getValue()));
                        if(c.getTableView().getItems().size()-1==c.getIndex()){
                            c.getTableView().getItems().add(new ReleaseDate("",null));
                        }
                    } else {
                        c.getTableView().getItems().get(c.getIndex()).setTerritory(new SimpleStringProperty(comboBox.getValue()));
                    }
                }
            });
            c.graphicProperty().bind(Bindings.when(c.emptyProperty()).then((Node)null).otherwise(comboBox));
            return c;
        });
        TableColumn2<ReleaseDate, ObjectProperty<LocalDate>> dateColumn = new FilteredTableColumn<>("Release Date");
        dateColumn.setCellValueFactory(i-> {
            final ObjectProperty<LocalDate> value = i.getValue().getDate();
            return Bindings.createObjectBinding(()->value);
        });
        dateColumn.setCellFactory(col -> {
            TableCell<ReleaseDate,ObjectProperty<LocalDate>> c = new TableCell<>();
            final BorderPane borderPane = new BorderPane();
            final DatePicker datePicker = new DatePicker();
            borderPane.centerProperty().setValue(datePicker);
            c.itemProperty().addListener(((observableValue, oldValue, newValue) -> {
                if(oldValue != null){
                    datePicker.valueProperty().unbindBidirectional(oldValue);
                }
                if(newValue != null){
                    datePicker.valueProperty().bindBidirectional(newValue);
                }
            }));
            datePicker.valueProperty().addListener(new ChangeListener<LocalDate>() {
                @Override
                public void changed(ObservableValue<? extends LocalDate> observableValue, LocalDate localDate, LocalDate t1) {
                    if(datePicker.getValue()!=null) {
                        c.getTableView().getItems().get(c.getIndex()).setDate(new SimpleObjectProperty<>(datePicker.getValue()));
                        if(c.getTableView().getItems().size()-1==c.getIndex()){
                            c.getTableView().getItems().add(new ReleaseDate("",null));
                        }
                    } else {
                        c.getTableView().getItems().get(c.getIndex()).setDate(new SimpleObjectProperty<>(datePicker.getValue()));
                    }
                }
            });
            c.graphicProperty().bind(Bindings.when(c.emptyProperty()).then((Node)null).otherwise(datePicker));
            return c;
        });
        dateColumn.setEditable(true);
        platformEditScreenController.getReleaseDatesTable().getColumns().setAll(territoryColumn,dateColumn);
    }

    private void configureTriviaTable(PlatformEditScreenController platformEditScreenController) {
        TriviaDAO triviaDAO = new TriviaDAO();
        platformEditScreenController.getTriviaTable().setEditable(true);
        platformEditScreenController.getTriviaTable().setItems(FXCollections.observableList(platformEditScreenController.getPlatform().getTrivia()));
        Trivia blank = new Trivia("","");
        platformEditScreenController.getTriviaTable().getItems().add(blank);
        HashMap<String, String> triviaHashmap = triviaDAO.getTrivia();
        ObservableList<String> triviaIDs = FXCollections.observableList(triviaHashmap.keySet().stream().toList());
        TableColumn2<Trivia,String> factColumn = new FilteredTableColumn<>("Fact");
        factColumn.setEditable(true);
        factColumn.setCellValueFactory(p -> p.getValue().factProperty());
        factColumn.setCellFactory(TextField2TableCell.forTableColumn());
        factColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Trivia, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Trivia, String> event) {
                if(!event.getNewValue().isBlank()){
                    if(event.getTableView().getItems().size()-1==event.getTablePosition().getRow()){
                        event.getTableView().getItems().add(new Trivia("",""));
                    }
                }
                event.getTableView().getItems().get(event.getTablePosition().getRow()).setFact(new SimpleStringProperty(event.getNewValue()));
            }
        });
        TableColumn2<Trivia, StringProperty> triviaIdColumn = new FilteredTableColumn<>("Trivia ID");
        triviaIdColumn.setCellValueFactory(i-> {
            final StringProperty value = i.getValue().getTriviaID();
            return Bindings.createObjectBinding(()->value);
        });
        triviaIdColumn.setCellFactory(col -> {
            TableCell<Trivia,StringProperty> c = new TableCell<>();
            final BorderPane borderPane = new BorderPane();
            final ComboBox<String> comboBox = new ComboBox<>(triviaIDs);
            borderPane.centerProperty().setValue(comboBox);
            comboBox.setEditable(true);
            c.itemProperty().addListener(((observableValue, oldValue, newValue) -> {
                if(oldValue != null){
                    comboBox.valueProperty().unbindBidirectional(oldValue);
                }
                if(newValue != null){
                    comboBox.valueProperty().bindBidirectional(newValue);
                }
            }));
            ComboBoxAutocompleteUtil.autoCompleteComboBoxPlus(comboBox, (typedText, itemToCompare) -> itemToCompare.toLowerCase().contains(typedText.toLowerCase()));
            comboBox.valueProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                    if(comboBox.getValue()!=null&&!comboBox.getValue().isBlank()) {
                        c.getTableView().getItems().get(c.getIndex()).setTriviaID(comboBox.getValue());
                        c.getTableView().getItems().get(c.getIndex()).setFact(new SimpleStringProperty(triviaHashmap.get(comboBox.valueProperty().get())));
                        if(c.getTableView().getItems().size()-1==c.getIndex()){
                            c.getTableView().getItems().add(new Trivia("",""));
                        }
                    } else {
                        c.getTableView().getItems().get(c.getIndex()).setFact(null);
                    }
                }
            });
            c.graphicProperty().bind(Bindings.when(c.emptyProperty()).then((Node)null).otherwise(comboBox));
            return c;
        });
        triviaIdColumn.setEditable(true);
        platformEditScreenController.getTriviaTable().getColumns().setAll(triviaIdColumn,factColumn);
    }

    private void configureSpecificationsTable(PlatformEditScreenController platformEditScreenController) {
        PlatformDAO territoryDAO = new PlatformDAO();
        platformEditScreenController.getSpecificationTable().setEditable(true);
        ObservableList<String> specificationTypes = FXCollections.observableList(territoryDAO.getSpecificationTypes());
        ObservableList<PlatformSpecification> alternateNames = FXCollections.observableList(platformEditScreenController.getPlatform().getSpecs());
        platformEditScreenController.getSpecificationTable().setItems(alternateNames);
        platformEditScreenController.getSpecificationTable().getItems().add(new PlatformSpecification("",""));
        TableColumn2<PlatformSpecification, StringProperty> specificationTypeColumn = new FilteredTableColumn<>("Specification Type");
        specificationTypeColumn.setCellValueFactory(i-> {
            final StringProperty value = i.getValue().specificationTypeProperty();
            return Bindings.createObjectBinding(()->value);
        });
        specificationTypeColumn.setCellFactory(col -> {
            TableCell<PlatformSpecification,StringProperty> c = new TableCell<>();
            final BorderPane borderPane = new BorderPane();
            final Button button = new Button("Edit");
            button.setOnAction(event -> {
                platformEditScreenController.openSpecificationConfigurationScreen();
            });
            final ComboBox<String> comboBox = new ComboBox<>(specificationTypes);
            final HBox hBox = new HBox(5, comboBox,button);
            borderPane.centerProperty().setValue(hBox);
            comboBox.setEditable(true);
            c.itemProperty().addListener(((observableValue, oldValue, newValue) -> {
                if(oldValue != null){
                    comboBox.valueProperty().unbindBidirectional(oldValue);
                }
                if(newValue != null){
                    comboBox.valueProperty().bindBidirectional(newValue);
                }
            }));
            ComboBoxAutocompleteUtil.autoCompleteComboBoxPlus(comboBox, (typedText, itemToCompare) -> itemToCompare.toLowerCase().contains(typedText.toLowerCase()));
            comboBox.valueProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                    if(comboBox.getValue()!=null&&!comboBox.getValue().isBlank()) {
                        c.getTableView().getItems().get(c.getIndex()).setSpecificationType(comboBox.getValue());
                        if(c.getTableView().getItems().size()-1==c.getIndex()){
                            c.getTableView().getItems().add(new PlatformSpecification("",""));
                        }
                    } else {
                        c.getTableView().getItems().get(c.getIndex()).setSpecificationType("");
                    }
                }
            });
            c.graphicProperty().bind(Bindings.when(c.emptyProperty()).then((Node)null).otherwise(borderPane));
            return c;
        });
        TableColumn2<PlatformSpecification,String> specificationColumn = new FilteredTableColumn<>("Specification");
        specificationColumn.setEditable(true);
        specificationColumn.setCellValueFactory(p->p.getValue().specificationProperty());
        specificationColumn.setCellFactory(TextField2TableCell.forTableColumn());
        specificationColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<PlatformSpecification,String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<PlatformSpecification,String> event) {
                if(!event.getNewValue().isBlank()){
                    if(event.getTableView().getItems().size()-1==event.getTablePosition().getRow()){
                        event.getTableView().getItems().add(new PlatformSpecification("",""));
                    }
                }
                event.getTableView().getItems().get(event.getTablePosition().getRow()).setSpecification(event.getNewValue());
            }
        });
        platformEditScreenController.getSpecificationTable().getColumns().setAll(specificationTypeColumn,specificationColumn);
    }

    private HashMap<String, RelatedPlatform> relatedPlatformMap = new HashMap<>();
    private void configureRelatedPlatformsTable(PlatformEditScreenController platformEditScreenController) {
        RelationshipDAO relationshipDAO = new RelationshipDAO();
        PlatformDAO platformDAO = new PlatformDAO();
        platformEditScreenController.getRelatedPlatformsTable().setEditable(true);
        ArrayList<String> options = new ArrayList<>();
        for (RelatedPlatform relatedGameEntry : platformDAO.getRelatedPlatformOptions()) {
            String platformAndGame = relatedGameEntry.getPlatformName();
            relatedPlatformMap.put(platformAndGame, relatedGameEntry);
            options.add(platformAndGame);
        }
        ObservableList<String> platforms = FXCollections.observableList(options);
        ObservableList<RelatedPlatform> relatedGames = FXCollections.observableList(platformEditScreenController.getPlatform().getRelatedPlatforms());
        ObservableList<String> relationships = FXCollections.observableList(relationshipDAO.getPlatformRelationShips());
        platformEditScreenController.getRelatedPlatformsTable().setItems(relatedGames);
        platformEditScreenController.getRelatedPlatformsTable().getItems().add(new RelatedPlatform("","","",""));
        TableColumn2<RelatedPlatform,StringProperty> relatedGameColumn = new FilteredTableColumn<>("Platform");
        relatedGameColumn.setCellValueFactory(i-> {
            final StringProperty value = i.getValue().platformNameProperty();
            return Bindings.createObjectBinding(()->value);
        });
        relatedGameColumn.setCellFactory(col -> {
            TableCell<RelatedPlatform,StringProperty> c = new TableCell<>();
            final BorderPane borderPane = new BorderPane();
            final ComboBox<String> comboBox = new ComboBox<>(platforms);
            final HBox hBox = new HBox(5, comboBox);
            borderPane.centerProperty().setValue(hBox);
            hBox.autosize();
            comboBox.setEditable(true);
            ComboBoxAutocompleteUtil.autoCompleteComboBoxPlus(comboBox, (typedText, itemToCompare) -> itemToCompare.toString().toLowerCase().contains(typedText.toLowerCase()));
            c.itemProperty().addListener(((observableValue, oldValue, newValue) -> {
                if(oldValue != null){
                    comboBox.valueProperty().unbindBidirectional(oldValue);
                }
                if(newValue != null){
                    comboBox.valueProperty().bindBidirectional(newValue);
                }
            }));
            ComboBoxAutocompleteUtil.autoCompleteComboBoxPlus(comboBox, (typedText, itemToCompare) -> itemToCompare.toLowerCase().contains(typedText.toLowerCase()));
            comboBox.valueProperty().addListener((observableValue, s, t1) -> {
                if(comboBox.getValue()!=null&&!comboBox.getValue().isBlank()) {
                    c.getTableView().getItems().get(c.getIndex()).setPlatformName(comboBox.getValue());
                    c.getTableView().getItems().get(c.getIndex()).setId(relatedPlatformMap.get(comboBox.getValue()).getId());
                    if(c.getTableView().getItems().size()-1==c.getIndex()){
                        c.getTableView().getItems().add(new RelatedPlatform("","","",""));
                    }
                } else {
                    c.getTableView().getItems().get(c.getIndex()).setPlatformName(comboBox.getValue());
                }
            });
            c.graphicProperty().bind(Bindings.when(c.emptyProperty()).then((Node)null).otherwise(hBox));
            return c;
        });
        relatedGameColumn.setEditable(true);
        relatedGameColumn.setMaxWidth(200);
        TableColumn2<RelatedPlatform,StringProperty> relationshipColumn = new FilteredTableColumn<>("Relationship");
        relationshipColumn.setCellValueFactory(i-> {
            final StringProperty value = i.getValue().relationshipProperty();
            return Bindings.createObjectBinding(()->value);
        });
        relationshipColumn.setCellFactory(col -> {
            TableCell<RelatedPlatform,StringProperty> c = new TableCell<>();
            final BorderPane borderPane = new BorderPane();
            final Button button = new Button("Edit");
            button.setOnAction(event -> {
                platformEditScreenController.openPlatformRelationshipTypeScreen();
            });
            final ComboBox<String> comboBox = new ComboBox<>(relationships);
            final HBox hBox = new HBox(5, comboBox,button);
            hBox.autosize();
            borderPane.centerProperty().setValue(hBox);
            comboBox.setEditable(true);
            ComboBoxAutocompleteUtil.autoCompleteComboBoxPlus(comboBox, (typedText, itemToCompare) -> itemToCompare.toLowerCase().contains(typedText.toLowerCase()));
            c.itemProperty().addListener(((observableValue, oldValue, newValue) -> {
                if(oldValue != null){
                    comboBox.valueProperty().unbindBidirectional(oldValue);
                }
                if(newValue != null){
                    comboBox.valueProperty().bindBidirectional(newValue);
                }
            }));
            ComboBoxAutocompleteUtil.autoCompleteComboBoxPlus(comboBox, (typedText, itemToCompare) -> itemToCompare.toLowerCase().contains(typedText.toLowerCase()));
            comboBox.valueProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                    if(comboBox.getValue()!=null&&!comboBox.getValue().isBlank()) {
                        c.getTableView().getItems().get(c.getIndex()).setRelationship(comboBox.getValue());
                        if(c.getTableView().getItems().size()-1==c.getIndex()){
                            c.getTableView().getItems().add(new RelatedPlatform("","","",""));
                        }
                    } else {
                        c.getTableView().getItems().get(c.getIndex()).setRelationship(comboBox.getValue());
                    }
                }
            });
            c.graphicProperty().bind(Bindings.when(c.emptyProperty()).then((Node)null).otherwise(hBox));
            return c;
        });
        relationshipColumn.setEditable(true);
        TableColumn2<RelatedPlatform,String> relationshipDescriptionColumn = new FilteredTableColumn<>("Relationship Description");
        relationshipDescriptionColumn.setEditable(true);
        relationshipDescriptionColumn.setCellValueFactory(c->c.getValue().descriptionProperty());
        relationshipDescriptionColumn.setCellFactory(TextAreaTableCell.forTableColumn());
        relationshipDescriptionColumn.setOnEditCommit(event -> {
            if(!event.getNewValue().isBlank()){
                if(event.getTableView().getItems().size()-1==event.getTablePosition().getRow()){
                    event.getTableView().getItems().add(new RelatedPlatform("","","",""));
                }
            }
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setDescription(event.getNewValue());
        });
        relationshipDescriptionColumn.setPrefWidth(400);
        platformEditScreenController.getRelatedPlatformsTable().getColumns().setAll(relatedGameColumn,relationshipColumn,relationshipDescriptionColumn);
    }

    private void configureAlternateNamesTable(PlatformEditScreenController platformEditScreenController) {
        TerritoryDAO territoryDAO = new TerritoryDAO();
        platformEditScreenController.getAlternateNamesTable().setEditable(true);
        ObservableList<String> territories = FXCollections.observableList(territoryDAO.getTerritories());
        territories.add("Community");
        ObservableList<AlternateName> alternateNames = FXCollections.observableList(platformEditScreenController.getPlatform().getAlternateNames());
        platformEditScreenController.getAlternateNamesTable().setItems(alternateNames);
        platformEditScreenController.getAlternateNamesTable().getItems().add(new AlternateName("","",""));
        TableColumn2<AlternateName,String> alternateNameIdColumn = new FilteredTableColumn<>("Alternate Name ID");
        alternateNameIdColumn.setEditable(true);
        alternateNameIdColumn.setCellValueFactory(p->p.getValue().getAlternateNameID());
        alternateNameIdColumn.setCellFactory(TextField2TableCell.forTableColumn());
        alternateNameIdColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<AlternateName,String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<AlternateName,String> event) {
                if(!event.getNewValue().isBlank()){
                    if(event.getTableView().getItems().size()-1==event.getTablePosition().getRow()){
                        event.getTableView().getItems().add(new AlternateName("","",""));
                    }
                }
                event.getTableView().getItems().get(event.getTablePosition().getRow()).setAlternateNameID(event.getNewValue());
            }
        });
        TableColumn2<AlternateName, StringProperty> reasonColumn = new FilteredTableColumn<>("Reason");
        reasonColumn.setCellValueFactory(i-> {
            final StringProperty value = i.getValue().getReason();
            return Bindings.createObjectBinding(()->value);
        });
        reasonColumn.setCellFactory(col -> {
            TableCell<AlternateName,StringProperty> c = new TableCell<>();
            final BorderPane borderPane = new BorderPane();
            final ComboBox<String> comboBox = new ComboBox<>(territories);
            borderPane.centerProperty().setValue(comboBox);
            comboBox.setEditable(true);
            c.itemProperty().addListener(((observableValue, oldValue, newValue) -> {
                if(oldValue != null){
                    comboBox.valueProperty().unbindBidirectional(oldValue);
                }
                if(newValue != null){
                    comboBox.valueProperty().bindBidirectional(newValue);
                }
            }));
            ComboBoxAutocompleteUtil.autoCompleteComboBoxPlus(comboBox, (typedText, itemToCompare) -> itemToCompare.toLowerCase().contains(typedText.toLowerCase()));
            comboBox.valueProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                    if(comboBox.getValue()!=null&&!comboBox.getValue().isBlank()&&!"()".equals(comboBox.getValue().trim())) {
                        c.getTableView().getItems().get(c.getIndex()).setRegion(comboBox.getValue());
                        if(c.getTableView().getItems().size()-1==c.getIndex()){
                            c.getTableView().getItems().add(new AlternateName("","",""));
                        }
                    } else {
                        c.getTableView().getItems().get(c.getIndex()).setRegion(null);
                    }
                }
            });
            c.graphicProperty().bind(Bindings.when(c.emptyProperty()).then((Node)null).otherwise(comboBox));
            return c;
        });
        TableColumn2<AlternateName,String> valueColumn = new FilteredTableColumn<>("Value");
        valueColumn.setEditable(true);
        valueColumn.setCellValueFactory(p->p.getValue().getAlternateName());
        valueColumn.setCellFactory(TextField2TableCell.forTableColumn());
        valueColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<AlternateName,String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<AlternateName,String> event) {
                if(!event.getNewValue().isBlank()){
                    if(event.getTableView().getItems().size()-1==event.getTablePosition().getRow()){
                        event.getTableView().getItems().add(new AlternateName("","",""));
                    }
                }
                event.getTableView().getItems().get(event.getTablePosition().getRow()).setAlternateName(event.getNewValue());
            }
        });
        platformEditScreenController.getAlternateNamesTable().getColumns().setAll(alternateNameIdColumn,reasonColumn,valueColumn);
    }

    private void configureDescriptionTextArea(PlatformEditScreenController platformEditScreenController) {
        if(!"null".equals(platformEditScreenController.getPlatform().getDescription())){
            platformEditScreenController.getDescriptionTextArea().setText(platformEditScreenController.getPlatform().getDescription());
        }
    }

    private void configureDefaultSummaryTextArea(PlatformEditScreenController platformEditScreenController) {
        if(!"null".equals(platformEditScreenController.getPlatform().getSummary())){
            platformEditScreenController.getDefaultSummaryTextArea().setText(platformEditScreenController.getPlatform().getSummary());
        }
    }

    private void configureDiscontinuedOnDatePicker(PlatformEditScreenController platformEditScreenController) {
        if(platformEditScreenController.getPlatform().getDateDiscontinued()!=null) {
            platformEditScreenController.getDiscontinuedDatePicker().setValue(platformEditScreenController.getPlatform().getDateDiscontinued());
        }
    }

    private void configureUnitsSoldField(PlatformEditScreenController platformEditScreenController) {
        if(!"null".equals(platformEditScreenController.getPlatform().getUnitsSold())){
            platformEditScreenController.getPlatformNameField().setText(platformEditScreenController.getPlatform().getUnitsSold());
        }
    }

    private void configureGenerationField(PlatformEditScreenController platformEditScreenController) {
        if(!"null".equals(platformEditScreenController.getPlatform().getGeneration())){
            platformEditScreenController.getPlatformNameField().setText(platformEditScreenController.getPlatform().getGeneration());
        }
    }

    private void configureProductFamilyComboBox(PlatformEditScreenController platformEditScreenController) {
        PlatformDAO platformDAO = new PlatformDAO();
        ObservableList<ComboBoxItemWrap<String>> options = FXCollections.observableList(generateComboBoxItemWrappers(
                platformDAO.getProductFamilies().stream().filter(publisher->!publisher.isEmpty()).toList()));
        String mediaValue = "";
        for (String manufacturer : platformEditScreenController.getPlatform().getProductFamilies()) {
            mediaValue += manufacturer + "; ";
        }
        platformEditScreenController.getProductFamilyComboBox().setValue(new ComboBoxItemWrap<>(mediaValue));
        platformEditScreenController.getProductFamilyComboBox().setConverter(new StringConverter<ComboBoxItemWrap<String>>() {
            @Override
            public String toString(ComboBoxItemWrap<String> stringComboBoxItemWrap) {
                return stringComboBoxItemWrap.itemProperty().getValue();
            }

            @Override
            public ComboBoxItemWrap<String> fromString(String s) {
                return new ComboBoxItemWrap<String>(s);
            }
        });
        platformEditScreenController.getProductFamilyComboBox().setCellFactory(c->{
            ListCell<ComboBoxItemWrap<String>> cell = new ListCell<>(){
                boolean initialized = false;
                @Override
                protected void updateItem(ComboBoxItemWrap<String> item, boolean empty) {
                    super.updateItem(item, empty);
                    if(!empty){
                        final CheckBox checkBox = new CheckBox(item.toString());
                        checkBox.selectedProperty().bind(item.checkProperty());
                        if(!initialized){
                            if(platformEditScreenController.getPlatform().getMedia().contains(item.getItem())){
                                item.checkProperty().set(true);
                            }
                            initialized = true;
                        }
                        setGraphic(checkBox);
                    }

                }
            };

            cell.addEventFilter(MouseEvent.MOUSE_RELEASED, mouseEvent -> {
                cell.getItem().checkProperty().set(!cell.getItem().checkProperty().get());
                StringBuilder stringBuilder = new StringBuilder();
                platformEditScreenController.getProductFamilyComboBox().getItems().filtered(f->f.getCheck()).forEach(p->{
                    stringBuilder.append("; "+p.getItem());
                });
                final String string = stringBuilder.toString();
                platformEditScreenController.getProductFamilyComboBox().setValue(new ComboBoxItemWrap<String>(string.substring(Integer.min(2, string.length()))));
                platformEditScreenController.getProductFamilyComboBox().tooltipProperty().setValue(new Tooltip(string.substring(Integer.min(2, string.length()))));
            });
            return cell;
        });
        platformEditScreenController.getProductFamilyComboBox().valueProperty().addListener((observableValue, stringComboBoxItemWrap, t1) -> {
            platformEditScreenController.getProductFamilyComboBox().show();
            List<String> selectedOptions = Arrays.stream(platformEditScreenController.getProductFamilyComboBox().getValue().getItem().split("; ")).toList();
            options.forEach(option -> {
                for (String selectedOption : selectedOptions) {
                    if(selectedOption.trim().toLowerCase().equals(option.getItem().toLowerCase())){
                        option.setCheck(true);
                        return;
                    }
                }
                option.setCheck(false);

            });
        });
        platformEditScreenController.getProductFamilyComboBox().focusedProperty().addListener(((observableValue, aBoolean, t1) -> {
            platformEditScreenController.getProductFamilyComboBox().setValue(new ComboBoxItemWrap<>(platformEditScreenController.getProductFamilyComboBox().getValue().itemProperty().getValue()));
        }));
        platformEditScreenController.getProductFamilyComboBox().setItems(options);
    }

    private void configureMediaTypeCheckComboBox(PlatformEditScreenController platformEditScreenController) {
        MediaDAO mediaDAO = new MediaDAO();
        ObservableList<ComboBoxItemWrap<String>> options = FXCollections.observableList(generateComboBoxItemWrappers(
                mediaDAO.getMediaTypesForPlatforms().stream().filter(publisher->!publisher.isEmpty()).toList()));
        String mediaValue = "";
        for (String manufacturer : platformEditScreenController.getPlatform().getMediaTypes()) {
            mediaValue += manufacturer + "; ";
        }
        platformEditScreenController.getMediaTypeComboBox().setValue(new ComboBoxItemWrap<>(mediaValue));
        platformEditScreenController.getMediaTypeComboBox().setConverter(new StringConverter<ComboBoxItemWrap<String>>() {
            @Override
            public String toString(ComboBoxItemWrap<String> stringComboBoxItemWrap) {
                return stringComboBoxItemWrap.itemProperty().getValue();
            }

            @Override
            public ComboBoxItemWrap<String> fromString(String s) {
                return new ComboBoxItemWrap<String>(s);
            }
        });
        platformEditScreenController.getMediaTypeComboBox().setCellFactory(c->{
            ListCell<ComboBoxItemWrap<String>> cell = new ListCell<>(){
                boolean initialized = false;
                @Override
                protected void updateItem(ComboBoxItemWrap<String> item, boolean empty) {
                    super.updateItem(item, empty);
                    if(!empty){
                        final CheckBox checkBox = new CheckBox(item.toString());
                        checkBox.selectedProperty().bind(item.checkProperty());
                        if(!initialized){
                            if(platformEditScreenController.getPlatform().getMedia().contains(item.getItem())){
                                item.checkProperty().set(true);
                            }
                            initialized = true;
                        }
                        setGraphic(checkBox);
                    }

                }
            };

            cell.addEventFilter(MouseEvent.MOUSE_RELEASED, mouseEvent -> {
                cell.getItem().checkProperty().set(!cell.getItem().checkProperty().get());
                StringBuilder stringBuilder = new StringBuilder();
                platformEditScreenController.getMediaTypeComboBox().getItems().filtered(f->f.getCheck()).forEach(p->{
                    stringBuilder.append("; "+p.getItem());
                });
                final String string = stringBuilder.toString();
                platformEditScreenController.getMediaTypeComboBox().setValue(new ComboBoxItemWrap<String>(string.substring(Integer.min(2, string.length()))));
                platformEditScreenController.getMediaTypeComboBox().tooltipProperty().setValue(new Tooltip(string.substring(Integer.min(2, string.length()))));
            });
            return cell;
        });
        platformEditScreenController.getMediaTypeComboBox().valueProperty().addListener((observableValue, stringComboBoxItemWrap, t1) -> {
            platformEditScreenController.getMediaTypeComboBox().show();
            List<String> selectedOptions = Arrays.stream(platformEditScreenController.getMediaTypeComboBox().getValue().getItem().split("; ")).toList();
            options.forEach(option -> {
                for (String selectedOption : selectedOptions) {
                    if(selectedOption.trim().equalsIgnoreCase(option.getItem())){
                        option.setCheck(true);
                        return;
                    }
                }
                option.setCheck(false);

            });
        });
        platformEditScreenController.getMediaTypeComboBox().focusedProperty().addListener(((observableValue, aBoolean, t1) -> {
            platformEditScreenController.getMediaTypeComboBox().setValue(new ComboBoxItemWrap<>(platformEditScreenController.getMediaTypeComboBox().getValue().itemProperty().getValue()));
        }));
        platformEditScreenController.getMediaTypeComboBox().setItems(options);
    }

    private void configureMediaCheckComboBox(PlatformEditScreenController platformEditScreenController) {
        MediaDAO mediaDAO = new MediaDAO();
        ObservableList<ComboBoxItemWrap<String>> options = FXCollections.observableList(generateComboBoxItemWrappers(
                mediaDAO.getMedia().stream().filter(publisher->!publisher.isEmpty()).toList()));
        String mediaValue = "";
        for (String manufacturer : platformEditScreenController.getPlatform().getMedia()) {
            mediaValue += manufacturer + "; ";
        }
        platformEditScreenController.getMediaComboBox().setValue(new ComboBoxItemWrap<>(mediaValue));
        platformEditScreenController.getMediaComboBox().setConverter(new StringConverter<ComboBoxItemWrap<String>>() {
            @Override
            public String toString(ComboBoxItemWrap<String> stringComboBoxItemWrap) {
                return stringComboBoxItemWrap.itemProperty().getValue();
            }

            @Override
            public ComboBoxItemWrap<String> fromString(String s) {
                return new ComboBoxItemWrap<String>(s);
            }
        });
        platformEditScreenController.getMediaComboBox().setCellFactory(c->{
            ListCell<ComboBoxItemWrap<String>> cell = new ListCell<>(){
                boolean initialized = false;
                @Override
                protected void updateItem(ComboBoxItemWrap<String> item, boolean empty) {
                    super.updateItem(item, empty);
                    if(!empty){
                        final CheckBox checkBox = new CheckBox(item.toString());
                        checkBox.selectedProperty().bind(item.checkProperty());
                        if(!initialized){
                            if(platformEditScreenController.getPlatform().getMedia().contains(item.getItem())){
                                item.checkProperty().set(true);
                            }
                            initialized = true;
                        }
                        setGraphic(checkBox);
                    }

                }
            };

            cell.addEventFilter(MouseEvent.MOUSE_RELEASED, mouseEvent -> {
                cell.getItem().checkProperty().set(!cell.getItem().checkProperty().get());
                StringBuilder stringBuilder = new StringBuilder();
                platformEditScreenController.getMediaComboBox().getItems().filtered(f->f.getCheck()).forEach(p->{
                    stringBuilder.append("; "+p.getItem());
                });
                final String string = stringBuilder.toString();
                platformEditScreenController.getMediaComboBox().setValue(new ComboBoxItemWrap<String>(string.substring(Integer.min(2, string.length()))));
                platformEditScreenController.getMediaComboBox().tooltipProperty().setValue(new Tooltip(string.substring(Integer.min(2, string.length()))));
            });
            return cell;
        });
        platformEditScreenController.getMediaComboBox().valueProperty().addListener((observableValue, stringComboBoxItemWrap, t1) -> {
            platformEditScreenController.getMediaComboBox().show();
            List<String> selectedOptions = Arrays.stream(platformEditScreenController.getMediaComboBox().getValue().getItem().split("; ")).toList();
            options.forEach(option -> {
                for (String selectedOption : selectedOptions) {
                    if(selectedOption.trim().toLowerCase().equals(option.getItem().toLowerCase())){
                        option.setCheck(true);
                        return;
                    }
                }
                option.setCheck(false);

            });
        });
        platformEditScreenController.getMediaComboBox().focusedProperty().addListener(((observableValue, aBoolean, t1) -> {
            platformEditScreenController.getMediaComboBox().setValue(new ComboBoxItemWrap<>(platformEditScreenController.getMediaComboBox().getValue().itemProperty().getValue()));
        }));
        platformEditScreenController.getMediaComboBox().setItems(options);
    }

    private void configureManufacturerCheckComboBox(PlatformEditScreenController platformEditScreenController) {
        ManufacturerDAO manufacturerDAO = new ManufacturerDAO();
        ObservableList<ComboBoxItemWrap<String>> options = FXCollections.observableList(generateComboBoxItemWrappers(
                manufacturerDAO.getManufacturers().stream().filter(publisher->!publisher.isEmpty()).toList()));
        String publisherValue = "";
        for (String manufacturer : platformEditScreenController.getPlatform().getManufacturers()) {
            publisherValue += manufacturer + "; ";
        }
        platformEditScreenController.getManufacturerComboBox().setValue(new ComboBoxItemWrap<>(publisherValue));
        platformEditScreenController.getManufacturerComboBox().setConverter(new StringConverter<ComboBoxItemWrap<String>>() {
            @Override
            public String toString(ComboBoxItemWrap<String> stringComboBoxItemWrap) {
                return stringComboBoxItemWrap.itemProperty().getValue();
            }

            @Override
            public ComboBoxItemWrap<String> fromString(String s) {
                return new ComboBoxItemWrap<String>(s);
            }
        });
        platformEditScreenController.getManufacturerComboBox().setCellFactory(c->{
            ListCell<ComboBoxItemWrap<String>> cell = new ListCell<>(){
                boolean initialized = false;
                @Override
                protected void updateItem(ComboBoxItemWrap<String> item, boolean empty) {
                    super.updateItem(item, empty);
                    if(!empty){
                        final CheckBox checkBox = new CheckBox(item.toString());
                        checkBox.selectedProperty().bind(item.checkProperty());
                        if(!initialized){
                            if(platformEditScreenController.getPlatform().getManufacturers().contains(item.getItem())){
                                item.checkProperty().set(true);
                            }
                            initialized = true;
                        }
                        setGraphic(checkBox);
                    }

                }
            };

            cell.addEventFilter(MouseEvent.MOUSE_RELEASED, mouseEvent -> {
                cell.getItem().checkProperty().set(!cell.getItem().checkProperty().get());
                StringBuilder stringBuilder = new StringBuilder();
                platformEditScreenController.getManufacturerComboBox().getItems().filtered(f->f.getCheck()).forEach(p->{
                    stringBuilder.append("; "+p.getItem());
                });
                final String string = stringBuilder.toString();
                platformEditScreenController.getManufacturerComboBox().setValue(new ComboBoxItemWrap<String>(string.substring(Integer.min(2, string.length()))));
                platformEditScreenController.getManufacturerComboBox().tooltipProperty().setValue(new Tooltip(string.substring(Integer.min(2, string.length()))));
            });
            return cell;
        });
        platformEditScreenController.getManufacturerComboBox().valueProperty().addListener((observableValue, stringComboBoxItemWrap, t1) -> {
            platformEditScreenController.getManufacturerComboBox().show();
            List<String> selectedOptions = Arrays.stream(platformEditScreenController.getManufacturerComboBox().getValue().getItem().split("; ")).toList();
            options.forEach(option -> {
                for (String selectedOption : selectedOptions) {
                    if(selectedOption.trim().toLowerCase().equals(option.getItem().toLowerCase())){
                        option.setCheck(true);
                        return;
                    }
                }
                option.setCheck(false);

            });
        });
        platformEditScreenController.getManufacturerComboBox().focusedProperty().addListener(((observableValue, aBoolean, t1) -> {
            platformEditScreenController.getManufacturerComboBox().setValue(new ComboBoxItemWrap<>(platformEditScreenController.getManufacturerComboBox().getValue().itemProperty().getValue()));
        }));
        platformEditScreenController.getManufacturerComboBox().setItems(options);
    }

    private void configurePlatformNameField(PlatformEditScreenController platformEditScreenController) {
        if(!"null".equals(platformEditScreenController.getPlatform().getPlatformName())){
            platformEditScreenController.getPlatformNameField().setText(platformEditScreenController.getPlatform().getPlatformName());
        }
    }

    private void configurePublisherCheckComboBox(PlatformEditScreenController platformEditScreenController) {
        PublisherDAO publisherDAO = new PublisherDAO();
        ObservableList<ComboBoxItemWrap<String>> options = FXCollections.observableList(generateComboBoxItemWrappers(
                publisherDAO.getPublishers().stream().filter(publisher->!publisher.isEmpty()).toList()));
        String publisherValue = "";
        for (String publisher : platformEditScreenController.getPlatform().getPublishers()) {
            publisherValue += publisher + "; ";
        }
        platformEditScreenController.getPublisherComboBox().setValue(new ComboBoxItemWrap<>(publisherValue));
        platformEditScreenController.getPublisherComboBox().setConverter(new StringConverter<ComboBoxItemWrap<String>>() {
            @Override
            public String toString(ComboBoxItemWrap<String> stringComboBoxItemWrap) {
                return stringComboBoxItemWrap.itemProperty().getValue();
            }

            @Override
            public ComboBoxItemWrap<String> fromString(String s) {
                return new ComboBoxItemWrap<String>(s);
            }
        });
        platformEditScreenController.getPublisherComboBox().setCellFactory(c->{
            ListCell<ComboBoxItemWrap<String>> cell = new ListCell<>(){
                boolean initialized = false;
                @Override
                protected void updateItem(ComboBoxItemWrap<String> item, boolean empty) {
                    super.updateItem(item, empty);
                    if(!empty){
                        final CheckBox checkBox = new CheckBox(item.toString());
                        checkBox.selectedProperty().bind(item.checkProperty());
                        if(!initialized){
                            if(platformEditScreenController.getPlatform().getPublishers().contains(item.getItem())){
                                item.checkProperty().set(true);
                            }
                            initialized = true;
                        }
                        setGraphic(checkBox);
                    }

                }
            };

            cell.addEventFilter(MouseEvent.MOUSE_RELEASED, mouseEvent -> {
                cell.getItem().checkProperty().set(!cell.getItem().checkProperty().get());
                StringBuilder stringBuilder = new StringBuilder();
                platformEditScreenController.getPublisherComboBox().getItems().filtered(f->f.getCheck()).forEach(p->{
                    stringBuilder.append("; "+p.getItem());
                });
                final String string = stringBuilder.toString();
                platformEditScreenController.getPublisherComboBox().setValue(new ComboBoxItemWrap<String>(string.substring(Integer.min(2, string.length()))));
                platformEditScreenController.getPublisherComboBox().tooltipProperty().setValue(new Tooltip(string.substring(Integer.min(2, string.length()))));
            });
            return cell;
        });
        platformEditScreenController.getPublisherComboBox().valueProperty().addListener((observableValue, stringComboBoxItemWrap, t1) -> {
            platformEditScreenController.getPublisherComboBox().show();
            List<String> selectedOptions = Arrays.stream(platformEditScreenController.getPublisherComboBox().getValue().getItem().split("; ")).toList();
            options.forEach(option -> {
                for (String selectedOption : selectedOptions) {
                    if(selectedOption.trim().toLowerCase().equals(option.getItem().toLowerCase())){
                        option.setCheck(true);
                        return;
                    }
                }
                option.setCheck(false);

            });
        });
        platformEditScreenController.getPublisherComboBox().focusedProperty().addListener(((observableValue, aBoolean, t1) -> {
            platformEditScreenController.getPublisherComboBox().setValue(new ComboBoxItemWrap<>(platformEditScreenController.getPublisherComboBox().getValue().itemProperty().getValue()));
        }));
        platformEditScreenController.getPublisherComboBox().setItems(options);
    }
}
