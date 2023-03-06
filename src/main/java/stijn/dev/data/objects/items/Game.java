package stijn.dev.data.objects.items;

import stijn.dev.data.objects.items.data.*;

import java.io.*;

public class Game extends Item{

    private String name;
    private String path;
    private int GameId;
    private String description;
    //TODO add How Long to Beat Scraper to fill in the remaining parameters
    private HowLongToBeatData HLTBData;
    private String launchParameters;
    private double priority;

    private Emulator emulator;

    public Game(String name, String path) {
        this.name = name;
        File file = new File(path);
        if(file.exists() && file.isFile()) {
            this.path = path;
        }
    }
}
