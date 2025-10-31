package com.example.myprojectfinancia.Index.UI.ViewModels

import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.myprojectfinancia.Data.API.StateUI.UiStateDolar
import com.example.myprojectfinancia.Data.API.network.DolarOficial
import com.example.myprojectfinancia.Index.Plans.ModelsPlans.planItem
import com.example.myprojectfinancia.Index.Plans.ViewModel.PlansViewModel
import com.example.myprojectfinancia.Index.home.Models.Movements.Movimiento
import com.example.myprojectfinancia.Index.home.UI.PreupuestoDialog
import com.example.myprojectfinancia.Index.home.ViewModels.homeViewModel
import com.example.myprojectfinancia.R

@Composable
fun HomeScreen(
    modifier: PaddingValues,
    homeViewModel: homeViewModel,
    plansViewModel: PlansViewModel
) {
    val context = LocalContext.current
    val isPressedIngresos by homeViewModel.ingrsosPressed.collectAsState()
    val isPressedGastos by homeViewModel.egresosIsPressed.collectAsState()
    val showDialog by homeViewModel.showDialog.observeAsState(false)
    val monto by homeViewModel.monto.observeAsState("")
    val montoBs by homeViewModel.montoBsstring.collectAsState("")
    val categoria by homeViewModel.category.observeAsState("")
    val fecha = homeViewModel.fechaActual
    val name: String by homeViewModel.name.collectAsState("")
    val presupuesto by homeViewModel.presupuesto.collectAsState()
    val isLoading by homeViewModel.isLoading.collectAsState()
    val error by homeViewModel.Error.collectAsState()
    val selectedTab by homeViewModel.currentSelectedTab.collectAsState()
    val plans by plansViewModel.plans.collectAsState()
    val missing by plansViewModel.missing.collectAsState()
    val totalSaved by plansViewModel.totalSaved.collectAsState()
    val budgetFree by plansViewModel.budgetfree.collectAsState()
    val UIDolar by homeViewModel.UIDolar.collectAsState()
    val DolarObject by homeViewModel.DolarObject.collectAsState()

    LaunchedEffect(error) {
        error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG)
                .show()
            homeViewModel.clearError()
        }
    }

    LaunchedEffect(Unit) {
        homeViewModel.initializeNewUser()
        kotlinx.coroutines.delay(50)
        plansViewModel.initializePlansUser()
        homeViewModel.getDolarBCV()
    }
    //Caja que contiene toda la Screen
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(modifier)


    ) {
        Column(Modifier.fillMaxWidth()) {


            Greeteng(name)
            PresupuestoCard(presupuesto, homeViewModel, UIDolar, DolarObject, Modifier.weight(1f))
            PlanDeAhorro(plans, plansViewModel, missing, totalSaved, budgetFree, UIDolar, DolarObject, homeViewModel)
            Movimientos(homeViewModel, isLoading)
            AggCuenta(
                showDialog,
                monto,
                montoBs,
                categoria,
                homeViewModel,
                isPressedIngresos,
                isPressedGastos,
                fecha,
                DolarObject
            )
            PreupuestoDialog(Modifier, homeViewModel, DolarObject)


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
            modifier = Modifier.size(35.dp)
        )
    }
}


//Dialogo agregar cuenta
@Composable
fun AggCuenta(
    showDialog: Boolean,
    monto: String,
    montoBs: String,
    categoria: String,
    homeViewModel: homeViewModel,
    isPressedIngresos: Boolean,
    isPressedGastos: Boolean,
    fecha: String,
    DolarObject: DolarOficial?
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
                        homeViewModel, isPressedIngresos, isPressedGastos, monto, montoBs, categoria, fecha, DolarObject
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
            .padding(6.dp), horizontalArrangement = Arrangement.Center
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
            ), modifier = Modifier.weight(1f)
        ) { Text("Guardar", fontSize = 20.sp) }
        Spacer(Modifier.width(10.dp))
        Button(
            onClick = { homeViewModel.ocultarDialog() },
            shape = MaterialTheme.shapes.small,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            ), modifier = Modifier.weight(1f)
        ) { Text("Cancelar", fontSize = 20.sp, color = MaterialTheme.colorScheme.onPrimary) }
    }
}

