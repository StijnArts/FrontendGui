package stijn.dev.resource.controllers;

import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.*;
import org.controlsfx.control.tableview2.*;
import org.controlsfx.control.tableview2.cell.*;
import stijn.dev.datasource.database.*;
import stijn.dev.datasource.importing.xml.*;
import stijn.dev.datasource.records.*;
import stijn.dev.resource.controllers.interfaces.*;
import stijn.dev.resource.service.*;
import stijn.dev.util.javafx.*;

import java.io.*;
import java.util.*;

public class ImportOverviewController implements IHasNextButton,IHasBackButton {

    @FXML
    private FilteredTableView<RomImportRecord> overviewTable;
    private ImportOverviewService importOVerviewService = new ImportOverviewService();
    private Stage stage;
    private Scene scene;
    private List<File> files;
    private RomXMLParser romXMLParser = new RomXMLParser();
    private ObservableList<RomImportRecord> roms;
    private String importingAsPlatform;
    private String scrapeAsPlatform;
    @Override
    public void onNext(){
        importOVerviewService.importRoms(roms, importingAsPlatform, scrapeAsPlatform);
        stage.close();
    }
    @Override
    public void onBack(){
        importOVerviewService.onBack(stage, scene, importingAsPlatform, scrapeAsPlatform, files);
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
        importOVerviewService.setTable(overviewTable, roms);
        setKeyBehavior(scene);
    }
    public Scene getScene() {
        return scene;
    }
    public void setScene(Scene scene) {
        this.scene = scene;
    }
    public List<RomImportRecord> getRoms() {
        return roms;
    }
    public void setRoms(ObservableList<RomImportRecord> roms) {
        this.roms = roms;
        importOVerviewService.setTable(overviewTable, roms);
    }
}
