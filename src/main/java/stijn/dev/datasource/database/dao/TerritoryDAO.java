package stijn.dev.datasource.database.dao;

import org.neo4j.driver.*;
import stijn.dev.datasource.database.*;
import stijn.dev.datasource.objects.data.*;
import stijn.dev.datasource.objects.items.*;
import stijn.dev.util.*;

import java.time.*;
import java.util.*;

public class TerritoryDAO {
    private Neo4JDatabaseHelper neo4JDatabaseHelper = new Neo4JDatabaseHelper();
    public void createReleaseDates(GameImportItem gameImportItem) {
        for (String locale : gameImportItem.getReleaseDates().keySet()) {
            if(!gameImportItem.getReleaseDates().get(locale).isEqual(LocalDate.parse("9999-12-31"))){
                HashMap<String, Object> releaseDateParameters = new HashMap<>();
                releaseDateParameters.put("gameName", gameImportItem.getName());
                releaseDateParameters.put("platformName", gameImportItem.getPlatform());
                releaseDateParameters.put("releaseDate", gameImportItem.getReleaseDates().get(locale).toString());
                releaseDateParameters.put("locale", locale);
                neo4JDatabaseHelper.runQuery(new Query("MATCH (g:Game {GameName:$gameName})-[:ON_PLATFORM]-(p:Platform {PlatformName:$platformName}) " +
                        "MERGE (t:Territory {Name:$locale}) " +
                        "MERGE (g)-[:RELEASED_IN {ReleaseDate:$releaseDate}]->(t)",releaseDateParameters));
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
                    "MERGE (p)-[:RELEASED_IN {ReleaseDate:$releaseDate}]->(t)",releaseDateParameters));
        }
    }

    public ArrayList<ReleaseDate> getReleaseDates(HashMap<String, Object> parameters) {
        String releaseDateQuery = "MATCH (t:Territory)-[r:RELEASED_IN]-(g:Game{GameName:$gameName})-[:ON_PLATFORM]-(p:Platform{PlatformName:$platformName}) " +
                "RETURN t.Name, r.ReleaseDate";
        Result result = neo4JDatabaseHelper.runQuery(new Query(releaseDateQuery,parameters));
        ArrayList<ReleaseDate> releaseDates = new ArrayList<>();
        while(result.hasNext()) {
            Map<String, Object> row = result.next().asMap();
            releaseDates.add(new ReleaseDate(String.valueOf(row.get("t.Name")),
                    LocalDate.parse(String.valueOf(row.get("r.ReleaseDate")))));
        }
        return releaseDates;
    }

    public ArrayList<String> getTerritories(){
        String releaseDateQuery = "MATCH (t:Territory) RETURN t.Name";
        Result result = neo4JDatabaseHelper.runQuery(new Query(releaseDateQuery));
        ArrayList<String> territories = new ArrayList<>();
        while(result.hasNext()) {
            Map<String, Object> row = result.next().asMap();
            territories.add(String.valueOf(row.get("t.Name")));
        }
        return territories;
    }
}
