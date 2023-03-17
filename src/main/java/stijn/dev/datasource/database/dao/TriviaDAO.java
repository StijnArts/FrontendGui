package stijn.dev.datasource.database.dao;

import org.neo4j.driver.*;
import stijn.dev.datasource.database.*;
import stijn.dev.datasource.objects.items.*;

import java.util.*;

public class TriviaDAO {
    private Neo4JDatabaseHelper neo4JDatabaseHelper = new Neo4JDatabaseHelper();

    public ArrayList<Trivia> getTrivia(HashMap<String, Object> parameters){
        String triviaQuery = "MATCH (g:Game{GameName:$gameName})-[:ON_PLATFORM]-(p:Platform{PlatformName:$platformName}), " +
                "(g)-[:TRIVIA_ABOUT]-(t:Trivia) RETURN t.TriviaID, t.Fact";
        Result result = neo4JDatabaseHelper.runQuery(new Query(triviaQuery,parameters));
        ArrayList<Trivia> trivia = new ArrayList<>();
        while(result.hasNext()) {
            Map<String, Object> row = result.next().asMap();
            trivia.add(new Trivia(String.valueOf(row.get("t.TriviaID")),String.valueOf(row.get("t.Fact"))));
        }
        return trivia;
    }
}
