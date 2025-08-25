package com.example.myprojectfinancia.Home.UI.ViewModels

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.myprojectfinancia.Home.UI.home.Models.Movements.Movimiento
import com.example.myprojectfinancia.Home.UI.home.UI.PreupuestoDialog
import com.example.myprojectfinancia.Home.UI.home.ViewModels.homeViewModel
import com.example.myprojectfinancia.R

@Composable
fun HomeScreen(
    modifier: PaddingValues,
    homeViewModel: homeViewModel
) {
    val isPressedIngresos by homeViewModel.ingrsosPressed.collectAsState()
    val isPressedGastos by homeViewModel.egresosIsPressed.collectAsState()
    val showDialog by homeViewModel.showDialog.observeAsState(false)
    val monto by homeViewModel.monto.observeAsState("")
    val categoria by homeViewModel.category.observeAsState("")
    val fecha = homeViewModel.fechaActual
    val name: String by homeViewModel.name.collectAsState("")
    val ingresos by homeViewModel.TotalIngresos.collectAsState()
    val gastos by homeViewModel.TotalGastos.collectAsState()
    val presupuesto by homeViewModel.Presupuesto.collectAsState()
    val isLoading by homeViewModel.isLoading.collectAsState()


    LaunchedEffect(Unit) {
        homeViewModel.initializeNewUser()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(modifier)


    ) {
        Column(Modifier.fillMaxWidth()) {


            Greeteng(name)
            Presupuesto(presupuesto, homeViewModel)
            PlanDeAhorro()
            Movimientos(homeViewModel, isLoading)
            AggCuenta(
                showDialog,
                monto,
                categoria,
                homeViewModel,
                isPressedIngresos,
                isPressedGastos,
                fecha
            )
            PreupuestoDialog(homeViewModel = homeViewModel)

        }
        FAB(
            homeViewModel = homeViewModel,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        )

    }

}


//boton flotante
@Composable
fun FAB(homeViewModel: homeViewModel, modifier: Modifier) {
    FloatingActionButton(
        onClick = {
            homeViewModel.mostrarDialog()

        },
        modifier = modifier,
        shape = FloatingActionButtonDefaults.smallShape

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
                        "Editar Cuenta",
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
                homeViewModel.guardarMovimiento()
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
        ) { Text("Cancelar", color = MaterialTheme.colorScheme.onSecondary) }
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
                    }, shape = MaterialTheme.shapes.small,
                    colors = ButtonDefaults.buttonColors(
                        if (isPressedIngresos) Color(0x8B024910) else Color(0xC105E32F)

                    )
                ) { Text("Ingreso") }
                Spacer(Modifier.width(10.dp))
                Button(
                    onClick = {
                        homeViewModel.gastosIsPressed()
                    }, shape = MaterialTheme.shapes.small, colors = ButtonDefaults.buttonColors(
                        if (isPressedGastos) Color(0x90530A0A) else Color(0xDFFC2323)
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
fun Movimientos(HomeViewModel: homeViewModel, isLoading: Boolean) {
    val tabs = listOf("Ingresos", "Gastos")
    var selectedTab by rememberSaveable() { mutableStateOf(0) }
    val movements by HomeViewModel.Movements.collectAsState()
    val showMovements by HomeViewModel.showMovements.collectAsState()
    val error by HomeViewModel.Error.collectAsState()

    // carga de pestanas de movimientos
    LaunchedEffect(selectedTab) {
        val natureSelected = if (selectedTab == 0) "Ingreso" else "Gasto"
        try {
            HomeViewModel.getMovements(nature = natureSelected)
        } catch (e: Exception) {
            Log.e("Movimientos", "Error al obtener movimientos: ${e.message}")

        }
    }


    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.End)
                .height(30.dp)
        ) {
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
                    onClick = { selectedTab = index }
                ) {
                    Text(title, fontSize = 24.sp)
                }

            }
        }
        Spacer(modifier = Modifier.height(4.dp))

        if (isLoading) {

            Box(
                Modifier
                    .fillMaxSize()
                    .align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }

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
                        .fillMaxWidth()
                ) {

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                    ) {
                        items(movements) { items ->
                            ListMount(items)

                        }
                    }
                }
            }
        }



        MovimientosDialog(showMovements, HomeViewModel)
    }
}


//lista de movimientos
@Composable
fun MovimientosDialog(
    showMovements: Boolean,
    homeViewModel: homeViewModel
) {
    val movements by homeViewModel.allMovements.collectAsState()
    val error by homeViewModel.ErrorAllMovements.collectAsState("")

    LaunchedEffect(showMovements) {
        if (showMovements) {
            try {
                homeViewModel.getAllMovements()
            } catch (e: Exception) {
                Log.e("MovimientosDialog", "Error al cargar movimientos: ${e.message}")
            }
        }
    }

    if (showMovements) {

        Dialog(onDismissRequest = { homeViewModel.showMovements(false) }) {
            Card(
                modifier = Modifier
                    .height(600.dp)
                    .width(500.dp)
            ) {
                Column(
                    Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Movimientos Realizados",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(34.dp)

                    )

                    when {
                        error != null -> {
                            Text(
                                error.toString(),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Light,
                                textAlign = TextAlign.Center
                            )
                        }

                        movements.isEmpty() -> {
                            Text(
                                "No hay movimientos aun",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Light,
                                textAlign = TextAlign.Center
                            )
                        }

                        else -> {
                            LazyColumn {

                                items(movements) { movimiento ->
                                    ListMount(movimiento)
                                }
                            }
                        }
                    }
                }
            }

        }


    }

}

//item de la lista de movimientos
@Composable
fun ListMount(
    movimiento: Movimiento
) {
    Card(modifier = Modifier.padding(10.dp)) {
        ListItem(headlineContent = { Text(movimiento.monto) },
            overlineContent = { Text(movimiento.categoria) },
            supportingContent = { Text(movimiento.fecha) },
            leadingContent = {
                Icon(painter = painterResource(R.drawable.ic_money), contentDescription = "Money")
            },
            trailingContent = {
                Text(movimiento.naturaleza)
            }
        )
    }


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
fun Presupuesto(presupuesto: Double, homeViewModel: homeViewModel) {

    PresupuestoCard(presupuesto, homeViewModel)

}

@Composable
fun PresupuestoCard(presupuesto: Double, homeViewModel: homeViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Box(
            Modifier.background(
                brush = Brush.horizontalGradient(
                    listOf(
                        MaterialTheme.colorScheme.primaryContainer, MaterialTheme
                            .colorScheme.onPrimaryContainer
                    )
                )
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                ) {

                    Text("Presupuesto", fontSize = 35.sp, fontWeight = FontWeight.Bold)

                    Spacer(Modifier.padding(5.dp))

                    Row() {
                        Text(
                            "$${presupuesto}/ ", fontSize = 35.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            "Bs.${presupuesto * 125.0}",
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                    }
                }
                Icon(
                    painter = painterResource(R.drawable.add_ic),
                    contentDescription = "presupuesto",
                    Modifier
                        .size(50.dp)
                        .clickable { homeViewModel.mostrarDialogBudget() },
                    tint = MaterialTheme.colorScheme.tertiary
                )
            }

        }


    }

}

//Saludo pantalla principal
@Composable
fun Greeteng(name: String) {

    Box(Modifier.padding(10.dp)) {
        Text(
            "Bienvenido ${name}",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
        )
        Log.i("user", "Usuario posee el nombre de ${name}")
    }


}