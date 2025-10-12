package com.example.myprojectfinancia.Index.Plans.UI

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.myprojectfinancia.Data.API.network.DolarOficial
import com.example.myprojectfinancia.Index.Plans.ModelsPlans.planItem
import com.example.myprojectfinancia.Index.Plans.ViewModel.PlansViewModel
import com.example.myprojectfinancia.R
import com.example.myprojectfinancia.theme.MyProjectFinanciaTheme


@Composable
fun SavingScreen(padding: PaddingValues, plansViewModel: PlansViewModel, modifier: Modifier) {
    val context = LocalContext.current
    val namePlan by plansViewModel.namePlan.collectAsState()
    val description by plansViewModel.description.collectAsState()
    val target by plansViewModel.target.collectAsState()
    val date by plansViewModel.date.collectAsState()
    val category by plansViewModel.category.collectAsState()
    val mountActually by plansViewModel.mountActually.collectAsState()
    val namePlaneEdit by plansViewModel.namePlanEdit.collectAsState(namePlan)
    val descriptionedit by plansViewModel.descriptionEdit.collectAsState(description)
    val targetEdit by plansViewModel.targetEdit.collectAsState(target)
    val categoryEdit by plansViewModel.categoryEdit.collectAsState(category)
    val mountActuallyEdit by plansViewModel.mountActuallyEdit.collectAsState(mountActually)
    val dateTarget by plansViewModel.dateTarget.collectAsState()
    val plansList by plansViewModel.plans.collectAsState()
    val isLoading by plansViewModel.isLoading.collectAsState()
    val aporte by plansViewModel.aporte.collectAsState()
    val error by plansViewModel.error.collectAsState()
    val progress by plansViewModel.progres.collectAsState()
    val missing by plansViewModel.missing.collectAsState()
    val totalSaved by plansViewModel.totalSaved.collectAsState()
    val budgetTarget by plansViewModel.budgetTarget.collectAsState()
    val UIDolar by plansViewModel.UIDolar.collectAsState()
    val dolarObject by plansViewModel.DolarObject.collectAsState()
    val montoBs by plansViewModel.montoBsstring.collectAsState("")
    val aportBs by plansViewModel.aporteBs.collectAsState("")
    val targetBs by plansViewModel.targetBs.collectAsState("")
    val targetBsEdit by plansViewModel.targetBsEdit.collectAsState(targetBs)
    val montoBsEdit by plansViewModel.mountActuallyBsEdit.collectAsState(montoBs)


    LaunchedEffect(error) {
        error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG)
                .show()
            plansViewModel.clearError()
        }
    }

    LaunchedEffect(Unit) {
        plansViewModel.getDolarBCV()
        plansViewModel.initializePlansUser()

    }


    MyProjectFinanciaTheme {
        Surface(
            modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
        ) {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                Column(modifier.fillMaxSize()) {
                    Greetings()
                    PlanTotal(Modifier, totalSaved, budgetTarget, progress, missing, plansViewModel)
                    PlanesList(Modifier, plansViewModel, plansList, isLoading)
                    AddPlansDialog(
                        plansViewModel,
                        namePlan = namePlan,
                        description = description,
                        target = target,
                        date = date,
                        category = category,
                        mountActually = mountActually,
                        dolarBs = dolarObject,
                        montoBs = montoBs,
                        targetBs = targetBs
                    )
                    AddmoneyToPlans(Modifier, plansViewModel, aporte, plansList, aportBs, dolarObject)
                    EditPlansDialog(
                        Modifier,
                        plansViewModel,
                        namePlaneEdit,
                        descriptionedit,
                        targetEdit,
                        date,
                        categoryEdit,
                        mountActuallyEdit,
                        targetBsEdit,
                        montoBsEdit,
                        dolarObject
                    )

                }
                FABP(
                    plansViewModel,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun EditPlansDialog(
    modifier: Modifier,
    plansViewModel: PlansViewModel,
    namePlan: String,
    description: String,
    target: String,
    date: String,
    category: String,
    mountActually: String,
    targetBsEdit: String,
    montoBsEdit: String,
    dolarObject: DolarOficial?
) {
    val showDialogEdit by plansViewModel.showDialogEdit.collectAsState()


    if (showDialogEdit) {
        Dialog(onDismissRequest = { plansViewModel.hideDialogEdit() }) {
            Box(
                modifier = Modifier
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
                                "Editar plan de ahorro",
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = modifier.padding(bottom = 10.dp)
                            )
                        }

                    }
                    //item para el dialogo
                    item {
                        BodyDialogEditPlans(
                            namePlan = namePlan,
                            description = description,
                            target = target,
                            targetBs = targetBsEdit,
                            date = date,
                            category = category,
                            mountActually = mountActually,
                            mountActuallyBs = montoBsEdit,
                            plansViewModel = plansViewModel,
                            dolarObject
                        )
                    }
                    //item para botones
                    item {
                        ButtonsDialogEditPlans(plansViewModel)
                    }
                }
            }
        }
    }

}

