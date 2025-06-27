package com.example.myprojectfinancia.Login.ui


import com.example.myprojectfinancia.Model.Routes


import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.myprojectfinancia.R
import com.example.myprojectfinancia.Login.ui.ViewModel.LoginViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException


@Composable
fun LogginScreen(
    modifier: Modifier,
    navigationControler: NavHostController,
    loginViewModel: LoginViewModel
) {
    Box(
        modifier
            .fillMaxSize()
            .padding(top = 26.dp)
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        val context = LocalContext.current

        LaunchedEffect(Unit) {

            loginViewModel.navController.collect { route ->
                println("DEBUG: Evento de navegación recibido: $route")
                route?.let {
                    Log.i("LoginScreen", "Navegando a $route")

                    navigationControler.navigate(route) {
                        popUpTo(Routes.LoginScreen.routes) { inclusive = true }
                    }
                }
            }
        }
        LaunchedEffect(Unit) {
            loginViewModel.message.collect { message ->
                message?.let {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        Header(
            Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        )
        Body(
            Modifier
                .align(Alignment.Center)
                .padding(horizontal = 16.dp),
            loginViewModel,
            navigationControler
        )

        Footer(
            Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            navigationControler
        )

    }

}


@Composable
fun Body(
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel,
    navigationControler: NavHostController
) {
    val email: String by loginViewModel.email.observeAsState("")
    val Pass: String by loginViewModel.password.observeAsState("")
    val isEnable: Boolean by loginViewModel.isEnable.observeAsState(false)
    val isLoading: Boolean by loginViewModel.isLoading.observeAsState(false)



    Column(modifier = modifier) {

        Greetings(modifier)
        Spacer(modifier = modifier.size(18.dp))


        Email(email) { loginViewModel.onLoginChange(email = it, password = Pass) }
        Spacer(modifier = modifier.size(16.dp))

        Password(Pass) { loginViewModel.onLoginChange(email = email, password = it) }
        Spacer(modifier = modifier.size(16.dp))

        ForgotButton(navigationControler,loginViewModel)
        Spacer(modifier = modifier.size(24.dp))

        Buttons(isEnable, loginViewModel, email, Pass)
        Spacer(modifier = modifier.size(32.dp))

        Divider()
        Spacer(modifier = modifier.size(20.dp))
        Googlebuttons(loginViewModel)

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center

            ) {
                CircularProgressIndicator()
            }
        }


    }

}

@Composable
fun Greetings(modifier: Modifier) {
    Column(modifier = modifier) {
        Text(
            text = "Hola, Bienvenido!",
            fontSize = 45.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        Text(
            text = "Estamos Felices de tenerte acá",
            fontSize = 25.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.LightGray
        )
        Box(modifier = modifier.fillMaxWidth().padding(top = 15.dp), contentAlignment = Alignment.Center){
            Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = "Logo-Bienvenida",
            modifier
                .size(200.dp)
                .clip(RoundedCornerShape(50))
        )}

    }
}

@Composable
fun Footer(modifier: Modifier, navigationControler: NavHostController) {
    Column(modifier = modifier.fillMaxWidth()) {

        SignIn(navigationControler)
    }
}

@Composable
fun SignIn(navigationControler: NavHostController) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Aun no tienes una cuenta?",
            color = MaterialTheme.colorScheme.onBackground
        )
        TextButton(
            onClick = { navigationControler.navigate(Routes.CreateScreen.routes) },
            Modifier.padding(start = 1.dp)
        ) {
            Text(
                "Crear cuenta",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun Googlebuttons(loginViewModel: LoginViewModel) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                loginViewModel.processGoogleSingIn(task)

            } catch (ex: ApiException) {
                Log.e("abrahan", "Error en el sing in ${ex.message}")
                Toast.makeText(
                    context,
                    "Error en el sing in ${ex.message}", Toast.LENGTH_SHORT
                )
                    .show()

            }
        }
    }
    Box(Modifier.fillMaxWidth()) {
        OutlinedButton(
            onClick = {
                val signInIntent = loginViewModel.getGoogleSignInClient().signInIntent
                launcher.launch(signInIntent)

            }, modifier = Modifier.fillMaxWidth().height(40.dp)

        ) {
            Icon(
                painter = painterResource(R.drawable.search),
                contentDescription = "Cuenta Google",
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(40.dp),
                tint = Color.Unspecified
            )
            Text(
                text = "Usar cuenta de Google",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}


@Composable
fun Divider() {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        HorizontalDivider(
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp), color = Color.LightGray
        )
        Text("O", color = Color.LightGray, fontSize = 14.sp)
        HorizontalDivider(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp), color = Color.LightGray
        )
    }

}

