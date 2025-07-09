package com.example.myprojectfinancia.Home.UI.Plans

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myprojectfinancia.theme.MyProjectFinanciaTheme


@Composable
fun SavingScreen(padding: PaddingValues, modifier: Modifier) {
    MyProjectFinanciaTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                Column(modifier.fillMaxSize()) {
                    Greetings()
                    PlanTotal()
                    PlanesList()
                }
            }
        }
    }

}

@Composable
fun Greetings(modifier: Modifier = Modifier) {
    Box(
        modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            "Planes Financieros",
            modifier = modifier.padding(16.dp),
            fontSize = 35.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun PlanTotal(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(.16.dp)
    ) {
        Row(modifier.fillMaxWidth()) {
            Card(
                modifier = modifier
                    .height(230.dp)
                    .width(400.dp)
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Box(modifier.padding(8.dp)) {
                    Column {
                        Text(
                            "Progreso de ahorro",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Spacer(modifier.padding(8.dp))
                        Row(
                            modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Ahorrado")
                            Text("Meta")
                        }

                        Row(
                            modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "USD $",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                "USD $",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                        Row(
                            modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Bs. ")
                            Text("Bs. ")
                        }

                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            LinearProgressIndicator(
                                progress = { 0.5f },
                                modifier = modifier.height(16.dp),
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                trackColor = MaterialTheme.colorScheme.onSecondary,
                                strokeCap = StrokeCap.Round,

                                )

                            Text("46%")

                        }
                        Spacer(modifier.padding(10.dp))
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                            Text(
                                "Faltan ---$ o Bs.--- para alcanzar su meta",
                                fontSize = 12.sp,
                            )
                        }

                    }
                }
            }
        }
    }
}


@Composable
fun PlanesList(modifier: Modifier = Modifier) {
    Box(
        modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Column {
            Text("Planes de ahorro", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            LazyColumn {
                item { PlanesCard() }
                item { PlanesCard() }
                item { PlanesCard() }

            }
        }



    }

}


@Composable
fun PlanesCard(modifier: Modifier = Modifier) {
        Card(
            modifier = modifier
                .height(200.dp)
                .width(400.dp)
                .padding(10.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Box(
                modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Column {
                    Row(
                        modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Nombre del plan D/C",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            "50% D/C",
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )

                    }
                    Spacer(modifier.padding(2.dp))
                    AssistChip(
                        modifier = modifier.height(18.dp),
                        onClick = {},
                        label = { Text("Category", fontSize = 10.sp) },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            labelColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                    Spacer(modifier.padding(5.dp))
                    Box(Modifier.fillMaxWidth()) {
                        Text(
                            "Descripcion especifica sobre el plan financiero agregado por el usuario D/C",
                            fontSize = 10.sp,
                            textAlign = TextAlign.Center,
                            lineHeight = 12.sp
                        )
                    }
                    Spacer(modifier.padding(2.dp))
                    Row(
                        modifier = modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        LinearProgressIndicator(
                            progress = { 0.5f },
                            modifier = modifier
                                .height(12.dp)
                                .width(350.dp),
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            trackColor = MaterialTheme.colorScheme.onSecondary,
                            strokeCap = StrokeCap.Round,

                            )
                    }
                    Spacer(modifier.padding(2.dp))
                    Row(
                        modifier = modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                "$40 D/C",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                            Text(
                                "Bs 4000 D/C",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Light,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }

                        Text("De", fontSize = 10.sp, fontWeight = FontWeight.Light)

                        Column {
                            Text(
                                "$100 D/C",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                            Text(
                                "Bs 10000 D/C",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                    Spacer(modifier.padding(6.dp))
                    Text(
                        "Consejo considerado por la plataforma para guiar al usuario en cuanto al ingreso mensual que deberia tener su cuota D/C",
                        fontSize = 10.sp,
                        lineHeight = 12.sp
                    )
                }

            }
        }

}