package stijn.dev.resource.service;

import javafx.stage.*;
import org.neo4j.driver.*;
import stijn.dev.datasource.database.*;
import stijn.dev.datasource.database.dao.*;
import stijn.dev.datasource.objects.items.*;
import stijn.dev.resource.controllers.*;

import java.util.*;

public class EditService {
    private Neo4JDatabaseHelper neo4JDatabaseHelper = new Neo4JDatabaseHelper();
    private DeveloperDAO developerDAO = new DeveloperDAO();
    private PublisherDAO publisherDAO = new PublisherDAO();
    private PriorityDataDAO priorityDataDAO = new PriorityDataDAO();
    private RatingDAO ratingDAO = new RatingDAO();
    private PlaymodeDAO playmodeDAO = new PlaymodeDAO();
    private AlternateNameDAO alternateNameDAO = new AlternateNameDAO();
    private TerritoryDAO territoryDAO = new TerritoryDAO();
    private RelatedGameDAO relatedGameDAO = new RelatedGameDAO();
    private RelationshipDAO relationshipDAO = new RelationshipDAO();
    private GameDAO gameDAO = new GameDAO();
    private AdditionalAppDAO additionalAppDAO = new AdditionalAppDAO();
    private TriviaDAO triviaDAO = new TriviaDAO();
    private StaffDAO staffDAO = new StaffDAO();
    private StaffRoleDAO staffRoleDAO = new StaffRoleDAO();
    private CharacterDAO characterDAO = new CharacterDAO();
    private CharacterRoleDAO characterRoleDAO = new CharacterRoleDAO();

    public void saveChanges(Game changedGame, Game originalGame, Stage stage, EditController editController) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("gameName", originalGame.getName());
        parameters.put("platformName", originalGame.getPlatform());
        String coreChangeQuery = "MATCH (g:Game{GameName:$gameName})-[:ON_PLATFORM]-(p:Platform{PlatformName:$platformName}) ";
        saveDevelopers(parameters, editController);
        savePublishers(parameters, editController);
        saveRatings(parameters, editController);
        savePlayModes(parameters, editController);

//        saveAlternateName();
//        saveRelatedGames();
//        saveAdditionalApps();
//        saveTrivia();
//        saveStaff();
//        saveCharacters();
//        saveTags();
//        saveReleaseDates();
        //Has to be done last
        saveGeneral(coreChangeQuery, parameters,changedGame);
        //savePlatformAndTitle();
        stage.close();
    }
    private void savePlayModes(HashMap<String, Object> parameters, EditController editController) {
        String query = "MATCH (g:Game{GameName:$gameName})-[:ON_PLATFORM]-(p:Platform{PlatformName:$platformName}), (g)-[r:HAS_RATING]-(:Rating) DELETE r";
        neo4JDatabaseHelper.runQuery(new Query(query,parameters));
        for (String playMode : editController.getPlayModesComboCheckbox().getValue().itemProperty().getValue().split("; ")) {
            HashMap<String, Object> playModeParameters = new HashMap<>();
            playModeParameters.putAll(parameters);
            playModeParameters.put("name",playMode);
            playmodeDAO.createPlayMode(playModeParameters);
        }
    }

    private void saveRatings(HashMap<String, Object> parameters, EditController editController) {
        String query = "MATCH (g:Game{GameName:$gameName})-[:ON_PLATFORM]-(p:Platform{PlatformName:$platformName}), (g)-[r:HAS_RATING]-(:Rating) DELETE r";
        neo4JDatabaseHelper.runQuery(new Query(query,parameters));
        String[] ratings = editController.getRatingComboCheckBox().getValue().itemProperty().getValue().split("; ");
        for (String rating : ratings) {
            HashMap<String, Object> ratingParameters = new HashMap<>();
            ratingParameters.putAll(parameters);
            ratingParameters.put("organization",rating.split(": ")[0]);
            ratingParameters.put("rating",rating.split(": ")[1]);
            ratingDAO.createRating(ratingParameters);
        }
    }

    private void saveDevelopers(HashMap<String, Object> parameters, EditController editController) {
        String query = "MATCH (g:Game{GameName:$gameName})-[:ON_PLATFORM]-(p:Platform{PlatformName:$platformName}), (g)-[r:MADE_BY]-(:Developer) DELETE r";
        neo4JDatabaseHelper.runQuery(new Query(query,parameters));
        for (String developer : editController.getDeveloperComboCheckBox().getValue().itemProperty().getValue().split("; ")) {
            if(!developer.isBlank()&&!developer.trim().equals(";")){
                HashMap<String, Object> developerParameters = new HashMap<>();
                developerParameters.putAll(parameters);
                developerParameters.put("developerName",developer.trim());
                developerDAO.createDeveloper(developerParameters);
            }
        }
    }

    private void savePublishers(HashMap<String, Object> parameters, EditController editController) {
        String query = "MATCH (g:Game{GameName:$gameName})-[:ON_PLATFORM]-(p:Platform{PlatformName:$platformName}), (g)-[r:PUBLISHED_BY]-(:Publisher) DELETE r";
        neo4JDatabaseHelper.runQuery(new Query(query,parameters));
        for (String publisher : editController.getPublisherComboCheckBox().getValue().itemProperty().getValue().split("; ")) {
            if(!publisher.isBlank()&&!publisher.trim().equals(";")){
                HashMap<String, Object> publisherParameters = new HashMap<>();
                publisherParameters.putAll(parameters);
                publisherParameters.put("publisherName",publisher.trim());
                publisherDAO.createPublisher(publisherParameters);
            }
        }
    }

    private void saveGeneral(String coreChangeQuery, HashMap<String, Object> parameters, Game game) {
        parameters.put("newGameName", game.getName());
        parameters.put("newPriority", game.getPriority().getName());
        parameters.put("newMaxPlayers", game.getMaxPlayers());
        parameters.put("newSortingTitle", game.getSortingTitle());
        parameters.put("newDefaultSummary", game.getSummary());
        parameters.put("newDescription", game.getDescription());
        String updateQuery = coreChangeQuery;
        updateQuery = updateQuery + "Set ";
        updateQuery = updateQuery +"g.Name:$newGameName, ";
        updateQuery = updateQuery +"g.Priority:$newPriority, ";
        updateQuery = updateQuery +"g.MaxPlayers:$newMaxPlayers, ";
        updateQuery = updateQuery +"g.DefaultSortingTitle:$newSortingTitle, ";
        updateQuery = updateQuery +"g.DefaultSummary:$newDefaultSummary, ";
        updateQuery = updateQuery +"g.Description:$newDescription";
        neo4JDatabaseHelper.runQuery(new Query(updateQuery,parameters));
        //TODO developer, publisher, ratings, playmodes, (preexisting changes), platform in that order
        System.out.println(updateQuery);
    }
}
