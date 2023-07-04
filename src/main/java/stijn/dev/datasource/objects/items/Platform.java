package stijn.dev.datasource.objects.items;

import javafx.beans.property.*;
import stijn.dev.datasource.objects.data.*;

import java.time.*;
import java.util.*;

public class Platform extends Item {
    private StringProperty platformName;
    private int id;
    private List<ReleaseDate> releaseDates;
    private List<String> publishers;
    private List<String> manufacturers;
    private StringProperty description;
    private Emulator defaultEmulator;
    private List<PlatformSpecification> specs;
    private StringProperty maxPlayers;
    private List<String> previousPublishers;
    private List<String> media;
    private StringProperty category;
    private List<Emulator> emulators;
    private StringProperty sortingTitle;
    private List<String> mediaTypes;
    private List<String> productFamilies;
    private String generation;
    private String unitsSold;
    private LocalDate dateDiscontinued;
    private List<AlternateName> alternateNames;
    private List<RelatedPlatform> relatedPlatforms;
    private List<Trivia> trivia;

    public Platform(String platformName, String sortingTitle, List<ReleaseDate> releaseDates, List<String> publishers, List<String> manufacturers,
                    String description, List<PlatformSpecification> specs, String maxPlayers, String category , List<String> mediaTypes){
        this.previousPublishers = publishers;
        this.platformName = new SimpleStringProperty(platformName);
        this.sortingTitle = new SimpleStringProperty(sortingTitle);
        this.releaseDates = releaseDates;
        this.publishers = publishers;
        this.description = new SimpleStringProperty(description);
        this.specs = specs;
        this.maxPlayers = new SimpleStringProperty(maxPlayers);
        this.category = new SimpleStringProperty(category);
        this.manufacturers = manufacturers;
        this.mediaTypes = mediaTypes;
    }

    public Platform(int id, String platformName, List<AlternateName> alternateNames, String sortingTitle, List<ReleaseDate> releaseDates, LocalDate dateDiscontinued,
                    String generation, String unitsSold, List<String> publishers, List<String> manufacturers, String description,
                    List<PlatformSpecification> specs, String maxPlayers, String category, List<String> media, List<String> mediaTypes,
                    List<String> productFamilies, List<RelatedPlatform> relatedPlatforms, List<Trivia> trivia,
                    Emulator defaultEmulator, List<Emulator> emulators){
        this(platformName, sortingTitle, releaseDates, publishers, manufacturers, description, specs, maxPlayers, category, mediaTypes);
        this.defaultEmulator = defaultEmulator;
        this.emulators = emulators;
        this.id = id;
        this.media = media;
        this.productFamilies = productFamilies;
        this.generation = generation;
        this.unitsSold = unitsSold;
        this.dateDiscontinued = dateDiscontinued;
        this.alternateNames = alternateNames;
        this.relatedPlatforms = relatedPlatforms;
        this.trivia = trivia;
    }

    @Override
    public String getSortingTitle() {
        return sortingTitle.get();
    }

    public List<PlatformSpecification> getSpecs() {
        return specs;
    }

    public List<String> getManufacturers() {
        return manufacturers;
    }

    public void setManufacturers(List<String> manufacturers) {
        this.manufacturers = manufacturers;
    }

    public void setSpecs(List<PlatformSpecification> specs) {
        this.specs = specs;
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

    public List<String> getPublishers() {
        return publishers;
    }

    public void setPublishers(List<String> publishers) {
        this.publishers = publishers;
    }

    public List<String> getMedia() {
        return media;
    }

    public void setMedia(List<String> media) {
        this.media = media;
    }

    public List<String> getPreviousPublishers() {
        return previousPublishers;
    }

    public void setPreviousPublishers(List<String> previousPublishers) {
        this.previousPublishers = previousPublishers;
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
        string += "\nPublisher: "+ publishers +"\nDescription: "+description+"\n Specs: ";
        for (PlatformSpecification spec: specs) {
            string += "\n"+spec.getSpecificationType()+": "+spec.getSpecification();
        }
        string += "\nMax Players: "+maxPlayers+"\nCategory: "+category;
        return string;
    }

    public List<String> getMediaTypes() {
        return mediaTypes;
    }

    public void setMediaTypes(List<String> mediaTypes) {
        this.mediaTypes = mediaTypes;
    }

    public List<String> getProductFamilies() {
        return productFamilies;
    }

    public void setProductFamilies(List<String> productFamilies) {
        this.productFamilies = productFamilies;
    }

    public String getGeneration() {
        return generation;
    }

    public void setGeneration(String generation) {
        this.generation = generation;
    }

    public String getUnitsSold() {
        return unitsSold;
    }

    public void setUnitsSold(String unitsSold) {
        this.unitsSold = unitsSold;
    }

    public LocalDate getDateDiscontinued() {
        return dateDiscontinued;
    }

    public void setDateDiscontinued(LocalDate dateDiscontinued) {
        this.dateDiscontinued = dateDiscontinued;
    }

    public List<AlternateName> getAlternateNames() {
        return alternateNames;
    }

    public void setAlternateNames(List<AlternateName> alternateNames) {
        this.alternateNames = alternateNames;
    }

    public List<RelatedPlatform> getRelatedPlatforms() {
        return relatedPlatforms;
    }

    public void setRelatedPlatforms(List<RelatedPlatform> relatedPlatforms) {
        this.relatedPlatforms = relatedPlatforms;
    }

    public List<Trivia> getTrivia() {
        return trivia;
    }

    public void setTrivia(List<Trivia> trivia) {
        this.trivia = trivia;
    }
}
