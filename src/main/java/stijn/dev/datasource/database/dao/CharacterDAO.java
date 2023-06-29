package stijn.dev.datasource.database.dao;

import org.neo4j.driver.*;
import stijn.dev.datasource.database.*;
import stijn.dev.datasource.objects.items.Character;

import java.util.*;

public class CharacterDAO {
    private Neo4JDatabaseHelper neo4JDatabaseHelper = new Neo4JDatabaseHelper();
    private CharacterRoleDAO characterRoleDAO = new CharacterRoleDAO();
    //TODO make roles an array that is filled with the roles of that character for the game.
    public ArrayList<Character> getCharacters(HashMap<String, Object> parameters) {
        String characterQuery = "MATCH (c:Character)-[f:FEATURED_IN]-(g:Game) " +
                "WHERE ID(g) = $id " +
                "RETURN c.CharacterID, c.Name, f.VoiceActor, f.Role";
        Result result = neo4JDatabaseHelper.runQuery(new Query(characterQuery,parameters));
        ArrayList<Character> characters = new ArrayList<>();
        ArrayList<String> addedCharacters = new ArrayList<>();
        while(result.hasNext()) {
            Map<String, Object> row = result.next().asMap();
            if(!addedCharacters.contains(String.valueOf(row.get("s.CharacterID")))) {
                HashMap<String, Object> characterParameters = new HashMap<>();
                characterParameters.put("characterID", String.valueOf(row.get("c.CharacterID")));
                characterParameters.putAll(parameters);
                characters.add(new Character(String.valueOf(row.get("c.CharacterID")), String.valueOf(row.get("c.Name")),
                        String.valueOf(row.get("f.Role")), String.valueOf(row.get("f.VoiceActor"))));
                addedCharacters.add(String.valueOf(row.get("c.CharacterID")));
            }
        }
        return characters;
    }

    public ArrayList<Character> getCharacters() {
        String characterQuery = "MATCH (c:Character) RETURN c.CharacterID, c.Name";
        Result result = neo4JDatabaseHelper.runQuery(new Query(characterQuery));
        ArrayList<Character> staff = new ArrayList<>();
        while(result.hasNext()) {
            Map<String, Object> row = result.next().asMap();
            staff.add(new Character(String.valueOf(row.get("c.CharacterID")),String.valueOf(row.get("c.Name"))));
        }
        return staff;
    }

    public void createCharacterEdge(HashMap<String, Object> characterParameters) {
        String query = "MATCH (g:Game) " +
                "WHERE ID(g) = $id " +
                "WITH g " +
                "MERGE (c:Character {CharacterID:$characterId}) " +
                "Set c.Name = $characterName " +
                "WITH g, c " +
                "MERGE (g)<-[:FEATURED_IN {Role:$role}]-(c)";
        neo4JDatabaseHelper.runQuery(new Query(query, characterParameters));
    }

    public void createCharacterEdgeWithVoiceActor(HashMap<String, Object> characterParameters) {
        String query = "MATCH (g:Game) " +
                "WHERE ID(g) = $id " +
                "WITH g " +
                "MERGE (c:Character {CharacterID:$characterId}) " +
                "Set c.Name = $characterName " +
                "WITH g, c " +
                "MERGE (g)<-[:FEATURED_IN {Role:$role, VoiceActor:$voiceActor}]-(c)";
        neo4JDatabaseHelper.runQuery(new Query(query, characterParameters));
    }
}
