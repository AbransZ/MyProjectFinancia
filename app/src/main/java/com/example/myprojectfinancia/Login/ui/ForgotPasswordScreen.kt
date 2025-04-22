package com.example.myprojectfinancia.Login.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.CheckCircle

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card

import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.myprojectfinancia.Model.Routes
import com.example.myprojectfinancia.Login.ui.ViewModel.LoginViewModel


@Composable
fun ForgotPassWord(
    modifier: Modifier,
    navigationControler: NavHostController,
    loginViewModel: LoginViewModel
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        loginViewModel.message.collect { message ->
            message?.let {
                Toast.makeText(
                    context,
                    it,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    Column(
        modifier = Modifier
            .padding(top = 30.dp)
            .fillMaxSize()
            .background(color = Color(0xFFECECEC))
    ) {
        HeaderScreen(
            modifier
                .align(alignment = Alignment.Start), navigationControler
        )
        BodyForgot(modifier.align(alignment = Alignment.CenterHorizontally), loginViewModel)
    }

}

@Composable
fun BodyForgot(modifier: Modifier, loginViewModel: LoginViewModel) {
    val email: String by loginViewModel.email.observeAsState("")
    val isDialogOk: Boolean by loginViewModel.isDialogOk.observeAsState(false)
    val isLoading: Boolean by loginViewModel.isLoading.observeAsState(false)



    Column(modifier.padding(horizontal = 16.dp)) {

        EmailToRememberPass(modifier, email) { loginViewModel.onRememberChange(email = it) }
        Spacer(modifier.padding(30.dp))

        Button(
            onClick = {

                loginViewModel.sendEmailToRevoverPassword(email)
            },
            colors = ButtonDefaults.buttonColors(
                contentColor = Color(0xFFFFFFFF),
                containerColor = Color(0xFF3C96F5),
                disabledContentColor = Color(0xFFA5A5A5),
                disabledContainerColor = Color(0xFF30669E),

                ),
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            shape = RoundedCornerShape(16.dp),


            ) {
            Text(text = "Recuperar", color = Color.White, fontSize = 20.sp)
        }
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }
        if (isDialogOk) {
            DiaologOk(
                show = isDialogOk,
                onDismis = { loginViewModel.onDialogChange(false) },
                modifier
            )
        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun muestra() {
    val show: Boolean = true
    val onDismis = {}
    val modifier = Modifier
    DiaologOk(show, onDismis, modifier)
}

@Composable
fun DiaologOk(show: Boolean, onDismis: () -> Unit, modifier: Modifier) {
    if (show) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color(0x20D2D2D2))
        ) {
            Dialog(onDismissRequest = onDismis) {
                Card(
                    modifier
                        .fillMaxWidth()
                        .padding(30.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xA1D2D2D2))
                ) {
                    Column(
                        modifier
                            .padding(40.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Recuperar \ncontraseña",
                            fontSize = 35.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            lineHeight = 40.sp,
                            modifier = modifier.padding(vertical = 8.dp)
                        )
                        Spacer(modifier.padding(10.dp))
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Checksito",
                            tint = Color(0xFF2A76F8),
                            modifier = modifier.size(80.dp)
                        )
                        Spacer(modifier.padding(25.dp))
                        Text(
                            "Se ha enviado un correo de recuperacion a su email",
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center
                        )
                    }

                }
            }
        }
    }

}


@Composable
fun EmailToRememberPass(modifier: Modifier, email: String, onTextChange: (String) -> Unit) {

    Column(modifier.padding(top = 80.dp)) {
        Text(
            "Email",
            modifier = Modifier.padding(bottom = 8.dp, start = 10.dp),
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF444444)
        )
        TextField(
            value = email,
            onValueChange = { onTextChange(it) },
            label = { Text("Ingresar email", color = Color(0xFF4E4E4E)) },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color(0xFF3D3D3D),
                unfocusedTextColor = Color(0xFF4B4B4B),
                focusedContainerColor = Color(0xFFAAAAAA),
                unfocusedContainerColor = Color(0xFFDBDBDB),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = Color(0xFF0D61EA)
            ),
            shape = RoundedCornerShape(10.dp)

        )
    }

}


@Composable
fun HeaderScreen(modifier: Modifier, navigationControler: NavHostController) {
    Column {
        Box(modifier = Modifier.fillMaxWidth()) {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = "Arrow",
                modifier
                    .clickable {
                        navigationControler.navigate(Routes.LoginScreen.routes)
                    }
                    .padding(start = 16.dp, top = 16.dp)
            )
        }
        Column(modifier.padding(20.dp)) {
            Text(
                text = "Recuperar  contraseña",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF454545),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = "Coloca tu email para que \n" +
                        "puedas recibir un correo \n" +
                        "con tu contraseña",
                fontSize = 25.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFFAFAFAF)

            )

        }

    }


}
