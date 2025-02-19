package com.example.myprojectfinancia


import android.app.Activity


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column


import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button

import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf

import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.sp


@Composable
fun LogginScreen(modifier: Modifier) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(top = 20.dp)
    ) {
        Header(
            Modifier
                .align(Alignment.TopEnd)
                .padding(10.dp)
        )
        Body(
            Modifier
                .align(Alignment.Center)
                .padding(24.dp)
        )
    }

}

@Composable
fun Body(modifier: Modifier) {
    var email by rememberSaveable { mutableStateOf("") }
    var Pass by rememberSaveable { mutableStateOf("") }
    var isEnable by rememberSaveable { mutableStateOf(false) }
    Column(modifier = modifier) {
        Logoimage()
        Spacer(Modifier.size(16.dp))
        Email(email) { email = it }
        Spacer(Modifier.size(8.dp))
        Password(Pass) { Pass = it }
        Spacer(Modifier.size(8.dp))
        ForgotButton()
        Spacer(Modifier.size(16.dp))
        Buttons(isEnable)


    }

}

@Composable
fun ForgotButton() {
    Box(Modifier.fillMaxWidth()) {
        TextButton(onClick = {}, Modifier.align(Alignment.CenterEnd)) {
            Text(
                text = "¿Olvidaste la contraseña?",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }

}

@Composable
fun Buttons(isEnable: Boolean) {
    Box (modifier = Modifier.fillMaxWidth(),contentAlignment = Alignment.Center){
        Row() {
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(Color.Blue),
                modifier = Modifier.fillMaxWidth()

            ) { Text("Ingresar") }

        }
    }


}

@Composable
fun Password(pass: String, onTextChange: (String) -> Unit) {
    var passVisible by rememberSaveable { mutableStateOf(false) }
    TextField(
        value = pass,
        onValueChange = { onTextChange(it) },
        label = { Text("Contraseña") },
        visualTransformation = if (passVisible) VisualTransformation.None else PasswordVisualTransformation(),
        modifier = Modifier.fillMaxWidth(),
        trailingIcon = {

            TextButton(onClick = { passVisible = !passVisible }) {
                if (passVisible) {
                    Text("Mostrar")
                } else {
                    Text("ocultar")
                }
            }
        }
    )
}

@Composable
fun Email(email: String, onTextChange: (String) -> Unit) {
    TextField(
        value = email,
        onValueChange = { onTextChange(it) },
        label = { Text("Ingresar email") },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun Header(modifier: Modifier) {
    val activity = LocalContext.current as Activity
    Icon(
        imageVector = Icons.Default.Close,
        contentDescription = "Close app",
        modifier = modifier.clickable { activity.finish() })
}
