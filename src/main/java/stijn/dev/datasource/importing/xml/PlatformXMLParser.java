package stijn.dev.datasource.importing.xml;

import nu.xom.*;
import stijn.dev.datasource.objects.items.*;

import java.io.*;
import java.time.*;
import java.util.*;

public class PlatformXMLParser extends XMLParser{
    private static File platforms = new File(metadataFolder+"\\Platforms.xml");
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

    public Platform parsePlatform(String platform) {
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

                    platformObject = new Platform(readElement(element.getFirstChildElement("Name")),releaseDate,
                            readElement(element.getFirstChildElement("Developer")),readElement(element.getFirstChildElement("Notes")),
                            specs, readElement(element.getFirstChildElement("MaxControllers")),readElement(element.getFirstChildElement("Category"))
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