@Composable
fun ButtonsDialogEditPlans(plansViewModel: PlansViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp), horizontalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {
                plansViewModel.updatePlans()
                plansViewModel.clearFields()

            }, shape = MaterialTheme.shapes.small, colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary
            ), modifier = Modifier.weight(1f)
        ) { Text("Guardar", fontSize = 12.sp) }
        Spacer(Modifier.width(10.dp))
        Button(
            onClick = { plansViewModel.hideDialogEdit() },
            shape = MaterialTheme.shapes.small,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            ), modifier = Modifier.weight(1f)
        ) { Text("Cancelar", color = MaterialTheme.colorScheme.onPrimary, fontSize = 12.sp) }
    }
}

@Composable
fun BodyDialogEditPlans(
    namePlan: String,
    description: String,
    target: String,
    targetBs: String,
    date: String,
    category: String,
    mountActually: String,
    mountActuallyBs: String,
    plansViewModel: PlansViewModel,
    dolarObject: DolarOficial?
) {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(Modifier.fillMaxWidth()) {


            //Campos de texto para ingresar Plan financiero
            NamePlanEdit(namePlan, plansViewModel)
            Spacer(Modifier.padding(5.dp))
            DescriptionPlanEdit(description, plansViewModel)
            Spacer(Modifier.padding(5.dp))
            CategoryPlanEdit(category, plansViewModel)
            Spacer(Modifier.height(20.dp))
            Text("Objetivos Financieros", fontSize = 18.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            Spacer(Modifier.height(20.dp))
            TargetPlanEdit(target, plansViewModel, targetBs, dolarObject)
            Spacer(Modifier.padding(5.dp))
            MountActuallyPlanEdit(mountActually, plansViewModel, mountActuallyBs, dolarObject)
            Spacer(Modifier.padding(5.dp))
            Text("Fecha $date", fontSize = 12.sp, fontWeight = FontWeight.Bold)

        }
    }

}

