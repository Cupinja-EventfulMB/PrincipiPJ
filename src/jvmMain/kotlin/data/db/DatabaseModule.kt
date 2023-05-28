
import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.ServerApi
import com.mongodb.ServerApiVersion
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase
import data.model.Event
import org.litote.kmongo.getCollection

class Database(val client: MongoClient, val database: MongoDatabase) {
    companion object {
        fun connect(username: String, password: String, dbName: String): Database {
            lateinit var mongoClient: MongoClient
            lateinit var database: MongoDatabase

            try {
                val connString = ConnectionString("mongodb+srv://$username:$password@cluster0.ux3rjiq.mongodb.net/")
                val settings = MongoClientSettings.builder()
                        .applyConnectionString(connString)
                        .serverApi(
                                ServerApi.builder().version(ServerApiVersion.V1).build()
                        )
                        .build()

                mongoClient = MongoClients.create(settings)

                database = mongoClient.getDatabase(dbName)
            } catch (e: Exception) {
                println("AppDebug: " + e.toString() + " - \n\n Message: " + e.message + " -\n\n StackTrace: " + e.stackTrace)
            }

            return Database(mongoClient, database)
        }
    }
    val eventsCollectionn = database.getCollection<Event>("events")

}