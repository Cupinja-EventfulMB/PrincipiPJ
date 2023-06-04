package scraping

import UI.HttpClient
import data.model.Event
import data.model.Location
import java.time.LocalDateTime

fun main() {
//    val connection = Database.connect()
//    // Insert events
//    val eventCollection = connection.database.getCollection("events")
//    val locationCollection = connection.database.getCollection("locations")
//    var locationId: ObjectId?
//    var eventDocument: Document?
//    lateinit var queryFilterLocationInstitution: Document
//    lateinit var queryFilterEventTitle: Document
//
//    val events = mutableListOf<Event>()
//    events.addAll(eventimScraperEvents())
//    events.addAll(sngScraperEvents())
//
//
//    for (event in events) {
//        queryFilterLocationInstitution = Document("institution", event.location.institution)
//        queryFilterEventTitle =
//                Document("title", event.title).append("date", event.date)
//
//        eventDocument = eventCollection.find(queryFilterEventTitle).first()
//        if (eventDocument == null) {
//            locationId = locationCollection.find(queryFilterLocationInstitution).firstOrNull()?.get("_id") as ObjectId?// check if location already exists
//            if (locationId == null) {
//                locationId = locationCollection.insertOne(
//                        Document().append("institution", event.location.institution).append("city", event.location.city)
//                                .append("street", event.location.street)
//                ).insertedId!!.asObjectId().value
//            }
//            eventCollection.insertOne(
//                    Document().append("title", event.title).append("date", event.date)
//                            .append("location", locationId).append("description", event.description).append("img_url", event.image)
//            )
//        }
//    }

//    eventimScraperEvents()
//    sngScraperEvents()

    val event = Event( "TESTTTT", LocalDateTime.now(), Location("", "", ""), "TEST DESC","image url",)
    HttpClient.insertEventIntoMongo(event)
}

