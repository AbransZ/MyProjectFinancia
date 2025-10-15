package com.example.myprojectfinancia.Model


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myprojectfinancia.Index.Login.ViewModel.LoginViewModel
import com.example.myprojectfinancia.Index.Login.ViewModel.SpalshViewModel
import com.example.myprojectfinancia.Index.Login.ui.ContainerCreate
import com.example.myprojectfinancia.Index.Login.ui.ForgotPassWord
import com.example.myprojectfinancia.Index.Login.ui.LogginScreen
import com.example.myprojectfinancia.Index.Login.ui.Spalsh
import com.example.myprojectfinancia.Index.Plans.ViewModel.PlansViewModel
import com.example.myprojectfinancia.Index.UI.InitialView
import com.example.myprojectfinancia.Index.home.ViewModels.homeViewModel
import com.example.myprojectfinancia.Index.settings.viewModel.settingsViewmodel

sealed class Routes(val routes: String) {
    object SplashScreen : Routes("Splash")
    object LoginScreen : Routes("Login")
    object CreateScreen : Routes("Create")
    object ForgotPasswordScreen : Routes("ForgotPassword")
    object StartingScreen : Routes("InitialView")

}

@Composable
fun Navhost(
    loginViewModel: LoginViewModel,
    splashViewModel: SpalshViewModel,
    homeViewModel: homeViewModel,
    plansViewModel: PlansViewModel,
    settingsViewmodel: settingsViewmodel
) {
    val navigationControler = rememberNavController()
    NavHost(
        navController = navigationControler,
        startDestination = (Routes.SplashScreen.routes)
    ) {
        composable(Routes.SplashScreen.routes) {
            Spalsh(splashViewModel, navigationControler)
        }
        composable(Routes.LoginScreen.routes) {
            LogginScreen(
                modifier = Modifier, navigationControler,
                loginViewModel
            )
        }
        composable(Routes.CreateScreen.routes) {
            ContainerCreate(navigationControler, loginViewModel)
        }
        composable(Routes.ForgotPasswordScreen.routes) {
            ForgotPassWord(modifier = Modifier, navigationControler, loginViewModel)
        }
        composable(Routes.StartingScreen.routes) {
            InitialView(navigationControler, homeViewModel, loginViewModel, plansViewModel, settingsViewmodel)
        }

    }

}