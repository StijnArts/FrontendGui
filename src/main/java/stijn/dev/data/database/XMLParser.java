package stijn.dev.data.database;

import java.io.*;
import java.time.*;
import java.util.*;

import javafx.util.*;
import nu.xom.*;
import nu.xom.Builder;
import org.apache.commons.lang3.*;
import stijn.dev.data.objects.items.*;
import stijn.dev.records.*;


public class XMLParser {
    private static String metadataFolder = "C:\\Users\\stijn\\Desktop\\Front End project\\Metadata";
    private static File metadata = new File(metadataFolder+"\\Metadata.xml");
    private static File platforms = new File(metadataFolder+"\\Platforms.xml");
    private static File gamesAndPlatforms = new File(metadataFolder+"\\Files.xml");
    private static File gameControllers = new File(metadataFolder+"\\GameControllers.xml");
    private static boolean noMatchFound = true;
    private static int lowestDatabaseID = 0;
    public ArrayList<RomDatabaseRecord> parseGames(List<RomImportRecord> romImportRecords){
        double startTime = System.currentTimeMillis();
        ArrayList<RomDatabaseRecord> results = new ArrayList<>();
        try{
            Builder parser = new Builder();
            Document doc = parser.build(metadata);
            Elements files = doc.getRootElement().getChildElements("Game");
            for (RomImportRecord rom : romImportRecords) {
                CheckRomToDatabase checkRomToDatabase = new CheckRomToDatabase(rom, files,results);
                Thread thread = new Thread(checkRomToDatabase);
                thread.run();
            }

        } catch (ParsingException e) {
            System.out.println("Something went wrong parsing the document");
        } catch (IOException e) {
            System.out.println("Document not found!");
        }
        results.forEach(game-> System.out.println(game.toString()));
        double endTime = System.currentTimeMillis();
        System.out.println("Parsing roms took: "+(endTime-startTime)+"ms.");
        return results;
    }
    public class CheckRomToDatabase implements Runnable{
        private RomImportRecord rom;
        private Elements files;
        private List<RomDatabaseRecord> results;
        public List<RomDatabaseRecord> getResults(){
            return results;
        }

        public CheckRomToDatabase(RomImportRecord rom , Elements files, List<RomDatabaseRecord> results){
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
                String mostMatchingContainingGameTitle =null;
                for (Element file : files) {
                    String gameTitle = file.getFirstChildElement("Name").getValue();
                    String platform = file.getFirstChildElement("Platform").getValue();
                    if (platform.equals(rom.scrapeAsPlatform().getValue())) {
                        if (StringUtils.stripAccents(gameTitle).toLowerCase().matches(rom.title().getValue().toLowerCase())) {
                            results.add(new RomDatabaseRecord(rom.fullFilename().getValue(),gameTitle,rom.region().getValue(),
                                    rom.platform().getValue(),file.getFirstChildElement("DatabaseID").getValue()));
                            return;
                        }
                        if (StringUtils.stripAccents(gameTitle).toLowerCase().contains(rom.title().getValue().toLowerCase())) {
                            if (Integer.valueOf(file.getFirstChildElement("DatabaseID").getValue()) < lowestDatabaseID || lowestDatabaseID == 0) {
                                lowestDatabaseID = Integer.valueOf(file.getFirstChildElement("DatabaseID").getValue());
                                mostMatchingContainingDatabaseId = file.getFirstChildElement("DatabaseID").getValue();
                                mostMatchingContainingGameTitle = gameTitle;
                            }
                        }
                    }
                }
                if(null!=mostMatchingContainingDatabaseId){
                    results.add(new RomDatabaseRecord(rom.fullFilename().getValue(),mostMatchingContainingGameTitle,rom.region().getValue(),
                            rom.platform().getValue(),mostMatchingContainingDatabaseId));
                    return;
                }
                int highestMatchRating = 0;

                String mostMatchingDatabaseID ="0";
                String mostMatchingGameTitle = "";
                for (Element file : files){

                    int matchRating = 0;
                    String gameTitle = file.getFirstChildElement("Name").getValue();
                    String platform = file.getFirstChildElement("Platform").getValue();
                    if(noMatchFound) {
                        int gameTitleSegments= gameTitle.split(" ").length;
                        for (String segment : rom.title().getValue().split(" ")) {
                            if(StringUtils.stripAccents(gameTitle).toLowerCase().contains(segment.toLowerCase())
                                    &&platform.equals(rom.scrapeAsPlatform().getValue())){
                                matchRating++;
                            }
                            if(matchRating>highestMatchRating && matchRating>=gameTitleSegments/2){
                                mostMatchingDatabaseID= file.getFirstChildElement("DatabaseID").getValue();
                                mostMatchingGameTitle=gameTitle;
                                highestMatchRating=matchRating;
                                if(Integer.valueOf(mostMatchingDatabaseID)<lowestDatabaseID){
                                    lowestDatabaseID = Integer.valueOf(mostMatchingDatabaseID);
                                }
                            } else if(matchRating==highestMatchRating&&Integer.valueOf(file.getFirstChildElement("DatabaseID").getValue())<lowestDatabaseID){
                                mostMatchingDatabaseID= file.getFirstChildElement("DatabaseID").getValue();
                                mostMatchingGameTitle=gameTitle;
                                highestMatchRating=matchRating;
                            }

                        }
                    }
                }
                if(noMatchFound && highestMatchRating>=2){
                    results.add(new RomDatabaseRecord(rom.fullFilename().getValue(),mostMatchingGameTitle,rom.region().getValue(),
                            rom.platform().getValue(),mostMatchingDatabaseID));
                } else{
                    results.add(new RomDatabaseRecord(rom.fullFilename().getValue(),rom.title().getValue(),rom.region().getValue(),
                            rom.platform().getValue(),"Not Found"));
                }
            }
        }



