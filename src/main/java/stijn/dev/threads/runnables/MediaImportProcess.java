package stijn.dev.threads.runnables;

import javafx.scene.*;
import javafx.scene.input.*;
import stijn.dev.datasource.database.dao.*;
import stijn.dev.datasource.objects.items.*;
import stijn.dev.resource.service.*;
import stijn.dev.util.*;

import java.io.*;
import java.util.*;
import java.util.function.*;

public class MediaImportProcess implements Runnable {
    private List<File> files;
    private Node node;
    private Parent root;
    private int id;
    private String category;
    public MediaImportProcess(List<File> files, Node node, int id, String category){
        this.files = files;
        this.node = node;
        this.id = id;
        this.category= category;
    }
    @Override
    public void run() {
        GalleryDAO galleryDAO = new GalleryDAO();
        for (File file : files) {
            galleryDAO.createGalleryNode(new GalleryItem(file.getAbsolutePath(),FilenameUtil.cleanFilename(file.getName())+"_"+category,
                    FileExtensionUtil.getMediaType(FilenameUtil.extractFileExtension(file))
            ),id, category);
        }
        PlatformEditPrepper.galleryNeedsRefresh.set(true);
    }

    public static Thread createMediaImportProcess(Dragboard dragboard, Node node, int id, Supplier<String> category){
        List<File> files = dragboard.getFiles();
        MediaImportProcess importProcess = new MediaImportProcess(files, node, id, category.get());
        Thread thread = new Thread(importProcess);
        return thread;
    }

    public static Thread createMediaImportProcess(List<File> files, Node node, int id, Supplier<String> category){
        MediaImportProcess importProcess = new MediaImportProcess(files, node, id, category.get());
        Thread thread = new Thread(importProcess);
        return thread;
    }
}
