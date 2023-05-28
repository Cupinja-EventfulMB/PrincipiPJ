package UI.service

import Database
import androidx.compose.runtime.mutableStateListOf
import data.model.Event
import org.bson.Document
import org.bson.types.ObjectId
import org.litote.kmongo.deleteOneById
import org.litote.kmongo.findOneById
import org.litote.kmongo.updateOneById

object EventService {

    private val events = mutableStateListOf<Event>()
    private val connection = Database.connect()
    private val eventCollection = connection.database.getCollection("events")

    fun getEvents() = sequence<Event> {
        yieldAll(events)
        val eventDocs = eventCollection.find().filter { eventDoc -> events.find { it.id != eventDoc.getObjectId("_id") } == null }
        for (eventDoc in eventDocs) {
            val event = Event.mapDataToObject(eventDoc)!!
            yield(event)
            events.add(event)
        }
    }

    fun getById(id: ObjectId): Event {
        val event = events.find { it.id!! == id }
        if (event != null) return event
        val newEvent = Event.mapDataToObject(eventCollection.findOneById(id)!!)!!
        events.add(newEvent)
        return newEvent
    }

    fun updateTitle(id: ObjectId, newTitle: String) {
        val updateBson = Document("\$set", Document("title", newTitle))
        val index = events.indexOfFirst { it.id == id }
        events[index].title = newTitle
        eventCollection.updateOneById(id, updateBson)
    }

    fun getByLocationId(locId: ObjectId) = sequence<Event> {
        val initialEvents = events.filter { it.location.id == locId }
        yieldAll(initialEvents)
        val query = Document("location", locId)
        val eventDocs = eventCollection.find(query).filter { eventDoc -> initialEvents.find { it.id != eventDoc.getObjectId("_id") } == null }
        for (eventDoc in eventDocs) {
            val event = Event.mapDataToObject(eventDoc)!!
            yield(event)
        }
    }

    fun delete(id: ObjectId): Boolean {
        val res = eventCollection.deleteOneById(id)
        events.removeIf { it.id == id }
        return res.deletedCount > 0
    }

}