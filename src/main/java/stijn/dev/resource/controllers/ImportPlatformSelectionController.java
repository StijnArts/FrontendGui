package stijn.dev.resource.controllers;

import java.util.*;

public class ImportPlatformSelectionController {
    private ArrayList<String> fileExtensions;

    public ArrayList<String> getFileExtension() {
        return fileExtensions;
    }

    public void setFileExtensions(ArrayList<String> fileExtension) {
        this.fileExtensions = fileExtension;
    }
}
