package stijn.dev.resource.controllers;

import javafx.beans.value.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.stage.*;
import org.controlsfx.control.*;
import stijn.dev.data.*;
import stijn.dev.service.*;
import stijn.dev.service.javafx.*;

import java.io.*;
import java.net.*;
import java.util.*;

import static stijn.dev.service.FileExtensionService.MULTIPLE;
import static stijn.dev.service.FileExtensionService.NOT_RECOGNIZED;

public class ImportPlatformSelectionController implements Initializable{

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
    private List<File> files;


    public static ImportPlatformSelectionController create(FXMLLoader loader, List<File> files, Stage stage, Scene scene) {
        return create(loader,files,stage,scene,null,null);
    }

    public static ImportPlatformSelectionController create(FXMLLoader loader, List<File> files, Stage stage, Scene scene, String importAs, String scrapeAs) {
        ImportPlatformSelectionController ipsc = loader.getController();
        ipsc.configure(files, stage, scene, importAs,scrapeAs);
        return ipsc;
    }

    public void onNext(){
        checkCustomPlatform();
        FXMLLoader loader = FXMLLoaderUtil.createFMXLLoader("importOverview.fxml");
        root = RootUtil.createRoot(loader);
        Scene overviewScene = new Scene(root,scene.getWidth(),scene.getHeight());
        ImportOverviewController.create(loader, files, stage, overviewScene);
        stage.setScene(overviewScene);
        stage.show();
    }

    private void checkCustomPlatform(){
        if(customPlatformBox.getText()!=null&&!customPlatformBox.getText().isEmpty()){
            XMLParser.importingAsPlatform = customPlatformBox.getText();
            System.out.println("Chosen Platform: "+ XMLParser.importingAsPlatform);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nextButton.setDisable(true);
        scrapeAsPlatformComboBox.setDisable(true);
        platformComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                XMLParser.importingAsPlatform = platformComboBox.getValue();
                scrapeAsPlatformComboBox.setDisable(false);
                scrapeAsPlatformComboBox.setValue(platformComboBox.getValue());
                System.out.println("Chosen Platform: "+ platformComboBox.getValue());
            }
        });
        customPlatformBox.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                XMLParser.importingAsPlatform = customPlatformBox.getText();
                scrapeAsPlatformComboBox.setDisable(false);
                XMLParser.importingAsPlatform = customPlatformBox.getText();
                System.out.println("Chosen Platform: "+ customPlatformBox.getText());
            }
        });

        scrapeAsPlatformComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                XMLParser.scrapeAsPlatform = scrapeAsPlatformComboBox.getValue();
                System.out.println("Scraping As Platform: "+ scrapeAsPlatformComboBox.getValue());
                if(null!= scrapeAsPlatformComboBox.getValue()&&!scrapeAsPlatformComboBox.getValue().isEmpty()) {
                    nextButton.setDisable(false);
                }
            }
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

    public void configure(List<File> files, Stage stage, Scene scene, String importAs, String scrapeAs){
        this.files = files;
        this.stage = stage;
        this.scene = scene;
        processPlatforms();
        setKeyBehavior();
        setPlatformSelections(importAs,scrapeAs);
    }



    public void processPlatforms() {
        ArrayList<String> fileExtensions = FilenameUtil.extractFileExtensions(files);
        ArrayList<String> platforms = platformFromExtension(fileExtensions);
        List<String> platformList = XMLParser.getPlatforms();
        if (platforms.size() <= 1 && !platforms.contains(MULTIPLE) && !platforms.contains(NOT_RECOGNIZED)) {
            platformComboBox.setValue(platforms.get(0));
            scrapeAsPlatformComboBox.setValue(platforms.get(0));
        } else if(!"".equals(checkIfDirectoryContainsPlatformString(platformList, files.get(0).getParentFile().getAbsolutePath()))){
            String platform = checkIfDirectoryContainsPlatformString(platformList, files.get(0).getParentFile().getAbsolutePath());
            platformComboBox.setValue(platform);
            scrapeAsPlatformComboBox.setValue(platform);
        }
        platformComboBox.getItems().addAll(platformList);
        scrapeAsPlatformComboBox.getItems().addAll(platformList);
    }

    private String checkIfDirectoryContainsPlatformString(List<String> platforms,String directory){
        //System.out.println(directory.toLowerCase());
        for (String platform :
                platforms) {
            //System.out.println(platform.toLowerCase());
            if (directory.toLowerCase().contains(platform.toLowerCase())) {
                //System.out.println("Match Found!");
                return platform;
                }
            }
        return "";
    }

    private ArrayList<String> platformFromExtension(ArrayList<String> fileExtensions) {
        ArrayList<String> platforms = new ArrayList<>();
        fileExtensions.stream().forEach(fileExtension->{
            if(FileExtensionService.getExtensionsForPlatforms().containsKey(fileExtension)) {
                if(!platforms.contains(FileExtensionService.getExtensionsForPlatforms().get(fileExtension))){
                    platforms.add(FileExtensionService.getExtensionsForPlatforms().get(fileExtension));
                }
            } else {
                platforms.add(NOT_RECOGNIZED);
            }
        });
        return platforms;
    }



    public void setKeyBehavior(){
        scene.setOnKeyPressed(keyEvent -> {
            if(keyEvent.getCode()== KeyCode.ENTER){
                onNext();
            }
        });
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
