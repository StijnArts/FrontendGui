package stijn.dev.data.database;

import org.neo4j.driver.*;
import stijn.dev.data.objects.items.*;
import stijn.dev.records.*;

import java.util.*;

import static org.neo4j.driver.Values.parameters;

public class DatabaseHelper {
    private static Neo4JDatabaseHelper neo4JDatabaseHelper = new Neo4JDatabaseHelper();
    private static List<RomImportRecord> romImportRecords;

    public static List<RomImportRecord> getRomImportRecords() {
        return romImportRecords;
    }

    private static void setRomImportRecords(List<RomImportRecord> romImportRecordsIn) {
        romImportRecords = romImportRecordsIn;
    }

    public static void importRoms(List<RomDatabaseRecord> romImportRecordsIn){
        //setRomImportRecords(romImportRecordsIn);
        String platform = romImportRecords.get(0).platform().getValue();
        String query = "Match (platform:Platform {PlatformName:'"+platform+"'}) RETURN platform.PlatformName";
        System.out.println("Running Query: "+ query);
        Result systemExistsCheckResult = neo4JDatabaseHelper.runQuery(query);
        boolean systemExistsInDatabase = systemExistsCheckResult.hasNext();
        System.out.println("Does the System Already Exist in the Database: "+systemExistsInDatabase);
        if(!systemExistsInDatabase){
            importSystem(platform);
        }
        //ArrayList<Game> games =
        //importGame(games);
    }

    private static void importGame(ArrayList<Game> games) {
        //TODO add image scrapers
        //TODO add relationship 'GAME_ON_PLATFORM' with Platform
        //TODO add relationship 'GAME_PUBLISHED_BY' with Publisher
        //TODO add relationship 'RELEASE_DATE_OF' with DATE and relationship DATE - 'RELEASED_IN' - TERRITORY
        //TODO add relationship 'STAFF_WORKED_ON' with STAFF
    }

    private static void importSystem(String platform) {
        Platform platformObject = XMLParser.parsePlatform(platform);
        createSystem(platformObject);
        importPublisher(platformObject);
    }

    private static void createSystem(Platform platformObject) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("platformName", platformObject.getPlatformName());
        parameters.put("description", platformObject.getDescription());
        parameters.put("summary", platformObject.getSummary());
        parameters.put("sortingTitle", platformObject.getPlatformName());
        parameters.put("maxPlayers", platformObject.getMaxPlayers());
        parameters.put("category", platformObject.getCategory());
        String queryString = "CREATE r = (p:Platform {PlatformName:$platformName, " +
                "Description:$description, MaxPlayers:$maxPlayers, Category:$category})";
        for (String spec : platformObject.getSpecs().keySet()) {
            queryString +=", (p)-[:"+spec+" {spec:$"+spec+"}]->(p)";
            parameters.put(spec, platformObject.getSpecs().get(spec));
        }
        queryString += " Return r";
        Query query = new Query(queryString, parameters);
        System.out.println("Running Query: "+ query);
        Result platformCreateResult = neo4JDatabaseHelper.runQuery(query);
        for (String locale : platformObject.getReleaseDates().keySet()) {
            HashMap<String, Object> releaseDateParameters = new HashMap<>();
            releaseDateParameters.put("platformName", platformObject.getPlatformName());
            releaseDateParameters.put("releaseDate"+locale, platformObject.getReleaseDates().get(locale).toString());
            neo4JDatabaseHelper.runQuery(new Query("MATCH (p:Platform {PlatformName:$platformName}) MERGE (p)-[:RELEASED_ON]->(:Date " +
                    "{Date: $releaseDate"+locale+"})-[:RELEASED_IN]->(:Territory {Name:'"+locale+"'})",
                    releaseDateParameters));
        }
    }

    private static void importPublisher(Platform platform) {
        String publisher = platform.getPublisher();
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("platformName", platform.getPlatformName());
        parameters.put("publisherName",platform.getPublisher());
        //TODO proper scraper implementation
        String queryString = "Match (platform:Platform {PlatformName:$platformName}) "+
                    "MERGE r = (n:Publisher {PublisherName:$publisherName})<-[:MADE_BY]-(platform) Return r";
        Query query = new Query(queryString,parameters);
        Result createPublisherResult = neo4JDatabaseHelper.runQuery(query);
        HashMap<String, Object> resultHashmap = resultToHashMap(createPublisherResult);
        readHashMap(resultHashmap);
    }

    private static void readHashMap(HashMap<String, Object> resultHashmap) {
        for (String key :
                resultHashmap.keySet()) {
            System.out.println(key+": "+resultHashmap.get(key).toString());
        }
    }

    private static HashMap<String, Object> resultToHashMap(Result result){
        HashMap<String, Object> resultHashmap = new HashMap<>();
        while(result.hasNext()){
            Map<String, Object> row = result.next().asMap();
            for (String key: row.keySet()) {
                resultHashmap.put(key,row.get(key));
            }
        }
        return resultHashmap;
    }
}
