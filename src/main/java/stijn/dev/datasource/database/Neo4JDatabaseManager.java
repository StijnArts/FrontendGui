//package stijn.dev.datasource.database;
//import org.neo4j.dbms.api.*;
//import org.neo4j.graphdb.*;
//
//import java.io.*;
//
//import static stijn.dev.resource.FrontEndApplication.DEFAULT_DATABASE_NAME;
//
//public class Neo4JDatabaseManager {
//
//
//    private DatabaseManagementService managementService = new DatabaseManagementServiceBuilder(new File("C:\\Users\\Stijn\\.Neo4jDesktop\\projects").toPath()).build();
//    private GraphDatabaseService graphDb = managementService.database(DEFAULT_DATABASE_NAME);
//    public void startDatabase(){
//        if(!managementService.listDatabases().contains(managementService.database(DEFAULT_DATABASE_NAME))){
//            managementService.createDatabase(DEFAULT_DATABASE_NAME);
//        }
//        managementService.startDatabase(DEFAULT_DATABASE_NAME);
//        registerShutdownHook(managementService);
//    }
//
//    private static void registerShutdownHook( final DatabaseManagementService managementService )
//    {
//        // Registers a shutdown hook for the Neo4j instance so that it
//        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
//        // running application).
//        Runtime.getRuntime().addShutdownHook( new Thread()
//        {
//            @Override
//            public void run()
//            {
//                managementService.shutdown();
//            }
//        } );
//    }
//}
