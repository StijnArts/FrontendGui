package stijn.dev.resource.controllers;

import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.stage.*;
import org.controlsfx.control.*;
import stijn.dev.data.database.*;
import stijn.dev.records.*;
import stijn.dev.resource.*;
import stijn.dev.service.*;

import java.io.*;
import java.net.*;
import java.util.*;

public class ImportPlatformSelectionController implements Initializable {

    @FXML
    private PrefixSelectionComboBox<String> platformComboBox;
    @FXML
    private PrefixSelectionComboBox<String> scrapeAsPlatformComboBox;
    @FXML
    private TextField customPlatformBox;
    @FXML
    private Button nextButton;

    private static Stage stage;
    private static Parent root;
    private static Scene scene;
    public static final String NOT_RECOGNIZED = "Not Recognized";
    private List<File> files;
    private final String MULTIPLE = "Multiple";
    private String importingAsPlatform = "";
    private String scrapeAsPlatform = "";
    private HashMap<String,String> extensionsForPlatforms = new HashMap<>();
    private HashMap<String,String> regionMarkers = new HashMap<>();
    public void setFiles(List<File> files) {
        this.files = files;
    }
    public void importRoms(ActionEvent event){

        importRoms();

    }
    public void importRoms(){
        if(customPlatformBox.getText()!=null&&!customPlatformBox.getText().isEmpty()){
            importingAsPlatform=customPlatformBox.getText();
            System.out.println("Chosen Platform: "+ importingAsPlatform);
        }
        List<RomImportRecord> roms = parseRoms(files, importingAsPlatform, scrapeAsPlatform);
        System.out.println("switching to import scene");
        try {
            FXMLLoader loader = new FXMLLoader(FrontEndApplication.class.getResource("importOverview.fxml"));
            root = loader.load();
            ImportOverviewController importOverviewController = loader.getController();
            importOverviewController.setRoms(roms);
            importOverviewController.setStage(stage);
            Scene scene = new Scene(root);
            importOverviewController.setScene(scene);
            importOverviewController.setKeyBehavior();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        fillPlatformExtensionList();
        nextButton.setDisable(true);
        scrapeAsPlatformComboBox.setDisable(true);
        platformComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                importingAsPlatform = platformComboBox.getValue();
                scrapeAsPlatformComboBox.setDisable(false);
                scrapeAsPlatformComboBox.setValue(platformComboBox.getValue());
                System.out.println("Chosen Platform: "+ importingAsPlatform);
            }
        });
        customPlatformBox.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                importingAsPlatform = customPlatformBox.getText();
                scrapeAsPlatformComboBox.setDisable(false);
                System.out.println("Chosen Platform: "+ importingAsPlatform);
            }
        });

        scrapeAsPlatformComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                scrapeAsPlatform = scrapeAsPlatformComboBox.getValue();
                System.out.println("Scraping As Platform: "+ scrapeAsPlatform);
                if(null!= scrapeAsPlatformComboBox.getValue()&&!scrapeAsPlatformComboBox.getValue().isEmpty()) {
                    nextButton.setDisable(false);
                }
            }
        });
    }

    public void setKeyBehavior(){
        scene.setOnKeyPressed(keyEvent -> {
            if(keyEvent.getCode()== KeyCode.ENTER){
                importRoms();
            }
        });
    }

    public void processPlatforms() {
        ArrayList<String> fileExtensions = FilenameUtil.extractFileExtensions(files);
        ArrayList<String> platforms = platformFromExtension(fileExtensions);
        List<String> platformList = XMLParser.getPlatforms();
        if (platforms.size() < 1 && !platforms.contains(MULTIPLE) && !platforms.contains(NOT_RECOGNIZED)) {
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
        System.out.println(directory.toLowerCase());
        for (String platform :
                platforms) {
            System.out.println(platform.toLowerCase());
            if (directory.toLowerCase().contains(platform.toLowerCase())) {
                System.out.println("Match Found!");
                return platform;
                }
            }
        return "";
    }

    public static List<RomImportRecord> parseRoms(List<File> files, String platform, String scrapeAsPlatform){
        List<RomImportRecord> romImportRecords=new ArrayList<>();;
        for (File file: files){
            String filename = file.getName();
            String cleanFilename = FilenameUtil.cleanFilename(filename);
            String region = getFileRegion(filename);
            romImportRecords.add(new RomImportRecord(new SimpleStringProperty(file.getPath()),
                    new SimpleStringProperty(FilenameUtil.extractFileExtension(file)), new SimpleStringProperty(cleanFilename),
                    new SimpleStringProperty(region), new SimpleStringProperty(platform), new SimpleStringProperty(scrapeAsPlatform)));
        }
        readResults(romImportRecords);
        return romImportRecords;
    }

    private static void readResults(List<RomImportRecord> romImportRecords) {
        romImportRecords.stream().forEach(romImportRecord -> System.out.println(romImportRecord));
    }

    private ArrayList<String> platformFromExtension(ArrayList<String> fileExtensions) {
        ArrayList<String> platforms = new ArrayList<>();
        fileExtensions.stream().forEach(fileExtension->{
            if(extensionsForPlatforms.containsKey(fileExtension)) {
                if(!platforms.contains(extensionsForPlatforms.get(fileExtension))){
                    platforms.add(extensionsForPlatforms.get(fileExtension));
                }
            } else {
                platforms.add(NOT_RECOGNIZED);
            }
        });
        return platforms;
    }

    private void fillPlatformExtensionList() {
        extensionsForPlatforms.put("rar",MULTIPLE);
        extensionsForPlatforms.put("7z",MULTIPLE);
        extensionsForPlatforms.put("7zip",MULTIPLE);
        extensionsForPlatforms.put("tar.gz.gz",MULTIPLE);
        extensionsForPlatforms.put("tar",MULTIPLE);
        extensionsForPlatforms.put("001",MULTIPLE);
        extensionsForPlatforms.put("part1",MULTIPLE);
        extensionsForPlatforms.put("r01",MULTIPLE);
        extensionsForPlatforms.put("gzip",MULTIPLE);
        extensionsForPlatforms.put("iso",MULTIPLE);
        extensionsForPlatforms.put("img",MULTIPLE);
        extensionsForPlatforms.put("ccd",MULTIPLE);
        extensionsForPlatforms.put("bin",MULTIPLE);
        extensionsForPlatforms.put("cue",MULTIPLE);
        extensionsForPlatforms.put("chd",MULTIPLE);
        extensionsForPlatforms.put("mdf",MULTIPLE);
        extensionsForPlatforms.put("mds",MULTIPLE);
        extensionsForPlatforms.put("ecm","Sony Playstation");
        extensionsForPlatforms.put("cso",MULTIPLE);
        extensionsForPlatforms.put("gcz",MULTIPLE);
        extensionsForPlatforms.put("rvz",MULTIPLE);
        extensionsForPlatforms.put("cdi","Sega Dreamcast");
        extensionsForPlatforms.put("gdi","Sega Dreamcast");
        extensionsForPlatforms.put("sbl",MULTIPLE);
        extensionsForPlatforms.put("fds","Nintendo Entertainment System");
        extensionsForPlatforms.put("ndd","Nintendo 64");
        extensionsForPlatforms.put("wav",MULTIPLE);
        extensionsForPlatforms.put("tap",MULTIPLE);
        extensionsForPlatforms.put("tzx",MULTIPLE);
        extensionsForPlatforms.put("cdc","Amstrad CPC");
        extensionsForPlatforms.put("cas",MULTIPLE);
        extensionsForPlatforms.put("nes","Nintendo Entertainment System");
        extensionsForPlatforms.put("nez","Nintendo Entertainment System");
        extensionsForPlatforms.put("unf","Nintendo Entertainment System");
        extensionsForPlatforms.put("unif","Nintendo Entertainment System");
        extensionsForPlatforms.put("smc","Super Nintendo Entertainment System");
        extensionsForPlatforms.put("sfc","Super Nintendo Entertainment System");
        extensionsForPlatforms.put("md","Sega Genesis");
        extensionsForPlatforms.put("smd","Sega Genesis");
        extensionsForPlatforms.put("gen","Sega Genesis");
        extensionsForPlatforms.put("gg","Sega Game Gear");
        extensionsForPlatforms.put("z64","Nintendo 64");
        extensionsForPlatforms.put("v64","Nintendo 64");
        extensionsForPlatforms.put("n64","Nintendo 64");
        extensionsForPlatforms.put("gb","Nintendo Game Boy");
        extensionsForPlatforms.put("gbc","Nintendo Game Boy Color");
        extensionsForPlatforms.put("gba","Nintendo Game Boy Advanced");
        extensionsForPlatforms.put("gcm","Nintendo GameCube");
        extensionsForPlatforms.put("nkit.iso",MULTIPLE);
        extensionsForPlatforms.put("nds","Nintendo DS");
        extensionsForPlatforms.put("srl",MULTIPLE);
        extensionsForPlatforms.put("dsi","Nintendo DS");
        extensionsForPlatforms.put("app","Nintendo DS");
        extensionsForPlatforms.put("ids","Nintendo DS");
        extensionsForPlatforms.put("wbfs","Nintendo Wii");
        extensionsForPlatforms.put("wad",MULTIPLE);
        extensionsForPlatforms.put("cia","Nintendo 3DS");
        extensionsForPlatforms.put("3ds","Nintendo 3DS");
        extensionsForPlatforms.put("nsp","Nintendo Switch");
        extensionsForPlatforms.put("xci","Nintendo Switch");
        extensionsForPlatforms.put("ngp",MULTIPLE);
        extensionsForPlatforms.put("ngc",MULTIPLE);
        extensionsForPlatforms.put("pce",MULTIPLE);
        extensionsForPlatforms.put("vpk","Sony Playstation Vita");
        extensionsForPlatforms.put("vb","Nintendo Virtual Boy");
        extensionsForPlatforms.put("ws","WonderSwan");
        extensionsForPlatforms.put("wsc","WonderSwanColor");
        extensionsForPlatforms.put("ipa","Apple iOS");
        extensionsForPlatforms.put("apk","Android");
        extensionsForPlatforms.put("obb","Android");
        extensionsForPlatforms.put("elf",MULTIPLE);
        extensionsForPlatforms.put("pbp",MULTIPLE);
        extensionsForPlatforms.put("dol",MULTIPLE);
        extensionsForPlatforms.put("xbe",MULTIPLE);
        extensionsForPlatforms.put("xex",MULTIPLE);
        extensionsForPlatforms.put("xml",MULTIPLE);
        extensionsForPlatforms.put("hsi","MESS");
        extensionsForPlatforms.put("lay",MULTIPLE);
        extensionsForPlatforms.put("nv",MULTIPLE);
        extensionsForPlatforms.put("m3u",MULTIPLE);
        extensionsForPlatforms.put("exe","Windows");
        extensionsForPlatforms.put("lnk","Windows");
        extensionsForPlatforms.put("url","Windows");
        extensionsForPlatforms.put("cda","Windows");
        extensionsForPlatforms.put("rpx","Nintendo Wii U");
    }

    private void fillRegionSignifiersList(){
        regionMarkers.put("J","Japan");
        regionMarkers.put("Japan","Japan");
        regionMarkers.put("USA","USA");
    }

    private static String getFileRegion(String filename) {
        List<String> segments = Arrays.stream(filename.split("\\(|\\)")).toList();
        String returnRegion ="";
        for (String region : segments) {
            region = region.toLowerCase().replace('(',' ').replace(')',' ').trim();
            System.out.println("Trying segment: "+region);
            if (region.equals("j") || region.equals("japan")) {
                returnRegion = "Japan";
            } else if (region.equals("usa")) {
                returnRegion = "USA";
            }
        }
        if(returnRegion.isEmpty()||returnRegion==null){
            return "N/A";
        }
        return returnRegion;
    }



    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public static void setScene(Scene scene) {
        ImportPlatformSelectionController.scene = scene;
    }
}
