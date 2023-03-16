package stijn.dev.datasource.importing.xml;

import java.util.*;

import nu.xom.*;
import stijn.dev.datasource.importing.xml.interfaces.*;
import stijn.dev.datasource.records.*;


public class XMLParser implements IElementReader {
    protected static String metadataFolder = "C:\\Users\\stijn\\Desktop\\Front End project\\Metadata";
//    private static File gamesAndPlatforms = new File(metadataFolder+"\\Files.xml");
//    private static File gameControllers = new File(metadataFolder+"\\GameControllers.xml");

    public static void listChildren(Element current, int depth){
        printSpaces(depth);
        String data ="";
        if(current instanceof Element){
            Element temp = (Element) current;
            data = ": "+temp.getFirstChildElement("FileName").getValue();
        }
    }

    public static void printSpaces(int n){
        for (int i = 0; i < n; i++) {
            System.out.println(' ');
        }
    }

    protected static void readResults(List<RomImportRecord> romImportRecords) {
        romImportRecords.stream().forEach(romImportRecord -> System.out.println(romImportRecord));
    }
}
