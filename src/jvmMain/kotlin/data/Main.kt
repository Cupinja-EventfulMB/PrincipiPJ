package scraping

import Database
import data.model.Event
import data.service.eventimScraperEvents
import data.service.sngScraperEvents
import io.github.cdimascio.dotenv.dotenv
import org.bson.Document
import org.bson.types.ObjectId

fun main() {
    val dotenv = dotenv {
        directory =
            "C:\\Users\\User\\Desktop\\Materijali_2_letnik\\PrincipiPJ\\vaje\\projektna_vaja_1\\PrincipiPJ\\.env"
        ignoreIfMissing = true
    }

    val DATABASE_USERNAME: String? = dotenv["DATABASE_USERNAME"]
    val DATABASE_PASSWORD: String? = dotenv["DATABASE_PASSWORD"]
    val DATABASE_NAME: String? = dotenv["DATABASE_NAME"]

    val connection = Database.connect(DATABASE_USERNAME!!, DATABASE_PASSWORD!!, DATABASE_NAME!!)
    // Insert events
    val eventCollection = connection.database.getCollection("events")
    val locationCollection = connection.database.getCollection("locations")
    var locationId: ObjectId?
    var eventDocument: Document?
    lateinit var queryFilterLocationInstitution: Document
    lateinit var queryFilterEventTitle: Document

    val events = mutableListOf<Event>()
    events.addAll(eventimScraperEvents())
    events.addAll(sngScraperEvents())


    for (event in events) {
        queryFilterLocationInstitution = Document("institution", event.location.institution)
        queryFilterEventTitle =
            Document("title", event.title).append("date", event.date)

        eventDocument = eventCollection.find(queryFilterEventTitle).first()
        if (eventDocument == null) {
            locationId = locationCollection.find(queryFilterLocationInstitution).firstOrNull()?.get("_id") as ObjectId?// check if location already exists
            if (locationId == null) {
                locationId = locationCollection.insertOne(
                    Document().append("institution", event.location.institution).append("city", event.location.city)
                        .append("street", event.location.street)
                ).insertedId!!.asObjectId().value
            }
            eventCollection.insertOne(
                Document().append("title", event.title).append("date", event.date)
                    .append("location", locationId).append("description", event.description).append("img_url", event.image)
            )
        }
    }

    eventimScraperEvents()
    sngScraperEvents()
}

