package com.example.myprojectfinancia.Index.Movements

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.myprojectfinancia.Data.API.network.DolarOficial
import com.example.myprojectfinancia.Index.home.Models.Movements.Movimiento
import com.example.myprojectfinancia.Index.home.ViewModels.homeViewModel
import com.example.myprojectfinancia.R

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
    val movements by homeViewModel.allMovements.collectAsState()
    val error by homeViewModel.Error.collectAsState()
    val showMovementsEdit by homeViewModel.showMovementsEdit.collectAsState()
    val monto by homeViewModel.monto.observeAsState("")
    val categoria by homeViewModel.category.observeAsState("")
    val naturaleza by homeViewModel.naturalezaEdit.collectAsState()
    val isPressedIngresos by homeViewModel.ingrsosPressed.collectAsState()
    val isPressedGastos by homeViewModel.egresosIsPressed.collectAsState()
    val categoryEdit by homeViewModel.categoryEdit.collectAsState(categoria)
    val montoEdit by homeViewModel.montoEdit.collectAsState(monto)
    val isPressedIngresosEdit by homeViewModel.ingrsosPressedEdit.collectAsState(isPressedIngresos)
    val isPressedGastosEdit by homeViewModel.egresosIsPressedEdit.collectAsState(isPressedGastos)
    val fecha = homeViewModel.fechaActual
    val DolarObject by homeViewModel.DolarObject.collectAsState()
    val montoBs by homeViewModel.montoBsEdit.collectAsState("")


    // carga de pestanas de movimientos
    LaunchedEffect(movements) {
        homeViewModel.getAllMovements()

    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(padding)
            .padding(5.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(modifier = modifier.fillMaxWidth()) {
//            Selecction(modifier, homeViewModel, isPressedIngresos, isPressedGastos)
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
            Greeting(Modifier)
            EditCuenta(
                showMovementsEdit,
                montoEdit,
                categoryEdit,
                naturaleza,
                homeViewModel,
                isPressedIngresosEdit,
                isPressedGastosEdit,
                fecha,
                montoBs,
                DolarObject
            )
            Spacer(modifier = modifier.padding(6.dp))
            MovementList(modifier, homeViewModel, isLoading, movements)
        }
    }


}

@Composable
fun Greeting(modifier: Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text("Movimientos", fontSize = 25.sp, fontWeight = FontWeight.Bold)
    }
}


@Composable
fun MovementList(
    modifier: Modifier,
    homeViewModel: homeViewModel,
    isLoading: Boolean,
    movements: List<Movimiento>,

    ) {

    Box(modifier = modifier.padding(5.dp)) {
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
                        items(
                            movements
                        ) { item ->
                            ListMountMovements(homeViewModel, item)
                        }
                    }
                }
            }
        }

    }


}

