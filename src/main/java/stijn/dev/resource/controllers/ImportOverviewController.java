package stijn.dev.resource.controllers;

import javafx.collections.*;
import javafx.collections.transformation.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.*;
import org.controlsfx.control.tableview2.*;
import org.controlsfx.control.tableview2.cell.*;
import stijn.dev.data.database.*;
import stijn.dev.records.*;
import stijn.dev.resource.*;

import java.io.*;
import java.net.*;
import java.util.*;

public class ImportOverviewController implements Initializable{

    @FXML
    private FilteredTableView<RomImportRecord> overviewTable;
    @FXML
    private Button nextButton;
    @FXML
    private Button backButton;
    private static Stage stage;
    private static Parent root;
    ObservableList<RomImportRecord> roms;


    public List<RomImportRecord> getRoms() {
        return roms;
    }

    public void setRoms(List<RomImportRecord> roms) {
        this.roms = FXCollections.observableList(roms);
        setTable();
    }

    public void onNext(ActionEvent event){
//        roms.forEach(record->{
//            System.out.println(record.toString());
//        });
        List<RomDatabaseRecord> databaseRecords = XMLParser.parseGames(roms);
        DatabaseHelper.importRoms(databaseRecords);
        System.out.println("switching to ImportDatabaseComparison scene");

        //TODO Make it close the diaglogue box and run the import sequence
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

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

    public static Stage getStage() {
        return stage;
    }

    public static void setStage(Stage stage) {
        ImportOverviewController.stage = stage;
    }

    public static Parent getRoot() {
        return root;
    }

    public static void setRoot(Parent root) {
        ImportOverviewController.root = root;
    }
}
