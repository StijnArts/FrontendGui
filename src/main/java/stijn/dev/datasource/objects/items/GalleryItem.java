package stijn.dev.datasource.objects.items;

import stijn.dev.datasource.objects.data.enums.*;

import java.io.*;

public class GalleryItem {
    private File file;
    private String name;
    private MediaTypes mediaType;
    private int id;

    public GalleryItem(String filePath, String name, MediaTypes mediaType) {
        this.file = new File(filePath);
        this.name = name;
        this.mediaType = mediaType;
    }

    public GalleryItem(int id, String filePath, String name, MediaTypes mediaType) {
        this(filePath, name, mediaType);
        this.id = id;
    }

    public MediaTypes getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaTypes mediaType) {
        this.mediaType = mediaType;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
