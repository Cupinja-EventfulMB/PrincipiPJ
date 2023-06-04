package UI.service

import androidx.compose.runtime.mutableStateListOf
import data.model.Event
import data.model.Location
import org.bson.types.ObjectId
import org.litote.kmongo.findOneById

object LocationService {
    private val locations = mutableStateListOf<Location>()
    private val connection = Database.connect()
    private val locationCollection = connection.database.getCollection("locations")

    fun getLocations(): List<Location> {
        val events = EventService.getEvents()
        val locations = mutableListOf<Location>()

        for (event in events) {
            if (!locations.contains(event.location)) {
                locations.add(event.location)
            }
        }

        return locations
    }


    fun getById(id: ObjectId): Location {
        val location = locations.find { it.id!! == id }
        if (location != null) return location
        val newLoc = Location.mapDataToObject(locationCollection.findOneById(id)!!)
        locations.add(newLoc)
        return newLoc
    }
}