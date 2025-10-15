package com.example.myprojectfinancia

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import com.example.myprojectfinancia.Data.message.SetupFirebaseNotifications
import com.example.myprojectfinancia.Index.Login.ViewModel.LoginViewModel
import com.example.myprojectfinancia.Index.Login.ViewModel.SpalshViewModel
import com.example.myprojectfinancia.Index.Plans.ViewModel.PlansViewModel
import com.example.myprojectfinancia.Index.home.ViewModels.homeViewModel
import com.example.myprojectfinancia.Index.settings.viewModel.settingsViewmodel
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
    private val settingsViewmodel: settingsViewmodel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val configuration = Configuration(resources.configuration).apply {
                fontScale = 1.0f
            }



            MyProjectFinanciaTheme {
                val currentDensity = LocalDensity.current
                val fixedDensity = Density(
                    density = currentDensity.density,
                    fontScale = 1.0f
                )
                CompositionLocalProvider(
                    LocalDensity provides fixedDensity
                ) {
                    SetupFirebaseNotifications()
                    Navhost(loginViewModel, splashViewModel, homeViewModel, plansViewModel, settingsViewmodel)
                }
            }

        }
    }
}



