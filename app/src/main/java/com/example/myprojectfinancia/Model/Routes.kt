package com.example.myprojectfinancia.Model


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myprojectfinancia.Login.ui.ContainerCreate
import com.example.myprojectfinancia.Login.ui.LogginScreen
import com.example.myprojectfinancia.Login.ui.ViewModel.LoginViewModel
import com.example.myprojectfinancia.Home.UI.InitialView
import com.example.myprojectfinancia.Login.ui.ForgotPassWord
import com.example.myprojectfinancia.Login.ui.Spalsh
import com.example.myprojectfinancia.Login.ui.ViewModel.SpalshViewModel
import com.example.myprojectfinancia.Home.UI.ViewModels.homeViewModel

sealed class Routes(val routes: String) {
    object SplashScreen : Routes("Splash")
    object LoginScreen : Routes("Login")
    object CreateScreen : Routes("Create")
    object ForgotPasswordScreen : Routes("ForgotPassword")
    object StartingScreen:Routes("InitialView")

}

@Composable
fun Navhost(loginViewModel: LoginViewModel,splashViewModel: SpalshViewModel,homeViewModel: homeViewModel){
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
            ContainerCreate(navigationControler,loginViewModel)
        }
        composable(Routes.ForgotPasswordScreen.routes){
            ForgotPassWord(modifier = Modifier,navigationControler,loginViewModel)
        }
        composable(Routes.StartingScreen.routes){
            InitialView(navigationControler,homeViewModel)
        }

    }

}