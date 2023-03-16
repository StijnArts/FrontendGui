package stijn.dev.datasource.database;

import stijn.dev.datasource.database.dao.*;
import stijn.dev.datasource.importing.xml.*;
import stijn.dev.datasource.objects.items.*;
import stijn.dev.resource.*;

import java.util.*;
import java.util.concurrent.*;
public class DatabaseHelper {
    private PlatformDAO platformDAO = new PlatformDAO();
    private PublisherDAO publisherDAO = new PublisherDAO();
    private TagDAO tagDAO = new TagDAO();
    private EsrbDAO esrbDAO = new EsrbDAO();
    private DeveloperDAO developerDAO = new DeveloperDAO();
    private GameDAO gameDAO = new GameDAO();
    private PlatformXMLParser platformXMLParser = new PlatformXMLParser();

    public void importRoms(ArrayBlockingQueue<Game> games){
        System.out.println("Importing Games into database...");
        ArrayList<String> platforms = new ArrayList<>();
        synchronized (games) {
            for (Game game : games) {
                if (!platforms.contains(game.getPlatform())) {
                    String platform = game.getPlatform();
                    platforms.add(platform);
                    if (!platformDAO.checkIfPlatformExists(platform)) {
                        importPlatform(platform);
                    }
                }
                importGame(game);
            }
        }
        FrontEndApplication.importProcessIsRunning = false;
        System.out.println("Finished Importing game(s).");
    }

    private void importGame(Game game) {
            gameDAO.createInDatabaseGame(game);
            publisherDAO.createPublisher(game);
            createESRBRating(game);
            developerDAO.createDeveloper(game);
            createGameTags(game);
        //TODO add file with gallery categories
    }

    private void createGameTags(Game game) {
        for (String tag : game.getTags()) {
            if(!"N/A".equals(tag)) {
                tagDAO.createTag(game,tag);
            }
        }
        if(game.isCooperative()){
            tagDAO.createTag(game,"Cooperative");
        }
    }

    private void createESRBRating(Game game) {
        if(!"N/A".equals(game.getESRBRating())){
            esrbDAO.createESRBRating(game);
        }
    }

    private void importPlatform(String platform) {
        Platform platformObject = platformXMLParser.parsePlatform(platform);
        platformDAO.createPlatform(platformObject);
        if(platformObject.getPublisher()!=null){
            publisherDAO.createPublisher(platformObject);
        }
    }
}
