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
        List<PlatformSpecification> specs = new ArrayList<>();
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
                    specs.add(new PlatformSpecification("Cpu",readElement(element.getFirstChildElement("Cpu"))));
                    specs.add(new PlatformSpecification("Memory",readElement(element.getFirstChildElement("Memory"))));
                    specs.add(new PlatformSpecification("Graphics",readElement(element.getFirstChildElement("Graphics"))));
                    specs.add(new PlatformSpecification("Sound",readElement(element.getFirstChildElement("Sound"))));
                    specs.add(new PlatformSpecification("Display",readElement(element.getFirstChildElement("Display"))));
                    specs.add(new PlatformSpecification("MediaType",readElement(element.getFirstChildElement("Media"))));

                    platformObject = new Platform(readElement(element.getFirstChildElement("Name")),readElement(element.getFirstChildElement("Name")),List.of(releaseDate),
                            List.of(readElement(element.getFirstChildElement("Developer"))), List.of(readElement(element.getFirstChildElement("Manufacturer"))),readElement(element.getFirstChildElement("Notes")),
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
            specs.add(new PlatformSpecification("Cpu",""));
            specs.add(new PlatformSpecification("Memory",""));
            specs.add(new PlatformSpecification("Graphics",""));
            specs.add(new PlatformSpecification("Sound",""));
            specs.add(new PlatformSpecification("Display",""));
            specs.add(new PlatformSpecification("MediaType",""));
            platformObject = new Platform(platform, platform,List.of(new ReleaseDate("USA", null)),List.of(), List.of(),null,specs,null,null);
        }
        return platformObject;
    }
}
