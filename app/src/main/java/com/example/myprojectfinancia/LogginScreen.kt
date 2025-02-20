package com.example.myprojectfinancia


import android.app.Activity
import androidx.compose.foundation.background


import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button

import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextFieldDefaults.textFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf

import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType

import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.sp


@Composable
fun LogginScreen(modifier: Modifier) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(top = 26.dp).background(color = Color(0xFF000000))
    ) {

        Header(
            Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        )
        Body(
            Modifier
                .align(Alignment.Center)
                .padding(horizontal = 16.dp)
        )

        Footer(
            Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )

    }

}

@Composable
fun Body(modifier: Modifier) {
    var email by rememberSaveable { mutableStateOf("") }
    var Pass by rememberSaveable { mutableStateOf("") }
    var isEnable by rememberSaveable { mutableStateOf(false) }

    isEnable = isEmailValid(email)&& isPasswordValid(Pass)
    Column(modifier = modifier) {
        Logoimage()
        Spacer(modifier = modifier.size(24.dp))
        Email(email) { email = it }
        Spacer(modifier = modifier.size(16.dp))
        Password(Pass) { Pass = it }
        Spacer(modifier = modifier.size(16.dp))
        ForgotButton()
        Spacer(modifier = modifier.size(24.dp))
        Buttons(isEnable)
        Spacer(modifier = modifier.size(32.dp))
        divider()
        Spacer(modifier = modifier.size(32.dp))
        Googlebuttons()


    }


}

fun isEmailValid(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}


fun isPasswordValid(password: String): Boolean {
    return password.length >= 8
}

@Composable
fun Footer(modifier: Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {

        Divider(
            Modifier
                .height(1.dp)
                .fillMaxWidth()
        )
        SignIn()
    }


}

@Composable
fun SignIn() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text("Aun no tienes una cuenta?")
        TextButton(onClick = {}, Modifier.padding(start = 1.dp)) {
            Text(
                "Crear cuenta",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xff0060C5)
            )
        }
    }
}

@Composable
fun Googlebuttons() {
    Box(Modifier.fillMaxWidth()) {
        OutlinedButton(onClick = {}, modifier = Modifier.fillMaxWidth()) {
            Icon(
                painter = painterResource(R.drawable.search),
                contentDescription = "Cuenta Google",
                modifier = Modifier
                    .padding(8.dp)
                    .size(20.dp)
            )
            Text(
                text = "Usar cuenta de Google",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


@Composable
fun divider() {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Divider(
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp), color = Color.LightGray
        )
        Text("O", color = Color.LightGray, fontSize = 14.sp)
        Divider(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp), color = Color.LightGray
        )
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
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Row() {
            Button(
                onClick = {},
                enabled = isEnable,
                colors = ButtonDefaults.buttonColors(
                    Color(0xFF006FE0),
                    disabledContentColor = Color(0xFFA5A5A5),
                    disabledContainerColor = Color(0xFF7EBBFF),

                ),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)

            ) { Text(text = "Ingresar", color = Color.White) }

        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Password(pass: String, onTextChange: (String) -> Unit) {
    var passVisible by rememberSaveable { mutableStateOf(false) }
    TextField(
        value = pass,
        onValueChange = { onTextChange(it) },
        label = { Text("Contraseña", color = Color(0xFF979797)) },
        visualTransformation = if (passVisible) VisualTransformation.None else PasswordVisualTransformation(),
        modifier = Modifier.fillMaxWidth(),
        maxLines = 1,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        colors = TextFieldDefaults.textFieldColors(
            focusedTextColor = Color(0xFFAAAAAA),
            unfocusedTextColor = Color(0xFF4B4B4B),
            containerColor = Color(0xFFEFEFEF),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        trailingIcon = {

            TextButton(onClick = { passVisible = !passVisible }) {
                if (passVisible) {
                    Text("Mostrar", color = Color(0xFF979797))
                } else {
                    Text("ocultar", color = Color(0xFF979797))
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Email(email: String, onTextChange: (String) -> Unit) {
    TextField(
        value = email,
        onValueChange = { onTextChange(it) },
        label = { Text("Ingresar email", color = Color(0xFF979797)) },
        modifier = Modifier.fillMaxWidth(),
        maxLines = 1,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        colors = TextFieldDefaults.textFieldColors(
            focusedTextColor = Color(0xFFAAAAAA),
            unfocusedTextColor = Color(0xFF4B4B4B),
            containerColor = Color(0xFFEFEFEF),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
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
