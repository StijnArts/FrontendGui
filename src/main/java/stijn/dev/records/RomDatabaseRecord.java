package stijn.dev.records;

import javafx.beans.property.*;

public record RomDatabaseRecord (RomImportRecord romImportRecord, StringProperty databaseId) {
}