//item de la lista de movimientos
@Composable
fun ListMountMovements(
    homeViewModel: homeViewModel,
    movimiento: Movimiento
) {
    val montoBs = movimiento.montoBs.toDouble()
    val precioUSD = homeViewModel.convertBsToUSD(montoBs)
    Card(
        modifier = Modifier
            .padding(5.dp)
            .clickable { homeViewModel.showMovementsEdit(movimiento) }) {
        ListItem(
            headlineContent = {
                Row {
                    Text("$${precioUSD}/ ", fontSize = 15.sp)
                    if (montoBs != null) {
                        Text("Bs.${String.format("%.2f", montoBs)}", fontSize = 12.sp)
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

//Botones confirmar o cancelar dialogo editar cuenta
@Composable
fun ButtonsDialogEdit(homeViewModel: homeViewModel, isPressedIngresosEdit: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp), horizontalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {
                if (isPressedIngresosEdit) {
                    Log.i("movimientos", "Ingreso se presiono")
                } else Log.i("movimientos", "Gasto se presiono")
                homeViewModel.EditarMovimiento()
                homeViewModel.clearfields()

            }, shape = MaterialTheme.shapes.small, colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) { Text("Guardar", fontSize = 15.sp) }
        Spacer(Modifier.width(10.dp))
        Button(
            onClick = { homeViewModel.ocultarDialogEdit() },
            shape = MaterialTheme.shapes.small,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            )
        ) { Text("Cancelar", fontSize = 15.sp, color = MaterialTheme.colorScheme.onSecondary) }
    }
}

//cuerpo del dialogo editar cuenta
@Composable
fun BodyDialogEdit(
    homeViewModel: homeViewModel,
    isPressedIngresosEdit: Boolean,
    isPressedGastosEdit: Boolean,
    montoEdit: String,
    categoriaEdit: String,
    naturalezaEdit: String,
    fecha: String,
    montoBs: String,
    DolarObject: DolarOficial?
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
                        homeViewModel.ingresosIsPressedEdit()
                    },
                    shape = MaterialTheme.shapes.small,
                    colors = ButtonDefaults.buttonColors(
                        if (isPressedIngresosEdit) Color(0x8B024910) else Color(0xC147F1C6)
                    ),
                    enabled = if (naturalezaEdit == "Asignacion") false else true
                ) { Text("Ingreso", fontSize = 15.sp) }
                Spacer(Modifier.width(10.dp))
                Button(
                    onClick = {
                        homeViewModel.gastosIsPressedEdit()
                    },
                    shape = MaterialTheme.shapes.small, colors = ButtonDefaults.buttonColors(
                        if (isPressedGastosEdit) Color(0x90530A0A) else Color(0xA9EF7352)
                    ),
                    enabled = if (naturalezaEdit == "Asignacion") false else true
                ) {
                    Text("Gasto", fontSize = 15.sp)
                }
            }

            //Campos de texto para ingresar monto y categoria
            Spacer(Modifier.padding(8.dp))
            Text("Monto", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.padding(6.dp))
            OutlinedTextField(
                value = montoEdit,
                onValueChange = { homeViewModel.onMontoChangeEdit(it, DolarObject?.promedio) },
                placeholder = { Text("Ingresar monto.", fontSize = 10.sp) },
                prefix = { Text("$", fontSize = 10.sp) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .height(56.dp)
                    .width(210.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            )
            Spacer(Modifier.padding(6.dp))
            OutlinedTextField(
                value = montoBs,
                onValueChange = { homeViewModel.onMontoBsChangeEdit(it, DolarObject?.promedio) },
                placeholder = { Text("Ingresar monto.", fontSize = 10.sp) },
                prefix = { Text("Bs", fontSize = 10.sp) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .height(56.dp)
                    .width(210.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            )
            Spacer(Modifier.padding(5.dp))
            Text("Categoria", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.padding(6.dp))
            OutlinedTextField(
                value = categoriaEdit,
                onValueChange = { homeViewModel.onCategoriaChangeEdit(it) },
                placeholder = { Text("Ej. Salario, Comida, Salario.", fontSize = 10.sp) },

                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .height(58.dp)
                    .width(210.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            )
            Spacer(Modifier.padding(5.dp))
            Text("Fecha $fecha", fontSize = 15.sp, fontWeight = FontWeight.Bold)

        }
    }

}

//Dialogo editar cuenta
@Composable
fun EditCuenta(
    showDialogEdit: Boolean,
    montoEdit: String,
    categoriaEdit: String,
    naturalezaEdit: String,
    homeViewModel: homeViewModel,
    isPressedIngresosEdit: Boolean,
    isPressedGastosEdit: Boolean,
    fecha: String,
    montoBs: String,
    DolarObject: DolarOficial?
) {
    if (showDialogEdit) {
        Dialog(onDismissRequest = { homeViewModel.ocultarDialogEdit() }) {
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
                    BodyDialogEdit(
                        homeViewModel,
                        isPressedIngresosEdit,
                        isPressedGastosEdit,
                        montoEdit,
                        categoriaEdit,
                        naturalezaEdit,
                        fecha,
                        montoBs,
                        DolarObject
                    )
                    ButtonsDialogEdit(
                        homeViewModel,
                        isPressedIngresosEdit
                    )

                }
            }
        }
    }

}




