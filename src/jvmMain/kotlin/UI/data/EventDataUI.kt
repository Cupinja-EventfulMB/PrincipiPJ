package UI.data

import UI.service.EventService
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import data.model.Event
import kotlinx.coroutines.delay
import net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader.Dispatcher
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

@Composable
fun EventCard(event: Event) {
    Button(
        onClick = {},
        modifier = Modifier.fillMaxWidth(),
        elevation = ButtonDefaults.elevation(1.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row {
                Icon(
                    Icons.Outlined.DateRange,
                    contentDescription = event.title,
                    modifier = Modifier.scale(1.5f).padding(end = 20.dp),
                )
                Text(event.title)
            }
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
            val formattedDateTime = event.date.format(formatter)
            Text(formattedDateTime)
        }
    }
}

@Composable
fun EventCardList() {
    val events = mutableStateListOf<Event>()
    val scope = rememberCoroutineScope()
    scope.launch {
        for (event in EventService.getEvents()) {
            events.add(event)
        }
    }
    val stateVertical = rememberScrollState(0)
    Box(modifier = Modifier.padding(top = 10.dp)) {
        LazyColumn(
            modifier = Modifier.padding(
                horizontal = 10.dp,
                vertical = 15.dp,
            ).verticalScroll(stateVertical).height(1000.dp)
        ) {
            items(events) { event ->
                EventCard(event = event)
            }
        }
        VerticalScrollbar(
            modifier = Modifier.fillMaxHeight().align(Alignment.CenterEnd),
            adapter = rememberScrollbarAdapter(stateVertical)
        )
    }

}

