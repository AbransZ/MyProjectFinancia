package com.example.myprojectfinancia.Home.UI


import android.util.Log
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.myprojectfinancia.Home.Movimiento
import com.example.myprojectfinancia.Home.UI.ViewModels.homeViewModel
import com.example.myprojectfinancia.Login.ui.ViewModel.LoginViewModel
import com.example.myprojectfinancia.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InitialView(navController: NavHostController, homeViewModel: homeViewModel,loginViewModel: LoginViewModel) {
    val isPressedIngresos by homeViewModel.ingrsosPressed.collectAsState()
    val isPressedGastos by homeViewModel.egresosIsPressed.collectAsState()
    val showDialog by homeViewModel.showDialog.observeAsState(false)
    val monto by homeViewModel.monto.observeAsState("")
    val categoria by homeViewModel.category.observeAsState("")
    val fecha = homeViewModel.fechaActual



    Scaffold(topBar = {
        TopAppBar(title = { Text("Financia", fontWeight = FontWeight.Bold, fontSize = 30.sp) },
            actions = {
                Button(onClick = {
                    val route = homeViewModel.logout()

                    navController.navigate(route) {
                        popUpTo(0) { inclusive = true }
                        loginViewModel.clearFields()
                    }
                }) {
                    Text("Salir", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }
            })
    },
        floatingActionButton = { FAB(homeViewModel) },
        bottomBar = { BarraDeBotones() }) { paddinContent ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddinContent)

        ) {
            LazyColumn(

                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                //Box {
                // Column(modifier = Modifier.fillMaxSize()) {
                item { Greeteng() }
                item { Presupuesto() }

                item { PlanDeAhorro() }

                item { Movimientos(homeViewModel) }
                item {
                    AggCuenta(
                        showDialog,
                        monto,
                        categoria,
                        homeViewModel,
                        isPressedIngresos,
                        isPressedGastos,
                        fecha
                    )
                }
            }
        }
    }
}

//Barra de botones
@Composable
fun BarraDeBotones() {
    NavigationBar {

        //Inicio
        NavigationBarItem(selected = false, onClick = {}, icon = {
            Icon(
                painter = painterResource(R.drawable.ic_home),
                contentDescription = "Home",
                modifier = Modifier.size(40.dp)
            )
        }, label = { Text("Inicio") })

        //planes de ahorro
        NavigationBarItem(selected = false, onClick = {}, icon = {
            Icon(
                painter = painterResource(R.drawable.ic_ahorro),
                contentDescription = "Planes de ahorro",
                modifier = Modifier.size(40.dp)
            )
        }, label = { Text("P. Ahorro") })

        //Movimientos
        NavigationBarItem(selected = false, onClick = {}, icon = {
            Icon(
                painter = painterResource(R.drawable.ic_walet),
                contentDescription = "Movimientos",
                modifier = Modifier.size(40.dp)
            )
        }, label = { Text("Movimientos") })

        //Ajustes
        NavigationBarItem(selected = false, onClick = {}, icon = {
            Icon(
                painter = painterResource(R.drawable.ic_settings),
                contentDescription = "settings",
                modifier = Modifier.size(40.dp)
            )
        }, label = { Text("Ajustes") })
    }
}

//boton flotante
@Composable
fun FAB(homeViewModel: homeViewModel) {
    FloatingActionButton(
        onClick = {
            homeViewModel.mostrarDialog()
        }, modifier = Modifier, shape = FloatingActionButtonDefaults.smallShape

    ) {
        Icon(
            painter = painterResource(R.drawable.ic_add),
            contentDescription = "add Bill",
            modifier = Modifier.size(45.dp)
        )
    }
}

