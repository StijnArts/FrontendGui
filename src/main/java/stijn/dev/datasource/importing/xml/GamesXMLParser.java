package stijn.dev.datasource.importing.xml;

import nu.xom.*;
import stijn.dev.datasource.importing.xml.runnables.*;
import stijn.dev.datasource.objects.items.*;
import stijn.dev.datasource.records.*;
import stijn.dev.threads.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class GamesXMLParser extends XMLParser{
    private static File metadata = new File(metadataFolder+"\\Metadata.xml");

    public ArrayBlockingQueue<Game> parseGames(List<RomImportRecord> romImportRecords,String importingAsPlatform, String scrapeAsPlatform){
        double startTime = System.currentTimeMillis();
        ArrayBlockingQueue<Game> results = new ArrayBlockingQueue<>(romImportRecords.size()*2);
        try{
            synchronized(results) {
                Builder parser = new Builder();
                Document doc = parser.build(metadata);
                Elements files = doc.getRootElement().getChildElements("Game");
                for (RomImportRecord rom : romImportRecords) {
                    GameParsingProcess gameParsingProcess = new GameParsingProcess(rom, files, results,importingAsPlatform,scrapeAsPlatform);
                    Thread thread = new Thread(gameParsingProcess);
                    ThreadManager.addParsingThread(thread);
                    thread.start();
                }
            }
        } catch (ParsingException e) {
            System.out.println("Something went wrong parsing the document");
        } catch (IOException e) {
            System.out.println("Document not found!");
        }
        double endTime = System.currentTimeMillis();
        System.out.println("Made "+ThreadManager.getParsingThreads().size()+" Parsing Threads.");
        ThreadManager.waitForParsingThreads();
        System.out.println("Parsing roms took: "+(endTime-startTime)+"ms for "+romImportRecords.size()+" roms.");
        System.out.println("Games Parsed: "+results.size());
        return results;
    }
}
