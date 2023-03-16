package stijn.dev.datasource.database.dao;

import org.neo4j.driver.*;
import stijn.dev.datasource.database.*;
import stijn.dev.datasource.objects.items.*;

import java.util.*;

public class PublisherDAO {
    private Neo4JDatabaseHelper neo4JDatabaseHelper = new Neo4JDatabaseHelper();
    public void createPublisher(Platform platform) {
        boolean publisherExists = false;
        while(!publisherExists) {
            HashMap<String, Object> parameters = new HashMap<>();

            parameters.put("publisherName", platform.getPublisher().trim());
            parameters.put("platformName", platform.getPlatformName());
            String queryString = "MATCH (d:Developer {DeveloperName:$publisherName}) " +
                    "SET d:Publisher, d.PublisherName = $publisherName " +
                    "MERGE (d)<-[:MADE_BY]-(platform) Return d";
            Result result = neo4JDatabaseHelper.runQuery(new Query(queryString, parameters));
            if (!result.hasNext()) {
                queryString = "Match (platform:Platform {PlatformName:$platformName}) " +
                        "MERGE (n:Publisher {PublisherName:$publisherName})" +
                        "MERGE r = (n)<-[:MADE_BY]-(platform) Return r";
                Query query = new Query(queryString, parameters);
                neo4JDatabaseHelper.runQuery(query);
            }
                String checkExistsQuery = "MATCH (pu:Publisher {PublisherName:$publisherName})<-[r:MADE_BY]-(platform:Platform {PlatformName:$platformName})" +
                        "Return r";
                Query checkExists = new Query(checkExistsQuery, parameters);
                Result exists = neo4JDatabaseHelper.runQuery(checkExists);
                publisherExists = exists.hasNext();

        }
    }

    public void createPublisher(GameImportItem gameImportItem) {
        if(!gameImportItem.getPublisher().equals("N/A")) {
            boolean publisherExists = false;
            while(!publisherExists){
                HashMap<String, Object> parameters = new HashMap<>();
                parameters.put("publisherName", gameImportItem.getPublisher().trim());
                parameters.put("gameId", gameImportItem.getGameId());
                parameters.put("gameName", gameImportItem.getName());
                parameters.put("platformName", gameImportItem.getPlatform());
                String queryString = "MATCH (d:Developer {DeveloperName:$publisherName}), (game:Game {GameName:$gameName, GameId:$gameId})-[:ON_PLATFORM]-(p:Platform {PlatformName:$platformName}) " +
                        "SET d:Publisher, d.PublisherName = $publisherName " +
                        "MERGE (d)<-[:PUBLISHED_BY]-(game) Return d";
                Result result = neo4JDatabaseHelper.runQuery(new Query(queryString, parameters));
                if (!result.hasNext()) {
                    queryString = "MATCH (game:Game {GameName:$gameName, GameId:$gameId})-[:ON_PLATFORM]-(p:Platform {PlatformName:$platformName}) " +
                            "MERGE (n:Publisher {PublisherName:$publisherName}) " +
                            "MERGE r = (n)<-[:PUBLISHED_BY]-(game) Return r";
                    Query query = new Query(queryString, parameters);
                    neo4JDatabaseHelper.runQuery(query);
                }
                String checkExistsQuery = "MATCH (pu:Publisher {PublisherName:$publisherName})<-[r:PUBLISHED_BY]-(game:Game {GameName:$gameName, GameId:$gameId})-[:ON_PLATFORM]-(p:Platform {PlatformName:$platformName})" +
                        "Return r";
                Query checkExists = new Query(checkExistsQuery, parameters);
                Result exists = neo4JDatabaseHelper.runQuery(checkExists);
                publisherExists = exists.hasNext();
            }
        }
    }
}