//Dialogo editar cuenta
@Composable
fun EditCuenta(
    showDialog: Boolean,
    monto: String,
    categoria: String,
    homeViewModel: homeViewModel,
    isPressedIngresos: Boolean,
    isPressedGastos: Boolean,
    fecha: String
) {
    if (showDialog) {
        Dialog(onDismissRequest = { homeViewModel.ocultarDialog() }) {
            Box(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.surface)

            ) {
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
                    Spacer(Modifier.padding(10.dp))
                    BodyDialog(
                        homeViewModel, isPressedIngresos, isPressedGastos, monto, categoria, fecha
                    )
                    ButtonsDialog(homeViewModel, isPressedIngresos)

                }
            }
        }
    }

}

//Dialogo agregar cuenta
@Composable
fun AggCuenta(
    showDialog: Boolean,
    monto: String,
    categoria: String,
    homeViewModel: homeViewModel,
    isPressedIngresos: Boolean,
    isPressedGastos: Boolean,
    fecha: String
) {
    if (showDialog) {
        Dialog(onDismissRequest = { homeViewModel.ocultarDialog() }) {
            Box(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.surface)

            ) {
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
                    Spacer(Modifier.padding(10.dp))
                    BodyDialog(
                        homeViewModel, isPressedIngresos, isPressedGastos, monto, categoria, fecha
                    )
                    ButtonsDialog(homeViewModel, isPressedIngresos)

                }
            }
        }
    }

}

//Botones confirmar o cancelar dialogo
@Composable
fun ButtonsDialog(homeViewModel: homeViewModel, isPressedIngresos: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp), horizontalArrangement = Arrangement.End
    ) {
        Button(
            onClick = {
                if (isPressedIngresos) {
                    Log.i("movimientos", "Ingreso se presiono")
                } else Log.i("movimientos", "Gasto se presiono")
                homeViewModel.clearfields()

            }, shape = MaterialTheme.shapes.small, colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) { Text("Guardar") }
        Spacer(Modifier.width(10.dp))
        Button(
            onClick = { homeViewModel.ocultarDialog() },
            shape = MaterialTheme.shapes.small,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            )
        ) { Text("Cancelar") }
    }
}

//cuerpo del dialogo
@Composable
fun BodyDialog(
    homeViewModel: homeViewModel,
    isPressedIngresos: Boolean,
    isPressedGastos: Boolean,
    monto: String,
    categoria: String,
    fecha: String
) {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(Modifier.fillMaxWidth()) {

            //Botones de ingresos y gastos
            Row(
                Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        homeViewModel.ingresosIsPressed()
                    }, shape = MaterialTheme.shapes.small, colors = ButtonDefaults.buttonColors(
                        if (isPressedIngresos) Color(0x8B024910) else Color(0x7A05E32F)

                    )
                ) { Text("Ingreso") }
                Spacer(Modifier.width(10.dp))
                Button(
                    onClick = {
                        homeViewModel.gastosIsPressed()
                    }, shape = MaterialTheme.shapes.small, colors = ButtonDefaults.buttonColors(
                        if (isPressedGastos) Color(0x90530A0A) else Color(0x75FC2323)
                    )
                ) {
                    Text("Gasto")
                }
            }

            //Campos de texto para ingresar monto y categoria
            Spacer(Modifier.padding(8.dp))
            Text("Monto", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.padding(6.dp))
            OutlinedTextField(
                value = monto,
                onValueChange = { homeViewModel.onMontoChange(it) },
                placeholder = { Text("Ingresar monto.") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.height(56.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            )
            Spacer(Modifier.padding(5.dp))
            Text("Categoria", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.padding(6.dp))
            OutlinedTextField(
                value = categoria,
                onValueChange = { homeViewModel.onCategoriaChange(it) },
                placeholder = { Text("Ej. Salario, Comida, Salario.") },

                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.height(56.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            )
            Spacer(Modifier.padding(5.dp))
            Text("Fecha $fecha", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        }
    }

}

// seleccionador de movimientos
@Composable
fun Movimientos(HomeViewModel: homeViewModel) {
    val tabs = listOf("Ingresos", "Gastos")
    var selectedTab by remember { mutableStateOf(0) }
    val showMovements by HomeViewModel.showMovements.collectAsState()
    val fecha = HomeViewModel.fechaActual
    val monto = HomeViewModel.monto.observeAsState("")
    val categoria = HomeViewModel.category.observeAsState("")
    val naturaleza = HomeViewModel.naturaleza.observeAsState("")

    // Listas de ejemplo para cada pestaña
    val ingresos = listOf(
        Movimiento("24/10/2022", "$ 2000", "Salario", "Ingreso"),
        Movimiento("25/10/2022", "$ 500", "Freelance", "Ingreso"),
        Movimiento("26/10/2022", "$ 300", "Venta", "Ingreso")
    )

    val gastos = listOf(
        Movimiento("24/10/2022", "$ 150", "Comida", "Gasto"),
        Movimiento("25/10/2022", "$ 80", "Transporte", "Gasto"),
        Movimiento("26/10/2022", "$ 200", "Entretenimiento", "Gasto")
    )

    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Box(modifier = Modifier
            .align(Alignment.End)
            .height(30.dp)) {
            TextButton(
                onClick = { HomeViewModel.showMovements(true) },
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.primary)
            ) { Text("Ver Todos") }
        }

        TabRow(
            modifier = Modifier
                .height(30.dp)
                .clip(RoundedCornerShape(5.dp)),
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
        MovimientosDialog(showMovements, fecha, monto, categoria, naturaleza, HomeViewModel)
        val movimientosAMostrar = if (selectedTab == 0) ingresos else gastos
        Column() {
            movimientosAMostrar.forEach { movimiento ->
                ListMount(
                    fecha = movimiento.fecha,
                    monto = movimiento.monto,
                    categoria = movimiento.categoria,
                    naturaleza = movimiento.naturaleza
                )
            }


        }


    }
}

