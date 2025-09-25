package com.example.myprojectfinancia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.myprojectfinancia.Data.message.SetupFirebaseNotifications
import com.example.myprojectfinancia.Index.Login.ViewModel.LoginViewModel
import com.example.myprojectfinancia.Index.Login.ViewModel.SpalshViewModel
import com.example.myprojectfinancia.Index.Plans.ViewModel.PlansViewModel
import com.example.myprojectfinancia.Index.home.ViewModels.homeViewModel
import com.example.myprojectfinancia.Model.Navhost
import com.example.myprojectfinancia.theme.MyProjectFinanciaTheme
import dagger.hilt.android.AndroidEntryPoint


//etiqueta de dagger hilt
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    //inyectando viewmodel
    private val loginViewModel: LoginViewModel by viewModels()
    private val splashViewModel: SpalshViewModel by viewModels()
    private val homeViewModel: homeViewModel by viewModels()
    private val plansViewModel: PlansViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SetupFirebaseNotifications()

            MyProjectFinanciaTheme {

                Navhost(loginViewModel, splashViewModel, homeViewModel, plansViewModel)
            }
        }
    }
}



