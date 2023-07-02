package stijn.dev.datasource.database.dao;

import org.neo4j.driver.*;
import stijn.dev.datasource.database.*;
import stijn.dev.datasource.objects.items.*;

import java.util.*;

public class EmulatorDAO {
    private Neo4JDatabaseHelper neo4JDatabaseHelper = new Neo4JDatabaseHelper();

    public Emulator getDefaultEmulatorForPlatform(int platformId){
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("id",platformId);
        String query = "MATCH (p:Platform) " +
                "WHERE ID(p) = $id " +
                "WITH p " +
                "MATCH (p)-[d:DefaultEmulator]-(e:Emulator)" +
                "RETURN e.EmulatorName, e.Description, e.Path, e.DefaultLaunchParameters, d.LaunchParameters ORDER BY e.EmulatorName";
        Result result = neo4JDatabaseHelper.runQuery(new Query(query, parameters));
        Emulator emulator = new Emulator("","","","");
        while(result.hasNext()) {
            Map<String, Object> row = result.next().asMap();
            String args = row.get("e.DefaultLaunchParameters")+"";
            if(!row.get("d.LaunchParameters").equals("")){
                args = row.get("d.LaunchParameters")+"";
            }
            emulator = new Emulator(row.get("e.EmulatorName")+"",row.get("e.Path")+"",row.get("e.Description")+"",args);
        }
        return emulator;
    }

    public List<Emulator> getPlatformEmulators(int platformId) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("id",platformId);
        String query = "MATCH (p:Platform) " +
                "WHERE ID(p) = $id " +
                "WITH p " +
                "MATCH (p)-[d:Emulates]-(e:Emulator)" +
                "RETURN e.EmulatorName, e.Description, e.Path, e.DefaultLaunchParameters, d.LaunchParameters ORDER BY e.EmulatorName";
        Result result = neo4JDatabaseHelper.runQuery(new Query(query, parameters));
        List<Emulator> emulators = new ArrayList<>();
        while(result.hasNext()) {
            Map<String, Object> row = result.next().asMap();
            String args = row.get("e.DefaultLaunchParameters")+"";
            if(!row.get("d.LaunchParameters").equals("")){
                args = row.get("d.LaunchParameters")+"";
            }
            emulators.add(new Emulator(row.get("e.EmulatorName")+"",row.get("e.Path")+"",row.get("e.Description")+"",args));
        }
        return emulators;
    }

    public HashMap<String, Emulator> getEmulators() {
        String query = "MATCH (e:Platform) " +
                "RETURN e.EmulatorName, e.Description, e.Path, e.DefaultLaunchParameters ORDER BY e.EmulatorName";
        Result result = neo4JDatabaseHelper.runQuery(query);
        HashMap<String, Emulator> emulators = new HashMap<>();
        while(result.hasNext()) {
            Map<String, Object> row = result.next().asMap();
            if(row.get("e.EmulatorName")!=null){
                emulators.put(row.get("e.EmulatorName")+"",new Emulator(row.get("e.EmulatorName")+"",row.get("e.Path")+"",row.get("e.Description")+"",row.get("d.LaunchParameters")+""));
            }
        }
        return emulators;
    }
}
