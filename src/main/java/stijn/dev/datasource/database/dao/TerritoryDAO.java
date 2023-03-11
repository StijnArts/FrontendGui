package stijn.dev.datasource.database.dao;

import org.neo4j.driver.*;
import stijn.dev.datasource.database.*;
import stijn.dev.datasource.objects.items.*;

import java.time.*;
import java.util.*;

public class TerritoryDAO {
    private Neo4JDatabaseHelper neo4JDatabaseHelper = new Neo4JDatabaseHelper();
    public void createReleaseDates(Game game) {
        for (String locale : game.getReleaseDates().keySet()) {
            if(!game.getReleaseDates().get(locale).isEqual(LocalDate.parse("9999-12-31"))){
                HashMap<String, Object> releaseDateParameters = new HashMap<>();
                releaseDateParameters.put("gameName", game.getName());
                releaseDateParameters.put("platformName", game.getPlatform());
                releaseDateParameters.put("releaseDate", game.getReleaseDates().get(locale).toString());
                releaseDateParameters.put("locale", locale);
                neo4JDatabaseHelper.runQuery(new Query("MATCH (g:Game {GameName:$gameName})-[:ON_PLATFORM]-(p:Platform {PlatformName:$gamePlatform}) " +
                        "MERGE (t:Territory {Name:$locale}) " +
                        "MERGE (g)-[:RELEASED_IN {Territory:$releaseDate}]->(t)",releaseDateParameters));
            }
        }
    }

    public void createReleaseDates(Platform platformObject) {
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
}
