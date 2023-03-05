package stijn.dev.data.database;

import java.io.*;
import java.util.*;

import com.google.common.collect.*;
import javafx.util.*;
import nu.xom.*;
import nu.xom.Builder;
import stijn.dev.records.*;
import stijn.dev.service.*;


public class XMLParser {
    private static String metadataFolder = "C:\\Users\\stijn\\Desktop\\Front End project\\Metadata";
    private static File metadata = new File(metadataFolder+"Metadata.xml");
    private static File platforms = new File(metadataFolder+"Platforms.xml");
    private static File gamesAndPlatforms = new File(metadataFolder+"\\Files.xml");
    private static File gameControllers = new File(metadataFolder+"GameControllers.xml");

    public static LinkedHashMultimap<String, Pair<String, String>> parseGames(String[] searchTerms){
        LinkedHashMultimap<String, Pair<String, String>> results = LinkedHashMultimap.create();
        try{
            Builder parser = new Builder();
            Document doc = parser.build(gamesAndPlatforms);
            Elements files = doc.getRootElement().getChildElements("File");
            for (Element file : files) {
                for (String searchTerm : searchTerms) {
                    String gameTitle = file.getFirstChildElement("GameName").getValue();
                    if(gameTitle.toLowerCase().contains(searchTerm)||gameTitle.toLowerCase().matches(searchTerm)){
                        String platform = file.getFirstChildElement("Platform").getValue();
                        results.put(searchTerm,new Pair<>(platform,gameTitle));
                    }
                }
            }
        } catch (ParsingException e) {
            System.out.println("Something went wrong parsing the document");
        } catch (IOException e) {
            System.out.println("Document not found!");
        }
        readResults(results);
        return results;
    }

    public static LinkedHashMultimap<String, Pair<String, String>> parseRoms(Pair<String,String>[] filenames){
        LinkedHashMultimap<String, Pair<String, String>> results = LinkedHashMultimap.create();
        try{
            Builder parser = new Builder();
            Document doc = parser.build(gamesAndPlatforms);
            List<RomImportRecord> romImportRecords = new ArrayList<>();
            for (Pair<String, String> file:
            filenames){
                String filename = file.getKey();
                String cleanFilename = FilenameService.cleanFilename(filename);
                //String region = getFileRegion(filename.getKey());
                String region = "(USA)";
                romImportRecords.add(new RomImportRecord(file.getKey(),cleanFilename,region,file.getValue()));
            }
            Elements files = doc.getRootElement().getChildElements("File");
            for (Element file : files) {
                for (RomImportRecord romImportRecord : romImportRecords) {
                    String gameTitle = file.getFirstChildElement("GameName").getValue();
                    String platform = file.getFirstChildElement("Platform").getValue();
                    if(gameTitle.toLowerCase().contains(romImportRecord.cleanFilename().toLowerCase())||gameTitle.toLowerCase().matches(romImportRecord.cleanFilename().toLowerCase())){

                        results.put(romImportRecord.fullFilename(),new Pair<>(platform,gameTitle));
                    }
                }
            }
        } catch (ParsingException e) {
            System.out.println("Something went wrong parsing the document");
        } catch (IOException e) {
            System.out.println("Document not found!");
        }
        readResults(results);
        return results;
    }

    private static void readResults(LinkedHashMultimap<String, Pair<String, String>> results) {
        for (String key :
                results.keySet()) {
            for (Pair<String, String> result :
                    results.get(key)) {
                System.out.println("Searchterm: " + key + ". Result: [Platform] " + result.getKey()+" [Game] "+result.getValue());
            }

        }
        if(results.isEmpty()){
            System.out.println("No results found!");
        }
    }

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
}
