package data.model

import Database
import UI.service.EventService
import androidx.compose.runtime.mutableStateListOf
import com.google.gson.Gson
import org.bson.Document
import org.bson.types.ObjectId
import org.litote.kmongo.deleteOneById
import org.litote.kmongo.findOneById
import java.time.LocalDateTime
import java.time.ZoneId

data class Event(
    var title: String,
    var date: LocalDateTime,
    var location: Location,
    val description: String,
    val image: String,
    val id: ObjectId? = null,

    ) {


    override fun toString(): String {
        return (" TTILE: $title \n" +
                " DATE: $date \n" +
                " LOCATION: $location \n" +
                " DESCRIPTION: $description \n" +
                " IMG_URL: $image")
    }

    fun toJson(): String {
        return """
            {
                "title": "$title",
                "date": "$date",
                "location": {
                    "institution": "${location.institution}",
                    "city": "${location.city}",
                    "street": "${location.street}",
                    "x": ${location.x},
                    "y": ${location.y}
                },
                "description": "$description",
                "image": "$image"
            }
        """.trimIndent()

    }

//    fun delete(): Boolean {
//        return Companion.delete(this)
//    }

    companion object {
        fun mapDataToObject(data: org.bson.Document): Event? {
            val title = data.getString("title")
            val dateTimestamp = data.getDate("date")
            val date = dateTimestamp.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
            val location = Location.getById(data.getObjectId("location"))
            val description = data.getString("description")
            val image = data.getString("img_url") ?: ""
            val id = data.getObjectId("_id")

            if (location != null) {
                return Event(title, date, location, description, image, id)
            }
            return null;
        }

        fun getById(id: ObjectId): Event {
            return EventService.getById(id)
        }

//        fun delete(event: Event): Boolean {
//            if (event.id == null) return false
//            return EventService.delete(event.id)
//        }

    }

}
