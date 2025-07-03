package com.example.myprojectfinancia.Home.UI

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myprojectfinancia.R
import com.example.myprojectfinancia.theme.MyProjectFinanciaTheme

@Preview(showSystemUi = true)
@Composable
fun prueba(modifier: Modifier = Modifier) {
    MyProjectFinanciaTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            SettingsScreen(modifier)
        }
    }

}


@Composable
fun SettingsScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn(modifier = modifier.fillMaxSize()) {
            item { UserItem(modifier) }
            item { Spacer(modifier.padding(18.dp)) }
            item { Text("Conversor de moneda", fontSize = 20.sp, fontWeight = FontWeight.Bold) }
            item { ConversorMoneda(modifier) }
        }

    }
}

@Composable
fun ConversorMoneda(modifier: Modifier) {
    Card(modifier = modifier
        .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(
            modifier = modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Dolar Estado Unidense")
                Text("USD")
            }
            Spacer(modifier.padding(2.dp))
            OutlinedTextField(
                value = "1",
                onValueChange = {},
                modifier = modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(10.dp),
                singleLine = true,
                maxLines = 1,
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = MaterialTheme.colorScheme.primary),
                textStyle = TextStyle(
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier.padding(20.dp))
            Image(
                painter = painterResource(R.drawable.ic_change),
                contentDescription = "change",
                modifier = modifier.size(50.dp),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
            )
            Spacer(modifier.padding(20.dp))
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Bolivares")
                Text("VES")
            }
            Spacer(modifier.padding(2.dp))
            OutlinedTextField(
                value = "100",
                onValueChange = {},
                modifier = modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(10.dp),
                singleLine = true,
                maxLines = 1,
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = MaterialTheme.colorScheme.primary),
                textStyle = TextStyle(
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier.padding(4.dp))
            Text(
                text = "Tasa oficial del Banco Central de Venezuela",
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSecondary,
            )


        }
    }
}

@Composable
fun UserItem(modifier: Modifier) {
    Card(
        modifier = modifier
            .height(100.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.ic_user),
                contentDescription = "Usericon",
                modifier.size(100.dp)
            )
            Column(verticalArrangement = Arrangement.Center) {
                Text(
                    "Usurario",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )

                Spacer(modifier.padding(3.dp))

                Text(
                    "Correodel usuario@gmail.com",
                    fontSize = 12.sp
                )

                Spacer(modifier.padding(3.dp))

                Text(
                    "fecha de Ingreso",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

        }

    }

}
