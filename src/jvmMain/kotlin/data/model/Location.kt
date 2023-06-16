package data.model

import Database
import UI.service.EventService
import UI.service.LocationService
import org.bson.Document
import org.bson.types.ObjectId
import org.litote.kmongo.findOneById

data class Location(
    val institution: String,
    val city: String,
    val street: String,
    var x: Double = 0.0,
    var y: Double = 0.0,
    val id: ObjectId? = null
) {
    override fun toString(): String {
        return "$institution, $city, $street, $x, $y "
    }

    companion object {
        fun mapDataToObject(data: org.bson.Document): Location {
          return Location(
                data.getString("institution"),
                data.getString("city"),
                data.getString("street"),
                data.getDouble("x"),
                data.getDouble("y"),
                data.getObjectId("_id")
            )
        }
        fun getById(id: ObjectId): Location {
            return LocationService.getById(id)
        }
    }
}

