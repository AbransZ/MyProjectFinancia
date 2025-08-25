package com.example.myprojectfinancia.Home.UI.Plans.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myprojectfinancia.Home.Data.UserFinancia
import com.example.myprojectfinancia.Home.UI.Plans.ModelsPlans.DataPlans
import com.example.myprojectfinancia.Home.UI.Plans.ModelsPlans.planItem
import com.example.myprojectfinancia.Login.Data.DI.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject


@HiltViewModel
class PlansViewModel @Inject constructor(
    private val authService: AuthService
) : ViewModel() {

    //Variables para Controlar Estados
    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: MutableStateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: MutableStateFlow<String?> = _error


    //Variables para los planes

    //Datos para ingresar Plan financiero
    private val _namePlan = MutableStateFlow<String>("")
    val namePlan: MutableStateFlow<String> = _namePlan

    private val _description = MutableStateFlow<String>("")
    val description: MutableStateFlow<String> = _description

    private val _target = MutableStateFlow<String>("")
    val target: MutableStateFlow<String> = _target

    private val _category = MutableStateFlow<String>("")
    val category: MutableStateFlow<String> = _category

    private val _date = MutableStateFlow<String>(fecha())
    val date: MutableStateFlow<String> = _date

    private val _mountActually = MutableStateFlow<String>("")
    val mountActually: MutableStateFlow<String> = _mountActually

    private val _dateTarget = MutableStateFlow<String>("")
    val dateTarget: MutableStateFlow<String> = _dateTarget

    //lista de planes
    private val _plans = MutableStateFlow<List<planItem>>(emptyList())
    val plans: MutableStateFlow<List<planItem>> = _plans


    //Dialogo Agregar
    private val _showDialogAdd = MutableStateFlow<Boolean>(false)
    val showDialogAdd: MutableStateFlow<Boolean> = _showDialogAdd

    //Funciones

    //iniciarlizar planes para nuevos usuarios
    fun initializePlansUser() {
        viewModelScope.launch {
            _isLoading.value = true
            clearPlans()
            initializePlans()
        }
    }

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

    //funcion para obtener planes
    fun getPlans() {
        viewModelScope.launch {
            _isLoading.value = true
            authService.getPlans(
                onResult = { plans ->
                    _plans.value = plans.map {
                        planItem(
                            id = it.id,
                            Name = it.Name,
                            Category = it.Category,
                            Description = it.Description,
                            Objective = it.Objective,
                            Actualy = it.Actualy,
                            Advice = it.Advice,
                            Date = it.Date
                        )
                    }
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
            val plan = DataPlans(
                Name = _namePlan.value ?: "",
                Category = _category.value ?: "",
                Description = _description.value ?: "",
                Objective = _target.value ?: "",
                Actualy = _mountActually.value ?: "",
                Advice = "se recomienda ahorrar $${_mountActually.value.toDouble() * 50 / 100}",
                Date = _date.value ?: ""
            )

            val userCurrent = authService.getCurrentUser()
            val user = userCurrent?.uid?.let { UserFinancia(uid = it, name = it) }

            val result = user?.let { authService.savePlan(plan, it) }

            result?.let {
                Log.i("result", "resultado del guardado correcto: $it")
                _showDialogAdd.value = false
            }
        }
    }

    //funcion para limpiar campos
    fun clearFields() {
        _namePlan.value = ""
        _description.value = ""
        _target.value = ""
        _category.value = ""
        _date.value = ""
        _mountActually.value = ""
    }

    //funcion para obtener fecha actual
    companion object {
        fun fecha(): String {
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            return sdf.format(Date())
        }
    }

    //Mostrar dialogo
    fun showDialogPlans() {
        _showDialogAdd.value = true

    }

    //Ocultar dialogo
    fun hideDialog() {
        _showDialogAdd.value = false
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


}