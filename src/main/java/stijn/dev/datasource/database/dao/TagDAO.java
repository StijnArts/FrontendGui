package stijn.dev.datasource.database.dao;

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

    public void createTag(HashMap<String, Object> parameters){
        Result result = neo4JDatabaseHelper.runQuery(new Query(
                "MATCH (t:Tag) " +
                        "WHERE ID(t) = $id " +
                        "SET t.Description = $description, t.SortingTitle = $sortingTitle, t.Name = $tag " +
                        "RETURN t",
                parameters));
        if(!result.hasNext()){
            neo4JDatabaseHelper.runQuery(new Query(
                    "CREATE (t:Tag {Description : $description, SortingTitle : $sortingTitle, Name : $tag})",
                    parameters));
        }

    }

    public ArrayList<Tag> getTags(HashMap<String, Object> parameters) {
        String tagQuery = "MATCH (t:Tag)-[:HAS_TAG]-(g:Game) " +
                "WHERE ID(g) = $id " +
                "RETURN t.Name";
        Result result = neo4JDatabaseHelper.runQuery(new Query(tagQuery,parameters));
        ArrayList<Tag> tags = new ArrayList<>();
        while(result.hasNext()) {
            Map<String, Object> row = result.next().asMap();
            tags.add(new Tag(String.valueOf(row.get("t.Name"))));
        }
        return tags;
    }

    public ArrayList<Tag> getTags(int id) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("id", id);
        String tagQuery = "MATCH (t:Tag)-[:HAS_TAG]-(g:Game) " +
                "WHERE ID(g) = $id " +
                "RETURN t.Name";
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

    public ArrayList<DetailedTagItem> getDetailedTags() {
        String tagQuery = "MATCH (t:Tag) RETURN ID(t), t.Name, t.Description, t.SortingTitle";
        Result result = neo4JDatabaseHelper.runQuery(new Query(tagQuery));
        ArrayList<DetailedTagItem> tags = new ArrayList<>();
        while(result.hasNext()) {
            Map<String, Object> row = result.next().asMap();
            tags.add(new DetailedTagItem(String.valueOf(row.get("ID(t)")), String.valueOf(row.get("t.Name")),String.valueOf(row.get("t.Description")),String.valueOf(row.get("t.SortingTitle"))));
        }
        return tags;
    }


    public void createTagEdge(HashMap<String, Object> tagParameters) {
        String query = "MATCH (g:Game) " +
                "WHERE ID(g) = $id " +
                "WITH g " +
                "MERGE (t:Tag {Name:$tag}) " +
                "WITH g, t " +
                "MERGE (g)-[:HAS_TAG]->(t)";
        neo4JDatabaseHelper.runQuery(new Query(query, tagParameters));
    }

    public void deleteTag(int name) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("id", name);
        String tagQuery = "MATCH (t:Tag) " +
                "WHERE ID(t) = $id " +
                "DETACH Delete t";
        neo4JDatabaseHelper.runQuery(new Query(tagQuery,parameters));
    }
}
