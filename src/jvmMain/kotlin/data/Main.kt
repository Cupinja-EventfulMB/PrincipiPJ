package scraping

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClients

fun main() {
    val connectionString = ConnectionString("mongodb://localhost:27017") // Replace with your MongoDB connection string
    val mongoClientSettings = MongoClientSettings.builder()
        .applyConnectionString(connectionString)
        .build()

    val mongoClient = MongoClients.create(mongoClientSettings)
    val database = mongoClient.getDatabase("principipj")

    database.createCollection("Event")


//    eventimScraper()
//    sngScraper()
//    cineplexScraper()
}

