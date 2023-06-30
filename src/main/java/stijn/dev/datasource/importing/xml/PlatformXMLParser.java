package stijn.dev.datasource.importing.xml;

import nu.xom.*;
import stijn.dev.datasource.objects.data.*;
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
                     ReleaseDate releaseDate = new ReleaseDate("USA", null);
                    if(element.getFirstChildElement("ReleaseDate")!=null){
                        releaseDate = new ReleaseDate("USA", LocalDate.parse(element.getFirstChildElement("ReleaseDate").getValue().substring(0,10)));
                    }
                    specs.put("Cpu",readElement(element.getFirstChildElement("Cpu")));
                    specs.put("Memory",readElement(element.getFirstChildElement("Memory")));
                    specs.put("Graphics",readElement(element.getFirstChildElement("Graphics")));
                    specs.put("Sound",readElement(element.getFirstChildElement("Sound")));
                    specs.put("Display",readElement(element.getFirstChildElement("Display")));
                    specs.put("MediaType",readElement(element.getFirstChildElement("Media")));

                    platformObject = new Platform(readElement(element.getFirstChildElement("Name")),readElement(element.getFirstChildElement("Name")),List.of(releaseDate),
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
            specs.put("Cpu","");
            specs.put("Memory","");
            specs.put("Graphics","");
            specs.put("Sound","");
            specs.put("Display","");
            specs.put("MediaType","");
            platformObject = new Platform(platform, platform,List.of(new ReleaseDate("USA", null)),null,null,specs,null,null);
        }
        return platformObject;
    }
}
