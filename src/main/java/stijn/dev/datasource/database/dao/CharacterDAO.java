package stijn.dev.datasource.database.dao;

import org.neo4j.driver.*;
import stijn.dev.datasource.database.*;
import stijn.dev.datasource.objects.items.*;
import stijn.dev.datasource.objects.items.Character;

import java.util.*;

public class CharacterDAO {
    private Neo4JDatabaseHelper neo4JDatabaseHelper = new Neo4JDatabaseHelper();
    public ArrayList<Character> getCharacters(HashMap<String, Object> parameters) {
        String characterQuery = "MATCH (g:Game{GameName:$gameName})-[:ON_PLATFORM]-(p:Platform{PlatformName:$platformName}), " +
                "(g)-[:FEATURED_IN]-(c:Character) RETURN c.StaffID, c.FirstName, c.LastName";
        Result result = neo4JDatabaseHelper.runQuery(new Query(characterQuery,parameters));
        ArrayList<Character> staff = new ArrayList<>();
        while(result.hasNext()) {
            Map<String, Object> row = result.next().asMap();
            staff.add(new Character(String.valueOf(row.get("c.StaffID")),String.valueOf(row.get("c.FirstName")),
                    String.valueOf(row.get("c.LastName"))));
        }
        return staff;
    }
}