//lista de movimientos
@Composable
fun MovimientosDialog(
    showMovements: Boolean,
    fecha: String,
    monto: State<String>,
    categoria: State<String>,
    naturaleza: State<String>,
    homeViewModel: homeViewModel
) {
    val movimientos = listOf<Movimiento>()

    if (showMovements) {
        Dialog(onDismissRequest = { homeViewModel.showMovements(false) }) {
            LazyColumn {
                items(10) { movimiento ->
                    ListMount(fecha, "$ ${monto.value}", categoria.value, naturaleza.value)
                }
            }
        }
    }

}

//item de la lista de movimientos
@Composable
fun ListMount(
    fecha: String, monto: String, categoria: String, naturaleza: String
) {

    ListItem(headlineContent = { Text(categoria.toString()) },
        overlineContent = { Text(monto.toString()) },
        supportingContent = { fecha },
        leadingContent = {
            Icon(painter = painterResource(R.drawable.ic_money), contentDescription = "Money")
        },
        trailingContent = { Text(naturaleza.toString()) })

}

//Informacion de ahorro pantalla principal
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
            modifier = Modifier.size(width = 150.dp, height = 200.dp),
            shape = CardDefaults.elevatedShape,
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 10.dp),

            ) {
            Text(
                "Planes de Ahorro",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )
        }
        Spacer(Modifier.padding(10.dp))
        ElevatedCard(
            onClick = {},
            modifier = Modifier.size(width = 280.dp, height = 200.dp),
            shape = CardDefaults.elevatedShape,
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 10.dp),

            ) {
            Text(
                "Grafico de planes",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

//Presupuesto pantalla principal
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
                fontWeight = FontWeight.Bold,
            )
            Text("$1000", fontSize = 15.sp, fontWeight = FontWeight.Bold)
        }
    }
}

//Saludo pantalla principal
@Composable
fun Greeteng() {
    Box(Modifier.padding(10.dp)) {
        Text(
            "Bienvenido 'User'",
            fontSize = 45.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}
