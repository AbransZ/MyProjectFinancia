package com.example.myprojectfinancia

import Model.Routes
import View.ContainerCreate
import View.LogginScreen
import ViewModel.loginViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myprojectfinancia.ui.theme.MyProjectFinanciaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyProjectFinanciaTheme {

                val navigationControler = rememberNavController()
                NavHost(
                    navController = navigationControler,
                    startDestination = (Routes.LoginScreen.routes)
                ) {
                    composable(Routes.LoginScreen.routes) {
                        LogginScreen(
                            modifier = Modifier, navigationControler,
                            loginViewModel()
                        )
                    }
                    composable(Routes.CreateScreen.routes) { ContainerCreate(navigationControler) }

                }
            }
        }
    }
}


