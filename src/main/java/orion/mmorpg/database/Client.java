package orion.mmorpg.database;

import com.mongodb.MongoClient;

public class Client {
    public MongoClient mongoClient = new MongoClient("localhost", 27017);
}
