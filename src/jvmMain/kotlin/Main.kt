
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import data.model.Event
import io.github.cdimascio.dotenv.dotenv
import org.bson.Document

enum class MenuState {
    Events,
    Locations,
    Generator
}
enum class DetailedViewMode {
    VIEW,
    EDIT,
    DELETE
}
@Composable
fun EventCard( event: Event) {
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
                        Icons.Default.List,
                        contentDescription = event.title,
                        modifier = Modifier.scale(1.5f).padding(end = 20.dp),
                )
                Text(event.title)
            }
            Text("${event.location.institution}")
        }
    }
}
@Composable
fun EventCardList(eventList: List<Document>) {
    val state = rememberLazyListState()
    val stateVertical = rememberScrollState(0)
    Box {
        LazyColumn(
            state = state,
            modifier = Modifier.padding(
                horizontal = 10.dp,
                vertical = 15.dp
            ).verticalScroll(stateVertical).height(1000.dp)
        ) {
            items(eventList) { event ->
                EventCard(event = Event.mapDataToObject(event))
            }
        }
        VerticalScrollbar(
            modifier = Modifier.fillMaxHeight().align(Alignment.CenterEnd),
            adapter = rememberScrollbarAdapter(stateVertical)
        )
    }

}


@Composable
@Preview
fun App() {
    val dotenv = dotenv {
        directory =
            "C:\\Users\\User\\Desktop\\Materijali_2_letnik\\PrincipiPJ\\vaje\\projektna_vaja_1\\PrincipiPJ\\.env"
        ignoreIfMissing = true
    }

    val DATABASE_USERNAME: String? = dotenv["DATABASE_USERNAME"]
    val DATABASE_PASSWORD: String? = dotenv["DATABASE_PASSWORD"]
    val DATABASE_NAME: String? = dotenv["DATABASE_NAME"]

    val connection = Database.connect(DATABASE_USERNAME!!, DATABASE_PASSWORD!!, DATABASE_NAME!!)

    val eventCollection = connection.database.getCollection("events")


    EventCardList(eventCollection.find().toList())

    MaterialTheme {

    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
