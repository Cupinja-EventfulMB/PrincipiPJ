package data.model

import java.time.LocalDateTime
import java.time.ZoneId

data class Event(
        val image: String,
        val title: String,
        val date: LocalDateTime,
        val location: Location,
        val description: String
) {
    override fun toString(): String {
        return (" TTILE: $title \n" +
                " DATE: $date \n" +
                " LOCATION: $location \n" +
                " DESCRIPTION: $description \n" +
                " IMG_URL: $image")
    }

    companion object{
        fun mapDataToObject(data: org.bson.Document): Event {
            val image = data.getString("image") ?: ""
            val title = data.getString("title")
            val dateTimestamp = data.getDate("date")
            val date = dateTimestamp.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
            val institution = data.getString("institution") ?: ""
            val city = data.getString("city") ?: ""
            val street = data.getString("street") ?: ""
            val location = Location(institution,city,street)
            val description = data.getString("description")

            return Event(image, title, date, location,description)
        }
    }

}
