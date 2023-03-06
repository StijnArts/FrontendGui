package stijn.dev.records;

import javafx.beans.property.*;

import java.util.*;

public record RomDatabaseComparingRecord (RomImportRecord romImportRecord, HashMap<StringProperty, StringProperty> databaseResults){
}
