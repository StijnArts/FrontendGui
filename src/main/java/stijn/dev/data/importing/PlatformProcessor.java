package stijn.dev.data.importing;

import org.controlsfx.control.*;
import stijn.dev.data.importing.xml.*;
import stijn.dev.service.*;

import java.io.*;
import java.util.*;

import static stijn.dev.service.FileExtensionService.MULTIPLE;
import static stijn.dev.service.FileExtensionService.NOT_RECOGNIZED;

public class PlatformProcessor {

    private PlatformXMLParser platformXMLParser = new PlatformXMLParser();
    private List<String> platformList = platformXMLParser.getPlatforms();

    public void processPlatforms(PrefixSelectionComboBox<String> platformComboBox, PrefixSelectionComboBox<String> scrapeAsPlatformComboBox, List<File> files){
        ArrayList<String> fileExtensions = FilenameUtil.extractFileExtensions(files);
        ArrayList<String> platforms = platformFromExtension(fileExtensions);
        setComboBoxValues(platformComboBox,scrapeAsPlatformComboBox,files,platforms);
        platformComboBox.getItems().addAll(platformList);
        scrapeAsPlatformComboBox.getItems().addAll(platformList);
    }

    public void setComboBoxValues(PrefixSelectionComboBox<String> platformComboBox, PrefixSelectionComboBox<String> scrapeAsPlatformComboBox, List<File> files, ArrayList<String> platforms){
        if (platforms.size() <= 1 && !platforms.contains(MULTIPLE) && !platforms.contains(NOT_RECOGNIZED)) {
            platformComboBox.setValue(platforms.get(0));
            scrapeAsPlatformComboBox.setValue(platforms.get(0));
        } else if(!"".equals(checkIfDirectoryContainsPlatformString(platformList, files.get(0).getParentFile().getAbsolutePath()))){
            String platform = checkIfDirectoryContainsPlatformString(platformList, files.get(0).getParentFile().getAbsolutePath());
            platformComboBox.setValue(platform);
            scrapeAsPlatformComboBox.setValue(platform);
        }
    }

    private ArrayList<String> platformFromExtension(ArrayList<String> fileExtensions) {
        ArrayList<String> platforms = new ArrayList<>();
        fileExtensions.forEach(fileExtension->{
            if(FileExtensionService.getExtensionsForPlatforms().containsKey(fileExtension.toLowerCase())) {
                if(!platforms.contains(FileExtensionService.getExtensionsForPlatforms().get(fileExtension))){
                    platforms.add(FileExtensionService.getExtensionsForPlatforms().get(fileExtension));
                }
            } else {
                platforms.add(NOT_RECOGNIZED);
            }
        });
        return platforms;
    }

    private String checkIfDirectoryContainsPlatformString(List<String> platforms,String directory){
        for (String platform :
                platforms) {
            if (directory.toLowerCase().contains(platform.toLowerCase())) {
                return platform;
            }
        }
        return "";
    }
}
