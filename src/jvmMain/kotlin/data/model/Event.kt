package data.model

import java.time.LocalDateTime

data class Event (
    val title: String,
    val date: LocalDateTime,
    val location: Location
) {
    override fun toString(): String {
        return " $title, $date, $location "
    }
}
