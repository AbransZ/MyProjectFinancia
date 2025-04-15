package com.example.myprojectfinancia

import com.example.myprojectfinancia.Model.Routes
import com.example.myprojectfinancia.Login.ui.ContainerCreate
import com.example.myprojectfinancia.Login.ui.LogginScreen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.example.myprojectfinancia.Login.ui.forgotPassWord
import com.example.myprojectfinancia.Login.ui.ViewModel.LoginViewModel
import com.example.myprojectfinancia.Model.Navhost
import com.example.myprojectfinancia.theme.MyProjectFinanciaTheme
import dagger.hilt.android.AndroidEntryPoint


//etiqueta de dagger hilt
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

//inyectando viewmodel
    private val loginViewModel : LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyProjectFinanciaTheme {

                Navhost(loginViewModel)
                }
            }
        }
    }



