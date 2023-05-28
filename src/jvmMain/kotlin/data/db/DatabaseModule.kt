
import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.ServerApi
import com.mongodb.ServerApiVersion
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase
import data.model.Event
import io.github.cdimascio.dotenv.dotenv
import org.litote.kmongo.getCollection

class Database(val client: MongoClient, val database: MongoDatabase) {
    companion object {
        private val dotenv = dotenv {
            ignoreIfMissing = true
        }
        fun connect(): Database {
            val DATABASE_USERNAME: String = dotenv["DATABASE_USERNAME"]!!
            val DATABASE_PASSWORD: String = dotenv["DATABASE_PASSWORD"]!!
            val DATABASE_NAME: String = dotenv["DATABASE_NAME"]!!

            lateinit var mongoClient: MongoClient
            lateinit var database: MongoDatabase

            try {
                val connString = ConnectionString("mongodb+srv://$DATABASE_USERNAME:$DATABASE_PASSWORD@cluster0.ux3rjiq.mongodb.net/")
                val settings = MongoClientSettings.builder()
                        .applyConnectionString(connString)
                        .serverApi(
                                ServerApi.builder().version(ServerApiVersion.V1).build()
                        )
                        .build()

                mongoClient = MongoClients.create(settings)

                database = mongoClient.getDatabase(DATABASE_NAME)
            } catch (e: Exception) {
                println("AppDebug: " + e.toString() + " - \n\n Message: " + e.message + " -\n\n StackTrace: " + e.stackTrace)
            }

            return Database(mongoClient, database)
        }
    }
    val eventsCollectionn = database.getCollection<Event>("events")

}