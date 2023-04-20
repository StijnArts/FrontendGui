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
import org.controlsfx.control.tableview2.*;
import org.controlsfx.control.tableview2.cell.*;
import stijn.dev.datasource.database.dao.*;
import stijn.dev.datasource.importing.xml.*;
import stijn.dev.datasource.objects.data.*;
import stijn.dev.datasource.objects.items.*;
import stijn.dev.datasource.objects.items.Character;
import stijn.dev.resource.controllers.*;
import stijn.dev.resource.controllers.components.*;
import stijn.dev.util.javafx.*;

import java.lang.*;
import java.time.*;
import java.util.*;

public class EditPrepper {
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
    private TagDAO tagDAO = new TagDAO();
    private HashMap<String, RelatedGameEntry> relatedGameMap = new HashMap<>();

    public void configure(EditController editController){
        configureAlternateNamesTable(editController);
        configureRelatedGameTable(editController);
        configureAdditionalAppsTable(editController);
        configureReleaseDatesTable(editController);
        configureTriviaTable(editController);
        configureTagTable(editController);
        configurePublisherComboCheckBox(editController);
        configureDeveloperComboCheckBox(editController);
        configureRatingComboCheckBox(editController);
        configurePlaymodeComboCheckBox(editController);
        configureTitleTextField(editController);
        configurePlatformComboBox(editController);
        configurePriorityComboBox(editController);
        configureMaxPlayersField(editController);
        configureDefaultSortingTitleField(editController);
        configureDefaultSummaryField(editController);
        configureDescriptionTextArea(editController);
        configureStaffTable(editController);
        configureCharacterTable(editController);
    }
    private void configureTagTable(EditController editController) {
        editController.getTagTable().setEditable(true);
        ObservableList<String> tagOptions = FXCollections.observableList(tagDAO.getTags());
        ObservableList<Tag> tags = FXCollections.observableList(editController.getGame().getTags());
        editController.getTagTable().setItems(tags);
        editController.getTagTable().getItems().add(new Tag(""));
        TableColumn2<Tag, StringProperty> tagColumn = new FilteredTableColumn<>("Tag");
        tagColumn.setCellValueFactory(i-> {
            final StringProperty value = i.getValue().nameProperty();
            return Bindings.createObjectBinding(()->value);
        });
        tagColumn.setCellFactory(col -> {
            TableCell<Tag,StringProperty> c = new TableCell<>();
            final Button button = new Button("Edit");
            //TODO make button open edit screen for the relationship in the cell
            //button.setOnAction();
            final BorderPane borderPane = new BorderPane();
            final ComboBox<String> comboBox = new ComboBox<>(tagOptions);
            final HBox hBox = new HBox(5, comboBox,button);
            borderPane.centerProperty().setValue(hBox);
            comboBox.setEditable(true);
            comboBox.valueProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                    if(comboBox.getValue()!=null&&!comboBox.getValue().isBlank()){
                        c.getTableView().getItems().get(c.getIndex()).setName(comboBox.getValue());
                        if(c.getTableView().getItems().size()-1==c.getIndex()){
                            c.getTableView().getItems().add(new Tag(""));
                        }
                    } else{
                        c.getTableView().getItems().get(c.getIndex()).setName(null);
                    }
                }
            });
            c.itemProperty().addListener(((observableValue, oldValue, newValue) -> {
                if(oldValue != null){
                    comboBox.valueProperty().unbindBidirectional(oldValue);
                }
                if(newValue != null){
                    comboBox.valueProperty().bindBidirectional(newValue);
                }
            }));
            c.graphicProperty().bind(Bindings.when(c.emptyProperty()).then((Node)null).otherwise(hBox));
            return c;
        });
        tagColumn.setEditable(true);
        editController.getTagTable().getColumns().setAll(tagColumn);
    }

    private void configureStaffTable(EditController editController) {
        editController.getStaffTable().setEditable(true);
        ArrayList<Staff> staff = editController.getGame().getStaff();
        HashMap<String, Staff> staffHashMap = new HashMap<>();
        for (Staff staffItem :
                staffDAO.getStaff()) {
            staffHashMap.put(staffItem.getStaffID(),staffItem);
        }

        ObservableList<String> staffOptions = FXCollections.observableList(staffHashMap.keySet().stream().toList());
        ObservableList<String> staffRoles = FXCollections.observableList(staffRoleDAO.getStaffRoles());
        ObservableList<Staff> staffObservable = FXCollections.observableList(staff);
        editController.getStaffTable().setItems(staffObservable);
        ArrayList<String> temp = new ArrayList<>();
        editController.getStaffTable().getItems().add(new Staff("","","",""));
        TableColumn2<Staff,String> firstNameColumn = new FilteredTableColumn<>("First Name");
        firstNameColumn.setEditable(true);
        firstNameColumn.setCellValueFactory(p->p.getValue().firstNameProperty());
        firstNameColumn.setCellFactory(TextField2TableCell.forTableColumn());
        firstNameColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Staff,String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Staff,String> event) {
                if(!event.getNewValue().isBlank()){
                    if(event.getTableView().getItems().size()-1==event.getTablePosition().getRow()){
                        event.getTableView().getItems().add(new Staff("","",""));
                    }
                }
                event.getTableView().getItems().get(event.getTablePosition().getRow()).setFirstName(event.getNewValue());
            }
        });
        TableColumn2<Staff,String> lastNameColumn = new FilteredTableColumn<>("Last Name");
        lastNameColumn.setEditable(true);
        lastNameColumn.setCellValueFactory(p->p.getValue().lastNameProperty());
        lastNameColumn.setCellFactory(TextField2TableCell.forTableColumn());
        lastNameColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Staff,String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Staff,String> event) {
                if(!event.getNewValue().isBlank()){
                    if(event.getTableView().getItems().size()-1==event.getTablePosition().getRow()){
                        event.getTableView().getItems().add(new Staff("","",""));
                    }
                }
                event.getTableView().getItems().get(event.getTablePosition().getRow()).setLastName(event.getNewValue());
            }
        });
        TableColumn2<Staff, StringProperty> roleColumn = new FilteredTableColumn<>("Role");
        roleColumn.setCellValueFactory(i-> {
            final StringProperty value = i.getValue().roleProperty();
            return Bindings.createObjectBinding(()->value);
        });
        roleColumn.setCellFactory(col -> {
            TableCell<Staff,StringProperty> c = new TableCell<>();
            final BorderPane borderPane = new BorderPane();
            final Button button = new Button("Edit");
            //TODO make button open edit screen for the relationship in the cell
            //button.setOnAction();
            final ComboBox<String> comboBox = new ComboBox<>(staffRoles);
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
            comboBox.valueProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                    if(comboBox.getValue()!=null&&!comboBox.getValue().isBlank()){
                        c.getTableView().getItems().get(c.getIndex()).setRole(comboBox.getValue());
                        if(c.getTableView().getItems().size()-1==c.getIndex()){
                            c.getTableView().getItems().add(new Staff("","","",""));
                        }
                    } else{
                        c.getTableView().getItems().get(c.getIndex()).setRole(comboBox.getValue());
                    }
                }
            });
            c.graphicProperty().bind(Bindings.when(c.emptyProperty()).then((Node)null).otherwise(hBox));
            return c;
        });
        roleColumn.setEditable(true);
        TableColumn2<Staff, StringProperty> staffIDColumn = new FilteredTableColumn<>("Staff ID");
        staffIDColumn.setCellValueFactory(i-> {
            final StringProperty value = i.getValue().staffIDProperty();
            return Bindings.createObjectBinding(()->value);
        });
        staffIDColumn.setCellFactory(col -> {
            TableCell<Staff,StringProperty> c = new TableCell<>();
            final Button button = new Button("Edit");
            //TODO make button open edit screen for the relationship in the cell
            //button.setOnAction();
            final BorderPane borderPane = new BorderPane();
            final ComboBox<String> comboBox = new ComboBox<>(staffOptions);
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
            comboBox.valueProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                    if(comboBox.getValue()!=null&&!comboBox.getValue().isBlank()){
                        c.getTableView().getItems().get(c.getIndex()).setStaffID(comboBox.getValue());
                        if(staffHashMap.get(comboBox.valueProperty().get())!=null){
                            c.getTableView().getItems().get(c.getIndex()).setFirstName(staffHashMap.get(comboBox.valueProperty().get()).getFirstName());
                        } else {
                            c.getTableView().getItems().get(c.getIndex()).setFirstName(c.getTableView().getItems().get(c.getIndex()).getFirstName());
                        }

                        if(staffHashMap.get(comboBox.valueProperty().get())!=null){
                            c.getTableView().getItems().get(c.getIndex()).setLastName(staffHashMap.get(comboBox.valueProperty().get()).getLastName());
                        } else {
                            c.getTableView().getItems().get(c.getIndex()).setLastName(c.getTableView().getItems().get(c.getIndex()).getLastName());
                        }

                        if(c.getTableView().getItems().size()-1==c.getIndex()){
                            c.getTableView().getItems().add(new Staff("","","",""));
                        }
                    } else{
                        c.getTableView().getItems().get(c.getIndex()).setStaffID(comboBox.getValue());
                        c.getTableView().getItems().get(c.getIndex()).setFirstName(null);
                        c.getTableView().getItems().get(c.getIndex()).setLastName(null);
                    }
                }
            });
            c.graphicProperty().bind(Bindings.when(c.emptyProperty()).then((Node)null).otherwise(hBox));
            return c;
        });
        staffIDColumn.setEditable(true);
        editController.getStaffTable().getColumns().setAll(staffIDColumn,firstNameColumn,lastNameColumn,roleColumn);
    }

    private void configureCharacterTable(EditController editController) {
        editController.getCharacterTable().setEditable(true);
        ArrayList<Character> character = editController.getGame().getCharacters();
        HashMap<String, Character> characterHashMap = new HashMap<>();
        for (Character characterItem :
                characterDAO.getCharacters()) {
            characterHashMap.put(characterItem.getCharacterID(),characterItem);
        }
        ObservableList<String> staff =FXCollections.observableList(staffDAO.getStaffIDs());
        ObservableList<String> characterOptions = FXCollections.observableList(characterHashMap.keySet().stream().toList());
        ObservableList<String> characterRoles = FXCollections.observableList(characterRoleDAO.getCharacterRoles());
        ObservableList<Character> characterObservable = FXCollections.observableList(character);
        editController.getCharacterTable().setItems(characterObservable);
        editController.getCharacterTable().getItems().add(new Character("",""));
        TableColumn2<Character,String> nameColumn = new FilteredTableColumn<>("Name");
        nameColumn.setEditable(true);
        nameColumn.setCellValueFactory(p->p.getValue().nameProperty());
        nameColumn.setCellFactory(TextField2TableCell.forTableColumn());
        nameColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Character,String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Character,String> event) {
                if(!event.getNewValue().isBlank()){
                    if(event.getTableView().getItems().size()-1==event.getTablePosition().getRow()){
                        event.getTableView().getItems().add(new Character("",""));
                    }
                }
                event.getTableView().getItems().get(event.getTablePosition().getRow()).setName(event.getNewValue());
            }
        });
        TableColumn2<Character, StringProperty> roleColumn = new FilteredTableColumn<>("Role");
        roleColumn.setCellValueFactory(i-> {
            final StringProperty value = i.getValue().roleProperty();
            return Bindings.createObjectBinding(()->value);
        });
        roleColumn.setCellFactory(col -> {
            TableCell<Character,StringProperty> c = new TableCell<>();
            final Button button = new Button("Edit");
            //TODO make button open edit screen for the relationship in the cell
            //button.setOnAction();
            final BorderPane borderPane = new BorderPane();
            final ComboBox<String> comboBox = new ComboBox<>(characterRoles);
            final HBox hBox = new HBox(5, comboBox,button);
            borderPane.centerProperty().setValue(hBox);
            comboBox.setEditable(true);
            comboBox.valueProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                    if(comboBox.getValue()!=null&&!comboBox.getValue().isBlank()){
                        c.getTableView().getItems().get(c.getIndex()).setRole(comboBox.getValue());
                        if(c.getTableView().getItems().size()-1==c.getIndex()){
                            c.getTableView().getItems().add(new Character("","","",""));
                        }
                    } else{
                        c.getTableView().getItems().get(c.getIndex()).setRole("");
                    }
                }
            });
            c.itemProperty().addListener(((observableValue, oldValue, newValue) -> {
                if(oldValue != null){
                    comboBox.valueProperty().unbindBidirectional(oldValue);
                }
                if(newValue != null){
                    comboBox.valueProperty().bindBidirectional(newValue);
                }
            }));
            c.graphicProperty().bind(Bindings.when(c.emptyProperty()).then((Node)null).otherwise(hBox));
            return c;
        });
        roleColumn.setEditable(true);
        TableColumn2<Character, StringProperty> characterIDColumn = new FilteredTableColumn<>("Character ID");
        characterIDColumn.setCellValueFactory(i-> {
            final StringProperty value = i.getValue().characterIDProperty();
            return Bindings.createObjectBinding(()->value);
        });
        characterIDColumn.setCellFactory(col -> {
            TableCell<Character,StringProperty> c = new TableCell<>();
            final Button button = new Button("Edit");
            //TODO make button open edit screen for the relationship in the cell
            //button.setOnAction();
            final BorderPane borderPane = new BorderPane();
            final ComboBox<String> comboBox = new ComboBox<>(characterOptions);
            final HBox hBox = new HBox(5, comboBox,button);
            borderPane.centerProperty().setValue(hBox);
            comboBox.setEditable(true);
            comboBox.valueProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                    if(comboBox.getValue()!=null&&!comboBox.getValue().isBlank()){
                        c.getTableView().getItems().get(c.getIndex()).setCharacterID(comboBox.getValue());
                        if(characterHashMap.get(comboBox.valueProperty().get())!=null){
                            c.getTableView().getItems().get(c.getIndex()).setName(characterHashMap.get(comboBox.valueProperty().get()).getName());
                        } else {
                            c.getTableView().getItems().get(c.getIndex()).setName(c.getTableView().getItems().get(c.getIndex()).getName());
                        }

                        if(c.getTableView().getItems().size()-1==c.getIndex()){
                            c.getTableView().getItems().add(new Character("","","",""));
                        }
                    } else{
                        c.getTableView().getItems().get(c.getIndex()).setCharacterID(comboBox.getValue());
                        c.getTableView().getItems().get(c.getIndex()).setName(null);
                    }
                }
            });
            c.itemProperty().addListener(((observableValue, oldValue, newValue) -> {
                if(oldValue != null){
                    comboBox.valueProperty().unbindBidirectional(oldValue);
                }
                if(newValue != null){
                    comboBox.valueProperty().bindBidirectional(newValue);
                }
            }));
            c.graphicProperty().bind(Bindings.when(c.emptyProperty()).then((Node)null).otherwise(hBox));
            return c;
        });
        characterIDColumn.setEditable(true);
        TableColumn2<Character,StringProperty> voiceActorColumn = new FilteredTableColumn<>("Voice Actor");
        voiceActorColumn.setEditable(true);
        voiceActorColumn.setCellValueFactory(i-> {
            final StringProperty value = i.getValue().voiceActorProperty();
            return Bindings.createObjectBinding(()->value);
        });
        voiceActorColumn.setCellFactory(col -> {
            TableCell<Character,StringProperty> c = new TableCell<>();
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
                    if(comboBox.getValue()!=null&&!comboBox.getValue().isBlank()){
                        c.getTableView().getItems().get(c.getIndex()).setCharacterID(comboBox.getValue());
                        c.getTableView().getItems().get(c.getIndex()).setName(characterHashMap.get(comboBox.valueProperty().get()).getName());
                        if(c.getTableView().getItems().size()-1==c.getIndex()){
                            c.getTableView().getItems().add(new Character("","","",""));
                        }
                    } else{
                        c.getTableView().getItems().get(c.getIndex()).setCharacterID(comboBox.getValue());
                        c.getTableView().getItems().get(c.getIndex()).setName(null);
                    }
                }
            });
            c.graphicProperty().bind(Bindings.when(c.emptyProperty()).then((Node) null).otherwise(hBox));
            return c;
        });
        editController.getCharacterTable().getColumns().setAll(characterIDColumn,nameColumn,roleColumn,voiceActorColumn);
    }

    private void configureTriviaTable(EditController editController) {
        editController.getTriviaTable().setEditable(true);
        editController.getTriviaTable().setItems(FXCollections.observableList(editController.getGame().getTrivia()));
        Trivia blank = new Trivia("","");
        editController.getTriviaTable().getItems().add(blank);
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
        editController.getTriviaTable().getColumns().setAll(triviaIdColumn,factColumn);

    }

    private void configureReleaseDatesTable(EditController editController) {
        editController.getReleaseDatesTable().setEditable(true);
        ObservableList<String> territories = FXCollections.observableList(territoryDAO.getTerritories());
        ObservableList<ReleaseDate> releaseDates = FXCollections.observableList(editController.getGame().getReleaseDates());
        editController.getReleaseDatesTable().setItems(releaseDates);
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
            //TODO fix date not being read from game.
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
        editController.getReleaseDatesTable().getColumns().setAll(territoryColumn,dateColumn);

    }

    private void configureAdditionalAppsTable(EditController editController) {
        editController.getAdditionalAppsTable().setEditable(true);
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("gameName",editController.getGame().getName());
        parameters.put("platformName",editController.getGame().getPlatform());
        ObservableList<AdditionalApp> additionalApps = FXCollections.observableList(editController.getGame().getAdditionalApps());
        editController.getAdditionalAppsTable().setItems(additionalApps);
        editController.getAdditionalAppsTable().getItems().add(new AdditionalApp("","",""));
        TableColumn2<AdditionalApp,String> nameColumn = new FilteredTableColumn<>("Name");
        nameColumn.setEditable(true);
        nameColumn.setCellValueFactory(c->c.getValue().getName());
        nameColumn.setCellFactory(TextField2TableCell.forTableColumn());
        nameColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<AdditionalApp,String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<AdditionalApp,String> event) {
                if(!event.getNewValue().isBlank()){
                    if(event.getTableView().getItems().size()-1==event.getTablePosition().getRow()){
                        event.getTableView().getItems().add(new AdditionalApp("","",""));
                    }
                }
                event.getTableView().getItems().get(event.getTablePosition().getRow()).setName(new SimpleStringProperty(event.getNewValue()));
            }
        });
        TableColumn2<AdditionalApp,String> pathColumn = new FilteredTableColumn<>("Path");
        pathColumn.setEditable(true);
        pathColumn.setCellValueFactory(c->c.getValue().getPath());
        pathColumn.setCellFactory(TextField2TableCell.forTableColumn());
        pathColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<AdditionalApp,String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<AdditionalApp,String> event) {
                if(!event.getNewValue().isBlank()){
                    if(event.getTableView().getItems().size()-1==event.getTablePosition().getRow()){
                        event.getTableView().getItems().add(new AdditionalApp("","",""));
                    }
                }
                event.getTableView().getItems().get(event.getTablePosition().getRow()).setPath(new SimpleStringProperty(event.getNewValue()));
            }
        });
        TableColumn2<AdditionalApp,String> argumentsColumn = new FilteredTableColumn<>("Arguments");
        argumentsColumn.setEditable(true);
        argumentsColumn.setCellValueFactory(c->c.getValue().getArguments());
        argumentsColumn.setCellFactory(TextField2TableCell.forTableColumn());
        argumentsColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<AdditionalApp,String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<AdditionalApp,String> event) {
                if(!event.getNewValue().isBlank()){
                    if(event.getTableView().getItems().size()-1==event.getTablePosition().getRow()){
                        event.getTableView().getItems().add(new AdditionalApp("","",""));
                    }
                }
                event.getTableView().getItems().get(event.getTablePosition().getRow()).setArguments(new SimpleStringProperty(event.getNewValue()));
            }
        });
        nameColumn.setPrefWidth(200);
        pathColumn.setPrefWidth(200);
        argumentsColumn.setPrefWidth(200);
        editController.getAdditionalAppsTable().getColumns().setAll(nameColumn,pathColumn,argumentsColumn);
    }
    private void configureRelatedGameTable(EditController editController) {
        editController.getRelatedGameTable().setEditable(true);
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("gameName",editController.getGame().getName());
        parameters.put("platformName",editController.getGame().getPlatform());
        ArrayList<String> options = new ArrayList<>();
        for (RelatedGameEntry relatedGameEntry :
                gameDAO.getRelatedGamesOptions()) {
            String platformAndGame = "("+relatedGameEntry.getPlatform()+") "+relatedGameEntry.getGame();
            relatedGameMap.put(platformAndGame, relatedGameEntry);
            options.add(platformAndGame);
        }
        ObservableList<String> games = FXCollections.observableList(options);
        ObservableList<RelatedGame> relatedGames = FXCollections.observableList(editController.getGame().getRelatedGames());
        ObservableList<String> relationships = FXCollections.observableList(relationshipDAO.getRelationShips());
        editController.getRelatedGameTable().setItems(relatedGames);
        editController.getRelatedGameTable().getItems().add(new RelatedGame("","","",""));
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
                    if(comboBox.getValue()!=null&&!comboBox.getValue().isBlank()&&!"()".equals(comboBox.getValue().trim())) {
                        c.getTableView().getItems().get(c.getIndex()).setRelatedGameEntry(new SimpleStringProperty(comboBox.getValue()));
                        if(c.getTableView().getItems().size()-1==c.getIndex()){
                            c.getTableView().getItems().add(new RelatedGame("","","",""));
                        }
                    } else {
                        c.getTableView().getItems().get(c.getIndex()).setRelatedGameEntry(new SimpleStringProperty(comboBox.getValue()));
                    }
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
                    if(comboBox.getValue()!=null&&!comboBox.getValue().isBlank()) {
                        c.getTableView().getItems().get(c.getIndex()).setRelationship(new SimpleStringProperty(comboBox.getValue()));
                        if(c.getTableView().getItems().size()-1==c.getIndex()){
                            c.getTableView().getItems().add(new RelatedGame("","","",""));
                        }
                    } else {
                        c.getTableView().getItems().get(c.getIndex()).setRelationship(new SimpleStringProperty(comboBox.getValue()));
                    }
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
        relationshipDescriptionColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<RelatedGame, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<RelatedGame, String> event) {
                if(!event.getNewValue().isBlank()){
                    if(event.getTableView().getItems().size()-1==event.getTablePosition().getRow()){
                        event.getTableView().getItems().add(new RelatedGame("","","",""));
                    }
                }
                event.getTableView().getItems().get(event.getTablePosition().getRow()).setDescription(new SimpleStringProperty(event.getNewValue()));
            }
        });
        relationshipDescriptionColumn.setPrefWidth(400);
        editController.getRelatedGameTable().getColumns().setAll(relatedGameColumn,relationshipColumn,relationshipDescriptionColumn);
    }

    private void configureAlternateNamesTable(EditController editController){
        editController.getAlternateNamesTable().setEditable(true);
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("gameName",editController.getGame().getName());
        parameters.put("platformName",editController.getGame().getPlatform());
        ObservableList<String> territories = FXCollections.observableList(territoryDAO.getTerritories());
        territories.add("Community");
        ObservableList<AlternateName> alternateNames = FXCollections.observableList(editController.getGame().getAlternateNames());
        editController.getAlternateNamesTable().setItems(alternateNames);
        editController.getAlternateNamesTable().getItems().add(new AlternateName("","",""));
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
        editController.getAlternateNamesTable().getColumns().setAll(alternateNameIdColumn,reasonColumn,valueColumn);
    }

    private void configureDefaultSummaryField(EditController editController) {
        if(!"null".equals(editController.getGame().getSummary())){
            editController.getMaxPlayersField().setText(editController.getGame().getSummary());
        }
    }

    private void configurePlaymodeComboCheckBox(EditController editController) {
        ObservableList<ComboBoxItemWrap<String>> options = FXCollections.observableList(generateComboBoxItemWrappers(playmodeDAO.getPlayModes()));
        String publisherValue = "";
        for (String publisher : editController.getGame().getPlaymodes()) {
            publisherValue += publisher + "; ";
        }
        editController.getPlayModesComboCheckbox().setValue(new ComboBoxItemWrap<>(publisherValue));
        editController.getPlayModesComboCheckbox().setConverter(new StringConverter<ComboBoxItemWrap<String>>() {
            @Override
            public String toString(ComboBoxItemWrap<String> stringComboBoxItemWrap) {
                return stringComboBoxItemWrap.itemProperty().getValue();
            }

            @Override
            public ComboBoxItemWrap<String> fromString(String s) {
                return new ComboBoxItemWrap<String>(s);
            }
        });
        editController.getPlayModesComboCheckbox().focusedProperty().addListener(((observableValue, aBoolean, t1) -> {
            editController.getPlayModesComboCheckbox().setValue(new ComboBoxItemWrap<>(editController.getPlayModesComboCheckbox().getValue().itemProperty().getValue()));
        }));

        editController.getPlayModesComboCheckbox().setCellFactory(c->{
            ListCell<ComboBoxItemWrap<String>> cell = new ListCell<>(){
                boolean initialized = false;
                @Override
                protected void updateItem(ComboBoxItemWrap<String> item, boolean empty) {
                    super.updateItem(item, empty);
                    if(!empty){
                        final CheckBox checkBox = new CheckBox(item.toString());
                        checkBox.selectedProperty().bind(item.checkProperty());
                        if(!initialized){
                            if(editController.getGame().getPlaymodes().contains(item.getItem())){
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
                editController.getPlayModesComboCheckbox().getItems().filtered(f->f.getCheck()).forEach(p->{
                    stringBuilder.append("; "+p.getItem());
                });
                final String string = stringBuilder.toString();
                editController.getPlayModesComboCheckbox().setValue(new ComboBoxItemWrap<String>(string.substring(Integer.min(2, string.length()))));
            });
            return cell;
        });
        editController.getPlayModesComboCheckbox().setItems(options);
    }

    private void configurePriorityComboBox(EditController editController) {
        ObservableList<PriorityData> options = FXCollections.observableList(priorityDataDAO.getPriorities());
        editController.getPriorityComboBox().setItems(options);
        editController.getPriorityComboBox().setValue(editController.getGame().getPriority());
    }

    private void configureMaxPlayersField(EditController editController) {
        if(!"null".equals(editController.getGame().getMaxPlayers())){
            editController.getMaxPlayersField().setText(editController.getGame().getMaxPlayers());
        }
    }

    private void configureDefaultSortingTitleField(EditController editController) {
        if(!"null".equals(editController.getGame().getSortingTitle())){
            editController.getDefaultSortingTitleTextField().setText(editController.getGame().getSortingTitle());
        }
    }

    public void configureTitleTextField(EditController editController){
        editController.getTitleTextField().setText(editController.getGame().getName());
        //TODO add check that game doesnt already exist on selected console
        editController.getTitleTextField().focusedProperty().addListener((arg0,oldValue, newValue)->{
            if(!newValue){
                if(gameExistsOnConsole()){
                    editController.getWarningBorderpane().setVisible(true);
                    editController.getSaveButton().setDisable(true);
                } else{
                    editController.getWarningBorderpane().setVisible(false);
                    editController.getSaveButton().setDisable(false);
                }
            }
        });
    }

    private boolean gameExistsOnConsole() {
        return false;
        //TODO implement
    }

    private void configureDescriptionTextArea(EditController editController) {
        if(!"null".equals(editController.getGame().getDescription())){
            editController.getDescriptionTextArea().setText(editController.getGame().getDescription());
        }
    }

    public void configurePlatformComboBox(EditController editController){
        ObservableList<String> options = FXCollections.observableList(PlatformXMLParser.getPlatforms());
        editController.getPlatformComboBox().setItems(options);
        editController.getPlatformComboBox().setValue(editController.getGame().getPlatform());
        ComboBoxAutocompleteUtil.autoCompleteComboBoxPlus(editController.getPlatformComboBox(), (typedText, itemToCompare) -> itemToCompare.toLowerCase().contains(typedText.toLowerCase()));
        editController.getPlatformComboBox().focusedProperty().addListener((arg0,oldValue, newValue)->{
            if(!newValue){
                if(gameExistsOnConsole()){
                    editController.getPlatformComboBox().setVisible(true);
                    editController.getSaveButton().setDisable(true);
                } else{
                    editController.getPlatformComboBox().setVisible(false);
                    editController.getSaveButton().setDisable(false);
                }
            }
        });
    }

    public void configureRatingComboCheckBox(EditController editController){
        ObservableList<ComboBoxItemWrap<String>> options = FXCollections.observableList(generateComboBoxItemWrappers(ratingDAO.getRatings()));
        String ratingValue = "";
        for (String rating : editController.getGame().getRatings()) {
            ratingValue += rating + "; ";
        }
        editController.getRatingComboCheckBox().setValue(new ComboBoxItemWrap<>(ratingValue));
        editController.getRatingComboCheckBox().setConverter(new StringConverter<ComboBoxItemWrap<String>>() {
            @Override
            public String toString(ComboBoxItemWrap<String> stringComboBoxItemWrap) {
                return stringComboBoxItemWrap.itemProperty().getValue();
            }

            @Override
            public ComboBoxItemWrap<String> fromString(String s) {
                return new ComboBoxItemWrap<String>(s);
            }
        });
        editController.getRatingComboCheckBox().setCellFactory(c->{
            ListCell<ComboBoxItemWrap<String>> cell = new ListCell<>(){
                boolean initialized = false;
                @Override
                protected void updateItem(ComboBoxItemWrap<String> item, boolean empty) {
                    super.updateItem(item, empty);
                    if(!empty){
                        final CheckBox checkBox = new CheckBox(item.toString());
                        checkBox.selectedProperty().bind(item.checkProperty());
                        if(!initialized){
                            if(editController.getGame().getRatings().contains(item.getItem())){
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
                editController.getRatingComboCheckBox().getItems().filtered(f->f.getCheck()).forEach(p->{
                    stringBuilder.append("; "+p.getItem());
                });
                final String string = stringBuilder.toString();
                editController.getRatingComboCheckBox().setValue(new ComboBoxItemWrap<String>(string.substring(Integer.min(2, string.length()))));
                editController.getRatingComboCheckBox().tooltipProperty().setValue(new Tooltip(string.substring(Integer.min(2, string.length()))));
            });
            return cell;
        });
        editController.getRatingComboCheckBox().setItems(options);
    }

    public void configurePublisherComboCheckBox(EditController editController){
        ObservableList<ComboBoxItemWrap<String>> options = FXCollections.observableList(generateComboBoxItemWrappers(publisherDAO.getPublishers()));
        String publisherValue = "";
        for (String publisher : editController.getGame().getPublisher()) {
            publisherValue += publisher + "; ";
        }
        editController.getPublisherComboCheckBox().setValue(new ComboBoxItemWrap<>(publisherValue));
        editController.getPublisherComboCheckBox().setConverter(new StringConverter<ComboBoxItemWrap<String>>() {
            @Override
            public String toString(ComboBoxItemWrap<String> stringComboBoxItemWrap) {
                return stringComboBoxItemWrap.itemProperty().getValue();
            }

            @Override
            public ComboBoxItemWrap<String> fromString(String s) {
                return new ComboBoxItemWrap<String>(s);
            }
        });
        editController.getPublisherComboCheckBox().setCellFactory(c->{
            ListCell<ComboBoxItemWrap<String>> cell = new ListCell<>(){
                boolean initialized = false;
                @Override
                protected void updateItem(ComboBoxItemWrap<String> item, boolean empty) {
                    super.updateItem(item, empty);
                    if(!empty){
                        final CheckBox checkBox = new CheckBox(item.toString());
                        checkBox.selectedProperty().bind(item.checkProperty());
                        if(!initialized){
                            if(editController.getGame().getPublisher().contains(item.getItem())){
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
                editController.getPublisherComboCheckBox().getItems().filtered(f->f.getCheck()).forEach(p->{
                    stringBuilder.append("; "+p.getItem());
                });
                final String string = stringBuilder.toString();
                editController.getPublisherComboCheckBox().setValue(new ComboBoxItemWrap<String>(string.substring(Integer.min(2, string.length()))));
                editController.getPublisherComboCheckBox().tooltipProperty().setValue(new Tooltip(string.substring(Integer.min(2, string.length()))));
            });
            return cell;
        });
        editController.getPublisherComboCheckBox().focusedProperty().addListener(((observableValue, aBoolean, t1) -> {
            editController.getPublisherComboCheckBox().setValue(new ComboBoxItemWrap<>(editController.getPublisherComboCheckBox().getValue().itemProperty().getValue()));
        }));
        editController.getPublisherComboCheckBox().setItems(options);
    }

    public void configureDeveloperComboCheckBox(EditController editController){
        ObservableList<ComboBoxItemWrap<String>> options = FXCollections.observableList(generateComboBoxItemWrappers(developerDAO.getDevelopers()));
        String publisherValue = "";
        for (String publisher : editController.getGame().getDeveloper()) {
            publisherValue += publisher + "; ";
        }
        editController.getDeveloperComboCheckBox().setConverter(new StringConverter<ComboBoxItemWrap<String>>() {
            @Override
            public String toString(ComboBoxItemWrap<String> stringComboBoxItemWrap) {
                return stringComboBoxItemWrap.itemProperty().getValue();
            }

            @Override
            public ComboBoxItemWrap<String> fromString(String s) {
                return new ComboBoxItemWrap<String>(s);
            }
        });
        editController.getDeveloperComboCheckBox().focusedProperty().addListener(((observableValue, aBoolean, t1) -> {
            editController.getDeveloperComboCheckBox().setValue(new ComboBoxItemWrap<>(editController.getDeveloperComboCheckBox().getValue().itemProperty().getValue()));
        }));
        editController.getDeveloperComboCheckBox().setValue(new ComboBoxItemWrap<>(publisherValue));
        editController.getDeveloperComboCheckBox().setCellFactory(c->{
            ListCell<ComboBoxItemWrap<String>> cell = new ListCell<>(){
                boolean initialized = false;
                @Override
                protected void updateItem(ComboBoxItemWrap<String> item, boolean empty) {
                    super.updateItem(item, empty);
                    if(!empty){
                        final CheckBox checkBox = new CheckBox(item.toString());
                        checkBox.selectedProperty().bind(item.checkProperty());
                        if(!initialized){
                            if(editController.getGame().getDeveloper().contains(item.getItem())){
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
                editController.getDeveloperComboCheckBox().getItems().filtered(f->f.getCheck()).forEach(p->{
                    stringBuilder.append("; "+p.getItem());
                });
                final String string = stringBuilder.toString();
                editController.getDeveloperComboCheckBox().setValue(new ComboBoxItemWrap<String>(string.substring(Integer.min(2, string.length()))));
            });
            return cell;
        });
        editController.getDeveloperComboCheckBox().setItems(options);
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
