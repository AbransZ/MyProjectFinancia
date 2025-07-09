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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myprojectfinancia.R
import com.example.myprojectfinancia.theme.MyProjectFinanciaTheme

//@Preview(showSystemUi = true)
//@Composable
//fun ejemplo(modifier: Modifier = Modifier) {
//    MyProjectFinanciaTheme {
//        MovementsScreens(modifier.background(MaterialTheme.colorScheme.background))
//    }
//}



@Composable
fun MovementsScreens(padding: PaddingValues, modifier: Modifier) {

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(padding)
            .padding(10.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(modifier = modifier.fillMaxWidth()) {
            Selecction(modifier)
            Spacer(modifier = modifier.padding(6.dp))
            MovementList(modifier)
        }
    }


}

@Composable
fun Selecction(modifier: Modifier) {
    var isPressedIngresos by remember { mutableStateOf(true) }
    var isPressedGastos by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Button(
            onClick = {
                isPressedIngresos = true
                isPressedGastos = false
            },
            colors = ButtonDefaults.buttonColors(
                if (isPressedIngresos) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSecondary
            ), modifier = modifier.weight(1f), shape = MaterialTheme.shapes.small
        ) {
            Text("Ingresos")
        }
        Button(
            onClick = {
                isPressedIngresos = false
                isPressedGastos = true
            },
            colors = ButtonDefaults.buttonColors(
                if (isPressedGastos) Color(0xFF9E0000)
                else MaterialTheme.colorScheme.onSecondary
            ), modifier = modifier.weight(1f), shape = MaterialTheme.shapes.small
        ) {
            Text("Gastos")
        }


    }
}

@Composable
fun MovementList(modifier: Modifier) {
    Box(modifier = modifier.padding(6.dp)) {
        LazyColumn() {
            items(10) {
                MovementItem(modifier)
            }
        }
    }


}

@Composable
fun MovementItem(modifier: Modifier) {
    Card(modifier = modifier.padding(3.dp)) {
        ListItem(headlineContent = {
            Row {
                Text(
                    "Monto: $100 ",
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    "Monto: BS. 10000",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSecondary
                )

            }

        },
            overlineContent = {
                Text(
                    "Categoria",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            },
            supportingContent = { Text("Naturaleza") },
            leadingContent = {
                Icon(painter = painterResource(R.drawable.ic_money), contentDescription = "Money")
            },
            trailingContent = { Text("Fecha") }
        )
    }

}


