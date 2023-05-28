package UI.service

import androidx.compose.runtime.mutableStateListOf
import data.model.Location
import org.bson.types.ObjectId
import org.litote.kmongo.findOneById

object LocationService {
    private val locations = mutableStateListOf<Location>()
    private val connection = Database.connect()
    private val locationCollection = connection.database.getCollection("locations")

    fun getLocations() = sequence<Location> {
        yieldAll(locations)
        val locationDocs = locationCollection.find().filter { locDoc -> locations.find { it.id != locDoc.getObjectId("_id") } == null }
        for (locationDoc in locationDocs) {
            val location = Location.mapDataToObject(locationDoc)
            locations.add(location)
            yield(location)
        }
    }

    fun getById(id: ObjectId): Location {
        val location = locations.find { it.id!! == id }
        if (location != null) return location
        val newLoc = Location.mapDataToObject(locationCollection.findOneById(id)!!)
        locations.add(newLoc)
        return newLoc
    }
}