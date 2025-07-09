package com.example.myprojectfinancia.Home.UI.home.Models

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun RememberNavStateScreen(modifier: Modifier = Modifier):NavigationState {
    var currentScreen by remember { mutableStateOf(NavScreensModel.Home) }

    return NavigationState(
        currentState = currentScreen,
        onScreenSelected = { currentScreen = it }
    )
}
data class NavigationState(
    var currentState : NavScreensModel,
    var onScreenSelected: (NavScreensModel) -> Unit
)