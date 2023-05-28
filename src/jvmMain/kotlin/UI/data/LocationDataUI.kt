
import UI.DetailedViewMode
import UI.service.EventService
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
import data.model.Event
import data.model.Location
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun TextInputField(value: String = "", label: String = "Label", onChange: (newValue: String) -> Unit, modifier: Modifier = Modifier) {
    TextField(value = value, label = { Text(label) }, modifier = modifier, onValueChange = onChange)
}

@Composable
fun LocationEventList(location: Location) {
    val events = mutableStateListOf<Event>()
    val scope = rememberCoroutineScope()
    scope.launch {
        for (event in EventService.getByLocationId(location.id!!)) {
            events.add(event)
            delay(100)
        }
    }
    LazyColumn(modifier = Modifier.height(200.dp).padding(horizontal = 20.dp)) {
        items(events) { currEvent ->
            var mode by remember { mutableStateOf<DetailedViewMode>(DetailedViewMode.VIEW) }
            var title by remember { mutableStateOf(currEvent.title) }
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (mode == DetailedViewMode.VIEW) {
                    Text("Title: $title", modifier = Modifier.weight(1.5f))

                    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                    val formattedDateTime = currEvent.date.format(formatter)

                    Text("Date: ${formattedDateTime}", modifier = Modifier.weight(0.5f))
                    Row {
                        IconButton(onClick = { mode = DetailedViewMode.EDIT }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit event")
                        }
                        IconButton(onClick = {
                            currEvent.delete()
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
                    Row {
                        IconButton(onClick = {
                            EventService.updateTitle(currEvent.id!!, title)
                            mode = DetailedViewMode.VIEW
                        }) {
                            Icon(Icons.Default.Check, contentDescription = "Confirm change")
                        }
                        IconButton(onClick = { mode = DetailedViewMode.VIEW }) {
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
                    Text(text = location.institution,
                        fontWeight = FontWeight.Bold,
                    )                }
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
            delay(100)
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