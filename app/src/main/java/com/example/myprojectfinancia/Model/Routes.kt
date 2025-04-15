package com.example.myprojectfinancia.Model

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myprojectfinancia.Login.ui.ContainerCreate
import com.example.myprojectfinancia.Login.ui.LogginScreen
import com.example.myprojectfinancia.Login.ui.ViewModel.LoginViewModel
import com.example.myprojectfinancia.Login.ui.forgotPassWord
import com.example.myprojectfinancia.Starting.UI.InitialView

sealed class Routes(val routes: String) {
    object LoginScreen : Routes("Login")
    object CreateScreen : Routes("Create")
    object ForgotPasswordScreen : Routes("ForgotPassword")
    object StartingScreen:Routes("InitialView")

}

@Composable
fun Navhost(loginViewModel: LoginViewModel){
    val navigationControler = rememberNavController()
    NavHost(
        navController = navigationControler,
        startDestination = (Routes.LoginScreen.routes)
    ) {
        composable(Routes.LoginScreen.routes) {
            LogginScreen(
                modifier = Modifier, navigationControler,
                loginViewModel
            )
        }
        composable(Routes.CreateScreen.routes) {
            ContainerCreate(navigationControler)
        }
        composable(Routes.ForgotPasswordScreen.routes){
            forgotPassWord(modifier = Modifier,navigationControler,loginViewModel)
        }
        composable(Routes.StartingScreen.routes){
            InitialView()
        }

    }

}