@Composable
fun ForgotButton(navigationControler: NavHostController, loginViewModel: LoginViewModel) {
    Box(Modifier.fillMaxWidth()) {
        TextButton(
            onClick = {
                navigationControler.navigate(Routes.ForgotPasswordScreen.routes)
                },
            Modifier.align(Alignment.CenterEnd)
        ) {
            Text(
                text = "¿Olvidaste la contraseña?",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }

}

@Composable
fun Buttons(isEnable: Boolean, loginViewModel: LoginViewModel, email: String, Pass: String) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Row() {
            Button(
                onClick = {
                    Log.i("LoginScreen", "Login PRESIONADOOOOOOOOO")
                    loginViewModel.login(email, Pass)

                },
                enabled = isEnable,
                colors = ButtonDefaults.buttonColors(
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    containerColor = MaterialTheme.colorScheme.primary,
                    disabledContentColor = MaterialTheme.colorScheme.onErrorContainer,
                    disabledContainerColor = MaterialTheme.colorScheme.errorContainer,

                    ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                shape = RoundedCornerShape(30.dp),


                ) {
                Text(
                    text = "Ingresar",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 20.sp
                )
            }

        }
    }


}


@Composable
fun Password(pass: String, onTextChange: (String) -> Unit) {
    var passVisible by rememberSaveable { mutableStateOf(false) }

    Column {
        Text(
            "Contraseña",
            modifier = Modifier.padding(bottom = 8.dp, start = 10.dp),
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground
        )
        OutlinedTextField(
            value = pass,
            onValueChange = { onTextChange(it) },
            label = { Text("Contraseña", color = MaterialTheme.colorScheme.onBackground) },
            visualTransformation = if (passVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            maxLines = 1,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                cursorColor = MaterialTheme.colorScheme.primary
            ),
            trailingIcon = {

                TextButton(onClick = { passVisible = !passVisible }) {
                    if (passVisible) {
                        Text("Ocultar", color = MaterialTheme.colorScheme.onBackground)
                    } else {
                        Text("Mostrar", color = MaterialTheme.colorScheme.onBackground)
                    }
                }
            },
            shape = RoundedCornerShape(30.dp)

        )
    }

}


@Composable
fun Email(email: String, onTextChange: (String) -> Unit) {

    Column {
        Text(
            "Email",
            modifier = Modifier.padding(bottom = 8.dp, start = 10.dp),
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground
        )
        OutlinedTextField(
            value = email,
            onValueChange = { onTextChange(it) },
            label = { Text("Ingresar email", color = MaterialTheme.colorScheme.onBackground) },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            maxLines = 1,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                cursorColor = MaterialTheme.colorScheme.primary
            ),
            shape = RoundedCornerShape( 30.dp)

        )
    }


}

@Composable
fun Header(modifier: Modifier) {
    val activity = LocalContext.current as Activity
    Icon(
        imageVector = Icons.Default.Close,
        contentDescription = "Close app",
        modifier = modifier.clickable { activity.finish() },
        tint = MaterialTheme.colorScheme.onBackground
    )
}
