package com.example.myprojectfinancia.Index.settings

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.myprojectfinancia.Data.API.network.DolarOficial
import com.example.myprojectfinancia.Index.Login.ViewModel.LoginViewModel
import com.example.myprojectfinancia.Index.Plans.ViewModel.PlansViewModel
import com.example.myprojectfinancia.Index.home.ViewModels.homeViewModel
import com.example.myprojectfinancia.Index.settings.viewModel.settingsViewmodel
import com.example.myprojectfinancia.R


@Composable
fun SettingsScreen(
    padding: PaddingValues,
    modifier: Modifier = Modifier,
    homeViewModel: homeViewModel,
    plansViewModel: PlansViewModel,
    navController: NavController,
    loginViewModel: LoginViewModel,
    settingsViewmodel: settingsViewmodel
) {

    val showAbout by settingsViewmodel.showAbout.collectAsState(false)
    val dolarObject by homeViewModel.DolarObject.collectAsState()
    val monto by settingsViewmodel.monto.collectAsState("")
    val montoBs by settingsViewmodel.montoBs.collectAsState("")
    val context = LocalContext.current
    val error = settingsViewmodel.error.collectAsState("")


    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(padding)
            .padding(16.dp)
    ) {
        LaunchedEffect(Unit) {
            homeViewModel.getDolarBCV()

        }

        LaunchedEffect(error.value) {
            error.value?.let { message ->
                if (message.isNotEmpty()) {
                    Toast.makeText(context, message, Toast.LENGTH_LONG)
                        .show()
                    settingsViewmodel.clearError()
                }
            }
        }


        LazyColumn(modifier = modifier.fillMaxSize()) {
            item { UserItem(modifier) }
            item { Spacer(modifier.padding(18.dp)) }
            item { Text("Conversor de moneda", fontSize = 20.sp, fontWeight = FontWeight.Bold) }
            item { ConversorMoneda(modifier, settingsViewmodel, monto, montoBs, dolarObject) }
            item { Spacer(modifier.padding(18.dp)) }
            item {
                Card(
                    onClick = { settingsViewmodel.showAbout() },
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(Modifier.padding(10.dp)) {
                        Text("Sobre Financia", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text(
                            "Detalles sobre la aplicaccion y version",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                    }
                }

            }
            item { Spacer(modifier.padding(18.dp)) }
            item { AboutFinancia(modifier) }
            item { Spacer(modifier.padding(18.dp)) }
            item { ButtonClose(modifier, homeViewModel, navController, loginViewModel) }
            item { ConfigurationUser() }
            item { AboutFinanciaDialog(showAbout, settingsViewmodel) }

        }

    }
}


@Composable
fun AboutFinanciaDialog(showAbout: Boolean, settingsViewmodel: settingsViewmodel) {

    if (showAbout) {
        Dialog(onDismissRequest = { settingsViewmodel.hideAbout() }) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(shape = RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                LazyColumn(
                    Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    //verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        Text(
                            "Sobre Financia",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                    item { HeaderFinancia() }
                    item { Spacer(Modifier.padding(10.dp)) }
                    item { Text("Manual de usuario", fontSize = 30.sp, fontWeight = FontWeight.Bold) }
                    item { Spacer(Modifier.padding(10.dp)) }
                    item { Text("\uD83C\uDFE0 Pantalla Principal", fontSize = 20.sp, fontWeight = FontWeight.Bold) }
                    item { Spacer(Modifier.padding(6.dp)) }
                    item {
                        Text(
                            "• Visualiza tu presupuesto actual en USD/VES\n\n" +
                                    "• Revisa el resumen de tus ahorros\n\n" +
                                    "• Ve grafico de resumen de todos tus planes de ahorros\n\n" +
                                    "• Consulta tus transacciones filtradas por gasto o ingreso\n\n" +
                                    "• Presiona el botón \"+\" para agregar movimientos\n"
                        )
                    }
                    item { Spacer(Modifier.padding(10.dp)) }
                    item { Text("\uD83D\uDCB0 Planes Financieros", fontSize = 20.sp, fontWeight = FontWeight.Bold) }
                    item { Spacer(Modifier.padding(6.dp)) }
                    item {
                        Text(
                            "• Crea metas de ahorro personalizadas a tus necesidades\n\n" +
                                    "• Establece montos objetivo \n\n" +
                                    "• Recibe sugerencia de aportes adecuados \na tus necesidades y alcances\n\n" +
                                    "• Monitorea el progreso de cada plan con \nsu respectiva barra de progreso\n\n"
                        )
                    }
                    item { Text("\uD83D\uDCCA Transacciones\n", fontSize = 20.sp, fontWeight = FontWeight.Bold) }
                    item {
                        Text(
                            "• Registra ingresos y gastos fácilmente desde la pantalla principal \n\n" +
                                    "• Categoriza tus movimientos agregando tus propias categorias\n\n" +
                                    "• Agrega descripciones detalladas\n\n" +
                                    "• Visualiza conversión automática USD/VES en cada uno de los apartados\n\n"
                        )
                    }
                    item { Text("⚙\uFE0F Herramientas\n", fontSize = 20.sp, fontWeight = FontWeight.Bold) }
                    item {
                        Text(
                            "• Actualiza tu información personal\n\n" +
                                    "• Cambia tu contraseña de forma segura\n\n" +
                                    "• Usa el conversor de moneda con tasa BCV actualizada\n\n" +
                                    "• Consulta estadísticas de actividad\n\n" +
                                    "• Accede a información de la aplicación\n\n"
                        )
                    }
                    item { Text("\uD83D\uDCA1 Consejos Útiles\n", fontSize = 20.sp, fontWeight = FontWeight.Bold) }
                    item {
                        Text(
                            "• Registra tus gastos diariamente para mejor control\n" +
                                    "• Establece metas realistas en tus planes de ahorro\n" +
                                    "• Revisa regularmente tu progreso financiero\n" +
                                    "• Usa categorías consistentes para mejor análisis\n" +
                                    "• Mantén actualizada tu información de perfil"
                        )
                    }
                    item { Spacer(Modifier.padding(20.dp)) }
                    item { Text("Contáctanos", fontSize = 30.sp, fontWeight = FontWeight.Bold) }
                    item { ContactUs() }
                    item { ExtraInfo() }
                }
            }
        }
    }
}


