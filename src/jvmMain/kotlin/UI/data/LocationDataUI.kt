import UI.DetailedViewMode
import UI.service.EventService
import UI.service.EventService.deleteEvent
import UI.service.LocationService
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import data.model.Location
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun TextInputField(
    value: String = "",
    label: String = "Label",
    onChange: (newValue: String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(value = value, label = { Text(label) }, modifier = modifier, onValueChange = onChange)
}

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

@Composable
fun DateTimeInputField(
    value: LocalDateTime = LocalDateTime.now(),
    label: String = "Label",
    onChange: (newValue: LocalDateTime) -> Unit,
    modifier: Modifier = Modifier
) {
    var day by remember { mutableStateOf(value.dayOfMonth) }
    var month by remember { mutableStateOf(value.monthValue) }
    var year by remember { mutableStateOf(value.year) }
    var hour by remember { mutableStateOf(value.hour) }
    var minute by remember { mutableStateOf(value.minute) }

    fun onDateTimeChanged() {
        val stringDate = "${day.toString().padStart(2, '0')}/${month.toString().padStart(2, '0')}/$year ${
            hour.toString().padStart(2, '0')
        }:${minute.toString().padStart(2, '0')}"
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm") // dd/MM/yyyy HH:mm
        try {
            val date = LocalDateTime.parse(stringDate, formatter)
            onChange(date)
        } catch (ex: Exception) {
            println(ex)
        }
    }

    Row {
        IntInputField(day, label = "Day", onChange = {
            day = it
            onDateTimeChanged()
        })
        Text(".")
        IntInputField(month, label = "Month", onChange = {
            month = it
            onDateTimeChanged()
        })
        Text(".")
        IntInputField(year, label = "Year", onChange = {
            year = it
            onDateTimeChanged()
        })
        Text(" ")
        IntInputField(hour, label = "Hours", onChange = {
            hour = it
            onDateTimeChanged()
        })
        Text(":")
        IntInputField(minute, label = "Minute", onChange = {
            minute = it
            onDateTimeChanged()
        })
    }
}

@Composable
fun DateInputField(
    value: LocalDateTime = LocalDateTime.now(),
    onChange: (newValue: LocalDateTime) -> Unit,
    modifier: Modifier = Modifier,
    label: String
) {
    var day by remember { mutableStateOf(value.dayOfMonth) }
    var month by remember { mutableStateOf(value.monthValue) }
    var year by remember { mutableStateOf(value.year) }
    val hour by remember { mutableStateOf(value.hour) }
    val minute by remember { mutableStateOf(value.minute) }
    fun onDateTimeChanged() {
        val stringDate = "${day.toString().padStart(2, '0')}/${month.toString().padStart(2, '0')}/$year ${
            hour.toString().padStart(2, '0')
        }:${minute.toString().padStart(2, '0')}"
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm") // dd/MM/yyyy HH:mm
        try {
            val date = LocalDateTime.parse(stringDate, formatter)
            onChange(date)
        } catch (ex: Exception) {
            println(ex)
        }
    }

    Row(modifier = modifier) {
        IntInputField(day, label = "Day", onChange = {
            day = it
            onDateTimeChanged()
        })
        Text("/")
        IntInputField(month, label = "Month", onChange = {
            month = it
            onDateTimeChanged()
        })
        Text("/")
        IntInputField(year, label = "Year", onChange = {
            year = it
            onDateTimeChanged()
        })
    }
}

// todo da se popraj x
@Composable
fun LocationEventList(location: Location) {
    val events = EventService.getEventsByLocation(location)
    val scope = rememberCoroutineScope()

    LazyColumn(modifier = Modifier.height(200.dp).padding(horizontal = 20.dp)) {
        items(events) { currEvent ->
            var mode by remember { mutableStateOf<DetailedViewMode>(DetailedViewMode.VIEW) }
            var title by remember { mutableStateOf(currEvent.title) }
            var date by remember { mutableStateOf<LocalDateTime>(currEvent.date) }
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (mode == DetailedViewMode.VIEW) {
                    Text("Title: $title", modifier = Modifier.weight(1.5f))

                    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                    val formattedDateTime = date.format(formatter)

                    Text("Date: $formattedDateTime", modifier = Modifier.weight(0.5f))
                    Row {
                        IconButton(onClick = { mode = DetailedViewMode.EDIT }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit event")
                        }
                        IconButton(onClick = {
                            deleteEvent(currEvent)
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete event")
                        }
                    }
                } else if (mode == DetailedViewMode.EDIT) {
                    TextInputField(
                        value = title,
                        label = "Title",
                        modifier = Modifier.weight(1.5f),
                        onChange = {
                            title = it
                        }
                    )
                    DateInputField(
                        value = date,
                        label = "Date",
                        modifier = Modifier.weight(1.5f),
                        onChange = {
                            date = it
                        }
                    )
                    Row {
                        IconButton(onClick = {
                            EventService.updateTitle(currEvent, title)
                            mode = DetailedViewMode.VIEW
                        }) {
                            Icon(Icons.Default.Check, contentDescription = "Confirm change")
                            EventService.saveEventDate(currEvent, date)
                        }
                        IconButton(onClick = {
                            date = currEvent.date // todo nedela
                            title = currEvent.title
                            mode = DetailedViewMode.VIEW
                        }) {
                            Icon(Icons.Default.Clear, contentDescription = "Cancel change")
                        }
                    }
                }
            }
            Box(
                modifier = Modifier.fillMaxWidth()
                    .border(width = 2.dp, color = Color.Gray, shape = RoundedCornerShape(20)).background(Color.Gray)
                    .height(1.dp)
            )
        }
    }
}

@Composable
fun LocationDetailCard(location: Location) {
    Column(
        modifier = Modifier.border(width = 1.dp, color = Color.LightGray, shape = RoundedCornerShape(1))
    ) {
        Button(
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
            elevation = ButtonDefaults.elevation(1.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row {
                    Icon(
                        Icons.Outlined.MoreVert,
                        contentDescription = location.institution,
                        modifier = Modifier.scale(1.5f).padding(end = 20.dp),
                    )
                    Text(
                        text = location.institution,
                        fontWeight = FontWeight.Bold,
                    )
                }
                Text("${location.street} ${location.city}")
            }
        }
        LocationEventList(location)
    }

}

@Composable
fun LocationDetailCardList() {
    val locations = mutableStateListOf<Location>()
    val scope = rememberCoroutineScope()
    scope.launch {
        for (location in LocationService.getLocations()) {
            locations.add(location)
        }
    }
    val state = rememberLazyListState()
    val stateVertical = rememberScrollState(0)
    Box(modifier = Modifier.fillMaxHeight()) {
        LazyColumn(
            state = state,
            modifier = Modifier.padding(
                horizontal = 10.dp,
                vertical = 15.dp
            ).verticalScroll(stateVertical).height(1000.dp)
        ) {

            items(locations) { location ->
                Box(modifier = Modifier.padding(bottom = 15.dp)) {
                    LocationDetailCard(location)
                }
            }
        }
        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd)
                .fillMaxHeight(),
            adapter = rememberScrollbarAdapter(stateVertical)
        )
    }

}