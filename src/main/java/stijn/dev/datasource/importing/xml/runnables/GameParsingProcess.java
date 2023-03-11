package stijn.dev.datasource.importing.xml.runnables;

import nu.xom.*;
import stijn.dev.datasource.importing.xml.interfaces.*;
import stijn.dev.datasource.importing.xml.objecttranslator.*;
import stijn.dev.datasource.objects.items.*;
import stijn.dev.datasource.records.*;
import java.util.concurrent.*;

import static stijn.dev.datasource.importing.util.GameTitleMatcher.*;

public class GameParsingProcess implements Runnable, IElementReader {
    private RomImportRecord rom;
    private Elements files;
    private ArrayBlockingQueue<Game> results;
    private boolean noMatchFound = true;
    private int lowestDatabaseID = 0;
    private GameFromXML gameFromXML = new GameFromXML();
    public String importingAsPlatform;
    public String scrapeAsPlatform;

    public GameParsingProcess(RomImportRecord rom , Elements files, ArrayBlockingQueue<Game> results, String importingAsPlatform, String scrapeAsPlatform){
        this.rom = rom;
        this.files = files;
        this.results = results;
        this.importingAsPlatform = importingAsPlatform;
        this.scrapeAsPlatform = scrapeAsPlatform;
        gameFromXML.setRom(rom);
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

            if (platform.equals(rom.scrapeAsPlatform().getValue())) {
                if (isExactMatch(gameTitle, rom)) {
                    results.put(gameFromXML.createGame(file));
                    return true;
                }
                if (isContainedInTitle(gameTitle, rom)) {
                    if (Integer.valueOf(file.getFirstChildElement("DatabaseID").getValue()) < lowestDatabaseID || lowestDatabaseID == 0) {
                        lowestDatabaseID = Integer.valueOf(file.getFirstChildElement("DatabaseID").getValue());
                        mostMatchingContainingDatabaseId = file.getFirstChildElement("DatabaseID").getValue();
                        mostMatchingContainingFile = file;
                    }
                }
            }
        }
        if(null!= mostMatchingContainingDatabaseId){
            results.put(gameFromXML.createGame(mostMatchingContainingFile));
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
            if (platform.equals(rom.scrapeAsPlatform().getValue())) {
                if (noMatchFound) {
                    int gameTitleSegments = gameTitle.split(" ").length;
                    for (String segment : rom.title().getValue().split(" ")) {
                        if (segmentMatchesPartOfGameTitle(gameTitle, platform, segment, rom)) {
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
            results.put(gameFromXML.createGame(mostMatchingFile));
        } else{
            results.put(gameFromXML.createGame("Not Found"));
        }
    }


}
