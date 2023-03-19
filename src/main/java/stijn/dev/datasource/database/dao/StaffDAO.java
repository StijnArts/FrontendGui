package stijn.dev.datasource.database.dao;

import org.neo4j.driver.*;
import stijn.dev.datasource.database.*;
import stijn.dev.datasource.objects.items.*;

import java.util.*;

public class StaffDAO {
    private StaffRoleDAO staffRoleDAO = new StaffRoleDAO();
    private Neo4JDatabaseHelper neo4JDatabaseHelper = new Neo4JDatabaseHelper();
    //TODO make roles an array that is filled with the roles of the staff for the game.
    public ArrayList<Staff> getStaff(HashMap<String, Object> parameters) {
        String staffQuery = "MATCH (s:Staff)-[w:WORKED_ON]-(g:Game{GameName:$gameName})-[:ON_PLATFORM]-(p:Platform{PlatformName:$platformName}) " +
                "RETURN s.StaffID, s.FirstName, s.LastName, w.Role";
        Result result = neo4JDatabaseHelper.runQuery(new Query(staffQuery,parameters));
        ArrayList<Staff> staff = new ArrayList<>();
        while(result.hasNext()) {
            Map<String, Object> row = result.next().asMap();
                HashMap<String, Object> staffParameters = new HashMap<>();
                staffParameters.put("staffID",String.valueOf(row.get("s.StaffID")));
                staffParameters.putAll(parameters);
                staff.add(new Staff(String.valueOf(row.get("s.StaffID")),String.valueOf(row.get("s.FirstName")),
                        String.valueOf(row.get("s.LastName")),String.valueOf(row.get("w.Role"))));
        }
        return staff;
    }

    public ArrayList<Staff> getStaff() {
        String staffQuery = "MATCH (s:Staff) RETURN s.StaffID, s.FirstName, s.LastName";
        Result result = neo4JDatabaseHelper.runQuery(new Query(staffQuery));
        ArrayList<Staff> staff = new ArrayList<>();
        while(result.hasNext()) {
            Map<String, Object> row = result.next().asMap();
            staff.add(new Staff(String.valueOf(row.get("s.StaffID")),String.valueOf(row.get("s.FirstName")),
                    String.valueOf(row.get("s.LastName"))));
        }
        return staff;
    }

    public ArrayList<String> getStaffIDs() {
        String staffQuery = "MATCH (s:Staff) RETURN s.StaffID, s.FirstName, s.LastName";
        Result result = neo4JDatabaseHelper.runQuery(new Query(staffQuery));
        ArrayList<String> staff = new ArrayList<>();
        while(result.hasNext()) {
            Map<String, Object> row = result.next().asMap();
            staff.add(String.valueOf(row.get("s.StaffID")));
        }
        return staff;
    }
}
