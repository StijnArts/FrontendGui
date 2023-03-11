package stijn.dev.resource.controllers;

import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.*;
import org.controlsfx.control.tableview2.*;
import org.controlsfx.control.tableview2.cell.*;
import stijn.dev.data.database.*;
import stijn.dev.data.importing.xml.*;
import stijn.dev.records.*;
import stijn.dev.resource.controllers.interfaces.*;
import stijn.dev.service.javafx.*;

import java.io.*;
import java.util.*;

public class ImportOverviewController implements IHasNextButton,IHasBackButton {

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
    private GamesXMLParser parser = new GamesXMLParser();
    private RomXMLParser romXMLParser = new RomXMLParser();
    private DatabaseHelper databaseHelper = new DatabaseHelper();
    private ObservableList<RomImportRecord> roms;
    private String importingAsPlatform;
    private String scrapeAsPlatform;

    public List<RomImportRecord> getRoms() {
        return roms;
    }

    public void setRoms(ObservableList<RomImportRecord> roms) {
        this.roms = roms;
        setTable();
    }

    @Override
    public void onNext(){
        importRoms();
    }

    @Override
    public void onBack(){
        FXMLLoader loader = FXMLLoaderUtil.createFMXLLoader("importPlatformSelection.fxml");
        root = RootUtil.createRoot(loader);
        Scene scene = new Scene(root, this.scene.getWidth(),this.scene.getHeight());
        ImportPlatformSelectionController.create(loader, files, stage, scene, importingAsPlatform,scrapeAsPlatform);
        stage.setScene(scene);
        stage.show();
    }

    public void importRoms(){
        stage.close();
        databaseHelper.importRoms(parser.parseGames(roms,importingAsPlatform,scrapeAsPlatform));
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

    public static ImportOverviewController create(FXMLLoader loader, List<File> files, Stage stage, Scene scene,
                                                  String importingAsPlatform, String scrapeAsPlatform){
        ImportOverviewController ioc = loader.getController();
        ioc.configure(files, stage, scene, importingAsPlatform, scrapeAsPlatform);
        return ioc;
    }

    public void configure(List<File> files, Stage stage, Scene scene, String importingAsPlatform, String scrapeAsPlatform){
        this.files = files;
        this.importingAsPlatform = importingAsPlatform;
        this.scrapeAsPlatform = scrapeAsPlatform;
        this.roms = romXMLParser.parseRoms(files, importingAsPlatform, scrapeAsPlatform);
        this.stage = stage;
        this.scene = scene;
        setTable();
        setKeyBehavior(scene);
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }
}
