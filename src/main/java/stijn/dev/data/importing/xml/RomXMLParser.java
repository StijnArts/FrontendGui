package stijn.dev.data.importing.xml;

import javafx.beans.property.*;
import javafx.collections.*;
import stijn.dev.records.*;
import stijn.dev.service.*;

import java.io.*;
import java.util.*;

public class RomXMLParser extends XMLParser{
    public ObservableList<RomImportRecord> parseRoms(List<File> files, String importingAsPlatform, String scrapeAsPlatform){
        List<RomImportRecord> romImportRecords=new ArrayList<>();
        for (File file: files){
            String filename = file.getName();
            String cleanFilename = FilenameUtil.cleanFilename(filename);
            String region = RegionUtil.getFileRegion(filename);
            romImportRecords.add(new RomImportRecord(new SimpleStringProperty(file.getPath()),
                    new SimpleStringProperty(FilenameUtil.extractFileExtension(file)), new SimpleStringProperty(cleanFilename),
                    new SimpleStringProperty(region), new SimpleStringProperty(importingAsPlatform), new SimpleStringProperty(scrapeAsPlatform)));
        }
        //readResults(romImportRecords);
        return FXCollections.observableList(romImportRecords);
    }
}
