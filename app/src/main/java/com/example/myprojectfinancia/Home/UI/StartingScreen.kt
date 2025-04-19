package com.example.myprojectfinancia.Home.UI


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.myprojectfinancia.Login.ui.ViewModel.homeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InitialView(navController: NavHostController, homeViewModel: homeViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Financia") },
                actions = {
                    Button(onClick = {
                        val route = homeViewModel.logout()
                        navController.navigate(route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                    ) {
                        Text("Salir")
                    }
                }
            )
        }
    ) { paddinContent ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(paddinContent),
            contentAlignment = Alignment.Center
        ) {
            Greeteng()
        }
    }


}

@Composable
fun Greeteng() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Bienvenido", fontSize = 45.sp, fontWeight = FontWeight.Bold)
    }
}
