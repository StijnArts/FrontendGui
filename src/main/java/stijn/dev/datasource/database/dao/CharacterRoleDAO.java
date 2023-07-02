package stijn.dev.datasource.database.dao;

import org.neo4j.driver.*;
import stijn.dev.datasource.database.*;

import java.util.*;

public class CharacterRoleDAO {
    private Neo4JDatabaseHelper neo4JDatabaseHelper = new Neo4JDatabaseHelper();

    public ArrayList<String> getCharacterRoles() {
        String staffQuery = "MATCH (s:CharacterRole) RETURN s.Name ORDER BY s.Name";
        Result result = neo4JDatabaseHelper.runQuery(new Query(staffQuery));
        ArrayList<String> staff = new ArrayList<>();
        while(result.hasNext()) {
            Map<String, Object> row = result.next().asMap();
            staff.add(String.valueOf(row.get("s.Name")));
        }
        return staff;
    }

    public ArrayList<String> getCharacterRoles(HashMap<String, Object> parameters) {
        String staffQuery = "MATCH (s:Character{CharacterID:$characterID})-[f:FEATURED_IN]-(g:Game{GameName:$gameName})-[:ON_PLATFORM]-(p:Platform{PlatformName:$platformName}) " +
                "RETURN f.Role ORDER BY s.Role";
        Result result = neo4JDatabaseHelper.runQuery(new Query(staffQuery,parameters));
        ArrayList<String> roles = new ArrayList<>();
        while(result.hasNext()) {
            Map<String, Object> row = result.next().asMap();
            roles.add(String.valueOf(row.get("f.Role")));
        }
        return roles;
    }
}
