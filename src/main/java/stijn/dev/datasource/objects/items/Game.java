package stijn.dev.datasource.objects.items;

import stijn.dev.datasource.objects.collections.*;
import stijn.dev.datasource.objects.data.*;

import java.time.*;
import java.util.*;

public class Game extends Item{
    //Properties
    private String name;
    private String path;
    private String gameId;
    private String description;
    private ArrayList<ReleaseDate> releaseDates;
    private String maxPlayers;
    private ArrayList<String> tags;
    private ArrayList<String> playmodes;
    private ArrayList<AlternateName> alternateNames;
    private ArrayList<RelatedGame> relatedGames;
    private ArrayList<AdditionalApp> additionalApps;
    //Tag Nodes for the Cooperative parent and the Genres parent and ESRB
    private String videoURL;
    private String wikipediaURL;
    private String communityRating;
    private String communityRatingCount;
    private boolean cooperative;
    private ArrayList<String> rating;
    //Relationships
    private String platform;
    private ArrayList<String> developer;
    private ArrayList<String> publisher;
    private ArrayList<Staff> staff;
    private ArrayList<Character> characters;
    private HowLongToBeatData HLTBData;
    private String launchParameters;
    private PriorityData priority;
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
                String communityRating, String communityRatingCount, boolean cooperative) {
        this.name = name;
        this.path = path;
        this.gameId = gameId;
        this.description = description;
        this.maxPlayers = maxPlayers;
        this.platform = platform;
        this.communityRating = communityRating;
        this.communityRatingCount=communityRatingCount;
        this.cooperative =cooperative;
    }

    public HowLongToBeatData getHLTBData() {
        return HLTBData;
    }

    public ArrayList<AdditionalApp> getAdditionalApps() {
        return additionalApps;
    }

    public void setAdditionalApps(ArrayList<AdditionalApp> additionalApps) {
        this.additionalApps = additionalApps;
    }

    public String getLaunchParameters() {
        return launchParameters;
    }

    public PriorityData getPriority() {
        return priority;
    }

    public Emulator getEmulator() {
        return defaultEmulator;
    }

    public HashMap<String, Gallery> getGalleries() {
        return galleries;
    }

    public ArrayList<String> getPlaymodes() {
        return playmodes;
    }

    public void setPlaymodes(ArrayList<String> playmodes) {
        this.playmodes = playmodes;
    }

    public ArrayList<String> getRating() {
        return rating;
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

    public void setPriority(PriorityData priority) {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<ReleaseDate> getReleaseDates() {
        return releaseDates;
    }

    public void setReleaseDates(ArrayList<ReleaseDate> releaseDates) {
        this.releaseDates = releaseDates;
    }

    public String getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(String maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public ArrayList<Staff> getStaff() {
        return staff;
    }

    public void setStaff(ArrayList<Staff> staff) {
        this.staff = staff;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getWikipediaURL() {
        return wikipediaURL;
    }

    public void setWikipediaURL(String wikipediaURL) {
        this.wikipediaURL = wikipediaURL;
    }

    public String getCommunityRating() {
        return communityRating;
    }

    public void setCommunityRating(String communityRating) {
        this.communityRating = communityRating;
    }

    public String getCommunityRatingCount() {
        return communityRatingCount;
    }

    public void setCommunityRatingCount(String communityRatingCount) {
        this.communityRatingCount = communityRatingCount;
    }

    public boolean isCooperative() {
        return cooperative;
    }

    public void setCooperative(boolean cooperative) {
        this.cooperative = cooperative;
    }

    public ArrayList<String> getRatings() {
        return rating;
    }

    public void setRatings(ArrayList<String> rating) {
        this.rating = rating;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public ArrayList<String> getDeveloper() {
        return developer;
    }

    public void setDeveloper(ArrayList<String> developer) {
        this.developer = developer;
    }

    public ArrayList<String> getPublisher() {
        return publisher;
    }

    public void setPublisher(ArrayList<String> publisher) {
        this.publisher = publisher;
    }

    public void setHLTBData(HowLongToBeatData HLTBData) {
        this.HLTBData = HLTBData;
    }

    public ArrayList<Character> getCharacters() {
        return characters;
    }

    public void setCharacters(ArrayList<Character> characters) {
        this.characters = characters;
    }

    public ArrayList<AlternateName> getAlternateNames() {
        return alternateNames;
    }

    public void setAlternateNames(ArrayList<AlternateName> alternateNames) {
        this.alternateNames = alternateNames;
    }

    public ArrayList<RelatedGame> getRelatedGames() {
        return relatedGames;
    }

    public void setRelatedGames(ArrayList<RelatedGame> relatedGames) {
        this.relatedGames = relatedGames;
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
                ", alternateNames=" + alternateNames +
                ", relatedGames=" + relatedGames +
                ", additionalApps=" + additionalApps +
                ", videoURL='" + videoURL + '\'' +
                ", wikipediaURL='" + wikipediaURL + '\'' +
                ", communityRating='" + communityRating + '\'' +
                ", communityRatingCount='" + communityRatingCount + '\'' +
                ", cooperative=" + cooperative +
                ", ESRBRating='" + rating + '\'' +
                ", platform='" + platform + '\'' +
                ", developer=" + developer +
                ", publisher=" + publisher +
                ", staff=" + staff +
                ", characters=" + characters +
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
