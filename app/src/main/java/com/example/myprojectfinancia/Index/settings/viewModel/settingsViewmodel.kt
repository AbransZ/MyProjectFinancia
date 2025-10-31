package com.example.myprojectfinancia.Index.settings.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myprojectfinancia.Data.API.network.DolarOficial
import com.example.myprojectfinancia.Data.API.repository.DolarOficialRepository
import com.example.myprojectfinancia.Data.BD.AuthService
import com.example.myprojectfinancia.Index.Plans.ModelsPlans.planItem
import com.example.myprojectfinancia.Index.home.Models.Movements.Movimiento
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class settingsViewmodel @Inject constructor(
    private val authService: AuthService,
    private val dolarRepository: DolarOficialRepository
) : ViewModel() {

    //variables

    //variable para errores
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    //variable para manejar el montoUSD
    private val _monto = MutableStateFlow<String>("")
    val monto: StateFlow<String> = _monto

    //variable para manejar el montoBS
    private val _montoBs = MutableStateFlow<String>("")
    val montoBs: StateFlow<String> = _montoBs

    //variable para dialogo de aboutfinancia
    private val _showAbout = MutableStateFlow<Boolean>(false)
    val showAbout: StateFlow<Boolean> = _showAbout

    //variable para obtener el objeto de la api
    private val _dolarObject = MutableStateFlow<DolarOficial?>(null)
    val dolarObject: StateFlow<DolarOficial?> = _dolarObject.asStateFlow()

    //lista de movimientos
    private val _Movements = MutableStateFlow<List<Movimiento>>(emptyList())
    val Movements: StateFlow<List<Movimiento>> = _Movements

    //Cantidad movimientos
    private val _CMovements = MutableStateFlow<Int>(0)
    val CMovements: StateFlow<Int> = _CMovements

    // planes
    private val _planes = MutableStateFlow<List<planItem>>(emptyList())
    val planes: StateFlow<List<planItem>> = _planes

    //contador de planes completados
    private val _Cplanes = MutableStateFlow<Int>(0)
    val Cplanes: StateFlow<Int> = _Cplanes

    //contador de planes activos
    private val _Aplanes = MutableStateFlow<Int>(0)
    val Aplanes: StateFlow<Int> = _Aplanes

    //funciones

// Inicializar Funciones

    fun initialize() {
        viewModelScope.launch {
            countActivities()
        }

    }

    //funcion de conteo de datos en Actividad
    fun countActivities() {
        viewModelScope.launch {

            authService.getAllMovements(
                onResult = { movements ->
                    _Movements.value = movements.map {
                        Movimiento(
                            id = it.Id,
                            fecha = it.Fecha,
                            montoBs = it.MontoBs.toString(),
                            categoria = it.Categoria,
                            naturaleza = it.Naturaleza
                        )
                    }
                    _CMovements.value = movements.size


                },
                onError = { e ->
                    _error.value = e.message ?: "Error al extraer movimientos"
                }
            )

            authService.getPlans(
                onResult = { plans ->
                    _planes.value = plans.map {
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
                    val completed = _planes.value.count { plans ->


                        plans.Actualy.toDouble() >= plans.Objective.toDouble() && plans.Objective
                            .toDoubleOrNull() != 0.0
                    }
                    _Cplanes.value = completed

                    val active = _planes.value.count { plans ->
                        plans.Actualy.toDouble() < plans.Objective.toDouble() && plans.Objective
                            .toDoubleOrNull() != 0.0
                    }
                    _Aplanes.value = active
                },
                onError = { e ->
                    _error.value = e.message ?: "Error al extraer planes"

                }
            )


        }
    }

    //funcion de limbiar  error
    fun clearError() {
        _error.value = null
    }

    //funcion para formatear
    fun format(monto: Double): String {
        return String.format("%.2f", monto)
    }

    //funcion para convertir de usd a bs
    fun convertUSDToBs(monto: String, Bcv: Double?): String {
        _monto.value = monto
        val montoNum = monto.toDoubleOrNull()
        if (montoNum != null && Bcv != null) {
            val montoBs = montoNum * Bcv
            _montoBs.value = montoBs.toString()
            return format(_montoBs.value.toDouble())
        } else if (monto.isBlank()) {
            _montoBs.value = ""
        } else {
            _error.value = "No se pudo convertir el monto"
        }
        return _error.value.toString()
    }

    //funcion para convertir de bs a usd
    fun convertBstoUSD(montobs: String, Bcv: Double?): String {
        _montoBs.value = montobs
        val montoNum = montobs.toDoubleOrNull()
        if (montoNum != null && Bcv != null) {
            val montoUSD = montoNum / Bcv
            _monto.value = montoUSD.toString()
            return format(_monto.value.toDouble())
        } else if (montobs.isBlank()) {
            _monto.value = ""
        } else {
            _error.value = "No se pudo convertir el monto"

        }
        return _error.value.toString()
    }

    //funciones para el dialogo de aboutfinancia
    fun showAbout() {
        _showAbout.value = true

    }

    fun hideAbout() {
        _showAbout.value = false
    }

}