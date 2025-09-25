package com.example.myprojectfinancia.Index.Plans.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myprojectfinancia.Data.BD.AuthService
import com.example.myprojectfinancia.Index.Data.UserFinancia
import com.example.myprojectfinancia.Index.Plans.Domain.budgetRepository
import com.example.myprojectfinancia.Index.Plans.ModelsPlans.DataPlans
import com.example.myprojectfinancia.Index.Plans.ModelsPlans.planItem
import com.example.myprojectfinancia.Index.home.Models.Movements.MovementsItemSave
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject


@HiltViewModel
class PlansViewModel @Inject constructor(
    private val authService: AuthService,
    private val budgetRepository: budgetRepository

) : ViewModel() {


    //Variables para Controlar Estados
    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error


    //Variables para los planes

    //Datos para ingresar Plan financiero
    private val _namePlan = MutableStateFlow<String>("")
    val namePlan: StateFlow<String> = _namePlan

    private val _description = MutableStateFlow<String>("")
    val description: StateFlow<String> = _description

    private val _target = MutableStateFlow<String>("")
    val target: StateFlow<String> = _target

    private val _category = MutableStateFlow<String>("")
    val category: StateFlow<String> = _category

    private val _date = MutableStateFlow<String>(fecha())
    val date: StateFlow<String> = _date

    private val _mountActually = MutableStateFlow<String>("")
    val mountActually: StateFlow<String> = _mountActually

    private val _dateTarget = MutableStateFlow<String>("")
    val dateTarget: StateFlow<String> = _dateTarget

    private val _aporte = MutableStateFlow<String>("")
    val aporte: StateFlow<String> = _aporte


    //Datos para Editar Plan financiero
    private val _namePlanEdit = MutableStateFlow<String>("")
    val namePlanEdit: StateFlow<String> = _namePlanEdit

    private val _descriptionEdit = MutableStateFlow<String>("")
    val descriptionEdit: StateFlow<String> = _descriptionEdit

    private val _targetEdit = MutableStateFlow<String>("")
    val targetEdit: StateFlow<String> = _targetEdit

    private val _categoryEdit = MutableStateFlow<String>("")
    val categoryEdit: StateFlow<String> = _categoryEdit

    private val _mountActuallyEdit = MutableStateFlow<String>("")
    val mountActuallyEdit: StateFlow<String> = _mountActuallyEdit

    //lista de planes
    private val _plans = MutableStateFlow<List<planItem>>(emptyList())
    val plans: StateFlow<List<planItem>> = _plans

    //variable para seleccionar el plan
    private val _selecctedPlanItem = MutableStateFlow<planItem?>(null)
    val selectePlanItem: StateFlow<planItem?> = _selecctedPlanItem


    //Dialogo Agregar Planes
    private val _showDialogAdd = MutableStateFlow<Boolean>(false)
    val showDialogAdd: StateFlow<Boolean> = _showDialogAdd

    //Dialogo para agrear dinero a los planes
    private val _showDialogAddMoneey = MutableStateFlow<Boolean>(false)
    val showDialogAddMoney: StateFlow<Boolean> = _showDialogAddMoneey

    private val _showDialogEdit = MutableStateFlow<Boolean>(false)
    val showDialogEdit: StateFlow<Boolean> = _showDialogEdit

    //Variables para Plan total

    //Presupuesto total guardado
    private val _totalSaved = MutableStateFlow<Double>(0.0)
    val totalSaved: StateFlow<Double> = _totalSaved

    //Presupuesto objetivo
    private val _budgetTarget = MutableStateFlow<Double>(0.0)
    val budgetTarget: StateFlow<Double> = _budgetTarget

    //Progresado
    private val _progres = MutableStateFlow<Double>(0.0)
    val progres: StateFlow<Double> = _progres

    //PResupuesto Faltante
    private val _missing = MutableStateFlow<Double>(0.0)
    val missing: StateFlow<Double> = _missing


    val budgetfree: StateFlow<Double> = budgetRepository.budgetFree

    //Tasa de cambio a BCV
    private val _exchangeRate = 140.0


    //Funciones

    //Formatear Presupuesto
    fun formatTotal(budget: Double): String {
        return String.format("%.2f", budget)
    }

    //Funcion Para cargar datos generales
    fun calculateGeneralData() {
        val plans = _plans.value
        val totalSaved = plans.sumOf { it.Actualy.toDoubleOrNull() ?: 0.0 }
        val budgetTarget = plans.sumOf { it.Objective.toDoubleOrNull() ?: 0.0 }
        val progress = if (budgetTarget > 0.0) (totalSaved / budgetTarget) else 0.0
        val missing = budgetTarget - totalSaved

        _totalSaved.value = totalSaved
        _budgetTarget.value = budgetTarget
        _progres.value = progress
        _missing.value = missing

    }

    //iniciarlizar planes para nuevos usuarios
    fun initializePlansUser() {
        viewModelScope.launch {
            _isLoading.value = true
            clearPlans()
            initializePlans()
            getAllMovementsForBudget()
            clearFields()
        }
    }

    //limpiar planes
    fun clearPlans() {
        _plans.value = emptyList()
    }

    //Inicializar llamado de planes
    fun initializePlans() {
        viewModelScope.launch {
            try {
                Log.i("PlansViewModel", "Iniciando carga de planes")
                getPlans()
            } catch (ex: Exception) {
                Log.e("PlansViewModel", "Error inicializando planes: ${ex.message}")
                _isLoading.value = false
                _error.value = ex.message ?: "Error desconocido"
            }
        }

    }


    //Guarda Movimientos de planes
    fun savePlansMovements(selectedPlan: planItem, aporte: String) {
        viewModelScope.launch {
            val movimientos = MovementsItemSave(
                Fecha = fecha(),
                Categoria = selectedPlan.Name,
                Naturaleza = "Asignacion",
                Monto = aporte.toDoubleOrNull() ?: 0.0
            )

            val userCurrent = authService.getCurrentUser()
            val user = UserFinancia(uid = userCurrent?.uid ?: "", name = userCurrent?.displayName ?: "")

            val resultado = authService.saveMovements(movimientos, user)

            if (resultado) {
                Log.i("movimientos", "Movimiento guardado")
            }
        }
    }


    //funcion para seleccionar plan y agregar dinero
    fun selectedPlan(plan: planItem) {
        _selecctedPlanItem.value = plan
        Log.i("PlansViewModel", "Plan seleccionado: ${plan.Name} - ID ${plan.id}")
        showDialogAddMoney()
    }


    //funcion para seleccionar plan editable
    fun selectedPlanEdit(plan: planItem) {
        _selecctedPlanItem.value = plan
        Log.i("PlansViewModel", "Plan seleccionado: ${plan.Name} - ID ${plan.id}")
        _showDialogEdit.value = true

        _namePlanEdit.value = plan.Name
        _targetEdit.value = plan.Objective
        _categoryEdit.value = plan.Category
        _mountActuallyEdit.value = plan.Actualy
        _descriptionEdit.value = plan.Description
    }

    //funcion para cargar movimientos
    private suspend fun getAllMovementsForBudget() {
        // Llamar a authService para obtener movimientos y calcular presupuesto
        authService.getAllMovements(
            onResult = { movements ->
                budgetRepository.calculateBudget(movements)
            },
            onError = { error ->
                _error.value = error?.message ?: "Error desconocido"
            }
        )
    }


    //funcion para obtener planes
    fun getPlans() {
        viewModelScope.launch {
            val adviceAmount = budgetRepository.budgetFree.value

            Log.d("DEBUG", "Presupuesto Total: ${budgetRepository.totalBudget.value}")
            Log.d("DEBUG", "Total Asignado: ${budgetRepository.planBudget.value}")
            Log.d("DEBUG", "Presupuesto Libre: ${budgetRepository.budgetFree.value}")
            _isLoading.value = true
            authService.getPlans(
                onResult = { plans ->

                    val totalAsignadoEnPlanes = plans.sumOf { it.Actualy.toDoubleOrNull() ?: 0.0 }
                    val presupuestoTotal = budgetRepository.totalBudget.value
                    val presupuestoLibre = presupuestoTotal - totalAsignadoEnPlanes
                    _plans.value = plans.map {
                        planItem(
                            id = it.id,
                            Name = it.Name,
                            Category = it.Category,
                            Description = it.Description,
                            Objective = it.Objective,
                            Actualy = it.Actualy,
                            Advice = calculateAdvice(it, adviceAmount, plans),
                            Date = it.Date
                        )

                    }
                    plans.forEachIndexed { index, plan ->
                        Log.d("DEBUG_PLAN", "Plan $index:")
                        Log.d("DEBUG_PLAN", "  - Name: '${plan.Name}'")
                        Log.d(
                            "DEBUG_PLAN",
                            "  - Actualy: '${plan.Actualy}' (tipo: ${plan.Actualy?.javaClass?.simpleName})"
                        )
                        Log.d("DEBUG_PLAN", "  - Actualy toDouble: ${plan.Actualy.toDoubleOrNull()}")
                        Log.d("DEBUG_PLAN", "  - Objective: '${plan.Objective}'")
                    }
                    budgetRepository.updateTotalBudget(presupuestoTotal)
                    calculateGeneralData()
                    _isLoading.value = false
                },
                onError = { error ->
                    _plans.value = emptyList()
                    _error.value = error?.message ?: "Error desconocido"
                    _isLoading.value = false
                    Log.e("PlansViewModel", "Error obteniendo planes: ${error?.message}")

                }
            )

        }


    }

    //funcion para guardar planes
    fun savePlans() {
        viewModelScope.launch {

            val amount = _mountActually.value.toDoubleOrNull() ?: 0.0
            val adviceAmount = budgetRepository.budgetFree.value

            try {
                // Validar campos requeridos
                if (_namePlan.value.isBlank()) {
                    _error.value = "El nombre del plan es requerido"
                    return@launch
                }

                if (_target.value.isBlank()) {
                    _error.value = "El objetivo es requerido"
                    return@launch
                }

                if (_category.value.isBlank()) {
                    _error.value = "La categoría es requerida"
                    return@launch
                }

                if (_mountActually.value.isBlank()) {
                    _error.value = "El monto inicial es requerido"
                    return@launch
                }

                if (!budgetRepository.validateBudget(amount)) {
                    _error.value =
                        "No hay suficiente dinero en el presupuesto, ud posee $${budgetRepository.budgetFree.value}"
                    _isLoading.value = false
                    return@launch
                }

                val nameLocal = _namePlan.value
                val targetLocal = _target.value
                val categoryLocal = _category.value
                val mountActuallyLocal = _mountActually.value


                val plan = DataPlans(
                    Name = nameLocal ?: "",
                    Category = categoryLocal ?: "",
                    Description = _description.value ?: "",
                    Objective = targetLocal ?: "",
                    Actualy = mountActuallyLocal ?: "",
                    Advice = "se recomienda ahorrar $${0.00}",
                    Date = fecha()
                )


                Log.i("testCategory", "Categoria a guardar: ${categoryLocal} y monto: ${mountActuallyLocal}")

                val userCurrent = authService.getCurrentUser()
                val user = userCurrent?.uid?.let { UserFinancia(uid = it, name = it) }

                val result = user?.let { authService.savePlan(plan, it) }


                if (result == true) {
                    Log.i("result", "resultado del guardado correcto: $result")

                    val transferSuccess = budgetRepository.transferToPlan(amount)
                    if (transferSuccess) {

                        val planItem = planItem(
                            Name = nameLocal ?: "",
                            Category = categoryLocal ?: "",
                            Description = _description.value ?: "",
                            Objective = targetLocal ?: "",
                            Actualy = mountActuallyLocal ?: "",
                            Advice = "se recomienda ahorrar $${mountActuallyLocal.toDouble() * 50 / 100}",
                            Date = fecha()
                        )
                        savePlansMovements(planItem, mountActuallyLocal)

                        _showDialogAdd.value = false
                        getPlans()
                        clearFields()
                    }
                } else {
                    _error.value = "Error al guardar los plan"
                }

            } catch (ex: Exception) {
                Log.e("PlansViewModel", "Error guardando plan: ${ex.message}")
                _error.value = ex.message ?: "Error desconocido"
            }
        }
    }

    //funcion para actualizar planes
    fun updatePlans() {
        viewModelScope.launch {

            val selectedPlan = _selecctedPlanItem.value
            val user = authService.getCurrentUser()
            val oldAmount = selectedPlan?.Actualy?.toDoubleOrNull() ?: 0.0
            val newAmount = _mountActuallyEdit.value.toDoubleOrNull() ?: 0.0
            val difference = newAmount - oldAmount
            _isLoading.value = true

            try {
                // Validar campos requeridos
                if (_namePlanEdit.value.isBlank()) {
                    _error.value = "El nombre del plan es requerido"
                    _isLoading.value = false
                    return@launch
                }

                if (_targetEdit.value.isBlank()) {
                    _error.value = "El objetivo es requerido"
                    _isLoading.value = false
                    return@launch
                }

                if (_categoryEdit.value.isBlank()) {
                    _error.value = "La categoría es requerida"
                    _isLoading.value = false
                    return@launch
                }

                if (_mountActuallyEdit.value.isBlank()) {
                    _error.value = "El monto inicial es requerido"
                    _isLoading.value = false
                    return@launch
                }

                if (_descriptionEdit.value.isBlank()) {
                    _error.value = "La descripcion es requerida"
                    _isLoading.value = false
                    return@launch
                }

                if (user == null) {
                    _error.value = "Usuario no autenticado"
                    _isLoading.value = false
                    return@launch
                }


                val planUpdated = DataPlans(
                    Name = _namePlanEdit.value ?: "",
                    Category = _categoryEdit.value ?: "",
                    Description = _descriptionEdit.value ?: "",
                    Objective = _targetEdit.value ?: "",
                    Actualy = newAmount.toString() ?: "",
                    Advice = "se recomienda ahorrar $${(newAmount ?: 0.00) * 50 / 100}",
                    Date = fecha()
                )


                val userId = user?.uid?.let { UserFinancia(uid = it, name = it) }

                val result = authService.updatePlanByID(
                    planId = selectedPlan?.id.toString(),
                    userId = userId?.uid ?: "",
                    updatesPlans = planUpdated,
                    onError = { error ->
                        Log.e("PlansViewModel", "Error al actualizar plan: ${error.message}")
                        _error.value = error.message
                        _isLoading.value = false
                    }
                )

                if (result == true) {
                    Log.i("result", "resultado del guardado correcto: $result")

                    var transferSuccess = true

                    when {
                        difference > 0 -> {
                            transferSuccess = budgetRepository.transferToPlan(difference)
                            if (transferSuccess) {
                                // Crear movimiento para aumento
                                val planForMovement = selectedPlan?.copy(Name = _namePlanEdit.value)
                                planForMovement?.let {
                                    savePlansMovements(it, difference.toString())
                                }
                            }
                        }

                        difference < 0 -> {
                            val amountToReturn = kotlin.math.abs(difference)
                            transferSuccess = budgetRepository.transferToBudget(amountToReturn)
                            if (transferSuccess) {
                                // Crear movimiento de liberación
                                val movimientoLiberacion = MovementsItemSave(
                                    Fecha = fecha(),
                                    Categoria = "Liberación de ${_namePlanEdit.value}",
                                    Naturaleza = "Asignacion",
                                    Monto = amountToReturn
                                )
                                val userForMovement =
                                    UserFinancia(uid = user?.uid ?: "", name = user?.displayName ?: "")
                                authService.saveMovements(movimientoLiberacion, userForMovement)
                            }
                        }

                        else -> {
                            Log.i("PlansViewModel", "Plan actualizado sin cambios de monto")
                        }
                    }


                } else {
                    _error.value = "Error al guardar los plan"
                }
                Log.i("PlansViewModel", "Plan actualizado exitosamente")
                _showDialogEdit.value = false
                getPlans()
                clearFields()
                _isLoading.value = false
            } catch (ex: Exception) {
                Log.e("PlansViewModel", "Error guardando plan: ${ex.message}")
                _error.value = ex.message ?: "Error desconocido"
            }
        }
    }

    //funcion para calcular consejo
    private fun calculateAdvice(plan: DataPlans, budgetFree: Double, allPlans: List<DataPlans>): String {
        val actualy = plan.Actualy.toDoubleOrNull() ?: 0.0
        val objetive = plan.Objective.toDoubleOrNull() ?: 0.0
        val deficit = objetive - actualy

        val deficitTotal = allPlans.sumOf {
            val obj = it.Objective.toDoubleOrNull() ?: 0.0
            val act = it.Actualy.toDoubleOrNull() ?: 0.0
            maxOf(0.0, obj - act)
        }


        return when {
            deficit <= 0 -> "Meta conseguida!!!"
            budgetFree <= 0 -> "Presupuesto insuficiente para este plan"
            else -> {
                val proporcion = deficit / deficitTotal
                val recomProp = budgetFree * proporcion
                val advice = minOf(recomProp, deficit)
                "se recomienda ahorrar $${formatTotal(advice)}"
            }
        }
    }


    //funcion para agregar aporte a planes
    fun addMoneyToPlan(aporte: String) {
        viewModelScope.launch {
            try {
                _error.value = null
                val selectedPlan = _selecctedPlanItem.value
                val userCurrent = authService.getCurrentUser()
                val user = userCurrent?.uid
                val aporteDouble = aporte.toDoubleOrNull() ?: 0.0
                _isLoading.value = true

                if (selectedPlan == null) {
                    _error.value = "No hay plan seleccionado"
                    _isLoading.value = false  //
                    return@launch
                }

                if (user == null) {
                    _error.value = "Usuario no autenticado"
                    _isLoading.value = false  //
                    return@launch
                }

                if (aporte.isBlank()) {
                    _error.value = "Debe ingresar un monto"
                    _isLoading.value = false  //
                    return@launch
                }
                if (!budgetRepository.validateBudget(aporteDouble)) {
                    _error.value =
                        "No hay suficiente dinero en el presupuesto, ud posee $${budgetRepository.budgetFree.value}"
                    _isLoading.value = false
                    return@launch
                }


                val result = authService.getPlanById(
                    planId = selectedPlan.id,
                    userId = user,
                    contribution = aporteDouble,
                    onError = { error ->
                        Log.e("PlansViewModel", "Error al obtener el plan: ${error.message}")
                        _error.value = error.message
                        _isLoading.value = false  //
                    }
                )

                if (result) {

                    val transferSucces = budgetRepository.transferToPlan(aporteDouble)

                    if (transferSucces) {
                        savePlansMovements(selectedPlan, aporte)
                        _showDialogAddMoneey.value = false
                        _aporte.value = ""
                        _selecctedPlanItem.value = null
                        getPlans()
                        _isLoading.value = false
                    } else {
                        _isLoading.value = false
                        _error.value = "Error al transferir fondes al plan"
                        Log.e("PlansViewModel", "Error al transferir fondos al plan")
                    }


                } else {
                    _error.value = "Error al actualizar plan"
                    _isLoading.value = false
                }

            } catch (ex: Exception) {
                Log.e("PlansViewModel", "Error agregando aporte: ${ex.message}")
                _isLoading.value = false
                _error.value = ex.message ?: "Error desconocido"
            }
        }
    }

    //funcion para limpiar campos
    fun clearFields() {
        _namePlan.value = ""
        _description.value = ""
        _target.value = ""
        _category.value = ""
        _date.value = fecha()
        _mountActually.value = ""
        _aporte.value = ""
        _namePlanEdit.value = ""
        _descriptionEdit.value = ""
        _targetEdit.value = ""
        _categoryEdit.value = ""
        _mountActuallyEdit.value = ""
    }

    //funcion para obtener fecha actual
    companion object {
        fun fecha(): String {
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            return sdf.format(Date())
        }
    }

    //Mostrar dialogo para agg planes
    fun showDialogPlans() {
        _showDialogAdd.value = true

    }

    //Mostrar dialogo para agg dinero a planes
    fun showDialogAddMoney() {
        _showDialogAddMoneey.value = true
    }

    //Mostrar dialogo para editar planes
    fun showDialogEditPlan() {
        _showDialogEdit.value = true
    }


    //Ocultar dialogo para agg planes
    fun hideDialog() {
        _showDialogAdd.value = false
    }

    //Ocultar dialogo para editar planes
    fun hideDialogEdit() {
        _showDialogEdit.value = false
    }

    //Ocultar dialogo para agg dinero a planes
    fun hideDialogMoney() {
        _showDialogAddMoneey.value = false
        _aporte.value = ""
    }

    //Funciones para ingresar datos
    fun onAportePlanChange(aporte: String) {
        _aporte.value = aporte

        clearError()

    }

    fun clearError() {
        _error.value = null
    }

    //Funciones para ingresar datos
    fun onNameChange(name: String) {
        _namePlan.value = name
    }

    fun onTargetChange(target: String) {
        _target.value = target
    }

    fun onCategoryChange(category: String) {
        _category.value = category
    }

    fun onMountActuallyChange(mountActually: String) {
        _mountActually.value = mountActually
    }

    fun onDateTargetChange(dateTarget: String) {
        _dateTarget.value = dateTarget
    }

    fun onDescriptionChange(description: String) {
        _description.value = description
    }

    //Funciones para editar datos
    fun onNameChangeEdit(name: String) {
        _namePlanEdit.value = name
    }

    fun onTargetChangeEdit(target: String) {
        _targetEdit.value = target
    }

    fun onCategoryChangeEdit(category: String) {
        _categoryEdit.value = category
    }

    fun onMountActuallyChangeEdit(mountActually: String) {
        _mountActuallyEdit.value = mountActually
    }

    fun onDescriptionChangeEdit(description: String) {
        _descriptionEdit.value = description
    }


}