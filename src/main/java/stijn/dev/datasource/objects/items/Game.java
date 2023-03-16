package stijn.dev.datasource.objects.items;

import stijn.dev.datasource.objects.collections.*;
import stijn.dev.datasource.objects.items.data.*;

import java.time.*;
import java.util.*;

public class Game{
    //Properties
    private String name;
    private String path;
    private String gameId;
    private String description;
    private HashMap<String, LocalDate> releaseDates;
    private String maxPlayers;
    private ArrayList<String> tags;
    //Tag Nodes for the Cooperative parent and the Genres parent and ESRB
    private String videoURL;
    private String wikipediaURL;
    private String communityRating;
    private String communityRatingCount;
    private boolean cooperative;
    private String ESRBRating;
    //Relationships
    private String platform;
    private String developer;
    private String publisher;
    private HowLongToBeatData HLTBData;
    private String launchParameters;
    private double priority;
    //Relationships
    private HashMap<String, Gallery> galleries = new HashMap<>();
    private ArrayList<Trivia> trivia = new ArrayList<>();
    private Emulator defaultEmulator = null;
    private ArrayList<String> canons = new ArrayList<>();
    private ArrayList<String> series = new ArrayList<>();
    private ArrayList<String> franchises = new ArrayList<>();
    private ArrayList<String> playlists = new ArrayList<>();
    private ArrayList<String> timelineSteps = new ArrayList<>();
    public Game(String name, String path, String gameId,
                String description,
                String maxPlayers, String platform,
                String communityRating, String communityRatingCount, boolean cooperative, String ESRBRating) {
        this.name = name;
        this.path = path;
        this.gameId = gameId;
        this.description = description;
        this.maxPlayers = maxPlayers;
        this.platform = platform;
        this.communityRating = communityRating;
        this.communityRatingCount=communityRatingCount;
        this.cooperative =cooperative;
        this.ESRBRating = ESRBRating;
    }

    public HowLongToBeatData getHLTBData() {
        return HLTBData;
    }

    public String getLaunchParameters() {
        return launchParameters;
    }

    public double getPriority() {
        return priority;
    }

    public Emulator getEmulator() {
        return defaultEmulator;
    }

    public HashMap<String, Gallery> getGalleries() {
        return galleries;
    }

    public ArrayList<Trivia> getTrivia() {
        return trivia;
    }

    public Emulator getDefaultEmulator() {
        return defaultEmulator;
    }

    public ArrayList<String> getCanons() {
        return canons;
    }

    public ArrayList<String> getSeries() {
        return series;
    }

    public ArrayList<String> getFranchises() {
        return franchises;
    }

    public ArrayList<String> getPlaylists() {
        return playlists;
    }

    public ArrayList<String> getTimelineSteps() {
        return timelineSteps;
    }

    public void setLaunchParameters(String launchParameters) {
        this.launchParameters = launchParameters;
    }

    public void setPriority(double priority) {
        this.priority = priority;
    }

    public void setGalleries(HashMap<String, Gallery> galleries) {
        this.galleries = galleries;
    }

    public void setTrivia(ArrayList<Trivia> trivia) {
        this.trivia = trivia;
    }

    public void setDefaultEmulator(Emulator defaultEmulator) {
        this.defaultEmulator = defaultEmulator;
    }

    public void setCanons(ArrayList<String> canons) {
        this.canons = canons;
    }

    public void setSeries(ArrayList<String> series) {
        this.series = series;
    }

    public void setFranchises(ArrayList<String> franchises) {
        this.franchises = franchises;
    }

    public void setPlaylists(ArrayList<String> playlists) {
        this.playlists = playlists;
    }

    public void setTimelineSteps(ArrayList<String> timelineSteps) {
        this.timelineSteps = timelineSteps;
    }

    @Override
    public String toString() {
        return "Game{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", gameId='" + gameId + '\'' +
                ", description='" + description + '\'' +
                ", releaseDates=" + releaseDates +
                ", maxPlayers='" + maxPlayers + '\'' +
                ", tags=" + tags +
                ", videoURL='" + videoURL + '\'' +
                ", wikipediaURL='" + wikipediaURL + '\'' +
                ", communityRating='" + communityRating + '\'' +
                ", communityRatingCount='" + communityRatingCount + '\'' +
                ", cooperative=" + cooperative +
                ", ESRBRating='" + ESRBRating + '\'' +
                ", platform='" + platform + '\'' +
                ", developer='" + developer + '\'' +
                ", publisher='" + publisher + '\'' +
                ", HLTBData=" + HLTBData +
                ", launchParameters='" + launchParameters + '\'' +
                ", priority=" + priority +
                ", galleries=" + galleries +
                ", trivia=" + trivia +
                ", defaultEmulator=" + defaultEmulator +
                ", canons=" + canons +
                ", series=" + series +
                ", franchises=" + franchises +
                ", playlists=" + playlists +
                ", timelineSteps=" + timelineSteps +
                '}';
    }
}
