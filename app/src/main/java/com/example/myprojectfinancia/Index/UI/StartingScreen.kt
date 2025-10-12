package com.example.myprojectfinancia.Index.UI


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
import com.example.myprojectfinancia.Index.Login.ViewModel.LoginViewModel
import com.example.myprojectfinancia.Index.Movements.MovementsScreens
import com.example.myprojectfinancia.Index.Plans.UI.SavingScreen
import com.example.myprojectfinancia.Index.Plans.ViewModel.PlansViewModel
import com.example.myprojectfinancia.Index.UI.ViewModels.HomeScreen
import com.example.myprojectfinancia.Index.home.Models.NavScreensModel
import com.example.myprojectfinancia.Index.home.Models.RememberNavStateScreen
import com.example.myprojectfinancia.Index.home.ViewModels.homeViewModel
import com.example.myprojectfinancia.Index.settings.SettingsScreen


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
        TopAppBar(title = { Text("Financia", fontWeight = FontWeight.Bold, fontSize = 40.sp) },
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
                HomeScreen(paddingContent, homeViewModel, plansViewModel)
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


