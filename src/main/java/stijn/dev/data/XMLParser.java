package stijn.dev.data;

import java.io.*;
import java.time.*;
import java.util.*;
import java.util.concurrent.*;

import javafx.beans.property.*;
import javafx.collections.*;
import javafx.util.*;
import nu.xom.*;
import nu.xom.Builder;
import org.apache.commons.lang3.*;
import stijn.dev.data.*;
import stijn.dev.data.objects.items.*;
import stijn.dev.records.*;
import stijn.dev.service.*;


public class XMLParser {
    private static String metadataFolder = "C:\\Users\\stijn\\Desktop\\Front End project\\Metadata";
    private static File metadata = new File(metadataFolder+"\\Metadata.xml");
    private static File platforms = new File(metadataFolder+"\\Platforms.xml");
    private static File gamesAndPlatforms = new File(metadataFolder+"\\Files.xml");
    private static File gameControllers = new File(metadataFolder+"\\GameControllers.xml");
    private static boolean noMatchFound = true;
    private static int lowestDatabaseID = 0;

    public static String importingAsPlatform = "";
    public static String scrapeAsPlatform = "";


    public ArrayBlockingQueue<Game> parseGames(List<RomImportRecord> romImportRecords){
        double startTime = System.currentTimeMillis();
        ArrayBlockingQueue<Game> results = new ArrayBlockingQueue<>(romImportRecords.size()*2);
        Thread finalThread = new Thread();
        try{
            synchronized(results) {
                Builder parser = new Builder();
                Document doc = parser.build(metadata);
                Elements files = doc.getRootElement().getChildElements("Game");
                for (RomImportRecord rom : romImportRecords) {
                    CheckRomToDatabase checkRomToDatabase = new CheckRomToDatabase(rom, files, results);
                    Thread thread = new Thread(checkRomToDatabase);
                    thread.start();
                }
            }
        } catch (ParsingException e) {
            System.out.println("Something went wrong parsing the document");
        } catch (IOException e) {
            System.out.println("Document not found!");
        }
        try {
            finalThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        double endTime = System.currentTimeMillis();
        System.out.println("Parsing roms took: "+(endTime-startTime)+"ms.");
        return results;
    }
    public class CheckRomToDatabase implements Runnable{
        private RomImportRecord rom;
        private Elements files;
        private ArrayBlockingQueue<Game> results;
        public ArrayBlockingQueue<Game> getResults(){
            return results;
        }

        public CheckRomToDatabase(RomImportRecord rom , Elements files, ArrayBlockingQueue<Game> results){
            this.rom = rom;
            this.files = files;
            this.results = results;
        }
        @Override
        public void run() {
                noMatchFound = true;
                lowestDatabaseID = 0;
                HashMap<String, String> databaseId = new HashMap<>();
                System.out.println( rom.title().getValue());
                String mostMatchingContainingDatabaseId = null;
                Element mostMatchingContainingFile = null;
                for (Element file : files) {
                    String gameTitle = file.getFirstChildElement("Name").getValue();
                    String platform = file.getFirstChildElement("Platform").getValue();
                    if (platform.equals(rom.scrapeAsPlatform().getValue())) {
                        if (StringUtils.stripAccents(gameTitle).toLowerCase().matches(StringUtils.stripAccents(rom.title().getValue().toLowerCase()))) {
                            createGame(file);
                            return;
                        }
                        if (StringUtils.stripAccents(gameTitle).toLowerCase().contains(StringUtils.stripAccents(rom.title().getValue().toLowerCase()))) {
                            if (Integer.valueOf(file.getFirstChildElement("DatabaseID").getValue()) < lowestDatabaseID || lowestDatabaseID == 0) {
                                lowestDatabaseID = Integer.valueOf(file.getFirstChildElement("DatabaseID").getValue());
                                mostMatchingContainingDatabaseId = file.getFirstChildElement("DatabaseID").getValue();
                                mostMatchingContainingFile = file;
                            }
                        }
                    }
                }
                if(null!=mostMatchingContainingDatabaseId){
                    createGame(mostMatchingContainingFile);
                    return;
                }
                int highestMatchRating = 0;

                String mostMatchingDatabaseID ="0";
                String mostMatchingGameTitle = "";
                Element mostMatchingFile = null;
                for (Element file : files){

                    int matchRating = 0;
                    String gameTitle = file.getFirstChildElement("Name").getValue();
                    String platform = file.getFirstChildElement("Platform").getValue();
                    if(noMatchFound) {
                        int gameTitleSegments= gameTitle.split(" ").length;
                        for (String segment : rom.title().getValue().split(" ")) {
                            if(StringUtils.stripAccents(gameTitle).toLowerCase().contains(StringUtils.stripAccents(segment.toLowerCase()))
                                    &&platform.equals(rom.scrapeAsPlatform().getValue())){
                                matchRating++;
                            }
                            if(matchRating>highestMatchRating && matchRating>=gameTitleSegments/2){
                                mostMatchingDatabaseID= file.getFirstChildElement("DatabaseID").getValue();
                                mostMatchingGameTitle=gameTitle;
                                mostMatchingFile = file;
                                highestMatchRating=matchRating;
                                if(Integer.valueOf(mostMatchingDatabaseID)<lowestDatabaseID){
                                    lowestDatabaseID = Integer.valueOf(mostMatchingDatabaseID);
                                }
                            } else if(matchRating==highestMatchRating&&Integer.valueOf(file.getFirstChildElement("DatabaseID").getValue())<lowestDatabaseID){
                                mostMatchingDatabaseID= file.getFirstChildElement("DatabaseID").getValue();
                                mostMatchingGameTitle=gameTitle;
                                mostMatchingFile = file;
                                highestMatchRating=matchRating;
                            }

                        }
                    }
                }
                if(noMatchFound && highestMatchRating>=2){
                    createGame(mostMatchingFile);
                } else{
                    createGame("Not Found");
                }
            }

        private void createGame(Element file) {
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
            try {
                results.put(new Game(readElement(file.getFirstChildElement("Name")),
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
                        readElement(file.getFirstChildElement("ESRB"))));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        private void createGame(String status) {
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
            try {
                results.put(new Game(rom.title().getValue(),
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
                        readElement(null)));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        private String readElement(Element element){
            if(element!=null){
                return element.getValue();
            } else return "N/A";
        }
    }

    private Pair<String, String> checkFullName(RomImportRecord rom,Elements files){

        return null;
    }

    public static void listChildren(Element current, int depth){
        printSpaces(depth);
        String data ="";
        if(current instanceof Element){
            Element temp = (Element) current;
            data = ": "+temp.getFirstChildElement("FileName").getValue();
        }
        System.out.println(current.getClass().getName()+data);
    }

    public static void printSpaces(int n){
        for (int i = 0; i < n; i++) {
            System.out.println(' ');
        }
    }

    public static ObservableList<RomImportRecord> parseRoms(List<File> files){
        List<RomImportRecord> romImportRecords=new ArrayList<>();
        for (File file: files){
            String filename = file.getName();
            String cleanFilename = FilenameUtil.cleanFilename(filename);
            String region = RegionUtil.getFileRegion(filename);
            romImportRecords.add(new RomImportRecord(new SimpleStringProperty(file.getPath()),
                    new SimpleStringProperty(FilenameUtil.extractFileExtension(file)), new SimpleStringProperty(cleanFilename),
                    new SimpleStringProperty(region), new SimpleStringProperty(importingAsPlatform), new SimpleStringProperty(scrapeAsPlatform)));
        }
        readResults(romImportRecords);
        return FXCollections.observableList(romImportRecords);
    }

    private static void readResults(List<RomImportRecord> romImportRecords) {
        romImportRecords.stream().forEach(romImportRecord -> System.out.println(romImportRecord));
    }

    public static List<String> getPlatforms() {
        ArrayList<String> platformList = new ArrayList<>();
        try{
            Builder parser = new Builder();
            Document doc = parser.build(platforms);
            Elements files = doc.getRootElement().getChildElements("Platform");
            for (Element platform : files) {
                    String platformName = platform.getFirstChildElement("Name").getValue();
                platformList.add(platformName);
            }
        } catch (ParsingException e) {
            System.out.println("Something went wrong parsing the document");
        } catch (IOException e) {
            System.out.println("Document not found!");
        }
        List<String> results = platformList.stream().sorted().toList();
        return results;
    }

    public static Platform parsePlatform(String platform) {
        Platform platformObject = null;
        boolean platformFound = false;
        HashMap<String, String> specs = new HashMap<>();
        try{
            Builder parser = new Builder();
            Document doc = parser.build(platforms);
            Elements files = doc.getRootElement().getChildElements("Platform");
            for (Element element : files) {
                if(platform.equals(element.getFirstChildElement("Name").getValue())){
                    HashMap<String, LocalDate> releaseDate = new HashMap<>();
                    releaseDate.put("USA", LocalDate.parse(element.getFirstChildElement("ReleaseDate").getValue().substring(0,10)));
                    specs.put("Cpu",element.getFirstChildElement("Cpu").getValue());
                    specs.put("Memory",element.getFirstChildElement("Memory").getValue());
                    specs.put("Graphics",element.getFirstChildElement("Graphics").getValue());
                    specs.put("Sound",element.getFirstChildElement("Sound").getValue());
                    specs.put("Display",element.getFirstChildElement("Display").getValue());
                    specs.put("MediaType",element.getFirstChildElement("Media").getValue());

                    platformObject = new Platform(element.getFirstChildElement("Name").getValue(),releaseDate,
                            element.getFirstChildElement("Developer").getValue(),element.getFirstChildElement("Notes").getValue(),
                            specs, element.getFirstChildElement("MaxControllers").getValue(),element.getFirstChildElement("Category").getValue()
                    );
                    platformFound = true;
                }
            }
        } catch (ParsingException e) {
            System.out.println("Something went wrong parsing the document");
        } catch (IOException e) {
            System.out.println("Document not found!");
        }
        if(!platformFound){
            HashMap<String, LocalDate> releaseDate = new HashMap<>();

            specs.put("Cpu","");
            specs.put("Memory","");
            specs.put("Graphics","");
            specs.put("Sound","");
            specs.put("Display","");
            specs.put("MediaType","");
            releaseDate.put("USA", LocalDate.parse("9999-12-31"));
            platformObject = new Platform(platform,releaseDate,null,null,specs,null,null);
        }
        return platformObject;
    }
}
