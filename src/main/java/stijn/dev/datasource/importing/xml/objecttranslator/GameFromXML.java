package stijn.dev.datasource.importing.xml.objecttranslator;

import nu.xom.*;
import stijn.dev.datasource.importing.xml.interfaces.*;
import stijn.dev.datasource.objects.items.*;
import stijn.dev.datasource.records.*;
import stijn.dev.util.*;

import java.time.*;
import java.util.*;

public class GameFromXML implements IElementReader {
    private RomImportRecord rom;

    public GameImportItem createGame(Element file) {
        String region;
        if(rom.region().getValue().equals("N/A")){
            region = "USA";
        } else{
            region = rom.region().getValue();
        }
        HashMap<String, LocalDate> releaseDates = new HashMap<>();
        if(readElement(file.getFirstChildElement("ReleaseDate")).length()>=10){
            if(DateValidator.isValid(readElement(file.getFirstChildElement("ReleaseDate")).substring(0,10),"yyyy-MM-dd")){
                releaseDates.put(region, LocalDate.parse(readElement(file.getFirstChildElement("ReleaseDate")).substring(0,10)));
            }
        }  else {
            releaseDates.put(region, LocalDate.parse("9999-12-31"));
        }

        ArrayList<String> tags = new ArrayList<>();
        for (String tag :
                readElement(file.getFirstChildElement("Genres")).split(";")) {
            tags.add(tag.trim());
        }
        return new GameImportItem(readElement(file.getFirstChildElement("Name")),
                rom.fullFilename().getValue(),
                readElement(file.getFirstChildElement("DatabaseID")),
                readElement(file.getFirstChildElement("Overview")),
                releaseDates,
                readElement(file.getFirstChildElement("MaxPlayers")),
                tags,
                readElement(file.getFirstChildElement("VideoURL")),
                readElement(file.getFirstChildElement("WikipediaURL")),
                readElement(file.getFirstChildElement("Developer")),
                readElement(file.getFirstChildElement("Publisher")),
                rom.platform().getValue(),
                readElement(file.getFirstChildElement("CommunityRating")),
                readElement(file.getFirstChildElement("CommunityRatingCount")),
                Boolean.valueOf(readElement(file.getFirstChildElement("Cooperative"))),
                readElement(file.getFirstChildElement("ESRB")));
    }

    public GameImportItem createGame(String status) {
        String region;
        if(rom.region().getValue().equals("N/A")){
            region = "USA";
        } else{
            region = rom.region().getValue();
        }
        HashMap<String, LocalDate> releaseDates = new HashMap<>();
        releaseDates.put(region, LocalDate.parse("9999-12-31"));
        ArrayList<String> tags = new ArrayList<>();
        for (String tag :
                readElement(null).split(";")) {
            tags.add(tag.trim());
        }
        tags.add(readElement(null));

        tags.add(readElement(null));
        return new GameImportItem(rom.title().getValue(),
                rom.fullFilename().getValue(),
                readElement(null),
                readElement(null),
                releaseDates,
                readElement(null),
                tags,
                readElement(null),
                readElement(null),
                readElement(null),
                readElement(null),
                rom.platform().getValue(),
                readElement(null),
                readElement(null),
                false,
                readElement(null));
    }

    public GameImportItem createGame(Element file, String path, String importRegion, String platform) {
        String region;
        if (importRegion.equals("N/A")) {
            region = "USA";
        } else {
            region = importRegion;
        }
        HashMap<String, LocalDate> releaseDates = new HashMap<>();
        if (readElement(file.getFirstChildElement("ReleaseDate")).length() >= 10) {
            if (DateValidator.isValid(readElement(file.getFirstChildElement("ReleaseDate")).substring(0, 10), "yyyy-MM-dd")) {
                releaseDates.put(region, LocalDate.parse(readElement(file.getFirstChildElement("ReleaseDate")).substring(0, 10)));
            }
        } else {
            releaseDates.put(region, LocalDate.parse("9999-12-31"));
        }

        ArrayList<String> tags = new ArrayList<>();
        for (String tag :
                readElement(file.getFirstChildElement("Genres")).split(";")) {
            tags.add(tag.trim());
        }
        return new GameImportItem(readElement(file.getFirstChildElement("Name")),
                path,
                readElement(file.getFirstChildElement("DatabaseID")),
                readElement(file.getFirstChildElement("Overview")),
                releaseDates,
                readElement(file.getFirstChildElement("MaxPlayers")),
                tags,
                readElement(file.getFirstChildElement("VideoURL")),
                readElement(file.getFirstChildElement("WikipediaURL")),
                readElement(file.getFirstChildElement("Developer")),
                readElement(file.getFirstChildElement("Publisher")),
                platform,
                readElement(file.getFirstChildElement("CommunityRating")),
                readElement(file.getFirstChildElement("CommunityRatingCount")),
                Boolean.valueOf(readElement(file.getFirstChildElement("Cooperative"))),
                readElement(file.getFirstChildElement("ESRB")));
    }

    public void setRom(RomImportRecord rom) {
        this.rom = rom;
    }
}
