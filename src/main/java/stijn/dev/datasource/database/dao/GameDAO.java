package stijn.dev.datasource.database.dao;

import org.neo4j.driver.*;
import stijn.dev.datasource.database.*;
import stijn.dev.datasource.objects.data.*;
import stijn.dev.datasource.objects.items.*;

import java.util.*;

public class GameDAO {
    private Neo4JDatabaseHelper neo4JDatabaseHelper = new Neo4JDatabaseHelper();
    private TriviaDAO triviaDAO = new TriviaDAO();
    private PublisherDAO publisherDAO = new PublisherDAO();
    private DeveloperDAO developerDAO = new DeveloperDAO();
    private TagDAO tagDAO = new TagDAO();
    private TerritoryDAO territoryDAO = new TerritoryDAO();
    private StaffDAO staffDAO = new StaffDAO();
    private AlternateNameDAO alternateNameDAO = new AlternateNameDAO();
    private CharacterDAO characterDAO = new CharacterDAO();
    private RelatedGameDAO relatedGameDAO = new RelatedGameDAO();
    private AdditionalAppDAO additionalAppDAO = new AdditionalAppDAO();
    private RatingDAO ratingDAO = new RatingDAO();
    private PriorityDataDAO priorityDataDAO = new PriorityDataDAO();
    private PlaymodeDAO playmodeDAO = new PlaymodeDAO();
    private PlatformDAO platformDAO = new PlatformDAO();
    public ArrayList<Game> getGames(int offset, String searchTerm){
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("offset", offset);
        String query = "Match (g:Game)-[:ON_PLATFORM]-(p:Platform) ";
        if(!searchTerm.isEmpty()){
            parameters.put("searchTerm", searchTerm.trim().toLowerCase());
            query += "WHERE toLower(g.GameName) STARTS WITH $searchTerm OR toLower(g.GameName) ENDS WITH $searchTerm OR toLower(g.GameName) CONTAINS $searchTerm " +
                    "OR toLower(g.DefaultSortingTitle) STARTS WITH $searchTerm OR toLower(g.DefaultSortingTitle) ENDS WITH $searchTerm OR toLower(g.DefaultSortingTitle) CONTAINS $searchTerm ";
        }
        query +="Return ID(g), g.GameName, g.GameId, g.Description, g.Theme, g.DefaultSummary, g.DefaultSortingTitle, " +
                "g.LaunchParameters, g.GamePath, g.CommunityRating, g.CommunityRatingCount, g.MaxPlayers, g.HLTBStory, g.HLTBStoryAndExtra, g.HLTBCompletionist, p.PlatformName " +
                "ORDER BY g.DefaultSortingTitle " +
                "SKIP $offset " +
                "LIMIT 21";
        Result result = neo4JDatabaseHelper.runQuery(new Query(query, parameters));
        ArrayList<Game> gameItems = new ArrayList<>();
        while(result.hasNext()){
            Map<String, Object> row = result.next().asMap();
            Game game = createGameFromDatabase(row);
            HashMap<String, Object> gameParameters = new HashMap<>();
            gameParameters.put("gameName",game.getName());
            gameParameters.put("id", Integer.valueOf(game.getDatabaseId()));
            gameItems.add(game);
            //Metadata tab
            game.setPlaymodes(playmodeDAO.getPlayModes(gameParameters));
            game.setSortingTitle(String.valueOf(row.get("g.DefaultSortingTitle")));
            game.setPriority(priorityDataDAO.getPriority(gameParameters));
            game.setSummary(String.valueOf(row.get("g.DefaultSummary")));
            game.setTrivia(triviaDAO.getTrivia(gameParameters));
            game.setPublisher(publisherDAO.getPublishers(gameParameters));
            game.setDeveloper(developerDAO.getDevelopers(gameParameters));
            game.setTags(tagDAO.getTags(gameParameters));
            game.setReleaseDates(territoryDAO.getReleaseDates(gameParameters));
            game.setStaff(staffDAO.getStaff(gameParameters));
            game.setRatings(ratingDAO.getRatings(gameParameters));
            game.setCharacters(characterDAO.getCharacters(gameParameters));
            game.setAlternateNames(alternateNameDAO.getAlternateNames(gameParameters));
            game.setRelatedGames(relatedGameDAO.getRelatedGames(gameParameters));
            game.setAdditionalApps(additionalAppDAO.getAdditionalApps(gameParameters));
            //Collection Tab
            String seriesQuery;
            String franchiseQuery;

            //TODO write queries to get all relationships for games
            //System.out.println(game);
        }
        return gameItems;
    }

    public int getGameCount(String searchTerm) {
        HashMap<String, Object> parameters = new HashMap<>();
        String query = "Match (g:Game)-[:ON_PLATFORM]-(p:Platform) ";
        if(!searchTerm.isEmpty()){
            parameters.put("searchTerm", searchTerm.trim().toLowerCase());
            query += "WHERE toLower(g.GameName) STARTS WITH $searchTerm OR toLower(g.GameName) ENDS WITH $searchTerm OR toLower(g.GameName) CONTAINS $searchTerm " +
                    "OR toLower(g.DefaultSortingTitle) STARTS WITH $searchTerm OR toLower(g.DefaultSortingTitle) ENDS WITH $searchTerm OR toLower(g.DefaultSortingTitle) CONTAINS $searchTerm ";
        }
        query +="RETURN count(g) as count";
        Result result = neo4JDatabaseHelper.runQuery(new Query(query, parameters));
        int gameCount = 0;
        while(result.hasNext()){
            Map<String, Object> row = result.next().asMap();
            gameCount = Integer.parseInt(row.get("count")+"");
        }
        return gameCount;
    }

    public ArrayList<RelatedGameEntry> getRelatedGamesOptions() {
        String query = "Match (g:Game)-[:ON_PLATFORM]-(p:Platform) " +
                "Return ID(g), g.GameName, p.PlatformName";
        Result result = neo4JDatabaseHelper.runQuery(query);
        ArrayList<RelatedGameEntry> gameItems = new ArrayList<>();
        while (result.hasNext()) {
            Map<String, Object> row = result.next().asMap();
            gameItems.add(new RelatedGameEntry(String.valueOf(row.get("ID(g)")),String.valueOf(row.get("g.GameName")), String.valueOf(row.get("p.PlatformName"))));
        }
        return gameItems;
    }

    private Game createGameFromDatabase(Map<String, Object> row){
        return new Game(String.valueOf(row.get("ID(g)")), String.valueOf(row.get("g.GameName")), String.valueOf(row.get("g.GamePath")), String.valueOf(row.get("g.GameId")),
                String.valueOf(row.get("g.Description")), String.valueOf(row.get("g.MaxPlayers")),
                String.valueOf(row.get("p.PlatformName")), String.valueOf(row.get("g.CommunityRating")), String.valueOf(row.get("g.CommunityRatingCount")));
    }


}
