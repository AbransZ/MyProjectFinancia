package com.example.myprojectfinancia.Home.UI


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.myprojectfinancia.Home.UI.ViewModels.homeViewModel
import com.example.myprojectfinancia.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InitialView(navController: NavHostController, homeViewModel: homeViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Financia", fontWeight = FontWeight.Bold, fontSize = 30.sp) },
                actions = {
                    Button(
                        onClick = {
                            val route = homeViewModel.logout()
                            navController.navigate(route) {
                                popUpTo(0) { inclusive = true }
                            }
                        }/*, colors = ButtonDefaults.buttonColors(
                            contentColor = Color(0xFFFFFFFF),
                            containerColor = Color(0xFF1C82EC)
                        )*/
                    ) {
                        Text("Salir", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    }
                }
            )
        },
        floatingActionButton = { FAB() }
    ) { paddinContent ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(paddinContent)
            // .background(color = Color(0xFFECECEC))
        ) {
            Box {
                Column(modifier = Modifier.fillMaxSize()) {
                Greeteng()
                Presupuesto()
                Spacer(modifier = Modifier.padding(10.dp))
                PlanDeAhorro()
                Spacer(modifier = Modifier.padding(10.dp))
                Movimientos()

            } }

        }
    }


}

@Preview
@Composable
fun FAB() {
    FloatingActionButton(
        onClick = {

                  },
        modifier = Modifier,
        shape = FloatingActionButtonDefaults.smallShape

    ) {
        Icon(
            painter = painterResource(R.drawable.ic_add),
            contentDescription = "add Bill",
            modifier = Modifier.size(45.dp)
        )
    }
}

@Composable
fun AggCuenta() {

}


@Composable
fun Movimientos() {
    val tabs = listOf("Ingresos", "Gastos")
    var selectedTab by remember { mutableStateOf(0) }

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                ) {
                    Text(title, fontSize = 24.sp)
                }

            }
        }
        LazyColumn {
            item {
                ListMount()

            }
        }


    }

}

@Composable
fun ListMount() {

}


@Composable
fun PlanDeAhorro() {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.Center
    ) {

        ElevatedCard(
            onClick = {},
            modifier = Modifier
                .size(width = 150.dp, height = 200.dp),
            shape = CardDefaults.elevatedShape,
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 10.dp),
            /*colors = CardDefaults.elevatedCardColors(
                containerColor = Color(0xFFDEDEDE)
            )*/
        ) {
            Text(
                "Plan de Ahorro",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)/*,
                color = Color.Black*/
            )
        }
        Spacer(Modifier.padding(10.dp))
        ElevatedCard(
            onClick = {},
            modifier = Modifier
                .size(width = 280.dp, height = 200.dp),
            shape = CardDefaults.elevatedShape,
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 10.dp),
            /*colors = CardDefaults.elevatedCardColors(
                containerColor = Color(0xFFDEDEDE)
            )*/
        ) {
            Text(
                "Grafico de Gastos",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)/*,
               // color = Color.Black*/
            )
        }
    }
}


@Composable
fun Presupuesto() {
    Box(Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {
            Text(
                "Presupuesto",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold, /*color = Color.Black*/
            )
            Text("$1000", fontSize = 15.sp, fontWeight = FontWeight.Bold /*color = Color.Gray*/)
        }
    }
}

@Composable
fun Greeteng() {
    Box(Modifier.padding(10.dp)) {
        Text(
            "Bienvenido 'User'",
            fontSize = 45.sp,
            fontWeight = FontWeight.Bold,
            // color = Color(0xFF565657)
        )
    }
}
