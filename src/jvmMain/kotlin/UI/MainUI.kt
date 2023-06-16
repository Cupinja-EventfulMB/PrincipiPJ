package UI

import Database
import LocationDetailCardList
import UI.data.EventCardList
import UI.data.GenerateTextFunction
import UI.service.EventService
import UI.service.NavService
import UI.shared.FooterUI
import UI.shared.HeaderUI
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import data.model.Event
import data.model.Location

@Composable
@Preview
fun App() {
    MaterialTheme {
        Scaffold(
            topBar = { HeaderUI() },
            bottomBar = { FooterUI() }
        ) {
            Column(
                modifier = Modifier.fillMaxHeight().fillMaxWidth().padding(top = 15.dp)
            ) {
                if (NavService.currentNav == MenuState.Events) {
                    EventCardList()
                } else if (NavService.currentNav == MenuState.Locations) {
                    LocationDetailCardList()
                } else {
                    GenerateTextFunction()
                }
            }
        }
    }
}

fun main() = application {
    Window(
        title = "EventimMB Podatki",
        onCloseRequest = ::exitApplication
    ) {
        App()
    }
}
