package stijn.dev.datasource.objects.items;

import stijn.dev.datasource.objects.collections.*;
import stijn.dev.datasource.objects.items.data.*;

import java.time.*;
import java.util.*;

public class Game extends Item{


    //Properties
        private String name;
        private String path;
        private String gameId;
        private String description;
        private HowLongToBeatData HLTBData;
        private String launchParameters;
        private double priority;
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
        private HashMap<String, Gallery> galleries = new HashMap<>();
        private ArrayList<Trivia> trivia = new ArrayList<>();
        private Emulator defaultEmulator = null;
        private String platform;
        private String developer;
        private String publisher;
//        private ArrayList<Canon> canons = new ArrayList<>();
//        private ArrayList<Serie> series = new ArrayList<>();
//        private ArrayList<Franchise> franchises = new ArrayList<>();
//        private ArrayList<Playlist> playlists = new ArrayList<>();
//        private ArrayList<TimelineStep> timelineSteps = new ArrayList<>();

    //Summary is empty by default and needs to be added manually.

    public Game(String name){
        this.name = name;
    }
    public Game(String name,
                String path,
                String gameId,
                String description,
                HashMap<String, LocalDate> releaseDates,
                String maxPlayers,
                ArrayList<String> tags,
                String videoURL,
                String wikipediaURL,
                String developer,
                String publisher,
                String platform,
                String communityRating,
                String communityRatingCount,
                boolean cooperative,
                String ESRBRating) {
        this.name = name;
        this.path = path;
        this.gameId = gameId;
        this.description = description;
        this.releaseDates = releaseDates;
        this.maxPlayers = maxPlayers;
        this.tags = tags;
        this.videoURL = videoURL;
        this.wikipediaURL = wikipediaURL;
        this.developer = developer;
        this.publisher = publisher;
        this.platform = platform;
        this.communityRating = communityRating;
        this.communityRatingCount=communityRatingCount;
        this.cooperative =cooperative;
        this.ESRBRating = ESRBRating;
    }

    public boolean isCooperative() {
        return cooperative;
    }

    public String getESRBRating() {
        return ESRBRating;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public String getGameId() {
        return gameId;
    }

    public HashMap<String, LocalDate> getReleaseDates() {
        return releaseDates;
    }

    public String getMaxPlayers() {
        return maxPlayers;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public String getWikipediaURL() {
        return wikipediaURL;
    }

    public String getPlatform() {
        return platform;
    }

    public String getDeveloper() {
        return developer;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getCommunityRating() {
        return communityRating;
    }

    public String getCommunityRatingCount() {
        return communityRatingCount;
    }

    public String getDescription() {
        return description;
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
}
