package stijn.dev.datasource.importing.xml.objecttranslator;

import nu.xom.*;
import stijn.dev.datasource.*;
import stijn.dev.datasource.importing.xml.*;
import stijn.dev.datasource.importing.xml.interfaces.*;
import stijn.dev.datasource.objects.items.*;
import stijn.dev.datasource.records.*;

import java.time.*;
import java.util.*;

public class GameFromXML implements IElementReader {
    private RomImportRecord rom;

    public Game createGame(Element file, Platform platform) {
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
        return new Game(readElement(file.getFirstChildElement("Name")),
                rom.fullFilename().getValue(),
                readElement(file.getFirstChildElement("DatabaseID")),
                readElement(file.getFirstChildElement("Overview")),
                releaseDates,
                readElement(file.getFirstChildElement("MaxPlayers")),
                tags,
                readElement(file.getFirstChildElement("VideoURL")),
                readElement(file.getFirstChildElement("WikipediaURL")),
                new Developer(readElement(file.getFirstChildElement("Developer"))),
                new Publisher(readElement(file.getFirstChildElement("Publisher"))),
                platform,
                readElement(file.getFirstChildElement("CommunityRating")),
                readElement(file.getFirstChildElement("CommunityRatingCount")),
                Boolean.valueOf(readElement(file.getFirstChildElement("Cooperative"))),
                readElement(file.getFirstChildElement("ESRB")));
    }

    public Game createGame(String status,Platform platform) {
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
        return new Game(rom.title().getValue(),
                rom.fullFilename().getValue(),
                readElement(null),
                readElement(null),
                releaseDates,
                readElement(null),
                tags,
                readElement(null),
                readElement(null),
                new Developer(readElement(null)),
                new Publisher(readElement(null)),
                platform,
                readElement(null),
                readElement(null),
                false,
                readElement(null));
    }

    public void setRom(RomImportRecord rom) {
        this.rom = rom;
    }
}
