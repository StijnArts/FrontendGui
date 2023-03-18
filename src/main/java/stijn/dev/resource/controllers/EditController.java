package stijn.dev.resource.controllers;

import javafx.beans.binding.*;
import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import org.controlsfx.control.tableview2.*;
import org.controlsfx.control.tableview2.cell.*;
import stijn.dev.datasource.database.dao.*;
import stijn.dev.datasource.importing.xml.*;
import stijn.dev.datasource.objects.data.*;
import stijn.dev.datasource.objects.items.*;
import stijn.dev.datasource.objects.items.Character;
import stijn.dev.resource.controllers.components.*;
import stijn.dev.util.javafx.*;

import java.time.*;
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
    TextField maxPlayersField;
    @FXML
    TextField titleTextField;
    @FXML
    TextField defaultSortingTitleTextField;
    @FXML
    TextArea descriptionTextArea;
    private Game game;
    private GamesXMLParser gamesXMLParser = new GamesXMLParser();
    private DeveloperDAO developerDAO = new DeveloperDAO();
    private PublisherDAO publisherDAO = new PublisherDAO();
    private PriorityDataDAO priorityDataDAO = new PriorityDataDAO();
    private RatingDAO ratingDAO = new RatingDAO();
    private PlaymodeDAO playmodeDAO = new PlaymodeDAO();
    private AlternateNameDAO alternateNameDAO = new AlternateNameDAO();
    private TerritoryDAO territoryDAO = new TerritoryDAO();
    private RelatedGameDAO relatedGameDAO = new RelatedGameDAO();
    private RelationshipDAO relationshipDAO = new RelationshipDAO();
    private GameDAO gameDAO = new GameDAO();
    private AdditionalAppDAO additionalAppDAO = new AdditionalAppDAO();
    private TriviaDAO triviaDAO = new TriviaDAO();
    private StaffDAO staffDAO = new StaffDAO();
    private StaffRoleDAO staffRoleDAO = new StaffRoleDAO();
    private CharacterDAO characterDAO = new CharacterDAO();
    private CharacterRoleDAO characterRoleDAO = new CharacterRoleDAO();
    private HashMap<String, RelatedGameEntry> relatedGameMap = new HashMap<>();

    public static EditController create(FXMLLoader loader, Game gameImportItem){
        EditController ec = loader.getController();
        ec.configure(gameImportItem);
        return ec;
    }

    public void configure(Game gameImportItem){
        this.game = gameImportItem;
        configureAlternateNamesTable();
        configureRelatedGameTable();
        configureAdditionalAppsTable();
        configureReleaseDatesTable();
        configureTriviaTable();
        configurePublisherComboCheckBox();
        configureDeveloperComboCheckBox();
        configureRatingComboCheckBox();
        configurePlaymodeComboCheckBox();
        configureTitleTextField();
        configurePlatformComboBox();
        configurePriorityComboBox();
        configureMaxPlayersField();
        configureDefaultSortingTitleField();
        configureDefaultSummaryField();
        configureDescriptionTextArea();
        configureStaffTable();
        configureCharacterTable();
    }

    private void configureStaffTable() {
        staffTable.setEditable(true);
        ArrayList<Staff> staff = game.getStaff();
        HashMap<String, Staff> staffHashMap = new HashMap<>();
        for (Staff staffItem :
                staffDAO.getStaff()) {
            staffHashMap.put(staffItem.getStaffID(),staffItem);
        }

        ObservableList<String> staffOptions = FXCollections.observableList(staffHashMap.keySet().stream().toList());
        ObservableList<String> staffRoles = FXCollections.observableList(staffRoleDAO.getStaffRoles());
        ObservableList<Staff> staffObservable = FXCollections.observableList(staff);
        staffTable.setItems(staffObservable);
        staffTable.getItems().add(new Staff("","","",""));
        TableColumn2<Staff,String> firstNameColumn = new FilteredTableColumn<>("First Name");
        firstNameColumn.setEditable(false);
        firstNameColumn.setCellValueFactory(p->p.getValue().firstNameProperty());
        firstNameColumn.setCellFactory(TextField2TableCell.forTableColumn());
        TableColumn2<Staff,String> lastNameColumn = new FilteredTableColumn<>("Last Name");
        lastNameColumn.setEditable(false);
        lastNameColumn.setCellValueFactory(p->p.getValue().firstNameProperty());
        lastNameColumn.setCellFactory(TextField2TableCell.forTableColumn());
        TableColumn2<Staff,StringProperty> roleColumn = new FilteredTableColumn<>("Role");
        roleColumn.setEditable(true);
        roleColumn.setCellValueFactory(i-> {
            final StringProperty value = i.getValue().roleProperty();
            return Bindings.createObjectBinding(()->value);
        });
        roleColumn.setCellFactory(col -> {
            TableCell<Staff,StringProperty> tableCell = new TableCell<>();
            final BorderPane borderPane = new BorderPane();
            final Button button = new Button("Edit");
            //TODO make button open edit screen for the relationship in the cell
            //button.setOnAction();
            final ComboBox<ComboBoxItemWrap<String>> comboBox = new ComboBox<>(FXCollections.observableList(generateComboBoxItemWrappers(staffRoles)));
            final HBox hBox = new HBox(5, comboBox,button);
            hBox.autosize();
            hBox.setPrefWidth(300);
            borderPane.centerProperty().setValue(hBox);
            comboBox.setEditable(true);
            ComboBoxAutocompleteUtil.autoCompleteComboBoxPlus(comboBox, (typedText, itemToCompare) -> itemToCompare.getItem().toLowerCase().contains(typedText.toLowerCase()));
            comboBox.valueProperty().addListener(new ChangeListener<ComboBoxItemWrap<String>>() {
                @Override
                public void changed(ObservableValue<? extends ComboBoxItemWrap<String>> observableValue, ComboBoxItemWrap<String> stringComboBoxItemWrap, ComboBoxItemWrap<String> t1) {
                    tableCell.getTableView().getItems().get(tableCell.getIndex()).setRole(comboBox.getValue().getItem());
                }
            });
            comboBox.setCellFactory(c->{
                ListCell<ComboBoxItemWrap<String>> cell = new ListCell<>(){
                    boolean initialized = false;
                    @Override
                    protected void updateItem(ComboBoxItemWrap<String> item, boolean empty) {
                        super.updateItem(item, empty);
                        if(!empty){
                            final CheckBox checkBox = new CheckBox(item.toString());
                            checkBox.selectedProperty().bind(item.checkProperty());
                            if(!initialized){
                                if(game.getDeveloper().contains(item.getItem())){
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
                    comboBox.getItems().filtered(f->f.getCheck()).forEach(p->{
                        stringBuilder.append("; "+p.getItem());
                    });
                    final String string = stringBuilder.toString();
                    comboBox.setValue(new ComboBoxItemWrap<String>(string.substring(Integer.min(2, string.length()))));
                });
                return cell;
            });
            tableCell.graphicProperty().bind(Bindings.when(tableCell.emptyProperty()).then((Node)null).otherwise(hBox));
            return tableCell;
        });
        TableColumn2<Staff, StringProperty> staffIDColumn = new FilteredTableColumn<>("Staff ID");
        staffIDColumn.setCellValueFactory(i-> {
            final StringProperty value = i.getValue().staffIDProperty();
            return Bindings.createObjectBinding(()->value);
        });
        staffIDColumn.setCellFactory(col -> {
            TableCell<Staff,StringProperty> c = new TableCell<>();
            final BorderPane borderPane = new BorderPane();
            final ComboBox<String> comboBox = new ComboBox<>(staffOptions);
            borderPane.centerProperty().setValue(comboBox);
            comboBox.setEditable(true);
            comboBox.valueProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                    c.getTableView().getItems().get(c.getIndex()).setStaffID(comboBox.getValue());
                    c.getTableView().getItems().get(c.getIndex()).setFirstName(staffHashMap.get(comboBox.valueProperty().get()).getFirstName());
                    c.getTableView().getItems().get(c.getIndex()).setFirstName(staffHashMap.get(comboBox.valueProperty().get()).getLastName());
                }
            });
            c.graphicProperty().bind(Bindings.when(c.emptyProperty()).then((Node)null).otherwise(comboBox));
            return c;
        });
        staffIDColumn.setEditable(true);
        staffTable.getColumns().setAll(staffIDColumn,firstNameColumn,lastNameColumn,roleColumn);
    }

    private void configureCharacterTable() {
        characterTable.setEditable(true);
        ArrayList<Character> character = game.getCharacters();
        HashMap<String, Character> characterHashMap = new HashMap<>();
        for (Character characterItem :
                characterDAO.getCharacters()) {
            characterHashMap.put(characterItem.getCharacterID(),characterItem);
        }
        ObservableList<String> staff =FXCollections.observableList(staffDAO.getStaffIDs());
        ObservableList<String> characterOptions = FXCollections.observableList(characterHashMap.keySet().stream().toList());
        ObservableList<String> characterRoles = FXCollections.observableList(characterRoleDAO.getCharacterRoles());
        ObservableList<Character> characterObservable = FXCollections.observableList(character);
        characterTable.setItems(characterObservable);
        characterTable.getItems().add(new Character("","",""));
        TableColumn2<Character,String> firstNameColumn = new FilteredTableColumn<>("FirstName");
        firstNameColumn.setEditable(false);
        firstNameColumn.setCellValueFactory(p->p.getValue().firstNameProperty());
        firstNameColumn.setCellFactory(TextField2TableCell.forTableColumn());
        TableColumn2<Character,String> lastNameColumn = new FilteredTableColumn<>("Last Name");
        lastNameColumn.setEditable(false);
        lastNameColumn.setCellValueFactory(p->p.getValue().firstNameProperty());
        lastNameColumn.setCellFactory(TextField2TableCell.forTableColumn());
        TableColumn2<Character,StringProperty> roleColumn = new FilteredTableColumn<>("Role");
        roleColumn.setEditable(true);
        roleColumn.setCellValueFactory(i-> {
            final StringProperty value = i.getValue().roleProperty();
            return Bindings.createObjectBinding(()->value);
        });
        roleColumn.setCellFactory(col -> {
            TableCell<Character,StringProperty> tableCell = new TableCell<>();
            final BorderPane borderPane = new BorderPane();
            final Button button = new Button("Edit");
            //TODO make button open edit screen for the relationship in the cell
            //button.setOnAction();
            final ComboBox<ComboBoxItemWrap<String>> comboBox = new ComboBox<>(FXCollections.observableList(generateComboBoxItemWrappers(characterRoles)));
            final HBox hBox = new HBox(5, comboBox,button);
            hBox.autosize();
            borderPane.centerProperty().setValue(hBox);
            comboBox.setEditable(true);
            ComboBoxAutocompleteUtil.autoCompleteComboBoxPlus(comboBox, (typedText, itemToCompare) -> itemToCompare.getItem().toLowerCase().contains(typedText.toLowerCase()));
            comboBox.valueProperty().addListener(new ChangeListener<ComboBoxItemWrap<String>>() {
                @Override
                public void changed(ObservableValue<? extends ComboBoxItemWrap<String>> observableValue, ComboBoxItemWrap<String> stringComboBoxItemWrap, ComboBoxItemWrap<String> t1) {
                    tableCell.getTableView().getItems().get(tableCell.getIndex()).setVoiceActor(comboBox.getValue().toString());
                }
            });
            comboBox.setCellFactory(c->{
                ListCell<ComboBoxItemWrap<String>> cell = new ListCell<>(){
                    boolean initialized = false;
                    @Override
                    protected void updateItem(ComboBoxItemWrap<String> item, boolean empty) {
                        super.updateItem(item, empty);
                        if(!empty){
                            final CheckBox checkBox = new CheckBox(item.toString());
                            checkBox.selectedProperty().bind(item.checkProperty());
                            if(!initialized){
                                if(game.getDeveloper().contains(item.getItem())){
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
                    comboBox.getItems().filtered(f->f.getCheck()).forEach(p->{
                        stringBuilder.append("; "+p.getItem());
                    });
                    final String string = stringBuilder.toString();
                    comboBox.setValue(new ComboBoxItemWrap<String>(string.substring(Integer.min(2, string.length()))));
                });
                return cell;
            });
            tableCell.graphicProperty().bind(Bindings.when(tableCell.emptyProperty()).then((Node)null).otherwise(hBox));
            return tableCell;
        });
        TableColumn2<Character, StringProperty> characterIDColumn = new FilteredTableColumn<>("Character ID");
        characterIDColumn.setCellValueFactory(i-> {
            final StringProperty value = i.getValue().characterIDProperty();
            return Bindings.createObjectBinding(()->value);
        });
        characterIDColumn.setCellFactory(col -> {
            TableCell<Character,StringProperty> c = new TableCell<>();
            final BorderPane borderPane = new BorderPane();
            final ComboBox<String> comboBox = new ComboBox<>(characterOptions);
            borderPane.centerProperty().setValue(comboBox);
            comboBox.setEditable(true);
            comboBox.valueProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                    c.getTableView().getItems().get(c.getIndex()).setCharacterID(comboBox.getValue());
                    c.getTableView().getItems().get(c.getIndex()).setFirstName(characterHashMap.get(comboBox.valueProperty().get()).getFirstName());
                    c.getTableView().getItems().get(c.getIndex()).setFirstName(characterHashMap.get(comboBox.valueProperty().get()).getLastName());
                }
            });
            c.graphicProperty().bind(Bindings.when(c.emptyProperty()).then((Node)null).otherwise(comboBox));
            return c;
        });
        characterIDColumn.setEditable(true);
        TableColumn2<Character,StringProperty> voiceActorColumn = new FilteredTableColumn<>("Voice Actor");
        voiceActorColumn.setEditable(true);
        voiceActorColumn.setCellValueFactory(i-> {
            final StringProperty value = i.getValue().roleProperty();
            return Bindings.createObjectBinding(()->value);
        });
        voiceActorColumn.setCellFactory(col -> {
            TableCell<Character,StringProperty> tableCell = new TableCell<>();
            final BorderPane borderPane = new BorderPane();
            final Button button = new Button("Edit");
            //TODO make button open edit screen for the relationship in the cell
            //button.setOnAction();
            final ComboBox<String> comboBox = new ComboBox<>(staff);
            final HBox hBox = new HBox(5, comboBox,button);
            hBox.autosize();
            borderPane.centerProperty().setValue(hBox);
            comboBox.setEditable(true);
            ComboBoxAutocompleteUtil.autoCompleteComboBoxPlus(comboBox, (typedText, itemToCompare) -> itemToCompare.toLowerCase().contains(typedText.toLowerCase()));
            comboBox.valueProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                    tableCell.getTableView().getItems().get(tableCell.getIndex()).setVoiceActor(comboBox.getValue().toString());
                }
            });
            tableCell.graphicProperty().bind(Bindings.when(tableCell.emptyProperty()).then((Node) null).otherwise(hBox));
            return tableCell;
        });
        characterTable.getColumns().setAll(characterIDColumn,firstNameColumn,lastNameColumn,roleColumn,voiceActorColumn);
    }

    private void configureTriviaTable() {
        triviaTable.setEditable(true);
        triviaTable.setItems(FXCollections.observableList(game.getTrivia()));
        triviaTable.getItems().add(new Trivia("",""));
        HashMap<String, String> triviaHashmap = triviaDAO.getTrivia();
        ObservableList<String> triviaIDs = FXCollections.observableList(triviaHashmap.keySet().stream().toList());
        TableColumn2<Trivia,String> factColumn = new FilteredTableColumn<>("Fact");
        factColumn.setEditable(true);
        factColumn.setCellValueFactory(p->p.getValue().getFact());
        factColumn.setCellFactory(TextField2TableCell.forTableColumn());
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
            comboBox.valueProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                    c.getTableView().getItems().get(c.getIndex()).setTriviaID(comboBox.getValue());
                    c.getTableView().getItems().get(c.getIndex()).setFact(triviaHashmap.get(comboBox.valueProperty().get()));
                }
            });
            c.graphicProperty().bind(Bindings.when(c.emptyProperty()).then((Node)null).otherwise(comboBox));
            return c;
        });
        triviaIdColumn.setEditable(true);
        triviaTable.getColumns().setAll(triviaIdColumn,factColumn);

    }

    private void configureReleaseDatesTable() {
        releaseDatesTable.setEditable(true);
        ObservableList<String> territories = FXCollections.observableList(territoryDAO.getTerritories());
        ObservableList<ReleaseDate> releaseDates = FXCollections.observableList(game.getReleaseDates());
        releaseDatesTable.setItems(releaseDates);
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
            comboBox.valueProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                    c.getTableView().getItems().get(c.getIndex()).setTerritory(new SimpleStringProperty(comboBox.getValue()));
                }
            });
            c.graphicProperty().bind(Bindings.when(c.emptyProperty()).then((Node)null).otherwise(comboBox));
            return c;
        });
        TableColumn2<ReleaseDate, ObjectProperty<LocalDate>> dateColumn = new FilteredTableColumn<>("Territory");
        dateColumn.setCellValueFactory(i-> {
            final ObjectProperty<LocalDate> value = i.getValue().getDate();
            return Bindings.createObjectBinding(()->value);
        });
        dateColumn.setCellFactory(col -> {
            TableCell<ReleaseDate,ObjectProperty<LocalDate>> c = new TableCell<>();
            final BorderPane borderPane = new BorderPane();
            final DatePicker datePicker = new DatePicker();
            borderPane.centerProperty().setValue(datePicker);
            datePicker.valueProperty().addListener(new ChangeListener<LocalDate>() {
                @Override
                public void changed(ObservableValue<? extends LocalDate> observableValue, LocalDate localDate, LocalDate t1) {
                    c.getTableView().getItems().get(c.getIndex()).setDate(new SimpleObjectProperty<LocalDate>(datePicker.getValue()));
                }
            });
            c.graphicProperty().bind(Bindings.when(c.emptyProperty()).then((Node)null).otherwise(datePicker));
            return c;
        });
        releaseDatesTable.getColumns().setAll(territoryColumn,dateColumn);

    }

    private void configureAdditionalAppsTable() {
        additionalAppsTable.setEditable(true);
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("gameName",game.getName());
        parameters.put("platformName",game.getPlatform());
        ObservableList<AdditionalApp> additionalApps = FXCollections.observableList(additionalAppDAO.getAdditionalApps(parameters));
        additionalAppsTable.setItems(additionalApps);
        additionalAppsTable.getItems().add(new AdditionalApp("","",""));
        TableColumn2<AdditionalApp,String> nameColumn = new FilteredTableColumn<>("Name");
        nameColumn.setEditable(true);
        nameColumn.setCellValueFactory(c->c.getValue().getName());
        nameColumn.setCellFactory(TextField2TableCell.forTableColumn());
        TableColumn2<AdditionalApp,String> pathColumn = new FilteredTableColumn<>("Path");
        pathColumn.setEditable(true);
        pathColumn.setCellValueFactory(c->c.getValue().getName());
        pathColumn.setCellFactory(TextField2TableCell.forTableColumn());
        TableColumn2<AdditionalApp,String> argumentsColumn = new FilteredTableColumn<>("Arguments");
        argumentsColumn.setEditable(true);
        argumentsColumn.setCellValueFactory(c->c.getValue().getName());
        argumentsColumn.setCellFactory(TextField2TableCell.forTableColumn());
        nameColumn.setPrefWidth(200);
        pathColumn.setPrefWidth(200);
        argumentsColumn.setPrefWidth(200);
        additionalAppsTable.getColumns().setAll(nameColumn,pathColumn,argumentsColumn);
    }

    private void configureRelatedGameTable() {
        relatedGameTable.setEditable(true);
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("gameName",game.getName());
        parameters.put("platformName",game.getPlatform());
        ArrayList<String> options = new ArrayList<>();
        for (RelatedGameEntry relatedGameEntry :
                gameDAO.getRelatedGamesOptions()) {
            String platformAndGame = "("+relatedGameEntry.getPlatform()+") "+relatedGameEntry.getGame();
            relatedGameMap.put(platformAndGame, relatedGameEntry);
            options.add(platformAndGame);
        }
        ObservableList<String> games = FXCollections.observableList(options);
        ObservableList<RelatedGame> relatedGames = FXCollections.observableList(relatedGameDAO.getRelatedGames(parameters));
        ObservableList<String> relationships = FXCollections.observableList(relationshipDAO.getRelationShips());
        relatedGameTable.setItems(relatedGames);
        relatedGameTable.getItems().add(new RelatedGame("","","",""));
        TableColumn2<RelatedGame,StringProperty> relatedGameColumn = new FilteredTableColumn<>("Game");
        relatedGameColumn.setCellValueFactory(i-> {
            final StringProperty value = i.getValue().getRelatedGameEntry();
            return Bindings.createObjectBinding(()->value);
        });
        relatedGameColumn.setCellFactory(col -> {
            TableCell<RelatedGame,StringProperty> c = new TableCell<>();
            final BorderPane borderPane = new BorderPane();
            final ComboBox<String> comboBox = new ComboBox<>(games);
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
            comboBox.valueProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                    c.getTableView().getItems().get(c.getIndex()).setRelatedGameEntry(new SimpleStringProperty(comboBox.getValue()));
                }
            });
            c.graphicProperty().bind(Bindings.when(c.emptyProperty()).then((Node)null).otherwise(hBox));
            return c;
        });
        relatedGameColumn.setEditable(true);
        TableColumn2<RelatedGame,StringProperty> relationshipColumn = new FilteredTableColumn<>("Relationship");
        relationshipColumn.setCellValueFactory(i-> {
            final StringProperty value = i.getValue().getRelationship();
            return Bindings.createObjectBinding(()->value);
        });
        relationshipColumn.setCellFactory(col -> {
            TableCell<RelatedGame,StringProperty> c = new TableCell<>();
            final BorderPane borderPane = new BorderPane();
            final Button button = new Button("Edit");
            //TODO make button open edit screen for the relationship in the cell
            //button.setOnAction();
            final ComboBox<String> comboBox = new ComboBox<>(relationships);
            final HBox hBox = new HBox(5, comboBox,button);
            hBox.autosize();
            borderPane.centerProperty().setValue(hBox);
            comboBox.setEditable(true);
            ComboBoxAutocompleteUtil.autoCompleteComboBoxPlus(comboBox, (typedText, itemToCompare) -> itemToCompare.toLowerCase().contains(typedText.toLowerCase()));
            comboBox.valueProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                    c.getTableView().getItems().get(c.getIndex()).setRelationship(new SimpleStringProperty(comboBox.getValue()));
                }
            });
            c.graphicProperty().bind(Bindings.when(c.emptyProperty()).then((Node)null).otherwise(hBox));
            return c;
        });
        relationshipColumn.setEditable(true);
        TableColumn2<RelatedGame,String> relationshipDescriptionColumn = new FilteredTableColumn<>();
        relationshipDescriptionColumn.setEditable(true);
        relationshipDescriptionColumn.setCellValueFactory(c->c.getValue().getDescription());
        relationshipDescriptionColumn.setCellFactory(TextAreaTableCell.forTableColumn());
        relationshipDescriptionColumn.setPrefWidth(400);
        relatedGameTable.getColumns().setAll(relatedGameColumn,relationshipColumn,relationshipDescriptionColumn);
    }

    private void configureAlternateNamesTable(){
        alternateNamesTable.setEditable(true);
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("gameName",game.getName());
        parameters.put("platformName",game.getPlatform());
        ObservableList<String> territories = FXCollections.observableList(territoryDAO.getTerritories());
        territories.add("Community");
        ObservableList<AlternateName> alternateNames = FXCollections.observableList(alternateNameDAO.getAlternateNames(parameters));
        alternateNamesTable.setItems(alternateNames);
        alternateNamesTable.getItems().add(new AlternateName("","",""));
        TableColumn2<AlternateName,String> alternateNameIdColumn = new FilteredTableColumn<>("Alternate Name ID");
        alternateNameIdColumn.setEditable(true);
        alternateNameIdColumn.setCellValueFactory(p->p.getValue().getAlternateNameID());
        alternateNameIdColumn.setCellFactory(TextField2TableCell.forTableColumn());
        TableColumn2<AlternateName, StringProperty> reasonColumn = new FilteredTableColumn<>("Reason");
        reasonColumn.setCellValueFactory(i-> {
            final StringProperty value = i.getValue().getRegion();
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
            c.graphicProperty().bind(Bindings.when(c.emptyProperty()).then((Node)null).otherwise(comboBox));
            return c;
        });
        TableColumn2<AlternateName,String> valueColumn = new FilteredTableColumn<>("Value");
        valueColumn.setEditable(true);
        valueColumn.setCellValueFactory(p->p.getValue().getAlternateName());
        valueColumn.setCellFactory(TextField2TableCell.forTableColumn());
        alternateNamesTable.getColumns().setAll(alternateNameIdColumn,reasonColumn,valueColumn);
    }

    private void configureDefaultSummaryField() {
        if(!"null".equals(game.getSummary())){
            maxPlayersField.setText(game.getSummary());
        }
    }

    private void configurePlaymodeComboCheckBox() {
        ObservableList<ComboBoxItemWrap<String>> options = FXCollections.observableList(generateComboBoxItemWrappers(playmodeDAO.getPlaymodes()));
        String publisherValue = "";
        for (String publisher : game.getPlaymodes()) {
            publisherValue += publisher + "; ";
        }
        playModesComboCheckbox.setValue(new ComboBoxItemWrap<>(publisherValue));
        playModesComboCheckbox.setCellFactory(c->{
            ListCell<ComboBoxItemWrap<String>> cell = new ListCell<>(){
                boolean initialized = false;
                @Override
                protected void updateItem(ComboBoxItemWrap<String> item, boolean empty) {
                    super.updateItem(item, empty);
                    if(!empty){
                        final CheckBox checkBox = new CheckBox(item.toString());
                        checkBox.selectedProperty().bind(item.checkProperty());
                        if(!initialized){
                            if(game.getPlaymodes().contains(item.getItem())){
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
                playModesComboCheckbox.getItems().filtered(f->f.getCheck()).forEach(p->{
                    stringBuilder.append("; "+p.getItem());
                });
                final String string = stringBuilder.toString();
                playModesComboCheckbox.setValue(new ComboBoxItemWrap<String>(string.substring(Integer.min(2, string.length()))));
            });
            return cell;
        });
        playModesComboCheckbox.setItems(options);
    }

    private void configurePriorityComboBox() {
        ObservableList<PriorityData> options = FXCollections.observableList(priorityDataDAO.getPriorities());
        priorityComboBox.setItems(options);
        priorityComboBox.setValue(game.getPriority());
    }

    private void configureMaxPlayersField() {
        if(!"null".equals(game.getMaxPlayers())){
            maxPlayersField.setText(game.getMaxPlayers());
        }
    }

    private void configureDefaultSortingTitleField() {
        if(!"null".equals(game.getSortingTitle())){
            defaultSortingTitleTextField.setText(game.getSortingTitle());
        }
    }

    public void configureTitleTextField(){
        titleTextField.setText(game.getName());
    }

    private void configureDescriptionTextArea() {
        if(!"null".equals(game.getDescription())){
            descriptionTextArea.setText(game.getDescription());
        }
    }

    public void configurePlatformComboBox(){
        ObservableList<String> options = FXCollections.observableList(PlatformXMLParser.getPlatforms());
        platformComboBox.setItems(options);
        platformComboBox.setValue(game.getPlatform());
        ComboBoxAutocompleteUtil.autoCompleteComboBoxPlus(platformComboBox, (typedText, itemToCompare) -> itemToCompare.toLowerCase().contains(typedText.toLowerCase()));
    }

    public void configureRatingComboCheckBox(){
        ObservableList<ComboBoxItemWrap<String>> options = FXCollections.observableList(generateComboBoxItemWrappers(ratingDAO.getRatings()));
        String publisherValue = "";
        for (String publisher : game.getRatings()) {
            publisherValue += publisher + "; ";
        }
        ratingComboCheckBox.setValue(new ComboBoxItemWrap<>(publisherValue));
        ratingComboCheckBox.setCellFactory(c->{
            ListCell<ComboBoxItemWrap<String>> cell = new ListCell<>(){
                boolean initialized = false;
                @Override
                protected void updateItem(ComboBoxItemWrap<String> item, boolean empty) {
                    super.updateItem(item, empty);
                    if(!empty){
                        final CheckBox checkBox = new CheckBox(item.toString());
                        checkBox.selectedProperty().bind(item.checkProperty());
                        if(!initialized){
                            if(game.getRatings().contains(item.getItem())){
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
                ratingComboCheckBox.getItems().filtered(f->f.getCheck()).forEach(p->{
                    stringBuilder.append("; "+p.getItem());
                });
                final String string = stringBuilder.toString();
                ratingComboCheckBox.setValue(new ComboBoxItemWrap<String>(string.substring(Integer.min(2, string.length()))));
            });
            return cell;
        });
        ratingComboCheckBox.setItems(options);
    }

    public void configurePublisherComboCheckBox(){
        ObservableList<ComboBoxItemWrap<String>> options = FXCollections.observableList(generateComboBoxItemWrappers(publisherDAO.getPublishers()));
        String publisherValue = "";
        for (String publisher : game.getPublisher()) {
            publisherValue += publisher + "; ";
        }
        publisherComboCheckBox.setValue(new ComboBoxItemWrap<>(publisherValue));
        publisherComboCheckBox.setCellFactory(c->{
            ListCell<ComboBoxItemWrap<String>> cell = new ListCell<>(){
                boolean initialized = false;
                @Override
                protected void updateItem(ComboBoxItemWrap<String> item, boolean empty) {
                    super.updateItem(item, empty);
                    if(!empty){
                        final CheckBox checkBox = new CheckBox(item.toString());
                        checkBox.selectedProperty().bind(item.checkProperty());
                        if(!initialized){
                            if(game.getPublisher().contains(item.getItem())){
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
                publisherComboCheckBox.getItems().filtered(f->f.getCheck()).forEach(p->{
                    stringBuilder.append("; "+p.getItem());
                });
                final String string = stringBuilder.toString();
                publisherComboCheckBox.setValue(new ComboBoxItemWrap<String>(string.substring(Integer.min(2, string.length()))));
            });
            return cell;
        });
        publisherComboCheckBox.setItems(options);
    }

    public void configureDeveloperComboCheckBox(){
        ObservableList<ComboBoxItemWrap<String>> options = FXCollections.observableList(generateComboBoxItemWrappers(developerDAO.getDevelopers()));
        String publisherValue = "";
        for (String publisher : game.getDeveloper()) {
            publisherValue += publisher + "; ";
        }
        developerComboCheckBox.setValue(new ComboBoxItemWrap<>(publisherValue));
        developerComboCheckBox.setCellFactory(c->{
            ListCell<ComboBoxItemWrap<String>> cell = new ListCell<>(){
                boolean initialized = false;
                @Override
                protected void updateItem(ComboBoxItemWrap<String> item, boolean empty) {
                    super.updateItem(item, empty);
                    if(!empty){
                        final CheckBox checkBox = new CheckBox(item.toString());
                        checkBox.selectedProperty().bind(item.checkProperty());
                        if(!initialized){
                            if(game.getDeveloper().contains(item.getItem())){
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
                developerComboCheckBox.getItems().filtered(f->f.getCheck()).forEach(p->{
                    stringBuilder.append("; "+p.getItem());
                });
                final String string = stringBuilder.toString();
                developerComboCheckBox.setValue(new ComboBoxItemWrap<String>(string.substring(Integer.min(2, string.length()))));
            });
            return cell;
        });
        developerComboCheckBox.setItems(options);
    }

    public ArrayList<ComboBoxItemWrap<String>> generateComboBoxItemWrappers(List<String> strings){
        ArrayList<ComboBoxItemWrap<String>> comboBoxItemWraps = new ArrayList<>();
        for (String string :
                strings) {
            comboBoxItemWraps.add(new ComboBoxItemWrap<>(string));
        }
        return comboBoxItemWraps;
    }

}
