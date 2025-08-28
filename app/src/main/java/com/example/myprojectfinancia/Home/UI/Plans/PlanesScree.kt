package com.example.myprojectfinancia.Home.UI.Plans

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.myprojectfinancia.Home.UI.Plans.ModelsPlans.planItem
import com.example.myprojectfinancia.Home.UI.Plans.ViewModel.PlansViewModel
import com.example.myprojectfinancia.R
import com.example.myprojectfinancia.theme.MyProjectFinanciaTheme


@Composable
fun SavingScreen(padding: PaddingValues, plansViewModel: PlansViewModel, modifier: Modifier) {
    val namePlan by plansViewModel.namePlan.collectAsState()
    val description by plansViewModel.description.collectAsState()
    val target by plansViewModel.target.collectAsState()
    val date by plansViewModel.date.collectAsState()
    val category by plansViewModel.category.collectAsState()
    val mountActually by plansViewModel.mountActually.collectAsState()
    val dateTarget by plansViewModel.dateTarget.collectAsState()
    val plansList by plansViewModel.plans.collectAsState()
    val isLoading by plansViewModel.isLoading.collectAsState()
    val aporte by plansViewModel.aporte.collectAsState()

    LaunchedEffect(Unit) {
        plansViewModel.initializePlansUser()
    }


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
                    PlanesList(modifier, plansViewModel, plansList, isLoading)
                    AddPlansDialog(
                        plansViewModel,
                        namePlan = namePlan,
                        description = description,
                        target = target,
                        date = date,
                        category = category,
                        mountActually = mountActually
                    )
                    AddmoneyToPlans(modifier, plansViewModel, aporte, plansList)

                }
                FABP(
                    plansViewModel, modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                )
            }
        }
    }

}

@Composable
fun AddmoneyToPlans(modifier: Modifier, plansViewModel: PlansViewModel, aporte: String, plansList: List<planItem>) {
    val showDialogMoney by plansViewModel.showDialogAddMoney.collectAsState()
    val selectedPlan by plansViewModel.selectePlanItem.collectAsState()

    if (showDialogMoney) {
        Dialog(onDismissRequest = { plansViewModel.hideDialogMoney() }) {
            Card(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(30.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {

                    BodyDialogMoney(modifier, plansViewModel, aporte, selectedPlan)
                    Spacer(modifier.height(5.dp))
                    ButtonsDialogMoney(modifier, plansViewModel, aporte)
                }

            }
        }
    }
}

@Composable
fun ButtonsDialogMoney(modifier: Modifier, plansViewModel: PlansViewModel, aporte: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp), horizontalArrangement = Arrangement.End
    ) {
        Button(
            onClick = {

                plansViewModel.addMoneyToPlan(aporte)

            }, shape = MaterialTheme.shapes.small, colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) { Text("Guardar") }
        Spacer(Modifier.width(10.dp))
        Button(
            onClick = { plansViewModel.hideDialogMoney() },
            shape = MaterialTheme.shapes.small,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            )
        ) { Text("Cancelar", color = MaterialTheme.colorScheme.onSecondary) }
    }
}

@Composable
fun BodyDialogMoney(modifier: Modifier, plansViewModel: PlansViewModel, aporte: String, plan: planItem?) {


    Column(modifier.fillMaxWidth()) {
        Box(modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    plan?.Name.toString(),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier.height(10.dp))
                plan?.let { plan ->
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Text(
                                text = plan.Name,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Actual: $${plan.Actualy}",
                                fontSize = 14.sp
                            )
                            Text(
                                text = "Meta: $${plan.Objective}",
                                fontSize = 14.sp
                            )
                        }
                    }
                }
                Text("Agregar aporte al plan", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.padding(6.dp))


                OutlinedTextField(
                    value = aporte,
                    onValueChange = { plansViewModel.onAportePlanChange(it) },
                    placeholder = { Text("00.0") },
                    prefix = { Text("$") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = MaterialTheme.shapes.medium,
                    maxLines = 1,
                    singleLine = true,
                    modifier = Modifier.height(56.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    )
                )
            }


        }

    }
}

