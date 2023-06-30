package stijn.dev.datasource.objects.items;

import javafx.beans.property.*;
import stijn.dev.datasource.objects.data.*;

import java.util.*;

public class Platform extends Item {
    private StringProperty platformName;
    private int id;
    private List<ReleaseDate> releaseDates;
    private StringProperty publisher;
    private StringProperty description;
    private Emulator defaultEmulator;
    private HashMap<String,String> specs;
    private StringProperty maxPlayers;
    private String previousPublisher;
    private StringProperty category;
    private List<Emulator> emulators;
    private StringProperty sortingTitle;

    public Platform(String platformName, String sortingTitle, List<ReleaseDate> releaseDates, String developer, String description,
                    HashMap<String,String> specs, String maxPlayers, String category){
        this.previousPublisher = developer;
        this.platformName = new SimpleStringProperty(platformName);
        this.sortingTitle = new SimpleStringProperty(sortingTitle);
        this.releaseDates = releaseDates;
        this.publisher = new SimpleStringProperty(developer);
        this.description = new SimpleStringProperty(description);
        this.specs = specs;
        this.maxPlayers = new SimpleStringProperty(maxPlayers);
        this.category = new SimpleStringProperty(category);
    }

    public Platform(int id, String platformName, String sortingTitle, List<ReleaseDate> releaseDates, String developer, String description,
                    HashMap<String,String> specs, String maxPlayers, String category, Emulator defaultEmulator, List<Emulator> emulators){
        this(platformName, sortingTitle, releaseDates, developer, description, specs, maxPlayers, category);
        this.defaultEmulator = defaultEmulator;
        this.emulators = emulators;
        this.id = id;
    }

    @Override
    public String getSortingTitle() {
        return sortingTitle.get();
    }

    public StringProperty sortingTitleProperty() {
        return sortingTitle;
    }

    public void setSortingTitle(String sortingTitle) {
        this.sortingTitle.set(sortingTitle);
    }

    public int getId() {
        return id;
    }

    public String getPlatformName() {
        return platformName.get();
    }

    public String getPreviousPublisher() {
        return previousPublisher;
    }

    public void setPreviousPublisher(String previousPublisher) {
        this.previousPublisher = previousPublisher;
    }

    public StringProperty platformNameProperty() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName.set(platformName);
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<ReleaseDate> getReleaseDates() {
        return releaseDates;
    }

    public void setReleaseDates(List<ReleaseDate> releaseDates) {
        this.releaseDates = releaseDates;
    }

    public String getPublisher() {
        return publisher.get();
    }

    public StringProperty publisherProperty() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher.set(publisher);
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public Emulator getDefaultEmulator() {
        return defaultEmulator;
    }

    public void setDefaultEmulator(Emulator defaultEmulator) {
        this.defaultEmulator = defaultEmulator;
    }

    public HashMap<String, String> getSpecs() {
        return specs;
    }

    public void setSpecs(HashMap<String, String> specs) {
        this.specs = specs;
    }

    public String getMaxPlayers() {
        return maxPlayers.get();
    }

    public StringProperty maxPlayersProperty() {
        return maxPlayers;
    }

    public void setMaxPlayers(String maxPlayers) {
        this.maxPlayers.set(maxPlayers);
    }

    public String getCategory() {
        return category.get();
    }

    public StringProperty categoryProperty() {
        return category;
    }

    public void setCategory(String category) {
        this.category.set(category);
    }

    public List<Emulator> getEmulators() {
        return emulators;
    }

    public void setEmulators(List<Emulator> emulators) {
        this.emulators = emulators;
    }

    @Override
    public String toString() {
        String string = "Platform Name: "+ platformName+"\nRelease Dates: ";
        for (ReleaseDate releaseDate : releaseDates) {
            if(releaseDate.getDate().getValue()!=null){
                string += "\n" + releaseDate.getTerritory().getValue().toUpperCase() + ": " + releaseDate.getDate().getValue().toString();
            }
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