    private Pair<String, String> checkFullName(RomImportRecord rom,Elements files){

        return null;
    }


//    try{
//        Builder parser = new Builder();
//        Document doc = parser.build(gamesAndPlatforms);
//        romImportRecords = new ArrayList<>();
//        for (File file: files){
//            String filename = file.getPath();
//            String cleanFilename = FilenameService.cleanFilename(filename);
//            //String region = getFileRegion(filename.getKey());
//            String region = "(USA)";
//            romImportRecords.add(new RomImportRecord(file.getPath(),cleanFilename,region,platform,scrapeAsPlatform));
//        }
//        Elements files = doc.getRootElement().getChildElements("File");
//        for (Element file : files) {
//            for (RomImportRecord romImportRecord : romImportRecords) {
//                String gameTitle = file.getFirstChildElement("GameName").getValue();
//                String platform = file.getFirstChildElement("Platform").getValue();
//                if(gameTitle.toLowerCase().contains(romImportRecord.cleanFilename().toLowerCase())||gameTitle.toLowerCase().matches(romImportRecord.cleanFilename().toLowerCase())){
//
//                    results.put(romImportRecord.fullFilename(),new Pair<>(platform,gameTitle));
//                }
//            }
//        }
//    } catch (ParsingException e) {
//        System.out.println("Something went wrong parsing the document");
//    } catch (IOException e) {
//        System.out.println("Document not found!");
//    }



    public static void listChildren(Element current, int depth){
        printSpaces(depth);
        String data ="";
        if(current instanceof Element){
            Element temp = (Element) current;
            data = ": "+temp.getFirstChildElement("FileName").getValue();
        }
//        else if(current instanceof ProcessingInstruction){
//            ProcessingInstruction temp = (ProcessingInstruction) current;
//            data = ": " + temp.getTarget();
//        } else if(current instanceof DocType){
//            DocType temp = (DocType) current;
//            data = ": "+temp.getRootElementName();
//        } else if(current instanceof Text || current instanceof Comment){
//            String value = current.getValue();
//            value = value.replace('\n',' ').trim();
//            if (value.length() <= 20) data = ": " + value;
//            else data =": "+current.getValue().substring(0,17)+"...";
//        }
        //Attributes are never returned by getChild()
        System.out.println(current.getClass().getName()+data);
    }

    public static void printSpaces(int n){
        for (int i = 0; i < n; i++) {
            System.out.println(' ');
        }
    }

    public static List<String> getPlatforms() {
        ArrayList<String> platformList = new ArrayList<>();
        try{
            Builder parser = new Builder();
            Document doc = parser.build(platforms);
            Elements files = doc.getRootElement().getChildElements("Platform");
            for (Element platform : files) {
                System.out.println(platform.getQualifiedName());
                    String platformName = platform.getFirstChildElement("Name").getValue();
                platformList.add(platformName);
            }
        } catch (ParsingException e) {
            System.out.println("Something went wrong parsing the document");
        } catch (IOException e) {
            System.out.println("Document not found!");
        }
        System.out.println("Platforms Found during parsing: ");
        List<String> results = platformList.stream().sorted().toList();
        results.forEach(string-> System.out.println(string));
        return results;
    }

    public static Platform parsePlatform(String platform) {
        Platform platformObject = null;
        try{
            Builder parser = new Builder();
            Document doc = parser.build(platforms);
            Elements files = doc.getRootElement().getChildElements("Platform");
            for (Element element : files) {
                if(platform.equals(element.getFirstChildElement("Name").getValue())){
                    HashMap<String, LocalDate> releaseDate = new HashMap<>();
                    releaseDate.put("USA", LocalDate.parse(element.getFirstChildElement("ReleaseDate").getValue().substring(0,10)));
                    HashMap<String, String> specs = new HashMap<>();
                    specs.put("Cpu",element.getFirstChildElement("Cpu").getValue());
                    specs.put("Memory",element.getFirstChildElement("Memory").getValue());
                    specs.put("Graphics",element.getFirstChildElement("Graphics").getValue());
                    specs.put("Sound",element.getFirstChildElement("Sound").getValue());
                    specs.put("Display",element.getFirstChildElement("Display").getValue());
                    specs.put("MediaType",element.getFirstChildElement("Media").getValue());

                    platformObject = new Platform(element.getFirstChildElement("Name").getValue(),releaseDate,
                            element.getFirstChildElement("Developer").getValue(),element.getFirstChildElement("Notes").getValue(),
                            specs, Integer.valueOf(element.getFirstChildElement("MaxControllers").getValue()),element.getFirstChildElement("Category").getValue()
                    );
                }
            }
        } catch (ParsingException e) {
            System.out.println("Something went wrong parsing the document");
        } catch (IOException e) {
            System.out.println("Document not found!");
        }
        System.out.println("Platforms Found during parsing: ");
        System.out.println(platformObject.toString());
        return platformObject;
    }
}