//Boton para agg Plan
@Composable
fun FABP(plansViewModel: PlansViewModel, modifier: Modifier = Modifier) {
    FloatingActionButton(
        onClick = {
            plansViewModel.showDialogPlans()
        },
        modifier = modifier,
        shape = FloatingActionButtonDefaults.smallShape

    ) {
        Icon(
            painter = painterResource(R.drawable.ic_plans_add),
            contentDescription = "add Plans",
            modifier = Modifier.size(45.dp)
        )
    }
}

//Dialogo para agregar planes
@Composable
fun AddPlansDialog(
    plansViewModel: PlansViewModel,
    modifier: Modifier = Modifier,
    namePlan: String,
    description: String,
    target: String,
    date: String,
    category: String,
    mountActually: String
) {
    val showDialogPlans by plansViewModel.showDialogAdd.collectAsState()


    if (showDialogPlans) {
        Dialog(onDismissRequest = { plansViewModel.hideDialog() }) {
            Box(
                modifier = modifier
                    .clip(shape = RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.surface)

            ) {
                LazyColumn(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(16.dp)

                ) {
                    //item para encabezado
                    item {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                "Agregar nuevo plan de ahorro",
                                fontSize = 25.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = modifier.padding(bottom = 10.dp)
                            )
                        }

                    }
                    //item para el dialogo
                    item {
                        BodyDialogPlans(
                            namePlan = namePlan,
                            description = description,
                            target = target,
                            date = date,
                            category = category,
                            mountActually = mountActually,
                            plansViewModel = plansViewModel,
                        )
                    }
                    //item para botones
                    item {
                        ButtonsDialogPlans(plansViewModel)
                    }
                }
            }
        }
    }
}

//Botones del dialogo
@Composable
fun ButtonsDialogPlans(plansViewModel: PlansViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp), horizontalArrangement = Arrangement.End
    ) {
        Button(
            onClick = {
                plansViewModel.savePlans()
                plansViewModel.clearFields()

            }, shape = MaterialTheme.shapes.small, colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) { Text("Guardar") }
        Spacer(Modifier.width(10.dp))
        Button(
            onClick = { plansViewModel.hideDialog() },
            shape = MaterialTheme.shapes.small,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            )
        ) { Text("Cancelar", color = MaterialTheme.colorScheme.onSecondary) }
    }
}

//Cuerpo del dialogo
@Composable
fun BodyDialogPlans(
    namePlan: String,
    description: String,
    target: String,
    date: String,
    category: String,
    mountActually: String,
    plansViewModel: PlansViewModel
) {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(Modifier.fillMaxWidth()) {


            //Campos de texto para ingresar Plan financiero
            NamePlan(namePlan, plansViewModel)
            Spacer(Modifier.padding(5.dp))
            DescriptionPlan(description, plansViewModel)
            Spacer(Modifier.padding(5.dp))
            CategoryPlan(category, plansViewModel)
            Spacer(Modifier.height(20.dp))
            Text("Objetivos Financieros", fontSize = 30.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(20.dp))
            TargetPlan(target, plansViewModel)
            Spacer(Modifier.padding(5.dp))
            MountActuallyPlan(mountActually, plansViewModel)
            Spacer(Modifier.padding(5.dp))
            Text("Fecha $date", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        }
    }
}

