package stijn.dev.resource.controllers;

import javafx.beans.binding.*;
import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import org.controlsfx.control.tableview2.*;
import org.controlsfx.control.tableview2.cell.*;
import org.neo4j.driver.*;
import stijn.dev.datasource.database.*;
import stijn.dev.datasource.database.dao.*;
import stijn.dev.datasource.objects.data.*;
import stijn.dev.datasource.objects.items.*;
import stijn.dev.util.javafx.*;

import java.util.*;
import java.util.concurrent.atomic.*;


//TODO add ability to mark platforms as hidden
public class PlatformConfigurationScreenController {
    private static PlatformDAO platformDAO = new PlatformDAO();
    @FXML
    private TableView2 platformTable;
    private List<Platform> platforms;
    private EditController editController;
    private ObservableList<String> emulatorOptions;
    private Stage stage;
    public static PlatformConfigurationScreenController create(FXMLLoader loader, Stage stage, EditController editController){
        PlatformConfigurationScreenController controller = loader.getController();

        controller.configure(platformDAO.getPlatforms(), stage, editController);
        return controller;
    }

    public static void openPlatformConfigurationScreen(Stage stage){
        FXMLLoader loader = FXMLLoaderUtil.createFMXLLoader("platformConfigurationScreen.fxml");
        Parent root = RootUtil.createRoot(loader);
        Stage platformStage = new Stage();
        platformStage.initOwner(stage);
        Scene scene = new Scene(root);
        PlatformConfigurationScreenController.create(loader, platformStage);
        platformStage.setScene(scene);
        platformStage.show();
    }

    public static PlatformConfigurationScreenController create(FXMLLoader loader, Stage stage){
        PlatformConfigurationScreenController controller = loader.getController();
        controller.configure(platformDAO.getPlatforms(), stage);
        return controller;
    }



    public void configure(List<Platform> platforms, Stage stage){
        this.platforms = platforms;
        this.stage = stage;
        configurePlatformTable();
    }

