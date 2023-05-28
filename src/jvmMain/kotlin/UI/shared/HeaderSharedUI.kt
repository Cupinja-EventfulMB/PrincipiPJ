package UI.shared

import UI.MenuState
import UI.service.NavService
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun HeaderUI() {
    TopAppBar(backgroundColor = Color.Cyan) {
        val title = when (NavService.currentNav) {
            MenuState.Events -> "Events in Maribor"
            MenuState.Locations -> "Institutions in Maribor and events they have"
            MenuState.Generator -> "Generate events"
        }
        Text(
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            text = title,
            fontWeight = FontWeight.Bold,
            style = TextStyle(
                fontSize = 20.sp,
                color = Color.Black,
            )
        )
    }
}