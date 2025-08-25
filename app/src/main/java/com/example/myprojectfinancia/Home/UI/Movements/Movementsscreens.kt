package com.example.myprojectfinancia.Home.UI.Movements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myprojectfinancia.Home.UI.ViewModels.ListMount
import com.example.myprojectfinancia.Home.UI.home.ViewModels.homeViewModel

//@Preview(showSystemUi = true)
//@Composable
//fun ejemplo(modifier: Modifier = Modifier) {
//    MyProjectFinanciaTheme {
//        MovementsScreens(modifier.background(MaterialTheme.colorScheme.background))
//    }
//}


@Composable
fun MovementsScreens(padding: PaddingValues, modifier: Modifier, homeViewModel: homeViewModel) {
    val isLoading by homeViewModel.isLoading.collectAsState()
    val isPressedIngresos by homeViewModel.ingrsosPressed.collectAsState()
    val isPressedGastos by homeViewModel.egresosIsPressed.collectAsState()
    val error by homeViewModel.Error.collectAsState()


    // carga de pestanas de movimientos
    LaunchedEffect(isPressedIngresos) {
        val natureSelected = if (isPressedIngresos) "Ingreso" else "Gasto"
        homeViewModel.getMovements(nature = natureSelected)

    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(padding)
            .padding(10.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(modifier = modifier.fillMaxWidth()) {
            Selecction(modifier, homeViewModel, isPressedIngresos, isPressedGastos)
            error?.let { errorMsg ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = "Error al cargar $errorMsg",
                        modifier = Modifier.padding(12.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }

            Spacer(modifier = modifier.padding(6.dp))
            MovementList(modifier, homeViewModel, isLoading)
        }
    }


}

@Composable
fun Selecction(modifier: Modifier, homeViewModel: homeViewModel, isPressedIngresos: Boolean, isPressedGastos: Boolean) {


    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Button(
            onClick = {
                homeViewModel.ingresosIsPressed()
            },
            colors = ButtonDefaults.buttonColors(
                if (isPressedIngresos) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSecondary,
                contentColor = if (isPressedIngresos) MaterialTheme.colorScheme.onSecondary
                else MaterialTheme.colorScheme.background
            ), modifier = modifier.weight(1f), shape = MaterialTheme.shapes.small
        ) {
            Text("Ingresos", fontSize = 20.sp)
        }
        Button(
            onClick = {
                homeViewModel.gastosIsPressed()
            },
            colors = ButtonDefaults.buttonColors(

                containerColor = if (isPressedGastos) Color(0xD59E0000)
                else MaterialTheme.colorScheme.onSecondary,
                contentColor = if (isPressedGastos) MaterialTheme.colorScheme.onSecondary
                else MaterialTheme.colorScheme.background
            ), modifier = modifier.weight(1f), shape = MaterialTheme.shapes.small
        ) {
            Text("Gastos", fontSize = 20.sp)
        }


    }
}

@Composable
fun MovementList(
    modifier: Modifier,
    homeViewModel: homeViewModel,
    isLoading: Boolean,

    ) {
    val movements by homeViewModel.Movements.collectAsState()
    Box(modifier = modifier.padding(6.dp)) {
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }


        } else {
            if (movements.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No hay movimientos aun",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Light,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()

                    ) {
                        items(movements) { items ->
                            ListMount(items)

                        }
                    }
                }
            }
        }

    }


}




