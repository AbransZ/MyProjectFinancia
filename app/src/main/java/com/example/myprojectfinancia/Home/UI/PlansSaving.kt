package com.example.myprojectfinancia.Home.UI

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Preview
@Composable
fun SavingScreen(modifier: Modifier = Modifier) {
    
    

}

@Composable
fun PlanTotal(modifier: Modifier = Modifier) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(.16.dp)) {
        Card (modifier = Modifier.fillMaxSize()){  }

    }
}

@Composable
fun Greetings(modifier: Modifier = Modifier) {
    Box(modifier.fillMaxWidth()) {
        Text(
            "Planes Financieros",
            modifier = modifier.padding(16.dp),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )
    }
}