package stijn.dev.resource.controllers;

import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.stage.*;
import org.controlsfx.control.tableview2.*;
import org.controlsfx.control.tableview2.cell.*;
import stijn.dev.data.*;
import stijn.dev.data.database.*;
import stijn.dev.data.objects.items.*;
import stijn.dev.records.*;
import stijn.dev.service.javafx.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class ImportOverviewController{

    @FXML
    private FilteredTableView<RomImportRecord> overviewTable;
    @FXML
    private Button nextButton;
    @FXML
    private Button backButton;
    private Stage stage;
    private Parent root;
    private Scene scene;
    private List<File> files;

    private ObservableList<RomImportRecord> roms;

    public List<RomImportRecord> getRoms() {
        return roms;
    }

    public void setRoms(ObservableList<RomImportRecord> roms) {
        this.roms = roms;
        setTable();
    }

    public void onNext(){
        importRoms();
    }

    public void onBack(){
        FXMLLoader loader = FXMLLoaderUtil.createFMXLLoader("importPlatformSelection.fxml");
        root = RootUtil.createRoot(loader);
        Scene scene = new Scene(root, this.scene.getWidth(),this.scene.getHeight());
        ImportPlatformSelectionController.create(loader, files, stage, scene, XMLParser.importingAsPlatform,XMLParser.scrapeAsPlatform);
        stage.setScene(scene);
        stage.show();
    }

    public void importRoms(){
        stage.close();
        XMLParser parser = new XMLParser();
        ArrayBlockingQueue<Game> databaseRecords = parser.parseGames(roms);
        DatabaseHelper.importRoms(databaseRecords);
    }

    public void setKeyBehavior(){
        scene.setOnKeyPressed(keyEvent -> {
            if(keyEvent.getCode()== KeyCode.ENTER){
                importRoms();
            }
        });
    }

    private void setTable() {
        overviewTable.setItems(roms);
        overviewTable.setEditable(true);
        TableColumn2<RomImportRecord, String> filenameColumn = new FilteredTableColumn<>("Filename:");
        filenameColumn.setCellValueFactory(p->p.getValue().fullFilename());
        filenameColumn.setEditable(false);
        TableColumn2<RomImportRecord, String> fileExtensionColumn = new FilteredTableColumn<>("File Extension:");
        fileExtensionColumn.setCellValueFactory(p->p.getValue().fileExtension());
        fileExtensionColumn.setEditable(false);
        TableColumn2<RomImportRecord, String> titleColumn = new FilteredTableColumn<>("Title:");
        titleColumn.setCellValueFactory(p->p.getValue().title());
        titleColumn.setCellFactory(TextField2TableCell.forTableColumn());
        titleColumn.setEditable(true);
        TableColumn2<RomImportRecord, String> platformColumn = new FilteredTableColumn<>("Platform:");
        platformColumn.setCellValueFactory(p->p.getValue().platform());
        platformColumn.setCellFactory(TextField2TableCell.forTableColumn());
        platformColumn.setEditable(true);
        TableColumn2<RomImportRecord, String> regionColumn = new FilteredTableColumn<>("Region:");
        regionColumn.setCellValueFactory(p->p.getValue().region());
        regionColumn.setCellFactory(TextField2TableCell.forTableColumn());
        regionColumn.setEditable(true);
        overviewTable.getColumns().setAll(filenameColumn,fileExtensionColumn,titleColumn,platformColumn,regionColumn);
        if(!roms.get(0).platform().getValue().equals(roms.get(0).scrapeAsPlatform().getValue())) {
            TableColumn2<RomImportRecord, String> scrapeAsColumn = new FilteredTableColumn<>("Scrape As Platform:");
            scrapeAsColumn.setCellValueFactory(p -> p.getValue().scrapeAsPlatform());
            scrapeAsColumn.setEditable(true);
            scrapeAsColumn.setCellFactory(TextField2TableCell.forTableColumn());
            overviewTable.getColumns().add(scrapeAsColumn);
        }
        overviewTable.setRowHeaderVisible(true);
    }

    public static ImportOverviewController create(FXMLLoader loader, List<File> files, Stage stage, Scene scene){
        ImportOverviewController ioc = loader.getController();
        ioc.configure(files, stage, scene);
        return ioc;
    }

    public void configure(List<File> files, Stage stage, Scene scene){
        this.files = files;
        this.roms = XMLParser.parseRoms(files);
        this.stage = stage;
        this.scene = scene;
        setTable();
        setKeyBehavior();
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Parent getRoot() {
        return root;
    }

    public void setRoot(Parent root) {
        this.root = root;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }
}
