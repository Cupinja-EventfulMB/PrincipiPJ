package UI.data

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import data.model.Event
import data.model.Location
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Month
import java.time.temporal.ChronoUnit
import java.util.*

@Composable
fun IntInputField(
    value: Int = 0,
    label: String = "Label",
    onChange: (newValue: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(value = value.toString(), label = { Text(label) }, modifier = modifier, onValueChange = {
        val intValue = it.toIntOrNull()
        if (intValue != null) {
            onChange(intValue)
        }
    })
}

fun randomDate(): LocalDateTime {
    val  start = LocalDate.of(1970, Month.JANUARY, 1);
    val emd = ChronoUnit.DAYS.between(start, LocalDate.now());
    val randomDate = start.plusDays(Random().nextInt(117).toLong());
    return LocalDateTime.of(randomDate, LocalTime.now())
}

fun randomString(length: Int): String {
    val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    return (1..length)
        .map { allowedChars.random() }
        .joinToString("")
}

fun generateEvent(): Event {
    val title = randomString(10)
    val url = randomString(100)
    val date = randomDate()
    val location = Location(randomString(10),randomString(10), randomString(10))
    val desc = randomString(50)
    return Event(title, url, date, location, desc)
}

@Composable
fun GenerateTextFunction() {
    var generateAmount by remember { mutableStateOf(0) }
    var events =  remember { mutableStateListOf<Event>() }
    Column {
        IntInputField(
            label = "Generate Amount",
            value = generateAmount,
            onChange = {
                generateAmount = it
                events.clear()
                for (i in 0 until generateAmount) {
                    events.add(generateEvent())
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        LazyColumn {
            items(events) {event -> EventCard(event)}
        }
    }
}