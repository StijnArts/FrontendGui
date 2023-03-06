package stijn.dev.data.database;

import java.io.*;
import java.time.*;
import java.util.*;

import com.google.common.base.*;
import com.google.common.collect.*;
import javafx.beans.property.*;
import javafx.util.*;
import nu.xom.*;
import nu.xom.Builder;
import stijn.dev.data.objects.items.*;
import stijn.dev.records.*;
import stijn.dev.service.*;


public class XMLParser {
    private static String metadataFolder = "C:\\Users\\stijn\\Desktop\\Front End project\\Metadata";
    private static File metadata = new File(metadataFolder+"\\Metadata.xml");
    private static File platforms = new File(metadataFolder+"\\Platforms.xml");
    private static File gamesAndPlatforms = new File(metadataFolder+"\\Files.xml");
    private static File gameControllers = new File(metadataFolder+"\\GameControllers.xml");

    public static ArrayList<RomDatabaseComparingRecord> parseGames(List<RomImportRecord> romImportRecords){
        ArrayList<RomDatabaseComparingRecord> results = new ArrayList<>();
        try{
            Builder parser = new Builder();
            Document doc = parser.build(gamesAndPlatforms);
            Elements files = doc.getRootElement().getChildElements("Game");
            for (RomImportRecord rom : romImportRecords) {
                HashMap<StringProperty, StringProperty> databaseId = new HashMap<>();
                for (Element file : files) {
                    String gameTitle = file.getFirstChildElement("Name").getValue();
                    if(gameTitle.toLowerCase().contains(rom.title().getValue())||gameTitle.toLowerCase().matches(rom.title().getValue())){
                        databaseId.put(new SimpleStringProperty(file.getFirstChildElement("DatabaseID").getValue()),new SimpleStringProperty(gameTitle));
                    }
                }
                if(databaseId.isEmpty()){
                    databaseId.put(new SimpleStringProperty("Not Found"),new SimpleStringProperty("Not Found"));
                }
                results.add(new RomDatabaseComparingRecord(rom, databaseId));
            }
        } catch (ParsingException e) {
            System.out.println("Something went wrong parsing the document");
        } catch (IOException e) {
            System.out.println("Document not found!");
        }
        results.forEach(game-> System.out.println(game.toString()));
        return results;
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
