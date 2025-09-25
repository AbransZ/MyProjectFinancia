package com.example.myprojectfinancia.Index.Login.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myprojectfinancia.Index.Login.ViewModel.SpalshViewModel
import com.example.myprojectfinancia.Model.Routes
import com.example.myprojectfinancia.R
import kotlinx.coroutines.delay


@Composable
fun Spalsh(splashViewModel: SpalshViewModel, navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "LogoApp",
            modifier = Modifier
                .size(250.dp)
                .clip(RoundedCornerShape(80.dp))
        )
    }

    LaunchedEffect(Unit) {
        delay(1000)
        val destination = splashViewModel.checkDestination()
        navController.navigate(destination) {

            popUpTo(Routes.SplashScreen.routes) { inclusive = true }

        }
    }
}