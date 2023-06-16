package UI.shared

import UI.HttpClient
import UI.MenuState
import UI.service.EventService
import UI.service.NavService
import androidx.compose.foundation.layout.Row
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FooterButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    bgColor: Color = Color.Cyan,
    style: TextStyle
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        elevation = ButtonDefaults.elevation(0.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = bgColor
        )
    ) {
        Text(text)
    }
}


@Composable
fun FooterUI() {
    BottomAppBar(backgroundColor = Color.Cyan) {
        Row {
            FooterButton(
                text = "Events",
                onClick = { NavService.currentNav = MenuState.Events },
                modifier = Modifier.weight(1f),
                style = TextStyle(
                    fontSize = 20.sp,
                    color = Color.Black,
                )
            )
            FooterButton(
                text = "Locations",
                onClick = { NavService.currentNav = MenuState.Locations },
                modifier = Modifier.weight(1f),
                style = TextStyle(
                    fontSize = 20.sp,
                    color = Color.Black,
                )
            )
            FooterButton(
                text = "Generate data",
                onClick = { NavService.currentNav = MenuState.Generator },
                modifier = Modifier.weight(1f),
                style = TextStyle(
                    fontSize = 20.sp,
                    color = Color.Black,
                )
            )
            FooterButton(
                text = "Save Scraped Events",
                onClick = {
                    for (event in EventService.getEvents()) {
                        HttpClient.insertEventIntoMongo(event)
                    }
                },
                modifier = Modifier.weight(1f),
                style = TextStyle(
                    fontSize = 20.sp,
                    color = Color.Black,
                )
            )
        }
    }

}