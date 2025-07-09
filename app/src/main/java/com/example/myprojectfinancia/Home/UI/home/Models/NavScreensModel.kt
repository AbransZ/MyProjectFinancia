package com.example.myprojectfinancia.Home.UI.home.Models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ChangeCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import com.example.myprojectfinancia.R

enum class NavScreensModel {
    Home,
    Movements,
    Plans,
    Settings
}

data class BottomNavItem(
    val Screen: NavScreensModel,
    val title: String,
    val icon: ImageVector
)

val ScreenItem = listOf(
    BottomNavItem(
       Screen =  NavScreensModel.Home,
        title = "Inicio",
        icon = Icons.Default.Home
    ),
    BottomNavItem(
        Screen =  NavScreensModel.Plans,
        title = "Plan. Ahorro",
        icon = Icons.Default.AccountBalanceWallet
    ),
    BottomNavItem(
        Screen =  NavScreensModel.Movements,
        title = "Movimientos",
        icon = Icons.Default.ChangeCircle
    ),
    BottomNavItem(
        Screen =  NavScreensModel.Settings,
        title = "Herramientas",
        icon = Icons.Default.Settings
    )
    )