//Datos del dialogo para editar Planes
@Composable
fun MountActuallyPlanEdit(
    mountActually: String,
    plansViewModel: PlansViewModel,
    mountActuallyBs: String,
    dolarObject: DolarOficial?
) {
    Column(Modifier.fillMaxWidth()) {
        Text("Monto actual", fontSize = 15.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.padding(6.dp))
        OutlinedTextField(
            value = mountActually,
            onValueChange = { plansViewModel.onMountActuallyChangeEdit(it, dolarObject?.promedio) },
            placeholder = { Text("Monto al iniciar el plan.", fontSize = 10.sp) },
            prefix = { Text("$", fontSize = 10.sp) },
            textStyle = androidx.compose.ui.text.TextStyle(
                fontSize = 10.sp,  // ← Tamaño del texto que escribe el usuario
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface
            ),

            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = MaterialTheme.shapes.medium,
            maxLines = 1,
            modifier = Modifier
                .height(56.dp)
                .width(230.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        )
        Spacer(Modifier.padding(6.dp))
        OutlinedTextField(
            value = mountActuallyBs,
            onValueChange = { plansViewModel.onMountActuallyBsChangeEdit(it, dolarObject?.promedio) },
            placeholder = { Text("Monto al iniciar el plan.", fontSize = 10.sp) },
            prefix = { Text("Bs", fontSize = 10.sp) },

            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = MaterialTheme.shapes.medium,
            maxLines = 1,
            modifier = Modifier
                .height(56.dp)
                .width(230.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        )
    }
}

@Composable
fun TargetPlanEdit(target: String, plansViewModel: PlansViewModel, targetBs: String, dolarObject: DolarOficial?) {
    Column(Modifier.fillMaxWidth()) {
        Text("Monto objetivo a alcanzar", fontSize = 15.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.padding(6.dp))
        OutlinedTextField(
            value = target,
            onValueChange = { plansViewModel.onTargetChangeEdit(it, dolarObject?.promedio) },
            placeholder = { Text("Monto objetivo.", fontSize = 10.sp) },
            prefix = { Text("$", fontSize = 10.sp) },
            textStyle = androidx.compose.ui.text.TextStyle(
                fontSize = 10.sp,  // ← Tamaño del texto que escribe el usuario
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface
            ),

            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = MaterialTheme.shapes.medium,
            maxLines = 1,
            singleLine = true,
            modifier = Modifier
                .height(56.dp)
                .width(230.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        )
        Spacer(Modifier.padding(6.dp))
        OutlinedTextField(
            value = targetBs,
            onValueChange = { plansViewModel.onTargetBsChangeEdit(it, dolarObject?.promedio) },
            placeholder = { Text("Monto objetivo.", fontSize = 10.sp) },
            prefix = { Text("Bs", fontSize = 10.sp) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = MaterialTheme.shapes.medium,
            maxLines = 1,
            singleLine = true,
            modifier = Modifier
                .height(56.dp)
                .width(230.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        )
    }
}

@Composable
fun CategoryPlanEdit(category: String, plansViewModel: PlansViewModel) {
    Column(Modifier.fillMaxWidth()) {
        Text("Categoria ", fontSize = 15.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.padding(6.dp))
        OutlinedTextField(
            value = category,
            onValueChange = { plansViewModel.onCategoryChangeEdit(it) },
            placeholder = { Text("Ej. Viajes, Carro nuevo, etc", fontSize = 10.sp) },
            textStyle = androidx.compose.ui.text.TextStyle(
                fontSize = 10.sp,  // ← Tamaño del texto que escribe el usuario
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface
            ),

            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            shape = MaterialTheme.shapes.medium,
            maxLines = 1,
            modifier = Modifier
                .height(56.dp)
                .width(230.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        )
    }
}

@Composable
fun DescriptionPlanEdit(description: String, plansViewModel: PlansViewModel) {
    Column(Modifier.fillMaxWidth()) {
        Text("Descripcion", fontSize = 15.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.padding(6.dp))
        OutlinedTextField(
            value = description,
            onValueChange = { plansViewModel.onDescriptionChangeEdit(it) },
            placeholder = { Text("Descripcion de la finalidad del plan.", fontSize = 10.sp) },
            textStyle = androidx.compose.ui.text.TextStyle(
                fontSize = 10.sp,  // ← Tamaño del texto que escribe el usuario
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            shape = MaterialTheme.shapes.medium,
            maxLines = 3,
            modifier = Modifier
                .height(100.dp)
                .width(230.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        )
    }

}

@Composable
fun NamePlanEdit(namePlan: String, plansViewModel: PlansViewModel) {
    Column(Modifier.fillMaxWidth()) {
        Text("Nombre", fontSize = 15.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.padding(6.dp))
        OutlinedTextField(
            value = namePlan,
            onValueChange = { plansViewModel.onNameChangeEdit(it) },
            placeholder = { Text("Ingresar nombre del plan.", fontSize = 10.sp) },

            textStyle = androidx.compose.ui.text.TextStyle(
                fontSize = 10.sp,  // ← Tamaño del texto que escribe el usuario
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            shape = MaterialTheme.shapes.medium,
            maxLines = 1,
            singleLine = true,
            modifier = Modifier
                .height(60.dp)
                .width(230.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        )
    }

}

//dialogo para agregar dinero al plan
@Composable
fun AddmoneyToPlans(
    modifier: Modifier,
    plansViewModel: PlansViewModel,
    aporte: String,
    plansList: List<planItem>,
    aportBs: String,
    dolarObject: DolarOficial?
) {
    val showDialogMoney by plansViewModel.showDialogAddMoney.collectAsState()
    val selectedPlan by plansViewModel.selectePlanItem.collectAsState()

    if (showDialogMoney) {
        Dialog(onDismissRequest = { plansViewModel.hideDialogMoney() }) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 0.dp    // <- clave
            ) {
                Column(
                    modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {

                    BodyDialogMoney(modifier, plansViewModel, aporte, selectedPlan, aportBs, dolarObject)
                    Spacer(modifier.height(5.dp))
                    ButtonsDialogMoney(modifier, plansViewModel, aportBs)
                }

            }
        }
    }
}

@Composable
fun ButtonsDialogMoney(modifier: Modifier, plansViewModel: PlansViewModel, aportBs: String) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(6.dp), horizontalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {

                plansViewModel.addMoneyToPlan(aportBs)

            }, shape = MaterialTheme.shapes.small, colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary
            ), modifier = Modifier.weight(1f)
        ) { Text("Guardar", fontSize = 12.sp) }
        Spacer(Modifier.width(10.dp))
        Button(
            onClick = { plansViewModel.hideDialogMoney() },
            shape = MaterialTheme.shapes.small,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            ), modifier = Modifier.weight(1f)
        ) { Text("Cancelar", fontSize = 12.sp, color = MaterialTheme.colorScheme.onPrimary) }
    }
}

@Composable
fun BodyDialogMoney(
    modifier: Modifier,
    plansViewModel: PlansViewModel,
    aporte: String,
    plan: planItem?,
    aportBs: String,
    dolarObject: DolarOficial?
) {


    Column(modifier.fillMaxWidth()) {
        Box(modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    plan?.Name.toString(), fontSize = 20.sp, fontWeight = FontWeight.Bold
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
                                text = "Actual: $${plansViewModel.convertBsToUSD(plan.Actualy.toDouble())}",
                                fontSize = 10.sp,
                            )
                            Text(
                                text = "Actual: Bs${plan.Actualy}", fontSize = 10.sp
                            )
                            Text(
                                text = "Meta: $${plansViewModel.convertBsToUSD(plan.Objective.toDouble())}", fontSize =
                                10.sp
                            )
                            Text(
                                text = "Meta: $${plan.Objective.toDouble()}", fontSize = 10.sp
                            )
                        }
                    }
                }
                Spacer(Modifier.padding(8.dp))
                Text("Agregar aporte al plan", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.padding(8.dp))
                OutlinedTextField(
                    value = aporte,
                    onValueChange = {
                        plansViewModel.onAportePlanChange(it, dolarObject?.promedio)
                        plansViewModel.clearError()
                    },
                    placeholder = { Text("00.0", fontSize = 10.sp) },
                    prefix = { Text("$", fontSize = 10.sp) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = MaterialTheme.shapes.medium,
                    maxLines = 1,
                    singleLine = true,
                    modifier = Modifier
                        .height(56.dp)
                        .width(230.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    )
                )
                Spacer(Modifier.padding(10.dp))
                OutlinedTextField(
                    value = aportBs,
                    onValueChange = {
                        plansViewModel.onAporteBsPlanChange(it, dolarObject?.promedio)
                        plansViewModel.clearError()
                    },
                    placeholder = { Text("00.0", fontSize = 10.sp) },
                    prefix = { Text("Bs.", fontSize = 10.sp) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = MaterialTheme.shapes.medium,
                    maxLines = 1,
                    singleLine = true,
                    modifier = Modifier
                        .height(56.dp)
                        .width(230.dp),
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
        }, modifier = modifier, shape = FloatingActionButtonDefaults.smallShape

    ) {
        Icon(
            painter = painterResource(R.drawable.ic_plans_add),
            contentDescription = "add Plans",
            modifier = Modifier.size(35.dp)
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
    mountActually: String,
    dolarBs: DolarOficial?,
    montoBs: String,
    targetBs: String
) {
    val showDialogPlans by plansViewModel.showDialogAdd.collectAsState()


    if (showDialogPlans) {
        Dialog(onDismissRequest = { plansViewModel.hideDialog() }) {
            Box(
                modifier = Modifier
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
                            dolarBs,
                            montoBs,
                            targetBs
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
            .padding(6.dp), horizontalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {
                plansViewModel.savePlans()
                plansViewModel.clearFields()

            }, shape = MaterialTheme.shapes.small, colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary
            ), modifier = Modifier.weight(1f)
        ) { Text("Guardar", fontSize = 15.sp) }
        Spacer(Modifier.width(10.dp))
        Button(
            onClick = { plansViewModel.hideDialog() },
            shape = MaterialTheme.shapes.small,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            ), modifier = Modifier.weight(1f)
        ) { Text("Cancelar", color = MaterialTheme.colorScheme.onPrimary, fontSize = 15.sp) }
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
    plansViewModel: PlansViewModel,
    dolarBs: DolarOficial?,
    montoBs: String,
    targetBs: String
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
            Text("Objetivos Financieros", fontSize = 30.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            Spacer(Modifier.height(20.dp))
            TargetPlan(target, plansViewModel, dolarBs, targetBs)
            Spacer(Modifier.padding(5.dp))
            MountActuallyPlan(mountActually, plansViewModel, dolarBs, montoBs)
            Spacer(Modifier.padding(5.dp))
            Text("Fecha $date", fontSize = 15.sp, fontWeight = FontWeight.Bold)

        }
    }
}

@Composable
fun TargetPlan(target: String, plansViewModel: PlansViewModel, dolarBs: DolarOficial?, targetBs: String) {
    Column(Modifier.fillMaxWidth()) {
        Text(
            "Monto objetivo a alcanzar", fontSize = 20.sp, fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.padding(6.dp))
        OutlinedTextField(
            value = target,
            onValueChange = { plansViewModel.onTargetChange(it, dolarBs?.promedio) },
            placeholder = { Text("Monto objetivo USD.", fontSize = 10.sp) },
            prefix = { Text("$", fontSize = 10.sp) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = MaterialTheme.shapes.medium,
            maxLines = 1,
            singleLine = true,
            modifier = Modifier
                .height(56.dp)
                .width(230.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        )
        Spacer(Modifier.padding(6.dp))
        OutlinedTextField(
            value = targetBs,
            onValueChange = { plansViewModel.onTargetBsChange(it, dolarBs?.promedio) },
            placeholder = { Text("Monto objetivo", fontSize = 10.sp) },
            prefix = { Text("Bs", fontSize = 10.sp) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = MaterialTheme.shapes.medium,
            maxLines = 1,
            singleLine = true,
            modifier = Modifier
                .height(56.dp)
                .width(230.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        )
    }
}

@Composable
fun MountActuallyPlan(mountActually: String, plansViewModel: PlansViewModel, dolarBs: DolarOficial?, montoBs: String) {
    Column(Modifier.fillMaxWidth()) {
        Text("Monto actual", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.padding(6.dp))
        OutlinedTextField(
            value = mountActually,
            onValueChange = { plansViewModel.onMountActuallyChange(it, dolarBs?.promedio) },
            placeholder = { Text("Monto al iniciar el plan.", fontSize = 10.sp) },
            prefix = { Text("$", fontSize = 10.sp) },

            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = MaterialTheme.shapes.medium,
            maxLines = 1,
            modifier = Modifier
                .height(56.dp)
                .width(230.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        )
        Spacer(Modifier.padding(6.dp))
        OutlinedTextField(
            value = montoBs,
            onValueChange = { plansViewModel.onMountActuallyBsChange(it, dolarBs?.promedio) },
            placeholder = { Text("Monto al iniciar el plan.", fontSize = 10.sp) },
            prefix = { Text("Bs", fontSize = 10.sp) },

            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = MaterialTheme.shapes.medium,
            maxLines = 1,
            modifier = Modifier
                .height(56.dp)
                .width(230.dp),
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
        Text("Categoria ", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.padding(6.dp))
        OutlinedTextField(
            value = category,
            onValueChange = { plansViewModel.onCategoryChange(it) },
            placeholder = { Text("Ej. Viajes, Carro nuevo, etc", fontSize = 10.sp) },

            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            shape = MaterialTheme.shapes.medium,
            maxLines = 1,
            modifier = Modifier
                .height(56.dp)
                .width(230.dp),
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
        Text("Descripcion", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.padding(6.dp))
        OutlinedTextField(
            value = description,
            onValueChange = { plansViewModel.onDescriptionChange(it) },
            placeholder = { Text("Descripcion de la finalidad del plan.", fontSize = 10.sp) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            shape = MaterialTheme.shapes.medium,
            maxLines = 3,
            modifier = Modifier
                .height(60.dp)
                .width(230.dp),
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
        Text("Nombre", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.padding(6.dp))
        OutlinedTextField(
            value = namePlan,
            onValueChange = { plansViewModel.onNameChange(it) },
            placeholder = { Text("Ingresar nombre del plan.", fontSize = 10.sp) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            shape = MaterialTheme.shapes.medium,
            maxLines = 1,
            singleLine = true,
            modifier = Modifier
                .height(56.dp)
                .width(230.dp),
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
    ) {
        Text(
            "Planes Financieros", modifier = modifier.padding(16.dp), fontSize = 30.sp, fontWeight = FontWeight.Bold
        )
    }
}

//Plan de ahorro
@Composable
fun PlanTotal(
    modifier: Modifier = Modifier,
    totalSaved: Double,
    budgetTarget: Double,
    progres: Double,
    missing: Double,
    plansViewModel: PlansViewModel
) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(2.dp)
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
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Spacer(modifier.padding(8.dp))
                        Row(
                            modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Ahorrado", fontSize = 14.sp)
                            Text("Meta", fontSize = 14.sp)
                        }

                        Row(
                            modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "USD $${plansViewModel.convertBsToUSD(totalSaved)}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                "USD $${plansViewModel.convertBsToUSD(budgetTarget)}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Row(
                            modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Bs.${plansViewModel.formatTotal(totalSaved)}", fontSize = 12.sp)
                            Text("Bs.${plansViewModel.formatTotal(budgetTarget)}", fontSize = 12.sp)
                        }

                        Row(
                            Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            LinearProgressIndicator(
                                progress = { progres.toFloat() },
                                modifier = modifier.height(16.dp),
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                trackColor = MaterialTheme.colorScheme.onSecondary,
                                strokeCap = StrokeCap.Round,

                                )

                            Text(
                                "${
                                    plansViewModel.formatTotal(progres * 100)
                                        .toDouble()
                                }%", fontSize = 15.sp
                            )

                        }
                        Spacer(modifier.padding(10.dp))
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                            Text(
                                "Faltan $${plansViewModel.convertBsToUSD(missing)} o Bs.${missing} para alcanzar su " +
                                        "meta",
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
    modifier: Modifier = Modifier, plansViewModel: PlansViewModel, plansList: List<planItem>, isLoading: Boolean
) {
    Box(
        modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Column {
            Text("Planes de ahorro", fontSize = 30.sp, fontWeight = FontWeight.Bold)

            if (isLoading) {

                Box(
                    Modifier
                        .fillMaxSize()
                        .align(Alignment.CenterHorizontally), contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }

                }
            } else {
                if (plansList.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "No hay planes registrados aun",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Light,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier.fillMaxWidth()
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
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlanesCard(modifier: Modifier = Modifier, planItem: planItem, plansViewModel: PlansViewModel) {
    val percentge = planItem.Actualy.toDouble() / planItem.Objective.toDouble() * 100
    val percentegeFormated = plansViewModel.formatTotal(percentge)
    Card(
        modifier = Modifier
            .height(250.dp)
            .width(400.dp)
            .padding(10.dp)
            .combinedClickable(
                onClick = { plansViewModel.selectedPlan(planItem) },
                onLongClick = { plansViewModel.selectedPlanEdit(planItem) }),
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
                        "${percentegeFormated}%",
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                }
                Spacer(modifier.padding(2.dp))
                AssistChip(
                    modifier = modifier.height(20.dp),
                    onClick = {},
                    label = { Text(planItem.Category, fontSize = 14.sp) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        labelColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
                Spacer(modifier.padding(3.dp))
                Box(Modifier.fillMaxWidth()) {
                    Text(
                        planItem.Description, fontSize = 12.sp, textAlign = TextAlign.Center, lineHeight = 12.sp
                    )
                }
                Spacer(modifier.padding(5.dp))
                Row(
                    modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
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
                    modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            "$${plansViewModel.convertBsToUSD(planItem.Actualy.toDouble())}",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Text(
                            "Bs ${plansViewModel.formatTotal(planItem.Actualy.toDouble())}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Light,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    Text("De", fontSize = 12.sp, fontWeight = FontWeight.Light)

                    Column {
                        Text(
                            "$${plansViewModel.convertBsToUSD(planItem.Objective.toDouble())}",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Text(
                            "Bs ${plansViewModel.formatTotal(planItem.Objective.toDouble())}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
                Spacer(modifier.padding(6.dp))
                Text(
                    planItem.Advice, fontSize = 12.sp, lineHeight = 12.sp
                )
            }

        }
    }

}