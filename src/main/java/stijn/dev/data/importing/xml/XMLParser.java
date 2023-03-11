package stijn.dev.data.importing.xml;

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
import stijn.dev.data.importing.xml.interfaces.*;
import stijn.dev.data.objects.items.*;
import stijn.dev.records.*;
import stijn.dev.service.*;


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
        System.out.println(current.getClass().getName()+data);
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
