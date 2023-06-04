package UI.service

import Database
import androidx.compose.runtime.mutableStateListOf
import data.model.Event
import data.model.Location
import data.service.eventimScraperEvents
import data.service.sngScraperEvents
import org.bson.Document
import org.bson.types.ObjectId
import org.litote.kmongo.deleteOneById
import org.litote.kmongo.findOneById
import org.litote.kmongo.updateOneById
import java.time.LocalDateTime
import java.util.Date

object EventService {

    private val events = mutableStateListOf<Event>()
    private val connection = Database.connect()
    private val eventCollection = connection.database.getCollection("events")

    fun getEvents(): List<Event> {
        if (events.isEmpty()) {
            events.addAll(eventimScraperEvents())
            events.addAll(sngScraperEvents())
        }
        return events
    }

    fun getEventsByLocation(location: Location): List<Event> {
        val filteredEvents = getEvents().filter { event -> event.location == location }
        return filteredEvents
    }


    fun getById(id: ObjectId): Event {
        val event = events.find { it.id!! == id }
        if (event != null) return event
        val newEvent = Event.mapDataToObject(eventCollection.findOneById(id)!!)!!
        events.add(newEvent)
        return newEvent
    }

    fun updateTitle(event: Event, newTitle: String) {
        val index = events.indexOfFirst { it == event }
        events[index].title = newTitle
    }
    fun updateDate(event: Event, newDate: LocalDateTime) {
        val index = events.indexOfFirst { it == event }
        events[index].date = newDate
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

    fun deleteEvent(event: Event) {
        events.removeIf { it == event }
    }

    fun saveEventDate(event: Event, date: LocalDateTime) {
        val index = events.indexOfFirst { it == event }
        events[index].date = date
    }

}