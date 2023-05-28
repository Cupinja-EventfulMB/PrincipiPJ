package data.model

import Database
import UI.service.EventService
import androidx.compose.runtime.mutableStateListOf
import org.bson.Document
import org.bson.types.ObjectId
import org.litote.kmongo.deleteOneById
import org.litote.kmongo.findOneById
import java.time.LocalDateTime
import java.time.ZoneId

data class Event(
    val image: String,
    var title: String,
    val date: LocalDateTime,
    val location: Location,
    val description: String,
    val id: ObjectId? = null,

    ) {


    override fun toString(): String {
        return (" TTILE: $title \n" +
                " DATE: $date \n" +
                " LOCATION: $location \n" +
                " DESCRIPTION: $description \n" +
                " IMG_URL: $image")
    }

    fun delete(): Boolean {
        return Companion.delete(this)
    }

    companion object {
        fun mapDataToObject(data: org.bson.Document): Event? {
            val image = data.getString("img_url") ?: ""
            val title = data.getString("title")
            val dateTimestamp = data.getDate("date")
            val date = dateTimestamp.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
            val location = Location.getById(data.getObjectId("location"))
            val description = data.getString("description")
            val id = data.getObjectId("_id")

            if (location != null) {
                return Event(image, title, date, location, description, id)
            }
            return null;
        }

        fun getById(id: ObjectId): Event {
            return EventService.getById(id)
        }

        fun delete(event: Event): Boolean {
            if (event.id == null) return false
            return EventService.delete(event.id)
        }
    }

}
