package stijn.dev.datasource.database.dao;

import javafx.beans.property.*;
import org.neo4j.driver.*;
import stijn.dev.datasource.database.*;
import stijn.dev.datasource.objects.items.*;

import java.util.*;

public class TagDAO {

    private Neo4JDatabaseHelper neo4JDatabaseHelper = new Neo4JDatabaseHelper();
    public void createTag(GameImportItem gameImportItem, String tag){
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("gameName", gameImportItem.getName());
        parameters.put("platformName", gameImportItem.getPlatform());
        parameters.put("tag", tag);
        neo4JDatabaseHelper.runQuery(new Query("MATCH (g:Game {GameName:$gameName})-[:ON_PLATFORM]-(p:Platform {PlatformName:$platformName}) " +
                "MERGE (t:Tag {Name:$tag})" +
                "MERGE (g)-[:HAS_TAG]->(t)",
                parameters));
    }

    public ArrayList<Tag> getTags(HashMap<String, Object> parameters) {
        String tagQuery = "MATCH (t:Tag)-[:HAS_TAG]-(g:Game{GameName:$gameName})-[:ON_PLATFORM]-(p:Platform{PlatformName:$platformName}) RETURN t.Name";
        Result result = neo4JDatabaseHelper.runQuery(new Query(tagQuery,parameters));
        ArrayList<Tag> tags = new ArrayList<>();
        while(result.hasNext()) {
            Map<String, Object> row = result.next().asMap();
            tags.add(new Tag(String.valueOf(row.get("t.Name"))));
        }
        return tags;
    }

    public ArrayList<String> getTags() {
        String tagQuery = "MATCH (t:Tag) RETURN t.Name";
        Result result = neo4JDatabaseHelper.runQuery(new Query(tagQuery));
        ArrayList<String> tags = new ArrayList<>();
        while(result.hasNext()) {
            Map<String, Object> row = result.next().asMap();
            tags.add(String.valueOf(row.get("t.Name")));
        }
        return tags;
    }


}
