package stijn.dev.data.database;

import org.neo4j.driver.*;
import stijn.dev.data.objects.items.*;
import stijn.dev.resource.*;

import java.time.*;
import java.util.*;

//TODO split into multiple DAO
public class DatabaseHelper {
    private static Neo4JDatabaseHelper neo4JDatabaseHelper = new Neo4JDatabaseHelper();
    public static void importRoms(List<Game> games){
        String platform = games.get(0).getPlatform();
        String query = "Match (platform:Platform {PlatformName:'"+platform+"'}) RETURN platform.PlatformName";
        System.out.println("Running Query: "+ query);
        Result systemExistsCheckResult = neo4JDatabaseHelper.runQuery(query);
        boolean systemExistsInDatabase = systemExistsCheckResult.hasNext();
        System.out.println("Does the System Already Exist in the Database: "+systemExistsInDatabase);
        if(!systemExistsInDatabase){
            importSystem(platform);
        }
        importGame(games);
        FrontEndApplication.importProcessIsRunning = false;
        System.out.println("Finished Importing game(s).");
    }

    private static void importGame(List<Game> games) {
        for (Game game : games) {
            createGame(game);
            createPublisher(game);
            createDeveloper(game);
            createGameTags(game);
        }

        //TODO add file with gallery categories
    }

    private static void createGameTags(Game game) {
        for (String tag : game.getTags()) {
            if(!"N/A".equals(tag)) {
                HashMap<String, Object> parameters = new HashMap<>();
                parameters.put("gameName", game.getName());
                parameters.put("platformName",game.getPlatform());
                parameters.put("tag", tag);
                neo4JDatabaseHelper.runQuery(new Query("MATCH (g:Game {GameName:$gameName})-[:ON_PLATFORM]-(p:Platform {PlatformName:$platformName}) " +
                        "MERGE (t:Tag {Name:$tag})" +
                        "MERGE (g)-[:HAS_TAG]->(t)",
                        parameters));
            }
        }
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("gameName", game.getName());
        parameters.put("platformName",game.getPlatform());
        if(game.isCooperative()){
            neo4JDatabaseHelper.runQuery(new Query("MATCH (g:Game {GameName:$gameName})-[:ON_PLATFORM]-(p:Platform {PlatformName:$platformName}) " +
                    "MERGE (t:Tag {Name:'Cooperative'})" +
                    "MERGE (g)-[:HAS_TAG]->(t)",
                    parameters));
        }
        if(!"N/A".equals(game.getESRBRating())){
            parameters.put("rating", game.getESRBRating());
            neo4JDatabaseHelper.runQuery(new Query("MATCH (g:Game {GameName:$gameName})-[:ON_PLATFORM]-(p:Platform {PlatformName:$platformName}) " +
                    "MERGE (e:ESRBRating {Rating:$rating})" +
                    "MERGE (g)-[:HAS_RATING]->(e)",
                    parameters));
        }
    }

    private static void createGame(Game game){
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("gameName",game.getName());
        parameters.put("gameId",game.getGameId());
        parameters.put("platformName",game.getPlatform());
        parameters.put("gamePlatform",game.getPlatform());
        String queryString = "MATCH (p:Platform {PlatformName:$gamePlatform}) " +
                "MERGE r = (g:Game {GameName:$gameName, " +
                "GameId:$gameId, Description:$description, CommunityRating:$communityRating, " +
                "CommunityRatingCount:$communityRatingCount})" +
                " SET g.GamePath =$gamePath " +
                "MERGE (g)-[:ON_PLATFORM]->(p) Return r";
        if(!neo4JDatabaseHelper.runQuery(new Query(
                "Match (g:Game {GameName:$gameName, GameId:$gameId}), (p:Platform {PlatformName:$platformName})" +
                        "MATCH (g)-[r:ON_PLATFORM]->(p) RETURN r",parameters)).hasNext()){
            queryString = "MATCH (p:Platform {PlatformName:$gamePlatform}) " +
                    "CREATE r = (g:Game {GameName:$gameName, GamePath:$gamePath, " +
                    "GameId:$gameId, Description:$description, CommunityRating:$communityRating, " +
                    "CommunityRatingCount:$communityRatingCount}) " +
                    "MERGE (g)-[:ON_PLATFORM]->(p) Return r";
        }
        parameters = new HashMap<>();
        parameters.put("gameName",game.getName());
        parameters.put("gameId",game.getGameId());
        parameters.put("description",game.getDescription());
        parameters.put("gamePath",game.getPath());
        parameters.put("communityRating",game.getCommunityRating());
        parameters.put("communityRatingCount",game.getCommunityRatingCount());
        parameters.put("gamePlatform",game.getPlatform());
        Query query = new Query(queryString, parameters);
        neo4JDatabaseHelper.runQuery(query);
        for (String locale : game.getReleaseDates().keySet()) {
            if(!game.getReleaseDates().get(locale).isEqual(LocalDate.parse("9999-12-31"))){
                HashMap<String, Object> releaseDateParameters = new HashMap<>();
                releaseDateParameters.put("gameName", game.getName());
                releaseDateParameters.put("platformName",game.getPlatform());
                releaseDateParameters.put("releaseDate", game.getReleaseDates().get(locale).toString());
                releaseDateParameters.put("locale", locale);
                neo4JDatabaseHelper.runQuery(new Query("MATCH (g:Game {GameName:$gameName})-[:ON_PLATFORM]-(p:Platform {PlatformName:$platformName}) " +
                        "MERGE (t:Territory {Name:$locale}) " +
                        "MERGE (g)-[:RELEASED_IN {Territory:$releaseDate}]->(t)",releaseDateParameters));
            }
        }
    }

