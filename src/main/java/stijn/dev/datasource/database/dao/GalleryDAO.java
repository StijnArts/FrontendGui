package stijn.dev.datasource.database.dao;

import org.neo4j.driver.*;
import stijn.dev.datasource.database.*;
import stijn.dev.datasource.objects.data.enums.*;
import stijn.dev.datasource.objects.items.*;

import java.util.*;

public class GalleryDAO {
    Neo4JDatabaseHelper neo4JDatabaseHelper = new Neo4JDatabaseHelper();


    public List<String> getPlatformGalleryTypes() {
        String triviaQuery = "MATCH (m:GalleryCategory)-[:USED_BY_PLATFORMS]-() " +
                "RETURN m.UniqueName ORDER BY m.UniqueName";
        Result result = neo4JDatabaseHelper.runQuery(triviaQuery);
        ArrayList<String> manufacturers = new ArrayList<>();
        while(result.hasNext()) {
            Map<String, Object> row = result.next().asMap();
            manufacturers.add(String.valueOf(row.get("m.UniqueName")));
        }
        return manufacturers;
    }

    public int getGalleryCount(String selectedItem, int id) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("selectedItem",selectedItem);
        parameters.put("id", id);
        String query =
                "Match (g:GalleryItem)-[:BELONGS_TO]-(c:GalleryCategory {UniqueName:$selectedItem}) " +
                "WITH g " +
                "MATCH (g)-[:PART_OF_GALLERY_OF]-(p) " +
                "WHERE ID(p) = $id " +
                "RETURN count(g) as count";
        Result result = neo4JDatabaseHelper.runQuery(new Query(query, parameters));
        int gameCount = 0;
        while(result.hasNext()){
            Map<String, Object> row = result.next().asMap();
            gameCount = Integer.parseInt(row.get("count")+"");
        }
        return gameCount;
    }

    public List<GalleryItem> getMedia(int offset, int id, String selectedItem) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("selectedItem",selectedItem);
        parameters.put("id", id);
        parameters.put("offset", offset);
        String query =
                        "Match (g:GalleryItem)-[:BELONGS_TO]-(c:GalleryCategory {UniqueName:$selectedItem}) " +
                        "WITH g " +
                        "MATCH (g)-[:PART_OF_GALLERY_OF]-(p) " +
                        "WHERE ID(p) = $id " +
                        "RETURN ID(g), g.Name, g.FilePath, g.ItemType " +
                                "SKIP $offset " +
                                "LIMIT 12 ";
        Result result = neo4JDatabaseHelper.runQuery(new Query(query, parameters));
        ArrayList<GalleryItem> galleryItems = new ArrayList<>();
        while(result.hasNext()){
            Map<String, Object> row = result.next().asMap();
            galleryItems.add(new GalleryItem(Integer.parseInt(row.get("ID(g)")+""),row.get("g.FilePath")+"", row.get("g.Name")+"",MediaTypes.valueOf((row.get("g.ItemType")+"").toUpperCase())));
        }
        return galleryItems;
    }

    public void createGalleryNode(GalleryItem galleryItem, int id, String category) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("name", galleryItem.getName());
        parameters.put("id", id);
        parameters.put("filePath", galleryItem.getFile().getAbsolutePath());
        parameters.put("itemType", galleryItem.getMediaType().getType());
        parameters.put("category", category);
        String query =
                "MERGE (g:GalleryItem {Name: $name, FilePath:$filePath, ItemType:$itemType}) " +
                        "WITH g " +
                        "MATCH (c:GalleryCategory {UniqueName:$category}) " +
                        "MERGE (g)-[:BELONGS_TO]->(c) " +
                        "WITH g " +
                        "MATCH (p) " +
                        "WHERE ID(p) = $id " +
                        "MERGE (g)-[:PART_OF_GALLERY_OF]->(p) ";
        neo4JDatabaseHelper.runQuery(new Query(query, parameters));
    }

    public void removeFromGallery(int itemId, int id) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("id", id);
        parameters.put("itemId", itemId);
        String query =
                "Match (g:GalleryItem)-[r:PART_OF_GALLERY_OF]-(i)" +
                        "WHERE ID(i) = $id AND ID(g)=$itemId " +
                        "DELETE r";
        neo4JDatabaseHelper.runQuery(new Query(query, parameters));
    }
}
