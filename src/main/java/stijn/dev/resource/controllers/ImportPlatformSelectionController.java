package stijn.dev.resource.controllers;

import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.*;
import org.controlsfx.control.*;
import stijn.dev.resource.controllers.interfaces.*;
import stijn.dev.resource.service.*;

import java.io.*;
import java.net.*;
import java.util.*;

public class ImportPlatformSelectionController implements Initializable, IHasNextButton {

    @FXML
    private PrefixSelectionComboBox<String> platformComboBox;
    @FXML
    private PrefixSelectionComboBox<String> scrapeAsPlatformComboBox;
    @FXML
    private TextField customPlatformBox;
    @FXML
    private Button nextButton;
    private Stage stage;
    private Scene scene;
    private List<File> files;
    private String importingAsPlatform;
    private String scrapeAsPlatform;
    private ImportPlatformSelectionService importPlatformSelectionService = new ImportPlatformSelectionService();

    public static ImportPlatformSelectionController create(FXMLLoader loader, List<File> files, Stage stage, Scene scene) {
        return create(loader,files,stage,scene,null,null);
    }

    public static ImportPlatformSelectionController create(FXMLLoader loader, List<File> files, Stage stage, Scene scene, String importAs, String scrapeAs) {
        ImportPlatformSelectionController ipsc = loader.getController();
        ipsc.configure(files, stage, scene, importAs,scrapeAs);
        return ipsc;
    }

    @Override
    public void onNext(){
        checkCustomPlatform();
        importPlatformSelectionService.next(stage,scene, importingAsPlatform, scrapeAsPlatform, files);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        importPlatformSelectionService.initialize(nextButton, scrapeAsPlatformComboBox, platformComboBox, this,customPlatformBox);
    }

    public void configure(List<File> files, Stage stage, Scene scene, String importAs, String scrapeAs){
        this.files = files;
        this.stage = stage;
        this.scene = scene;
        importPlatformSelectionService.processPlatforms(platformComboBox,scrapeAsPlatformComboBox,files);
        setKeyBehavior(scene);
        importPlatformSelectionService.setPlatformSelections(importAs,scrapeAs, platformComboBox, scrapeAsPlatformComboBox);
    }

    private void checkCustomPlatform(){
        if(customPlatformBox.getText()!=null&&!customPlatformBox.getText().isEmpty()){
            importingAsPlatform = customPlatformBox.getText();
            System.out.println("Chosen Platform: "+ importingAsPlatform);
        }
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }
    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public void setScrapeAsPlatform(String value) {
        scrapeAsPlatform = value;
    }

    public void setImportingAsPlatform(String value) {
        importingAsPlatform = value;
    }
}