//cuerpo del dialogo
@Composable
fun BodyDialog(
    homeViewModel: homeViewModel,
    isPressedIngresos: Boolean,
    isPressedGastos: Boolean,
    monto: String,
    montoBs: String,
    categoria: String,
    fecha: String,
    dolarObject: DolarOficial?
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
                        if (isPressedIngresos) Color(0x8B024910) else Color(0xC147F1C6)

                    ),
                    modifier = Modifier

                        .weight(1f)
                ) { Text("Ingreso", fontSize = 18.sp) }
                Spacer(Modifier.width(10.dp))
                Button(
                    onClick = {
                        homeViewModel.gastosIsPressed()
                    }, shape = MaterialTheme.shapes.small, colors = ButtonDefaults.buttonColors(
                        if (isPressedGastos) Color(0x90530A0A) else Color(0xA9EF7352)
                    ),
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text("Gasto", fontSize = 18.sp)
                }
            }

            //Campos de texto para ingresar monto y categoria
            Spacer(Modifier.padding(8.dp))
            Text("Monto", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 40.dp))
            Spacer(Modifier.padding(4.dp))
            OutlinedTextField(
                value = monto,
                onValueChange = {
                    homeViewModel.onMontoChange(it, dolarObject?.promedio)
                },
                placeholder = { Text("Ingresar monto.", fontSize = 15.sp) },
                prefix = { Text("$", fontSize = 15.sp) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .height(56.dp)
                    .width(210.dp)
                    .align(Alignment.CenterHorizontally),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            )
            Spacer(Modifier.padding(10.dp))
            OutlinedTextField(
                value = montoBs,
                onValueChange = {
                    homeViewModel.onMontoBsChange(it, dolarObject?.promedio)
                },
                placeholder = { Text("Ingresar monto.", fontSize = 15.sp) },
                prefix = { Text("Bs.", fontSize = 15.sp) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .height(58.dp)
                    .width(210.dp)
                    .align(Alignment.CenterHorizontally),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            )
            Spacer(Modifier.padding(8.dp))
            Text(
                "Categoria", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 40.dp)
            )
            Spacer(Modifier.padding(4.dp))
            OutlinedTextField(
                value = categoria,
                onValueChange = { homeViewModel.onCategoriaChange(it) },
                placeholder = { Text("Ej. Salario, Comida, Mercado.", fontSize = 15.sp) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .height(60.dp)
                    .width(210.dp)
                    .align(Alignment.CenterHorizontally),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            )
            Spacer(Modifier.padding(5.dp))
            Box(Modifier.fillMaxWidth()) {
                Text(
                    "Fecha $fecha", fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding
                        (start = 40.dp)
                )
            }


        }
    }


}


