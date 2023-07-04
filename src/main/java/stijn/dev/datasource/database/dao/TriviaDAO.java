package stijn.dev.datasource.database.dao;

import org.neo4j.driver.*;
import stijn.dev.datasource.database.*;
import stijn.dev.datasource.objects.items.*;

import java.util.*;

public class TriviaDAO {
    private Neo4JDatabaseHelper neo4JDatabaseHelper = new Neo4JDatabaseHelper();

    public ArrayList<Trivia> getTrivia(HashMap<String, Object> parameters){
        String triviaQuery = "MATCH (t:Trivia)-[:TRIVIA_ABOUT]-(g:Game) " +
                "WHERE ID(g) = $id " +
                "RETURN t.TriviaID, t.Fact ORDER BY t.TriviaID";
        Result result = neo4JDatabaseHelper.runQuery(new Query(triviaQuery,parameters));
        ArrayList<Trivia> trivia = new ArrayList<>();
        while(result.hasNext()) {
            Map<String, Object> row = result.next().asMap();
            trivia.add(new Trivia(String.valueOf(row.get("t.TriviaID")),String.valueOf(row.get("t.Fact"))));
        }
        return trivia;
    }

    public HashMap<String, String> getTrivia(){
        String triviaQuery = "MATCH (t:Trivia) RETURN t.TriviaID, t.Fact ORDER BY t.TriviaID";
        Result result = neo4JDatabaseHelper.runQuery(new Query(triviaQuery));
        HashMap<String, String> trivia = new HashMap<>();
        while(result.hasNext()) {
            Map<String, Object> row = result.next().asMap();
            trivia.put(String.valueOf(row.get("t.TriviaID")),String.valueOf(row.get("t.Fact")));
        }
        return trivia;
    }

    public void createTriviaEdge(HashMap<String, Object> triviaParameters) {
        String query = "MATCH (g:Game) " +
                "WHERE ID(g) = $id " +
                "WITH g " +
                "MERGE (t:Trivia {TriviaID:$triviaId}) " +
                "Set t.Fact = $fact " +
                "WITH g, t " +
                "MERGE (g)-[:TRIVIA_ABOUT]->(t)";
        /*System.out.println("MATCH (g:Game {GameName: \""+parameters.get("gameName")+"\"})-[:ON_PLATFORM]-(p:Platform {PlatformName: \""+parameters.get("platformName")+"\"}) \n" +
                "                WITH g \n" +
                "                MATCH (e:Rating {Rating: \""+parameters.get("rating")+"\", Organization: \""+parameters.get("organization")+"\"}) \n" +
                "                MERGE (g)-[:HAS_RATING]->(e)");*/
        neo4JDatabaseHelper.runQuery(new Query(query, triviaParameters));
    }

    public List<Trivia> getPlatformTrivia(int id) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("id" , id);
        String triviaQuery = "MATCH (t:Trivia)-[:TRIVIA_ABOUT]-(g:Platform) " +
                "WHERE ID(g) = $id " +
                "RETURN t.TriviaID, t.Fact ORDER BY t.TriviaID";
        Result result = neo4JDatabaseHelper.runQuery(new Query(triviaQuery,parameters));
        ArrayList<Trivia> trivia = new ArrayList<>();
        while(result.hasNext()) {
            Map<String, Object> row = result.next().asMap();
            trivia.add(new Trivia(String.valueOf(row.get("t.TriviaID")),String.valueOf(row.get("t.Fact"))));
        }
        return trivia;
    }
}
