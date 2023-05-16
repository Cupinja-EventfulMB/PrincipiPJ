package data.model

import java.time.LocalDate

data class Event (
    val title: String,
    val date: LocalDate,
    val location: Location

) {
    override fun toString(): String {
        return " $title, $date, $location "
    }
}
