package com.example.myprojectfinancia.Index.home.UI

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.myprojectfinancia.Data.API.network.DolarOficial
import com.example.myprojectfinancia.Index.home.ViewModels.homeViewModel

@Composable
fun PreupuestoDialog(
    modifier: Modifier = Modifier,
    homeViewModel: homeViewModel,
    DolarObject: DolarOficial?
) {
    val showBudget = homeViewModel.showBudget.collectAsState(false)
    val monto by homeViewModel.budgetMount.collectAsState()
    val nombreAporte by homeViewModel.budgetCategory.collectAsState()
    val montoBs by homeViewModel.budgetBsMount.collectAsState()

    if (showBudget.value) {
        Dialog(onDismissRequest = { homeViewModel.ocultarDialogBudget() }) {

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
                    DialogBudget(homeViewModel, monto, montoBs, nombreAporte, DolarObject)
                    ButtonBudget(homeViewModel)
                }
            }
        }
    }
}

@Composable
fun ButtonBudget(homeViewModel: homeViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp), horizontalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {
                homeViewModel.aumentarPresupuesto()
                homeViewModel.clearBudget()
                homeViewModel.ocultarDialogBudget()

            }, shape = MaterialTheme.shapes.small, colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ), modifier = Modifier.weight(1f)
        ) { Text("Guardar", fontSize = 20.sp) }
        Spacer(Modifier.width(10.dp))
        Button(
            onClick = { homeViewModel.ocultarDialogBudget() },
            shape = MaterialTheme.shapes.small,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            ), modifier = Modifier.weight(1f)
        ) { Text("Cancelar", color = MaterialTheme.colorScheme.onPrimary, fontSize = 20.sp) }
    }
}

@Composable
fun DialogBudget(
    homeViewModel: homeViewModel,
    monto: String,
    montoBs: String,
    nombreAporte: String,
    dolarObject: DolarOficial?
) {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            Modifier.fillMaxWidth(),

            ) {
            Text(
                "Ingresar Presupuesto",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)

            )


            //Campos de texto para ingresar monto y categoria
            Spacer(Modifier.padding(8.dp))
            Text("Monto", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 48.dp))
            Spacer(Modifier.padding(6.dp))
            OutlinedTextField(
                value = monto,
                onValueChange = { homeViewModel.onBudgetChange(it, dolarObject?.promedio) },
                placeholder = { Text("Ingresar monto.", fontSize = 15.sp) },
                prefix = { Text("$", fontSize = 15.sp) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .height(56.dp)
                    .width(230.dp)
                    .align(Alignment.CenterHorizontally),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            )
            Spacer(Modifier.padding(10.dp))
            OutlinedTextField(
                value = montoBs,
                onValueChange = { homeViewModel.onBudgetBsChange(it, dolarObject?.promedio) },
                placeholder = { Text("Ingresar monto.", fontSize = 15.sp) },
                prefix = { Text("Bs.", fontSize = 15.sp) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .height(56.dp)
                    .width(230.dp)
                    .align(Alignment.CenterHorizontally),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            )
            Spacer(Modifier.padding(5.dp))
            Text(
                "Nombre del Aporte", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding
                    (start = 48.dp)
            )
            Spacer(Modifier.padding(6.dp))
            OutlinedTextField(
                value = nombreAporte,
                onValueChange = { homeViewModel.onBudgetCategoryChange(it) },
                placeholder = { Text("Ej. Salario, Comida, Salario.", fontSize = 15.sp) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .height(60.dp)
                    .width(230.dp)
                    .align(Alignment.CenterHorizontally),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            )
        }
    }
}
