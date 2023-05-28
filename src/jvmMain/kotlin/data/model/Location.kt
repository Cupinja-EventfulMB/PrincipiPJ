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
    val id: ObjectId? = null,
) {
    override fun toString(): String {
        return "$institution, $city, $street "
    }

    companion object {
        fun mapDataToObject(data: org.bson.Document): Location {
          return Location(
                data.getString("institution"),
                data.getString("city"),
                data.getString("street"),
                data.getObjectId("_id")
            )
        }
        fun getById(id: ObjectId): Location {
            return LocationService.getById(id)
        }
    }
}

