package stijn.dev.datasource.importing.xml.runnables;

import nu.xom.*;
import stijn.dev.datasource.importing.xml.*;
import stijn.dev.datasource.importing.xml.interfaces.*;
import stijn.dev.datasource.importing.xml.objecttranslator.*;
import stijn.dev.datasource.objects.items.*;
import stijn.dev.datasource.records.*;

import java.util.*;
import java.util.concurrent.*;

import static stijn.dev.datasource.importing.util.GameTitleMatcher.*;

public class GameSearchingProcess implements Runnable, IElementReader {
    private Elements files;
    private ArrayList<GameImportItem> results;
    private boolean noMatchFound = true;
    private int lowestDatabaseID = 0;
    private GameFromXML gameFromXML = new GameFromXML();
    private PlatformXMLParser platformXMLParser = new PlatformXMLParser();
    private Platform platform;
    private String title;
    private String path;
    private String region;

    public GameSearchingProcess(String title, String region, String path, String platform, Elements files, ArrayList<GameImportItem> results){
        this.title = title;
        this.region = region;
        this.path = path;
        this.files = files;
        this.results = results;
        this.platform = platformXMLParser.parsePlatform(platform);
    }
    @Override
    public void run() {
        noMatchFound = true;
        lowestDatabaseID = 0;
        //System.out.println( rom.title().getValue());
        try {
            if (findMatchThroughTitle()) return;
            findMatchThroughSegments();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean findMatchThroughTitle() throws InterruptedException {
        String mostMatchingContainingDatabaseId = null;
        Element mostMatchingContainingFile = null;
        for (Element file : files) {
            String gameTitle = file.getFirstChildElement("Name").getValue();
            String platform = file.getFirstChildElement("Platform").getValue();

            if (platform.equals(platform)) {
                if (isExactMatch(gameTitle, title)) {
                    results.add(gameFromXML.createGame(file,path,region,this.platform.getPlatformName()));
                    return true;
                }
                if (isContainedInTitle(gameTitle, title)) {
                    if (Integer.valueOf(file.getFirstChildElement("DatabaseID").getValue()) < lowestDatabaseID || lowestDatabaseID == 0) {
                        lowestDatabaseID = Integer.valueOf(file.getFirstChildElement("DatabaseID").getValue());
                        mostMatchingContainingDatabaseId = file.getFirstChildElement("DatabaseID").getValue();
                        mostMatchingContainingFile = file;
                    }
                }
            }
        }
        if(null!= mostMatchingContainingDatabaseId){
            results.add(gameFromXML.createGame(mostMatchingContainingFile,path,region,platform.getPlatformName()));
            return true;
        }
        return false;
    }

    private void findMatchThroughSegments() throws InterruptedException {
        int highestMatchRating = 0;
        String mostMatchingDatabaseID;
        Element mostMatchingFile = null;
        for (Element file : files) {
            int matchRating = 0;
            String gameTitle = file.getFirstChildElement("Name").getValue();
            String platform = file.getFirstChildElement("Platform").getValue();
            if (platform.equals(platform)) {
                if (noMatchFound) {
                    int gameTitleSegments = gameTitle.split(" ").length;
                    for (String segment : title.split(" ")) {
                        if (segmentMatchesPartOfGameTitle(gameTitle, platform, segment, this.platform.getPlatformName())) {
                            matchRating++;
                        }
                        if (matchRating > highestMatchRating && matchRating >= gameTitleSegments / 2) {
                            mostMatchingDatabaseID = file.getFirstChildElement("DatabaseID").getValue();
                            mostMatchingFile = file;
                            highestMatchRating = matchRating;
                            if (Integer.valueOf(mostMatchingDatabaseID) < lowestDatabaseID) {
                                lowestDatabaseID = Integer.valueOf(mostMatchingDatabaseID);
                            }
                        } else if (matchRating == highestMatchRating && Integer.valueOf(file.getFirstChildElement("DatabaseID").getValue()) < lowestDatabaseID) {
                            mostMatchingDatabaseID = file.getFirstChildElement("DatabaseID").getValue();
                            mostMatchingFile = file;
                            highestMatchRating = matchRating;
                        }
                    }
                }
            }
        }
        if(noMatchFound && highestMatchRating>=2){
            results.add(gameFromXML.createGame(mostMatchingFile,path,region,this.platform.getPlatformName()));
        }
    }
}
