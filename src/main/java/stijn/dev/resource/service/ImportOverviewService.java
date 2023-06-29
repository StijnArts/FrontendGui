package stijn.dev.resource.service;

import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.stage.*;
import org.controlsfx.control.tableview2.*;
import org.controlsfx.control.tableview2.cell.*;
import stijn.dev.datasource.database.dao.*;
import stijn.dev.datasource.importing.xml.*;
import stijn.dev.datasource.records.*;
import stijn.dev.resource.controllers.*;
import stijn.dev.util.javafx.*;

import java.io.*;
import java.util.*;

public class ImportOverviewService {
    private ImportDAO importDAO = new ImportDAO();
    private GamesXMLParser parser = new GamesXMLParser();
    public void importRoms(ObservableList<RomImportRecord> roms, String importingAsPlatform, String scrapeAsPlatform){
        importDAO.importRoms(parser.parseGames(roms,importingAsPlatform,scrapeAsPlatform));
    }

    public void onBack(Stage stage, Scene originalScene, String importingAsPlatform, String scrapeAsPlatform, List<File> files){
        FXMLLoader loader = FXMLLoaderUtil.createFMXLLoader("importPlatformSelection.fxml");
        Parent root = RootUtil.createRoot(loader);
        Scene scene = new Scene(root, originalScene.getWidth(), originalScene.getHeight());
        ImportPlatformSelectionController.create(loader, files, stage, scene, importingAsPlatform,scrapeAsPlatform);
        stage.setScene(scene);
        stage.show();
    }

    public void setTable(FilteredTableView<RomImportRecord> overviewTable, ObservableList<RomImportRecord> roms) {
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
}
