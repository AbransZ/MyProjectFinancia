package com.example.myprojectfinancia.Login.ui

import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.myprojectfinancia.Login.ui.ViewModel.LoginViewModel
import com.example.myprojectfinancia.Model.Routes


@Composable
fun ContainerCreate(navigationControler: NavHostController, loginViewModel: LoginViewModel) {
    //name
    val name: String by loginViewModel.name.observeAsState("")
    //email
    val email: String by loginViewModel.email.observeAsState("")
    //contraseña
    val Pass: String by loginViewModel.password.observeAsState("")
    //repeticion de contraseña
    val PassConfirm: String by loginViewModel.passconfirm.observeAsState("")
    //verificacion de contraseña
    val onPassMatc: Boolean by loginViewModel.onPassMatch.observeAsState(false)
    val isLoading: Boolean by loginViewModel.isLoading.observeAsState(false)


    Box(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .padding(bottom = 20.dp, start = 20.dp, end = 20.dp, top = 50.dp)
    ) {
        val context = LocalContext.current

        LaunchedEffect(key1 = true) {
            loginViewModel.navController.collect { route ->
                route?.let {
                    navigationControler.navigate(it) {
                        popUpTo(Routes.LoginScreen.routes) {
                            inclusive = true
                        }
                    }
                }
            }
        }
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
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center

            ) {
                CircularProgressIndicator(modifier = Modifier.size(50.dp))
            }
        }

        Column {
            TitleCreate()
            Spacer(modifier = Modifier.padding(20.dp))
            ContentCreate(name, email, Pass, PassConfirm, loginViewModel)
            Spacer(modifier = Modifier.padding(20.dp))
            ButtonCreate(loginViewModel, email, Pass, PassConfirm, isLoading)
            Spacer(modifier = Modifier.padding(28.dp))
            FooterCreate(navigationControler)
        }

    }
}

@Composable
fun FooterCreate(navigationControler: NavHostController) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(10.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "Ya tienes una cuenta?",
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onSecondary,
            fontWeight = FontWeight.Bold
        )
        TextButton(
            onClick = { navigationControler.navigate(Routes.LoginScreen.routes) },
        ) {
            Text(
                "Iniciar Sesion",
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


@Composable
fun ButtonCreate(
    loginViewModel: LoginViewModel,
    email: String,
    Pass: String,
    confirm: String,
    isLoading: Boolean = false

) {


    val isButtonEnable = loginViewModel.onButtonEnable(email, Pass, confirm) && !isLoading
    Button(
        onClick = {
            loginViewModel.onConnfirmPass(email, Pass, confirm)
        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        enabled = isButtonEnable,


        ) {
        Text(
            "Crear",
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 18.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }

}

@Composable
fun ContentCreate(
    name: String,
    email: String,
    Pass: String,
    PassConfirm: String,
    loginViewModel: LoginViewModel
) {


    Column(Modifier.fillMaxWidth()) {
        IngresarNombre(name) {
            loginViewModel.onRememberChangeConfirm(
                name = it,
                email = email,
                password = Pass,
                confirm = PassConfirm
            )
        }
        Spacer(Modifier.height(8.dp))

        IngresarEmail(email = email) {
            loginViewModel.onRememberChangeConfirm(
                name = name,
                email = it,
                password = Pass,
                confirm = PassConfirm
            )
        }
        Spacer(Modifier.height(8.dp))

        IngresarPassword(pass = Pass) {
            loginViewModel.onRememberChangeConfirm(
                name = name,
                email = email,
                password = it,
                confirm = PassConfirm
            )
        }
        Spacer(Modifier.height(8.dp))

        ConfirmPassword(PassConfirm) {
            loginViewModel.onRememberChangeConfirm(
                name = name,
                email = email,
                password = Pass,
                confirm = it
            )
        }

    }

}

@Composable
fun ConfirmPassword(ConfirmPass: String, onConfirmChange: (String) -> Unit) {
    var passVisibleConfirm by rememberSaveable { mutableStateOf(false) }
    Text(
        "Confirmar Contraseña",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(6.dp)
    )
    OutlinedTextField(
        value = ConfirmPass,
        onValueChange = { onConfirmChange(it) },
        label = { Text("Contraseña", color = MaterialTheme.colorScheme.onBackground) },
        visualTransformation = if (passVisibleConfirm) VisualTransformation.None else PasswordVisualTransformation(),
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            cursorColor = MaterialTheme.colorScheme.primary
        ),
        maxLines = 1,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {

            TextButton(onClick = { passVisibleConfirm = !passVisibleConfirm }) {
                if (passVisibleConfirm) {
                    Text("Ocultar", color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Mostrar", color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        },
        shape = RoundedCornerShape(30.dp)

    )
}

@Composable
fun IngresarPassword(pass: String, onValuePass: (String) -> Unit) {
    var passVisible by rememberSaveable { mutableStateOf(false) }
    Text(
        "Ingresar Contraseña",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(6.dp)
    )
    OutlinedTextField(
        value = pass,
        onValueChange = { onValuePass(it) },
        label = { Text("Contraseña", color = MaterialTheme.colorScheme.onBackground) },
        visualTransformation = if (passVisible) VisualTransformation.None else PasswordVisualTransformation(),
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            cursorColor = MaterialTheme.colorScheme.primary
        ),
        maxLines = 1,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),

        trailingIcon = {

            TextButton(onClick = { passVisible = !passVisible }) {
                if (passVisible) {
                    Text("Ocultar", color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Mostrar", color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        },
        shape = RoundedCornerShape(30.dp)

    )
}

@Composable
fun IngresarEmail(email: String, onValueEmail: (String) -> Unit) {
    Text(
        "Email",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(6.dp)
    )
    OutlinedTextField(
        value = email,
        onValueChange = { onValueEmail(it) },
        label = { Text("Email", color = MaterialTheme.colorScheme.onBackground) },
        maxLines = 1,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            cursorColor = MaterialTheme.colorScheme.primary
        ),
        shape = RoundedCornerShape(30.dp)
    )

}

@Composable
fun IngresarNombre(name: String, onvalueName: (String) -> Unit) {
    Text(
        "Ingresar nombre",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(6.dp)
    )
    OutlinedTextField(
        value = name,
        onValueChange = { onvalueName(it) },
        label = { Text("Nombre", color = MaterialTheme.colorScheme.onBackground) },
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            cursorColor = MaterialTheme.colorScheme.primary
        ),
        singleLine = true,
        shape = RoundedCornerShape(30.dp)

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
            color = MaterialTheme.colorScheme.onBackground
        )
    }

}