// seleccionador de movimientos
@Composable
fun Movimientos(
    HomeViewModel: homeViewModel,
    isLoading: Boolean
) {
    val tabs = listOf("Ingresos", "Gastos")
    var selectedTab by rememberSaveable() { mutableStateOf(0) }
    val movements by HomeViewModel.Movements.collectAsState()


    // carga de pestanas de movimientos
    LaunchedEffect(selectedTab) {

        HomeViewModel.updateSelectedTab(selectedTab)
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

        TabRow(
            modifier = Modifier
                .height(40.dp)
                .width(340.dp)
                .align(Alignment.CenterHorizontally)
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
                            ListMount(items, HomeViewModel)

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
    movimiento: Movimiento,
    HomeViewModel: homeViewModel
) {
    val montoBs = movimiento.montoBs.toDouble()
    val precioUSD = HomeViewModel.convertBsToUSD(montoBs)
    Card(
        modifier = Modifier
            .padding(4.dp)
    ) {

        ListItem(
            headlineContent = {
                Row {
                    Text("$${precioUSD}/ ", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    if (montoBs != null) {
                        Text(
                            "Bs.${String.format("%.2f", montoBs)}", fontSize = 12.sp, fontWeight = FontWeight
                                .ExtraLight
                        )
                    } else {
                        Text("Bs.---", fontSize = 10.sp)
                    }
                }

            },
            overlineContent = { Text(movimiento.categoria, fontSize = 20.sp, fontWeight = FontWeight.ExtraLight) },
            supportingContent = { Text(movimiento.fecha, fontSize = 15.sp) },
            leadingContent = {
                Icon(
                    painter = painterResource(R.drawable.ic_money), contentDescription = "Money", modifier =
                    Modifier
                        .size(30.dp)
                )
            },
            trailingContent = {
                Text(movimiento.naturaleza, fontSize = 14.sp, color = MaterialTheme.colorScheme.onPrimary)
            }
        )
    }


}


//Informacion de ahorro pantalla principal
@Composable
fun PlanDeAhorro(
    plans: List<planItem>,
    plansViewModel: PlansViewModel,
    missing: Double,
    totalSaved: Double,
    budgetFree: Double,
    UIDolar: UiStateDolar,
    DolarObject: DolarOficial?,
    homeViewModel: homeViewModel
) {

    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.Center
    ) {

        ElevatedCard(
            modifier = Modifier
                .weight(1f)
                .height(260.dp),
            shape = CardDefaults.elevatedShape,
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 10.dp),

            ) {
            Text(
                "Plan de Ahorro",
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal,
                lineHeight = 14.sp,
                maxLines = 2,
                modifier = Modifier.padding(10.dp)
            )
            if (plans.isEmpty()) {
                Text(
                    "Aqui se veran sus metas cuando comience a ahorrar",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Light,
                    modifier = Modifier.padding(16.dp)
                )
            } else {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp), contentAlignment = Alignment.Center
                ) {
                    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceAround) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {


                            Text("Ahorrado", fontSize = 15.sp, fontWeight = FontWeight.Bold)

                            Column() {
                                if (totalSaved != null) {
                                    Card(
                                        Modifier
                                            .clip(shape = RoundedCornerShape(5.dp)),
                                        colors = CardDefaults.cardColors(Color(0xC335783F))

                                    ) {
                                        Column(Modifier.padding(4.dp)) {
                                            Text(
                                                "$${homeViewModel.convertBsToUSD(totalSaved)}",
                                                fontSize = 14.sp,
                                                lineHeight = 5.sp,
                                                fontWeight = FontWeight.Bold,
                                                color =
                                                MaterialTheme.colorScheme.onPrimary
                                            )

                                            Text(
                                                "Bs.${homeViewModel.formatAmount(totalSaved.toDouble())}",
                                                fontSize = 12.sp,

                                                fontWeight =
                                                FontWeight.ExtraLight,
                                                color =
                                                MaterialTheme.colorScheme.onSecondary
                                            )
                                        }

                                    }


                                } else {
                                    Text(
                                        "Bs.---", fontSize = 17.sp, fontWeight = FontWeight.Bold, color =
                                        MaterialTheme.colorScheme.onSecondary
                                    )
                                }
                            }

                        }
                        // Spacer(modifier = Modifier.height(30.dp))
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {


                            Text("Faltante", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            Card(
                                Modifier
                                    .clip(shape = RoundedCornerShape(5.dp)),
                                colors = CardDefaults.cardColors(Color(0x95A80606))
                            ) {
                                Column(Modifier.padding(4.dp)) {
                                    Text(
                                        "$${homeViewModel.convertBsToUSD(missing)}",
                                        fontSize = 15.sp,
                                        lineHeight = 5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color =
                                        MaterialTheme.colorScheme.onPrimary
                                    )
                                    if (missing != null) {

                                        Text(
                                            "Bs.${homeViewModel.formatAmount(missing.toDouble())}",
                                            fontSize = 12.sp,
                                            fontWeight =
                                            FontWeight.ExtraLight,
                                            color =
                                            MaterialTheme.colorScheme.onSecondary
                                        )

                                    } else {
                                        Text(
                                            "Bs.---", fontSize = 17.sp, fontWeight = FontWeight.Bold, color =
                                            MaterialTheme.colorScheme.onSecondary
                                        )
                                    }
                                }
                            }


                        }
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {

                            Text("Disponible", fontSize = 15.sp, fontWeight = FontWeight.Bold)

                            Card(
                                Modifier
                                    .clip(shape = RoundedCornerShape(5.dp)),
                                colors = CardDefaults.cardColors(Color(0xC35F98F6))
                            ) {
                                Column(Modifier.padding(4.dp)) {
                                    Text(
                                        "$${homeViewModel.convertBsToUSD(budgetFree)}",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    if (budgetFree != null) {

                                        Text(
                                            "Bs.${homeViewModel.formatAmount(budgetFree.toDouble())}",
                                            fontSize = 12.sp,
                                            lineHeight = 5.sp,
                                            fontWeight =
                                            FontWeight.ExtraLight,
                                            color =
                                            MaterialTheme.colorScheme.onSecondary
                                        )

                                    } else {
                                        Text(
                                            "Bs.---", fontSize = 17.sp, fontWeight = FontWeight.Bold, color =
                                            MaterialTheme.colorScheme.onSecondary
                                        )
                                    }
                                }
                            }


                        }
                    }
                }

            }

        }
        Spacer(Modifier.padding(2.dp))
        ElevatedCard(
            modifier = Modifier
                .weight(1f)
                .height(260.dp),
            shape = CardDefaults.elevatedShape,
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 10.dp),

            ) {
            Text(
                "Grafico de planes",
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(10.dp)
            )
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                ItemPlangrafic(Modifier, plans = plans, plansViewModel = plansViewModel)
            }

        }
    }
}

