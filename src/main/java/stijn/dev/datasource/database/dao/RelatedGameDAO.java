package stijn.dev.datasource.database.dao;

import org.neo4j.driver.*;
import stijn.dev.datasource.database.*;
import stijn.dev.datasource.objects.data.*;

import java.util.*;

public class RelatedGameDAO {
    private Neo4JDatabaseHelper neo4JDatabaseHelper = new Neo4JDatabaseHelper();
    public ArrayList<RelatedGame> getRelatedGames(HashMap<String, Object> parameters) {
        String characterQuery = "MATCH (p:Platform)-[:ON_PLATFORM]-(g:Game)-[r:RELATED_TO]-(o:Game) " +
                "WHERE ID(o) = $id " +
                "Return ID(g), g.GameName, p.PlatformName, r.RelationType, r.Description";
        Result result = neo4JDatabaseHelper.runQuery(new Query(characterQuery,parameters));
        ArrayList<RelatedGame> relatedGames = new ArrayList<>();
        while(result.hasNext()) {
            Map<String, Object> row = result.next().asMap();
            relatedGames.add(new RelatedGame(String.valueOf(row.get("ID(g)")),String.valueOf(row.get("g.GameName")),String.valueOf(row.get("p.PlatformName")),
                    String.valueOf(row.get("r.RelationType")), String.valueOf(row.get("r.Description"))));
        }
        return relatedGames;
    }

    public void createRelatedGameEdge(HashMap<String, Object> relatedGameParameters) {
        String query = "MATCH (originalGame:Game), (relatedGame:Game)" +
                "WHERE ID(originalGame) = $id " +
                "WITH originalGame " +
                "MATCH (relatedGame:Game) " +
                "WHERE ID(relatedGame) = $relatedGameId " +
                "WITH originalGame, relatedGame " +
                "MERGE (relatedGame)-[:RELATED_TO {RelationType:$relationshipType, Description:$relationshipDescription}]->(originalGame)";
        neo4JDatabaseHelper.runQuery(new Query(query, relatedGameParameters));
    }
}
