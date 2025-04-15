package com.example.myprojectfinancia.Starting.UI

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Preview(showSystemUi = true)
@Composable
fun InitialView() {
    Greeteng()
}

@Composable
fun Greeteng() {
    Box (Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        Text("Bienvenido", fontSize = 45.sp, fontWeight = FontWeight.Bold)
    }
}
