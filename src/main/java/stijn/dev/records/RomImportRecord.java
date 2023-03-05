package stijn.dev.records;

import java.io.*;

public record RomImportRecord(String fullFilename, String cleanFilename, String region, String platform) {
}
