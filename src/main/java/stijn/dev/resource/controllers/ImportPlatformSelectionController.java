package stijn.dev.resource.controllers;

import javafx.beans.value.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.*;
import org.controlsfx.control.*;
import stijn.dev.datasource.importing.*;
import stijn.dev.resource.controllers.interfaces.*;
import stijn.dev.util.javafx.*;

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
    private Parent root;
    private Scene scene;
    private PlatformProcessor platformProcessor = new PlatformProcessor();
    private List<File> files;
    private String importingAsPlatform;
    private String scrapeAsPlatform;


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
        FXMLLoader loader = FXMLLoaderUtil.createFMXLLoader("importOverview.fxml");
        root = RootUtil.createRoot(loader);
        Scene overviewScene = new Scene(root,scene.getWidth(),scene.getHeight());
        ImportOverviewController.create(loader, files, stage, overviewScene, importingAsPlatform, scrapeAsPlatform);
        stage.setScene(overviewScene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nextButton.setDisable(true);
        scrapeAsPlatformComboBox.setDisable(true);
        setPlatformComboBoxBehavior();
        setCustomPlatformBoxBehavior();
        setScrapeAsPlatformComboBoxBehavior();
    }

    public void configure(List<File> files, Stage stage, Scene scene, String importAs, String scrapeAs){
        this.files = files;
        this.stage = stage;
        this.scene = scene;
        platformProcessor.processPlatforms(platformComboBox,scrapeAsPlatformComboBox,files);
        setKeyBehavior(scene);
        setPlatformSelections(importAs,scrapeAs);
    }

    private void setScrapeAsPlatformComboBoxBehavior() {
        scrapeAsPlatformComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                scrapeAsPlatform = scrapeAsPlatformComboBox.getValue();
                System.out.println("Scraping As Platform: "+ scrapeAsPlatformComboBox.getValue());
                if(null!= scrapeAsPlatformComboBox.getValue()&&!scrapeAsPlatformComboBox.getValue().isEmpty()) {
                    nextButton.setDisable(false);
                }
            }
        });
    }

    private void setCustomPlatformBoxBehavior() {
        customPlatformBox.textProperty().addListener((observableValue, s, t1) -> {
            importingAsPlatform = customPlatformBox.getText();
            scrapeAsPlatformComboBox.setDisable(false);
            nextButton.setDisable(true);
        });
    }

    private void setPlatformComboBoxBehavior() {
        platformComboBox.valueProperty().addListener((observableValue, s, t1) -> {
            importingAsPlatform = platformComboBox.getValue();
            scrapeAsPlatformComboBox.setDisable(false);
            scrapeAsPlatformComboBox.setValue(platformComboBox.getValue());
        });
    }

    public void setPlatformSelections(String importAs, String scrapeAs){
        if(importAs!=null){
            platformComboBox.setValue(importAs);
        }
        if(scrapeAs!=null){
            scrapeAsPlatformComboBox.setValue(scrapeAs);
        }
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
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    public void setScene(Scene scene) {
        this.scene = scene;
    }
}
