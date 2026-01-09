package com.example.myprojectfinancia

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.myprojectfinancia.Data.message.SetupFirebaseNotifications
import com.example.myprojectfinancia.Index.Login.ViewModel.LoginViewModel
import com.example.myprojectfinancia.Index.Login.ViewModel.SpalshViewModel
import com.example.myprojectfinancia.Index.Plans.ViewModel.PlansViewModel
import com.example.myprojectfinancia.Index.home.ViewModels.homeViewModel
import com.example.myprojectfinancia.Index.settings.viewModel.settingsViewmodel
import com.example.myprojectfinancia.Model.Navhost
import com.example.myprojectfinancia.theme.MyProjectFinanciaTheme
import dagger.hilt.android.AndroidEntryPoint
import com.example.myprojectfinancia.Data.message.WorkerNotification
import java.util.Calendar
import java.util.concurrent.TimeUnit


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

        programarAlarmaDolar()
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

    //funcion para que se mande notificacion del precio del dolar
    private fun programarAlarmaDolar() {
        val workManager= WorkManager.getInstance(this)

        val now = Calendar.getInstance()
        val target = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY,12)
            set(Calendar.MINUTE,47)
            set(Calendar.SECOND,0)
        }

        if (target.before(now)){
            target.add(Calendar.DAY_OF_YEAR,1)
            }
        val initWait = target.timeInMillis - now.timeInMillis
        val request = PeriodicWorkRequestBuilder<WorkerNotification>(8, TimeUnit.HOURS)
            .setInitialDelay(initWait, TimeUnit.MILLISECONDS)
            .addTag("precio_dolar")
            .build()

        workManager.enqueueUniquePeriodicWork(
            "Monitoreo_Dolar",
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )


    }
}