    private static void importSystem(String platform) {
        Platform platformObject = XMLParser.parsePlatform(platform);
        createSystem(platformObject);
        if(platformObject.getPublisher()!=null){
            createPublisher(platformObject);
        }

    }

    private static void createSystem(Platform platformObject) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("platformName", platformObject.getPlatformName());
        String queryString = "CREATE r = (p:Platform {PlatformName:$platformName";
        if(platformObject.getDescription()!=null){
            parameters.put("description", platformObject.getDescription());
            queryString+=", Description:$description";
        }
        if(platformObject.getMaxPlayers()!=null){
            parameters.put("maxPlayers", platformObject.getDescription());
            queryString+=", MaxPlayers:$maxPlayers";
        }
        if(platformObject.getCategory()!=null){
            parameters.put("category", platformObject.getDescription());
            queryString+=", Category:$category";
        }
        queryString += "})";
        for (String spec : platformObject.getSpecs().keySet()) {
            queryString +=", (p)-[:"+spec+" {spec:$"+spec+"}]->(p)";
            parameters.put(spec, platformObject.getSpecs().get(spec));
        }
        queryString += " Return r";
        Query query = new Query(queryString, parameters);
        Result platformCreateResult = neo4JDatabaseHelper.runQuery(query);
        for (String locale : platformObject.getReleaseDates().keySet()) {
            HashMap<String, Object> releaseDateParameters = new HashMap<>();
            releaseDateParameters.put("platformName", platformObject.getPlatformName());
            releaseDateParameters.put("releaseDate", platformObject.getReleaseDates().get(locale).toString());
            releaseDateParameters.put("locale", locale);
            neo4JDatabaseHelper.runQuery(new Query("MATCH (p:Platform {PlatformName:$platformName})" +
                    "MERGE (t:Territory {Name:$locale}) " +
                    "MERGE (p)-[:RELEASED_IN {Territory:$releaseDate}]->(t)",releaseDateParameters));
        }
    }

    private static void createPublisher(Platform platform) {
        HashMap<String, Object> parameters = new HashMap<>();

        parameters.put("publisherName",platform.getPublisher().trim());
        parameters.put("platformName", platform.getPlatformName());
            String queryString = "Match (platform:Platform {PlatformName:$platformName}) " +
                    "MERGE (n:Publisher {PublisherName:$publisherName})" +
                    "MERGE r = (n)<-[:MADE_BY]-(platform) Return r";
            Query query = new Query(queryString, parameters);
            neo4JDatabaseHelper.runQuery(query);
    }

    private static void createPublisher(Game game) {
        if(!game.getPublisher().equals("N/A")) {
            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("publisherName", game.getPublisher().trim());
            parameters.put("gameName", game.getName());
            parameters.put("platformName", game.getPlatform());
                String queryString = "MATCH (game:Game {GameName:$gameName})-[:ON_PLATFORM]-(p:Platform {PlatformName:$platformName}) " +
                        "MERGE (n:Publisher {PublisherName:$publisherName}) " +
                        "MERGE r = (n)<-[:PUBLISHED_BY]-(game) Return r";
                Query query = new Query(queryString, parameters);
                neo4JDatabaseHelper.runQuery(query);
            }

    }

    private static void createDeveloper(Game game) {
        if(!game.getDeveloper().equals("N/A")){
            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("developerName",game.getDeveloper().trim());
            parameters.put("gameName", game.getName());
            parameters.put("platformName",game.getPlatform());
                String queryString = "Match (game:Game {GameName:$gameName})-[:ON_PLATFORM]-(p:Platform {PlatformName:$platformName}) " +
                        "MERGE (n:Developer {DeveloperName:$developerName}) " +
                        "MERGE r = (n)<-[:MADE_BY]-(game) Return r";
                Query query = new Query(queryString, parameters);
                neo4JDatabaseHelper.runQuery(query);
            }

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