@Composable
fun ExtraInfo() {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Outlined.FavoriteBorder,
            contentDescription = "favorite",
            modifier = Modifier.size(50.dp),
            tint = Color.Red
        )
        Spacer(Modifier.height(15.dp))
        Text("¡Gracias por usas Financia!", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(5.dp))
        Text(
            "Tu confianza nos motiva a seguir mejorando y crear la mejor experiencia de gestión financiera para ti" +
                    ".", fontSize = 15.sp, textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ContactUs() {
    Box(Modifier.fillMaxWidth()) {

        Spacer(Modifier.padding(30.dp))
        Card(
            Modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(15.dp))
                .padding(4.dp)
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(6.dp)
            ) {
                Text("Desarrollador FullStack", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text("Email: abrahannieves700@gmail.com", fontSize = 15.sp)
                Spacer(Modifier.padding(2.dp))
                Text(
                    "Soy un desarrollador novato apasionado por la tecnologia y el desarrollo mobile usando kotlin y" +
                            " jetpack compose con manejo basico de la arquitectura mvvm y tambien la plataforma de " +
                            "firebase para llevar acabo las aplicaciones que desarrollo"
                )
            }


        }
    }
}


@Composable
fun HeaderFinancia() {
    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(R.drawable.logo), contentDescription = "LogoFinancia",
                contentScale = androidx.compose.ui.layout.ContentScale.None,
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(50.dp))
                    .size(100.dp)

            )
            Spacer(Modifier.height(10.dp))
            Text(
                "Financia",
                textAlign = TextAlign.Center,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme
                    .onPrimary
            )
            Text("V1.0", textAlign = TextAlign.Center, fontSize = 15.sp)
            Spacer(Modifier.height(6.dp))
            Text(
                "Tu compañero inteligente para la gestión financiera personal. Controla tus gastos, planifica tus " +
                        "ahorros y alcanza tus metas económicas.", fontSize = 13.sp, textAlign = TextAlign.Center,
                modifier =
                Modifier
                    .width(250.dp)
            )
        }

    }
}

@Composable
fun ConfigurationUser() {

}

@Composable
fun ButtonClose(
    modifier: Modifier,
    homeViewModel: homeViewModel,
    navController: NavController,
    loginViewModel: LoginViewModel
) {
    Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Button(
                onClick = {
                    val route = homeViewModel.logout()
                    navController.navigate(route) {
                        popUpTo(0) { inclusive = true }
                        loginViewModel.clearFields()
                    }
                },
                modifier = Modifier.height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onErrorContainer),
                shape = RoundedCornerShape(10.dp)
            ) {
                Row(modifier = modifier.fillMaxWidth()) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = "Logout",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                    Text(
                        "Cerrar Sesion",
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = modifier.fillMaxWidth()
                    )


                }
            }
            Spacer(Modifier.height(10.dp))
            Text(
                "Financia V1.0 una app para tus finanzas \n Hecha con \uD83E\uDE77 en Venezuela",
                textAlign = TextAlign.Center,
                fontSize = 15.sp,
                fontWeight = FontWeight.ExtraLight
            )
        }


    }
}

@Composable
fun AboutFinancia(modifier: Modifier) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Actividad",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Row(modifier = modifier.fillMaxWidth()) {
                Column(
                    modifier = modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "10",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        "Movimientos",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondary
                    )


                }
                Column(
                    modifier = modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "3",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xC6E79504)
                    )
                    Text(
                        "Planes Activos",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }
                Column(
                    modifier = modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "5",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xA901D758),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        "Planes Completados",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun ConversorMoneda(
    modifier: Modifier,
    settingsViewmodel: settingsViewmodel,
    monto: String,
    montoBs: String,
    dolarObject: DolarOficial?
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
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
                value = monto,
                onValueChange = { settingsViewmodel.convertUSDToBs(it) },
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
                value = montoBs,
                onValueChange = { settingsViewmodel.convertBstoUSD(it) },
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
        modifier = Modifier
            .height(100.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
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
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings",
                tint = MaterialTheme.colorScheme.primary,
                modifier = modifier
                    .align(Alignment.CenterVertically)
                    .padding(6.dp)
                    .size(30.dp)
                    .clickable { }
            )
        }

    }

}
