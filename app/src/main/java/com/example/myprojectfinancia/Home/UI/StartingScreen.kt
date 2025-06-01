package com.example.myprojectfinancia.Home.UI


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.myprojectfinancia.Home.UI.ViewModels.homeViewModel
import com.example.myprojectfinancia.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InitialView(navController: NavHostController, homeViewModel: homeViewModel) {
    val categoria by homeViewModel.category.observeAsState("")
    val nature by homeViewModel.naturaleza.observeAsState("")
    val valor: String by homeViewModel.monto.observeAsState("")
    val showDialog by homeViewModel.showDialog.observeAsState(false)
    val natureExpanded by homeViewModel.natureExpanded.observeAsState(false)


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
        floatingActionButton = { FAB(homeViewModel) }
    ) { paddinContent ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddinContent)

        ) {
            Box {
                Column(modifier = Modifier.fillMaxSize()) {
                    Greeteng()
                    Presupuesto()
                    Spacer(modifier = Modifier.padding(10.dp))
                    PlanDeAhorro()
                    Spacer(modifier = Modifier.padding(10.dp))
                    Movimientos()
                    AggCuenta(valor, nature, showDialog, natureExpanded, homeViewModel)

                }
            }

        }
    }


}


@Composable
fun FAB(homeViewModel: homeViewModel) {
    FloatingActionButton(
        onClick = {
            homeViewModel.mostrarDialog()
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
fun AggCuenta(
    valor: String,
    nature: String,
    showDialog: Boolean,
    expanded: Boolean,
    homeViewModel: homeViewModel
) {
    if (showDialog) {
        Dialog(onDismissRequest = { homeViewModel.ocultarDialog() }) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    "Agregar Cuenta",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(Modifier.padding(18.dp))
                TextField(
                    value = valor, onValueChange = { homeViewModel.onMontoChange(it) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    maxLines = 1,
                    singleLine = true,
                    modifier = Modifier.height(48.dp)
                )
                Spacer(Modifier.padding(15.dp))
                NatureMenu(nature, expanded, homeViewModel)


            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NatureMenu(nature: String, expanded: Boolean, homeViewModel: homeViewModel) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { homeViewModel.onNatureExpandedChange(it) },
        modifier = Modifier.width(180.dp)
    ) {
        TextField(
            value = nature,
            onValueChange = { homeViewModel.onNaturalezaChange(it) },
            readOnly = true,
            label = {
                Text(
                    "Naturaleza de la Transaccion",
                    // fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
                .height(48.dp),
            textStyle = TextStyle(fontSize = 18.sp)
        )
    }
    DropdownMenu(expanded, onDismissRequest = {homeViewModel.onNatureExpandedChange(false)})
    {
        DropdownMenuItem(text = { Text("Ingresos", fontSize = 18.sp) }, onClick = {
            homeViewModel.onNaturalezaChange("Ingresos")
            homeViewModel.onNatureExpandedChange(false)

        })
        DropdownMenuItem(text = { Text("Gastos", fontSize = 18.sp) }, onClick = {

            homeViewModel.onNaturalezaChange("Gastos")
            homeViewModel.onNatureExpandedChange(false)

        })
    }
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
