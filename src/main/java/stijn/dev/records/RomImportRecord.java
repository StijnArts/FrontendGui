package stijn.dev.records;

import javafx.beans.property.*;

import java.io.*;

public record RomImportRecord(StringProperty fullFilename, StringProperty fileExtension, StringProperty title, StringProperty region, StringProperty platform, StringProperty scrapeAsPlatform) {
    @Override
    public String toString() {
        return "Full Filename: "+fullFilename.getValue()+". File Extension: "+fileExtension.getValue()+". Title: "+title.getValue()+". Region: "+region.getValue()+".\n Platform: "+platform.getValue()+". Scraped as Platform: "+scrapeAsPlatform.getValue();
    }
}
