package data.model

import java.time.LocalDateTime

data class Event (
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
}
