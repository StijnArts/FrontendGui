package stijn.dev.datasource.database.dao;

import stijn.dev.datasource.importing.xml.*;
import stijn.dev.datasource.objects.items.*;
import stijn.dev.resource.*;
import stijn.dev.resource.controllers.*;

import java.util.*;
import java.util.concurrent.*;
public class ImportDAO {
    private PlatformDAO platformDAO = new PlatformDAO();
    private PublisherDAO publisherDAO = new PublisherDAO();
    private TagDAO tagDAO = new TagDAO();
    private RatingDAO ratingDAO = new RatingDAO();
    private DeveloperDAO developerDAO = new DeveloperDAO();
    private GameImportItemDAO gameImportItemDAO = new GameImportItemDAO();
    private PlatformXMLParser platformXMLParser = new PlatformXMLParser();

    public void importRoms(ArrayBlockingQueue<GameImportItem> gameImportItems){
        System.out.println("Importing Games into database...");
        //TODO option for force importing duplicate games
        //TODO use the created Id for identification in creating all the nodes
        ArrayList<String> platforms = new ArrayList<>();
        synchronized (gameImportItems) {
            for (GameImportItem gameImportItem : gameImportItems) {
                if (!platforms.contains(gameImportItem.getPlatform())) {
                    String platform = gameImportItem.getPlatform();
                    platforms.add(platform);
                    if (!platformDAO.platformExists(platform)) {
                        importPlatform(platform);
                    }
                }
                importGame(gameImportItem);
            }
        }
        FrontEndApplication.importProcessIsRunning = false;
        MainController.gamesHaveBeenAdded.set(true);
        System.out.println("Finished Importing game(s).");
    }

    private void importGame(GameImportItem gameImportItem) {
            gameImportItemDAO.createInDatabaseGame(gameImportItem);
            publisherDAO.createPublisher(gameImportItem);
            createESRBRating(gameImportItem);
            developerDAO.createDeveloper(gameImportItem);
            createGameTags(gameImportItem);
        //TODO add file with gallery categories
    }

    private void createGameTags(GameImportItem gameImportItem) {
        for (String tag : gameImportItem.getTags()) {
            if(!"N/A".equals(tag)) {
                tagDAO.createTag(gameImportItem,tag);
            }
        }
        if(gameImportItem.isCooperative()){
            tagDAO.createTag(gameImportItem,"Cooperative");
        }
    }

    private void createESRBRating(GameImportItem gameImportItem) {
        if(!"N/A".equals(gameImportItem.getESRBRating())){
            ratingDAO.createRating(gameImportItem);
        }
    }

    private void importPlatform(String platform) {
        Platform platformObject = platformXMLParser.parsePlatform(platform);
        platformDAO.savePlatform(platformObject);
        if(platformObject.getPublishers()!=null){
            publisherDAO.createPublisher(platformObject.getPlatformName(), platformObject.getPublishers());
        }
        if(platformObject.getManufacturers()!=null){
            ManufacturerDAO manufacturerDAO = new ManufacturerDAO();
            manufacturerDAO.createManufacturer(platformObject.getPlatformName(), platformObject.getManufacturers());
        }
        System.out.println("Help Im looping");
    }
}
