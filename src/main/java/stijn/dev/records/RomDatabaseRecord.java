package stijn.dev.records;

import javafx.beans.property.*;

public record RomDatabaseRecord (String fullFilename, String title,
                                 String region, String platform,
                                 String databaseId) {
    @Override
    public String toString() {
        return "Full Filename: "+fullFilename+". Title: "+title+". Region: "+region+
                ".\n Platform: "+platform+ "\n DatabaseID: "+databaseId;
    }
}