    public void configure(List<Platform> platforms, Stage stage, EditController editController){
        this.platforms = platforms;
        this.stage = stage;
        this.editController = editController;
        configurePlatformTable();
    }
    public void savePlatforms(){
        Neo4JDatabaseHelper neo4JDatabaseHelper = new Neo4JDatabaseHelper();
        PublisherDAO publisherDAO = new PublisherDAO();
        for(Object platform : platformTable.getItems()){
            if(platform instanceof Platform){
                if(!((Platform) platform).getPlatformName().isEmpty()){
                    HashMap<String, Object> parameters = new HashMap<>();
                    parameters.put("id", ((Platform) platform).getId());
                    String query = "MATCH (p:Platform)" +
                            "WHERE ID(p) = $id " +
                            "WITH p " +
                            "MATCH (p)-[r:DefaultEmulator]-(:Emulator) DELETE r";
                    neo4JDatabaseHelper.runQuery(new Query(query,parameters));
                    parameters.put("platformName",((Platform) platform).getPlatformName());
                    parameters.put("description",((Platform) platform).getDescription());
                    parameters.put("sortingTitle",((Platform) platform).getSortingTitle());
                    parameters.put("defaultEmulator",((Platform) platform).getDefaultEmulator().getName());
                    platformDAO.savePlatform(parameters);

                    query = "MATCH (p:Platform)" +
                            "WHERE ID(p) = $id " +
                            "WITH p " +
                            "MATCH (p)-[r:MADE_BY]-(:Publisher) DELETE r";
                    neo4JDatabaseHelper.runQuery(new Query(query,parameters));
                    AtomicBoolean listIsEmpty = new AtomicBoolean(true);
                    ((Platform) platform).getPublishers().forEach(publisher -> {
                        if(!publisher.isBlank() || !publisher.isEmpty()){
                            listIsEmpty.set(false);
                        }
                    });
                    if(((Platform) platform).getPublishers().isEmpty() || listIsEmpty.get()){
                        publisherDAO.createPublisher(((Platform) platform).getPlatformName(), ((Platform) platform).getPreviousPublishers());
                    } else {
                        publisherDAO.createPublisher(((Platform) platform).getPlatformName(), ((Platform) platform).getPublishers());
                    }
                }
            }
        }
        if(editController != null){
            editController.updatePlatforms();
        }
        this.stage.close();
    }
    private void configurePlatformTable() {
        platformTable.setEditable(true);
        ObservableList<Platform> platformObservable = FXCollections.observableList(platforms);
        platformTable.setItems(platformObservable);
        platformTable.getItems().add(generateEmptyPlatform());
        EmulatorDAO emulatorDAO = new EmulatorDAO();
        HashMap<String, Emulator> emulatorMap = emulatorDAO.getEmulators();
        emulatorOptions = FXCollections.observableList(emulatorMap.keySet().stream().toList());
        TableColumn2<Platform,String> platformNameColumn = new FilteredTableColumn<>("Platform Name");
        platformNameColumn.setEditable(true);
        platformNameColumn.setCellValueFactory(p->p.getValue().platformNameProperty());
        platformNameColumn.setCellFactory(TextField2TableCell.forTableColumn());
        platformNameColumn.setOnEditCommit(event -> {
            if(!event.getNewValue().isBlank()){
                if(event.getTableView().getItems().size()-1==event.getTablePosition().getRow()){
                    event.getTableView().getItems().add(generateEmptyPlatform());
                }
            }

            Platform platform = event.getTableView().getItems().get(event.getTablePosition().getRow());
            if(platform.getSortingTitle().equals(platform.getPlatformName())){
                platform.setSortingTitle(event.getNewValue());
            }
            platform.setPlatformName(event.getNewValue());

        });
//        TableColumn2<Platform,String> descriptionColumn = new FilteredTableColumn<>("Platform Description");
//        descriptionColumn.setEditable(true);
//        descriptionColumn.setCellValueFactory(p->p.getValue().descriptionProperty());
//        descriptionColumn.setCellFactory(TextAreaTableCell.forTableColumn());
//        descriptionColumn.setOnEditCommit(event -> {
//            if(!event.getNewValue().isBlank()){
//                if(event.getTableView().getItems().size()-1==event.getTablePosition().getRow()){
//                    event.getTableView().getItems().add(generateEmptyPlatform());
//                }
//            }
//            event.getTableView().getItems().get(event.getTablePosition().getRow()).setDescription(event.getNewValue());
//        });
//        descriptionColumn.setPrefWidth(400);
        TableColumn2<Platform,String> sortingTitleColumn = new FilteredTableColumn<>("Sorting Title");
        sortingTitleColumn.setEditable(true);
        sortingTitleColumn.setCellValueFactory(p->p.getValue().sortingTitleProperty());
        sortingTitleColumn.setCellFactory(TextField2TableCell.forTableColumn());
        sortingTitleColumn.setOnEditCommit(event -> {
            if (!event.getNewValue().isBlank()) {
                if (event.getTableView().getItems().size() - 1 == event.getTablePosition().getRow()) {
                    event.getTableView().getItems().add(generateEmptyPlatform());
                }
            }
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setSortingTitle(event.getNewValue());
        });
        TableColumn2<Platform, StringProperty> defaultEmulatorColumn = new FilteredTableColumn<>("Default Emulator");
        defaultEmulatorColumn.setCellValueFactory(i-> {
            final StringProperty value = new SimpleStringProperty(i.getValue().getDefaultEmulator().getName());
            return Bindings.createObjectBinding(()->value);
        });
        defaultEmulatorColumn.setCellFactory(col -> {
            TableCell<Platform,StringProperty> c = new TableCell<>();
            final BorderPane borderPane = new BorderPane();
            final ComboBox<String> comboBox = new ComboBox<>(emulatorOptions);
            final HBox hBox = new HBox(5, comboBox);
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
                    if(comboBox.getValue()!=null&&!comboBox.getValue().isBlank()){
                        c.getTableView().getItems().get(c.getIndex()).getDefaultEmulator().setName(comboBox.getValue());
                        if(emulatorMap.get(comboBox.valueProperty().get())!=null){
                            c.getTableView().getItems().get(c.getIndex()).getDefaultEmulator().setArgs(emulatorMap.get(comboBox.valueProperty().get()).getArgs());
                        } else {
                            c.getTableView().getItems().get(c.getIndex()).getDefaultEmulator().setArgs("");
                        }

                        if(c.getTableView().getItems().size()-1==c.getIndex()){
                            c.getTableView().getItems().add(generateEmptyPlatform());
                        }
                    } else{
                        c.getTableView().getItems().get(c.getIndex()).getDefaultEmulator().setName(comboBox.getValue());
                        c.getTableView().getItems().get(c.getIndex()).getDefaultEmulator().setArgs("");
                    }
                }
            });
            c.graphicProperty().bind(Bindings.when(c.emptyProperty()).then((Node)null).otherwise(hBox));
            return c;
        });
        defaultEmulatorColumn.setEditable(true);
        TableColumn2<Platform,String> launchArgumentColumn = new FilteredTableColumn<>("Launch Arguments");
        launchArgumentColumn.setEditable(true);
        launchArgumentColumn.setCellValueFactory(p->p.getValue().getDefaultEmulator().argsProperty());
        launchArgumentColumn.setCellFactory(TextField2TableCell.forTableColumn());
        launchArgumentColumn.setOnEditCommit(event -> {
            if(!event.getNewValue().isBlank()){
                if(event.getTableView().getItems().size()-1==event.getTablePosition().getRow()){
                    event.getTableView().getItems().add(generateEmptyPlatform());
                }
            }
            event.getTableView().getItems().get(event.getTablePosition().getRow()).getDefaultEmulator().setArgs(event.getNewValue());
        });
        TableColumn2<Platform,Integer> deleteColumn = new FilteredTableColumn<>("Remove Platform");
        deleteColumn.setCellValueFactory(p->new SimpleObjectProperty<>(p.getValue().getId()));
        deleteColumn.setCellFactory(col -> {
            TableCell<Platform,Integer> c = new TableCell<>();
            final Button button = new Button("Delete");
            final BorderPane borderPane = new BorderPane();
            final HBox hBox = new HBox(5, button);
            borderPane.centerProperty().setValue(hBox);

            button.setOnAction(event -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirm Delete");
                alert.setHeaderText("Deleting a platform is permanent and removes the platform from all the associated games, tags, collections and emulators.\n " +
                        "This also effectively deletes all the games associated with the platform.");
                alert.setContentText("Are you sure you want to delete the platform?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){

                    platformDAO.deletePlatform(c.getTableView().getItems().get(c.getIndex()).getId());
                    platforms.removeIf(platform -> platform.getId()==c.getTableView().getItems().get(c.getIndex()).getId());
                } else {
                    alert.close();
                }
            });
            c.graphicProperty().bind(Bindings.when(c.emptyProperty()).then((Node)null).otherwise(hBox));
            return c;
        });
        TableColumn2<Platform,Integer> editColumn = new FilteredTableColumn<>("Edit Platform");
        editColumn.setCellValueFactory(p->new SimpleObjectProperty<>(p.getValue().getId()));
        editColumn.setCellFactory(col -> {
            TableCell<Platform,Integer> c = new TableCell<>();
            final Button button = new Button("Edit");
            final BorderPane borderPane = new BorderPane();
            final HBox hBox = new HBox(5, button);
            borderPane.centerProperty().setValue(hBox);

            button.setOnAction(event -> {
                openPlatformEditScreen(c.getTableView().getItems().get(c.getIndex()));
            });
            c.graphicProperty().bind(Bindings.when(c.emptyProperty()).then((Node)null).otherwise(hBox));
            return c;
        });
        platformTable.getColumns().setAll(platformNameColumn,  editColumn, deleteColumn, sortingTitleColumn, defaultEmulatorColumn, launchArgumentColumn);
    }

    public void openPlatformEditScreen(Platform platform) {
        FXMLLoader loader = FXMLLoaderUtil.createFMXLLoader("platformEditScreen.fxml");
        Parent root = RootUtil.createRoot(loader);
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.initOwner(this.stage);
        PlatformEditScreenController.create(platform ,loader, stage, this);
        stage.setScene(scene);
        stage.show();
    }

    public void openEmulatorManagementScreen(){
        //TODO open Emulator Management Screen
    }

    private Platform generateEmptyPlatform() {
        return new Platform(generateNewPlatformID(),"",List.of(),"",List.of(new ReleaseDate("USA", null)), null, "", "",
                List.of(),List.of(),"",List.of(),"","", List.of(), List.of(), List.of(), List.of(), List.of(), new Emulator("","","",""), List.of());
    }

    public void closeDialog(ActionEvent actionEvent) {
        stage.close();
    }

    private int tempPlatformCounter = 0;
    
    private int generateNewPlatformID() {
        int number = 1000000000+ tempPlatformCounter;
        tempPlatformCounter++;
        return number;
    }
}
