package com.example.myprojectfinancia

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview
@Composable
fun ContainerCreate() {


    Box(
        modifier = Modifier
            .background(color = Color(0xFFECECEC))
            .fillMaxSize()
            .padding(bottom = 20.dp, start = 20.dp, end = 20.dp, top = 50.dp)
    ) {


        Column {
            TitleCreate()
            Spacer(modifier = Modifier.padding(20.dp))
            ContentCreate()
            Spacer(modifier = Modifier.padding(20.dp))
            ButtonCreate()
            Spacer(modifier = Modifier.padding(28.dp))
            FooterCreate()
        }


    }


}

@Composable
fun FooterCreate() {
    Row(Modifier
        .fillMaxWidth()
        .padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
        Text("Ya tienes una cuenta?", fontSize = 18.sp, color = Color(0xFF878787), fontWeight = FontWeight.Bold)
        TextButton(
            onClick = {},
        ) { Text("Iniciar Sesion", fontSize = 18.sp, color = Color(0xFF016AC4), fontWeight = FontWeight.Bold) }
    }
}


@Composable
fun ButtonCreate() {
    Button(
        onClick = {},
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            Color(0xFF016AC4)
        )
    ) {
        Text(
            "Crear",
            color = Color(0xFFFFFFFF),
            fontSize = 18.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }

}

@Composable
fun ContentCreate() {
    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var pass by rememberSaveable { mutableStateOf("") }
    var ConfirmPass by rememberSaveable { mutableStateOf("") }

    Column(Modifier.fillMaxWidth()) {
        IngresarNombre(name = name) { name = it }
        Spacer(Modifier.height(8.dp))
        IngresarEmail(email = email) { email = it }
        Spacer(Modifier.height(8.dp))
        IngresarPassword(pass = pass) { pass = it }
        Spacer(Modifier.height(8.dp))
        ConfirmPassword(ConfirmPass = ConfirmPass) { ConfirmPass = it }

    }

}

@Composable
fun ConfirmPassword(ConfirmPass: String, onValueConfirm: (String) -> Unit) {
    var passVisibleConfirm by rememberSaveable { mutableStateOf(false) }
    Text(
        "Confirmar Contraseña",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF3B3B3B),
        modifier = Modifier.padding(6.dp)
    )
    TextField(
        value = ConfirmPass,
        onValueChange = { onValueConfirm(it) },
        label = { Text("Contraseña", color = Color.Black) },
        visualTransformation = if (passVisibleConfirm) VisualTransformation.None else PasswordVisualTransformation(),
        modifier = Modifier.fillMaxWidth(),
        maxLines = 1,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedContainerColor = Color(0xFFD5D5D5),
            unfocusedContainerColor = Color(0xFFD0D0D0),
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            cursorColor = Color(0xFF4988CD)
        ),
        trailingIcon = {

            TextButton(onClick = { passVisibleConfirm = !passVisibleConfirm }) {
                if (passVisibleConfirm) {
                    Text("Ocultar", color = Color(0xFF979797))
                } else {
                    Text("Mostrar", color = Color(0xFF979797))
                }
            }
        },
        shape = RoundedCornerShape(10.dp)

    )
}

@Composable
fun IngresarPassword(pass: String, onValuePass: (String) -> Unit) {
    var passVisible by rememberSaveable { mutableStateOf(false) }
    Text(
        "Ingresar Contraseña",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF3B3B3B),
        modifier = Modifier.padding(6.dp)
    )
    TextField(
        value = pass,
        onValueChange = { onValuePass(it) },
        label = { Text("Contraseña", color = Color.Black) },
        visualTransformation = if (passVisible) VisualTransformation.None else PasswordVisualTransformation(),
        modifier = Modifier.fillMaxWidth(),
        maxLines = 1,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedContainerColor = Color(0xFFD5D5D5),
            unfocusedContainerColor = Color(0xFFD0D0D0),
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            cursorColor = Color(0xFF4988CD)
        ),
        trailingIcon = {

            TextButton(onClick = { passVisible = !passVisible }) {
                if (passVisible) {
                    Text("Ocultar", color = Color(0xFF979797))
                } else {
                    Text("Mostrar", color = Color(0xFF979797))
                }
            }
        },
        shape = RoundedCornerShape(10.dp)

    )
}

@Composable
fun IngresarEmail(email: String, onValueEmail: (String) -> Unit) {
    Text(
        "Ingresar email",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF3B3B3B),
        modifier = Modifier.padding(6.dp)
    )
    TextField(
        value = email,
        onValueChange = { onValueEmail(it) },
        label = { Text("Email", color = Color.Black) },
        modifier = Modifier.fillMaxWidth(),
        maxLines = 1,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedContainerColor = Color(0xFFD5D5D5),
            unfocusedContainerColor = Color(0xFFD0D0D0),
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            cursorColor = Color(0xFF4988CD)
        ),
        shape = RoundedCornerShape(10.dp)

    )
}

@Composable
fun IngresarNombre(name: String, onvalueName: (String) -> Unit) {
    Text(
        "Ingresar nombre",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF3B3B3B),
        modifier = Modifier.padding(6.dp)
    )
    TextField(
        value = name,
        onValueChange = { onvalueName(it) },
        label = { Text("Nombre", color = Color.Black) },
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedContainerColor = Color(0xFFD5D5D5),
            unfocusedContainerColor = Color(0xFFD0D0D0),
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            cursorColor = Color(0xFF4988CD)
        )
    )
}

@Composable
fun TitleCreate() {
    Box(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Crear cuenta",
            modifier = Modifier.align(Alignment.Center),
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }

}
