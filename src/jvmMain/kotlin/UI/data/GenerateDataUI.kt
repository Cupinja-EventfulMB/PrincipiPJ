package UI.data

import DateInputField
import IntInputField
import UI.HttpClient
import UI.service.EventService
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import data.model.Event
import data.model.Location
import java.time.LocalDateTime
import java.util.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

fun randomString(length: Int): String {
    val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    return (1..length)
        .map { allowedChars.random() }
        .joinToString("")
}

fun randomDate(start: LocalDateTime, end: LocalDateTime): LocalDateTime {
    val randomDate = start.plusDays(Random().nextInt(end.dayOfYear - start.dayOfYear + 1).toLong())
    val randomHour = Random().nextInt(24)
    val randomMinute = Random().nextInt(60)
    return randomDate.withHour(randomHour).withMinute(randomMinute)
}

fun generateEvent(start: LocalDateTime, end: LocalDateTime): Event {
    val title = randomString(10)
    val date = randomDate(start, end)
    val location = Location("Generated", "Generated Street", "Maribor")
    val desc = randomString(50)
    val imgUrl = "https://labraj.feri.um.si/images/3/3d/Feri_logo_bw.png"

    return Event(title, date, location, desc, imgUrl)
}


@Composable
fun GenerateTextFunction() {
    var generateAmount by remember { mutableStateOf(0) }
    var startDate by remember { mutableStateOf(LocalDateTime.now()) }
    var endDate by remember { mutableStateOf(LocalDateTime.now()) }
    var events by remember { mutableStateOf(emptyList<Event>()) }

    val stateVertical = rememberScrollState(0)

    fun generateEvents(start: LocalDateTime, end: LocalDateTime) {
        val newEvents = mutableListOf<Event>()
        for (i in 0 until generateAmount) {
            newEvents.add(generateEvent(start, end))
        }
        events = newEvents
    }

    Column {
        Column {
            IntInputField(
                label = "How many events should I generate?",
                value = generateAmount,
                onChange = {
                    generateAmount = it
                    generateEvents(startDate, endDate)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 15.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                DateInputField(
                    value = startDate,
                    onChange = {
                        startDate = it
                        randomDate(startDate, endDate)
                    },
                    modifier = Modifier.padding(5.dp),
                    label = "Date"
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                DateInputField(
                    value = endDate,
                    onChange = {
                        endDate = it
                        randomDate(startDate, endDate)
                    },
                    modifier = Modifier.padding(5.dp),
                    label = "Date"
                )
            }

        }
        Button(
            onClick = {
                for (event in events) {
                    HttpClient.insertEventIntoMongo(event)
                }
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Cyan),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, end = 15.dp)
        )
        {
            Text(text = "Save generated data", color = Color.Black)
        }
        Box {
            LazyColumn(
                modifier = Modifier.padding(
                    horizontal = 10.dp,
                    vertical = 15.dp,
                ).verticalScroll(stateVertical).height(1000.dp)
            ) {
                items(events) { event ->
                    EventCard(event)
                }
            }
            VerticalScrollbar(
                modifier = Modifier.fillMaxHeight().align(Alignment.CenterEnd),
                adapter = rememberScrollbarAdapter(stateVertical)
            )
        }

    }
}
