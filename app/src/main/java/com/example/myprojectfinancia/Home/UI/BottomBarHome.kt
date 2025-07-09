package com.example.myprojectfinancia.Home.UI

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import com.example.myprojectfinancia.Home.UI.home.Models.NavScreensModel
import com.example.myprojectfinancia.Home.UI.home.Models.ScreenItem

//Barra de botones
@Composable
fun BarraDeBotones(
    currentScreen: NavScreensModel,
    onScreenSelected: (NavScreensModel) -> Unit
) {
    NavigationBar {
        ScreenItem.forEach { item ->
            NavigationBarItem(
                selected = currentScreen == item.Screen,
                onClick = { onScreenSelected(item.Screen) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title)
                },
                label = { Text(text = item.title,fontSize = 12.sp)  }
            )
        }
    }
}