@Composable
fun TargetPlan(target: String, plansViewModel: PlansViewModel) {
    Column(Modifier.fillMaxWidth()) {
        Text("Monto objetivo a alcanzar", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.padding(6.dp))
        OutlinedTextField(
            value = target,
            onValueChange = { plansViewModel.onTargetChange(it) },
            placeholder = { Text("Monto objetivo.") },

            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = MaterialTheme.shapes.medium,
            maxLines = 1,
            singleLine = true,
            modifier = Modifier.height(56.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        )
    }
}

@Composable
fun MountActuallyPlan(mountActually: String, plansViewModel: PlansViewModel) {
    Column(Modifier.fillMaxWidth()) {
        Text("Monto actual", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.padding(6.dp))
        OutlinedTextField(
            value = mountActually,
            onValueChange = { plansViewModel.onMountActuallyChange(it) },
            placeholder = { Text("Monto al iniciar el plan.") },

            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = MaterialTheme.shapes.medium,
            maxLines = 1,
            modifier = Modifier.height(56.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        )
    }
}


@Composable
fun CategoryPlan(category: String, plansViewModel: PlansViewModel) {
    Column(Modifier.fillMaxWidth()) {
        Text("Categoria ", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.padding(6.dp))
        OutlinedTextField(
            value = category,
            onValueChange = { plansViewModel.onCategoryChange(it) },
            placeholder = { Text("Ej. Viajes, Carro nuevo, etc") },

            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            shape = MaterialTheme.shapes.medium,
            maxLines = 1,
            modifier = Modifier.height(56.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        )
    }
}

//Campo de texto para ingresar descripcion del plan
@Composable
fun DescriptionPlan(description: String, plansViewModel: PlansViewModel) {
    Column(Modifier.fillMaxWidth()) {
        Text("Descripcion", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.padding(6.dp))
        OutlinedTextField(
            value = description,
            onValueChange = { plansViewModel.onDescriptionChange(it) },
            placeholder = { Text("Descripcion de la finalidad del plan.") },

            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            shape = MaterialTheme.shapes.medium,
            maxLines = 3,
            modifier = Modifier.height(100.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        )
    }

}

//Campo de texto para ingresar nombre del plan
@Composable
fun NamePlan(namePlan: String, plansViewModel: PlansViewModel) {
    Column(Modifier.fillMaxWidth()) {
        Text("Nombre", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.padding(6.dp))
        OutlinedTextField(
            value = namePlan,
            onValueChange = { plansViewModel.onNameChange(it) },
            placeholder = { Text("Ingresar nombre del plan.") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            shape = MaterialTheme.shapes.medium,
            maxLines = 1,
            singleLine = true,
            modifier = Modifier.height(56.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        )
    }

}

//Encabezado
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

//Plan de ahorro
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

//Lista de planes
@Composable
fun PlanesList(
    modifier: Modifier = Modifier,
    plansViewModel: PlansViewModel,
    plansList: List<planItem>,
    isLoading: Boolean
) {
    Box(
        modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Column {
            Text("Planes de ahorro", fontSize = 20.sp, fontWeight = FontWeight.Bold)

            if (isLoading) {

                Box(
                    Modifier
                        .fillMaxSize()
                        .align(Alignment.CenterHorizontally),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }

                }
            } else {
                if (plansList.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "No hay planes registrados aun",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Light,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                        ) {
                            items(plansList) { plan ->
                                PlanesCard(modifier, plan, plansViewModel)
                            }
                        }
                    }
                }
            }

        }


    }

}

//Item de planes
@Composable
fun PlanesCard(modifier: Modifier = Modifier, planItem: planItem, plansViewModel: PlansViewModel) {

    ElevatedCard(
        onClick = { plansViewModel.selectedPlan(planItem) },
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
                        planItem.Name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        "${(planItem.Actualy.toDouble() / planItem.Objective.toDouble() * 100).toString()}%",
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                }
                Spacer(modifier.padding(2.dp))
                AssistChip(
                    modifier = modifier.height(18.dp),
                    onClick = {},
                    label = { Text(planItem.Category, fontSize = 10.sp) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        labelColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
                Spacer(modifier.padding(5.dp))
                Box(Modifier.fillMaxWidth()) {
                    Text(
                        planItem.Description,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 12.sp
                    )
                }
                Spacer(modifier.padding(5.dp))
                Row(
                    modifier = modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    LinearProgressIndicator(
                        progress = { (planItem.Actualy.toDouble() / planItem.Objective.toDouble()).toFloat() },
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
                            "$${planItem.Actualy}",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Text(
                            "Bs ${planItem.Actualy.toDouble() * 140}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Light,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    Text("De", fontSize = 10.sp, fontWeight = FontWeight.Light)

                    Column {
                        Text(
                            "$${planItem.Objective}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Text(
                            "Bs ${planItem.Objective.toDouble() * 140}",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
                Spacer(modifier.padding(6.dp))
                Text(
                    planItem.Advice,
                    fontSize = 15.sp,
                    lineHeight = 12.sp
                )
            }

        }
    }

}