//grafico de plane
@Composable
fun ItemPlangrafic(modifier: Modifier = Modifier, plans: List<planItem>, plansViewModel: PlansViewModel) {

    val coloresRandom = listOf(
        Color(0xff7CDBF3),
        Color(0xff7CA0F3),
        Color(0xff947CF3),
        Color(0xffAFEEEE),
        Color(0xff00BFFF),
        Color(0xff40E0D0),
        Color(0xFF7B68EE),
        Color(0xFF9C27B0)
    )

    Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {

        if (plans.isEmpty()) {
            Text(
                text = "No hay planes de ahorro",
                fontSize = 18.sp,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Center,

                )
        }

        LazyColumn(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.SpaceAround) {
            itemsIndexed(plans) { Index, plan ->
                val colorElegido = coloresRandom[Index % coloresRandom.size]
                ResumePlan(Modifier, colorElegido, plan, plansViewModel)
            }
        }
    }
}

//item plan resume
@Composable
fun ResumePlan(modifier: Modifier = Modifier, colorElegido: Color, plan: planItem, plansViewModel: PlansViewModel) {
    val progres = plan.Actualy.toFloat() / plan.Objective.toFloat()
    val percentge = (progres * 100)
    val percentgeformtat = plansViewModel.formatTotal(percentge.toDouble())
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        Column(Modifier.weight(0.4f), horizontalAlignment = Alignment.Start) {
            Text(
                plan.Name, fontSize = 14.sp,
                lineHeight = 12.sp
            )
        }

        Column(Modifier.weight(0.5f), horizontalAlignment = Alignment.CenterHorizontally) {
            LinearProgressIndicator(
                progress = { progres },
                modifier = modifier
                    .height(8.dp)
                    .fillMaxWidth(),
                color = colorElegido,
                trackColor = colorElegido.copy(alpha = 0.3f),
                strokeCap = StrokeCap.Round,
            )
        }

        Column(Modifier.weight(0.5f), horizontalAlignment = Alignment.End) {
            Text(
                "${percentgeformtat}%",
                fontSize = 15.sp
            )
        }

    }
}

//Presupuesto pantalla principal
@Composable
fun PresupuestoCard(
    presupuesto: Double,
    homeViewModel: homeViewModel,
    UIDolar: UiStateDolar,
    DolarObject: DolarOficial?,
    modifier: Modifier = Modifier
) {
    val PrecioUsd = homeViewModel.convertBsToUSD(presupuesto)
    val tasa = DolarObject?.promedio
    val fecha = DolarObject?.fechaActualizacion
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { homeViewModel.showMovementsEdit },
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
                        .weight(1f)
                        .padding(16.dp)
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(Modifier.fillMaxWidth()) {
                            Text(
                                "Presupuesto",
                                //style = MaterialTheme.typography.titleMedium,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Normal,
                                textAlign =
                                TextAlign.Start
                            )
                            Column(Modifier.fillMaxWidth()) {
                                Text(
                                    "$${PrecioUsd} ", fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                when (UIDolar) {
                                    is UiStateDolar.neutral -> {
                                        Text("Cargando...")

                                    }

                                    is UiStateDolar.error -> {
                                        Text("Error al cargar precio del dolar")
                                    }

                                    UiStateDolar.isLoading -> {
                                        Column(verticalArrangement = Arrangement.Center) {
                                            CircularProgressIndicator()
                                            Text("Cargando...")
                                        }
                                    }

                                    is UiStateDolar.success -> {


                                        if (presupuesto != null) {
                                            Text(
                                                "Bs.${presupuesto}",
                                                fontSize = 15.sp,
                                                color = MaterialTheme.colorScheme.onSecondary
                                            )
                                        } else {
                                            Text(
                                                "Bs. ---",
                                                fontSize = 20.sp,
                                                color = MaterialTheme.colorScheme.onSecondary
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (tasa != null && fecha != null) {
                    Column(
                        Modifier.weight(0.7f), verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            "Tasa del dia: Bs.${homeViewModel.formatAmount(tasa)}",
                            fontSize = 15.sp,
                            textAlign = TextAlign.Start,
                            lineHeight = 12.sp,
                            maxLines = 2
                        )
                        Text(
                            "Fecha:${homeViewModel.fechaActualtasa(fecha)}",
                            fontSize = 15.sp,
                            textAlign = TextAlign.Start,
                            maxLines = 2,
                            lineHeight = 12.sp
                        )
                    }

                }

                Column(
                    modifier = Modifier
                        .weight(0.3f)
                        .padding(2.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.End
                ) {
                    Icon(
                        painter = painterResource(R.drawable.add_ic),
                        contentDescription = "presupuesto",
                        Modifier
                            .size(40.dp)
                            .clickable { homeViewModel.mostrarDialogBudget() },
                        tint = MaterialTheme.colorScheme.onBackground
                    )

                }

            }

        }
    }
}

//Saludo pantalla principal
@Composable
fun Greeteng(name: String) {

    Box(Modifier.padding(horizontal = 10.dp, vertical = 8.dp)) {
        Text(
            "Bienvenido ${name}",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
        )
        Log.i("user", "Usuario posee el nombre de ${name}")
    }


}