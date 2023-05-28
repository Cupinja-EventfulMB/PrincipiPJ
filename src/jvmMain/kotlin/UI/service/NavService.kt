package UI.service

import UI.MenuState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object NavService {
    var currentNav by mutableStateOf(MenuState.Events)
}