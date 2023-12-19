package mapify.mapify.Database;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.InsertManyResult;
import mapify.mapify.Models.User;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class MongoDBController {

    private String connectionString = System.getenv("MONGODB_URI");
    private String databaseName = System.getenv("DATABASE_NAME");
    private String collectionName = System.getenv("COLLECTION_NAME");
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    public MongoDBController() throws Exception{
        this.mongoClient = MongoClients.create(connectionString);
        this.database = mongoClient.getDatabase(databaseName);
        this.collection  = database.getCollection(collectionName);
    }

    public void InsertUsers(List<User> users, double fileId) {
        List<Document> usersDocuments = new ArrayList<>();
        for (User user : users) {
            Document userLocation = new Document()
                    .append("lat", user.getAddressLocation().longitude())
                    .append("lng", user.getAddressLocation().longitude());
            Document doc = new Document("_id", new ObjectId())
                    .append("first_name",user.getFirstName())
                    .append("last_name",user.getLastName())
                    .append("address",user.getAddress())
                    .append("phone_number",user.getPhoneNumber())
                    .append("location",userLocation)
                    .append("file_id", fileId);
            usersDocuments.add(doc);
        }
        collection.insertMany(usersDocuments);
    }
    public FindIterable<Document> getAllUsers(double fileId) {
        return collection.find(Filters.eq("file_id", fileId));
    }
    public Document getUserById(ObjectId userId){
        Bson query = Filters.eq("_id", userId);
        return (Document) collection.find(query);
    }
    public void closeConnection() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}
