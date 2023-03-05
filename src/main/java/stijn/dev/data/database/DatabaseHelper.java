package stijn.dev.data.database;

public class DatabaseHelper {
/**
    static final String dbName = "_system";

    static private ArangoDB arangoDB;
    public DatabaseHelper(){
        arangoDB = new ArangoDB.Builder().user("root").serializer(new ArangoJack()).build();
    }

    public class Documents {
        private static BaseDocument createDocument(String collectionName, String key, Map<String,String> attributes){
            BaseDocument document = new BaseDocument();
            document.setKey("key");
            attributes.forEach(document::addAttribute);
            try {
                arangoDB.db(dbName).collection(collectionName).insertDocument(document);
                System.out.println("Document created");
            } catch (final ArangoDBException e){
                System.err.println("Failed to create document. " + e.getMessage());
            }
            return document;
        }

        private static BaseDocument readDocument(String key, String collectionName){
            BaseDocument document;
            try {
                document = arangoDB.db(dbName).collection(collectionName).getDocument(key, BaseDocument.class);

            } catch (final ArangoDBException e){
                System.err.println("Failed to get document: " + key + "; " + e.getMessage());
                Map<String, String> att = new HashMap();
                att.put("null","null");
                document = createDocument(collectionName, key, att);
            }
            return document;
        }

        private static BaseDocument addDocumentAttribute(BaseDocument document, Map<String,String> attributes){
            try {
                attributes.forEach(document::addAttribute);
            } catch (ArangoDBException e){
                System.err.println("Failed to update document. " + e.getMessage());
            }
            return document;
        }

        private static BaseDocument updateDocumentAttribute(BaseDocument document, String key, String value){
            try{
                document.updateAttribute(key, value);
            } catch (ArangoDBException e){
                System.err.println("Failed to update document. " + e.getMessage());
            }
            return document;
        }

        private static void deleteDocument(String collection, String key){
            try {
                arangoDB.db(dbName).collection(collection).deleteDocument(key);
            } catch (final ArangoDBException e) {
                System.err.println("Failed to delete document. " + e.getMessage());
            }
        }

        private static void deleteDocument(BaseDocument document){
            try {
                String collection = document.getId().replace("/"+document.getKey(), "");
                arangoDB.db(dbName).collection(collection).deleteDocument(document.getKey());
            } catch (final ArangoDBException e) {
                System.err.println("Failed to delete document. " + e.getMessage());
            }
        }
    }

    public class Collections{
        private static void createCollection(String collectionName){
            try{
                CollectionEntity collection = arangoDB.db(dbName).createCollection(collectionName);
            } catch (final ArangoDBException e) {
                System.err.println("Failed to create collection: " + collectionName + "; " + e.getMessage());
            }
        }

        private static void createCollection(String collectionName, int numberOfShards){
            try{
                CollectionEntity collection = arangoDB.db(dbName).createCollection(collectionName, new CollectionCreateOptions().numberOfShards(numberOfShards));
            } catch (final ArangoDBException e) {
                System.err.println("Failed to create collection: " + collectionName + "; " + e.getMessage());
            }
        }
    }


    public static List<BaseDocument> queryDocuments(String query){
        List<BaseDocument> results = new ArrayList<>();
        try{
            ArangoCursor<BaseDocument> cursor = arangoDB.db(dbName).query(query, BaseDocument.class);
            results = cursor.asListRemaining();
        } catch (final ArangoDBException e) {
            System.err.println("Failed to execute query. " + e.getMessage());
        }
        return results;
    }
    **/












}
