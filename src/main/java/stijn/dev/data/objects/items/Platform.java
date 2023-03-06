package stijn.dev.data.objects.items;

import java.time.*;
import java.util.*;

public class Platform extends Item {
    private String platformName;
    //TODO create territory's and releasedate relationships for releasedate attribute
    private HashMap<String, LocalDate> releaseDates;
    //TODO create developer if not exists
    private String publisher;
    private String description;
    private Emulator defaultEmulator;
    private HashMap<String,String> specs;
    private int maxPlayers;
    private String category;

    public Platform(String systemName, HashMap<String, LocalDate> releaseDates, String developer, String description,
                    HashMap<String,String> specs, int maxPlayers, String category){
        this.platformName = systemName;
        this.releaseDates =releaseDates;
        this.publisher = developer;
        this.description = description;
        this.specs = specs;
        this.maxPlayers = maxPlayers;
        this.category = category;
    }

    public String getPlatformName() {
        return platformName;
    }

    public HashMap<String, LocalDate> getReleaseDates() {
        return releaseDates;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getDescription() {
        return description;
    }

    public Emulator getDefaultEmulator() {
        return defaultEmulator;
    }

    public HashMap<String, String> getSpecs() {
        return specs;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        String string = "Platform Name: "+ platformName+"\nRelease Dates: ";
        for (String locale :
                releaseDates.keySet()) {
            string += "\n" + locale.toUpperCase() + ": " + releaseDates.get(locale);
        }
        string += "\nPublisher: "+ publisher +"\nDescription: "+description+"\n Specs: ";
        for (String spec:
             specs.keySet()) {
            string += "\n"+spec+": "+specs.get(spec);
        }
        string += "\nMax Players: "+maxPlayers+"\nCategory: "+category;
        return string;
    }
}
