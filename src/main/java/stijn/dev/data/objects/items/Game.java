package stijn.dev.data.objects.items;

import java.io.*;

public class Game extends Item{

    private String path;
    private String name;
    private Emulator emulator;

    public Game(String name, String path) {
        this.name = name;
        File file = new File(path);
        if(file.exists() && file.isFile()) {
            this.path = path;
        }
    }

    public Game(String name, String path, Emulator emulator, String args){
        this(name, path);
        this.emulator = emulator;
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
