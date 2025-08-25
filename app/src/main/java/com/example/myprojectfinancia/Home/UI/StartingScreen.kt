package com.example.myprojectfinancia.Home.UI


import androidx.compose.foundation.background
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.myprojectfinancia.Home.UI.Movements.MovementsScreens
import com.example.myprojectfinancia.Home.UI.Plans.SavingScreen
import com.example.myprojectfinancia.Home.UI.Plans.ViewModel.PlansViewModel
import com.example.myprojectfinancia.Home.UI.ViewModels.HomeScreen
import com.example.myprojectfinancia.Home.UI.home.Models.NavScreensModel
import com.example.myprojectfinancia.Home.UI.home.Models.RememberNavStateScreen
import com.example.myprojectfinancia.Home.UI.home.ViewModels.homeViewModel
import com.example.myprojectfinancia.Home.UI.settings.SettingsScreen
import com.example.myprojectfinancia.Login.ui.ViewModel.LoginViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InitialView(
    navController: NavHostController,
    homeViewModel: homeViewModel,
    loginViewModel: LoginViewModel,
    plansViewModel: PlansViewModel
) {

    val Navigation = RememberNavStateScreen()

    Scaffold(topBar = {
        TopAppBar(title = { Text("Financia", fontWeight = FontWeight.Bold, fontSize = 30.sp) },
            actions = {
                Button(onClick = {
                    val route = homeViewModel.logout()
                    navController.navigate(route) {
                        popUpTo(0) { inclusive = true }
                        loginViewModel.clearFields()
                    }
                }
                )
                {
                    Text("Salir", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }
            }
        )
    },

        bottomBar = {
            BarraDeBotones(
                currentScreen = Navigation.currentState,
                onScreenSelected = { Navigation.onScreenSelected(it) })
        }) { paddingContent ->
        when (Navigation.currentState) {
            NavScreensModel.Home -> {
                HomeScreen(paddingContent, homeViewModel)
            }

            NavScreensModel.Plans -> {
                SavingScreen(
                    paddingContent,
                    plansViewModel,
                    modifier = Modifier.background(MaterialTheme.colorScheme.background)
                )
            }

            NavScreensModel.Movements -> {
                MovementsScreens(
                    paddingContent,
                    modifier = Modifier.background(MaterialTheme.colorScheme.background),
                    homeViewModel
                )
            }

            NavScreensModel.Settings -> {
                SettingsScreen(
                    paddingContent
                )
            }
        }
    }
}


