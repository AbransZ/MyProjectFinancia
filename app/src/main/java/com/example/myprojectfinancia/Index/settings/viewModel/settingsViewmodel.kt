package com.example.myprojectfinancia.Index.settings.viewModel

import androidx.lifecycle.ViewModel
import com.example.myprojectfinancia.Data.API.network.DolarOficial
import com.example.myprojectfinancia.Data.API.repository.DolarOficialRepository
import com.example.myprojectfinancia.Data.BD.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    val Bcv = _dolarObject.value?.promedio ?: 0.0

    //funciones

    //funcion de limbiar  error
    fun clearError() {
        _error.value = null
    }

    //funcion para formatear
    fun format(monto: Double): String {
        return String.format("%.2f", monto)
    }

    //funcion para convertir de usd a bs
    fun convertUSDToBs(monto: String): String {
        _monto.value = monto
        val montoNum = monto.toDoubleOrNull()
        if (montoNum != null) {
            val montoBs = montoNum * Bcv
            _montoBs.value = montoBs.toString()
            return format(_montoBs.value.toDouble())
        }
        _error.value = "No se pudo convertir el monto"
        return _error.value.toString()
    }

    //funcion para convertir de bs a usd
    fun convertBstoUSD(montobs: String): String {
        _montoBs.value = montobs
        val montoNum = montobs.toDoubleOrNull()
        if (montoNum != null) {
            val montoUSD = montoNum / Bcv
            _monto.value = montoBs.toString()
            return format(_monto.value.toDouble())
        }
        _error.value = "No se pudo convertir el monto"
        return _error.value.toString()
    }

    //funciones para el dialogo de aboutfinancia
    fun showAbout(): Boolean {
        _showAbout.value = true
        return _showAbout.value
    }

    fun hideAbout(): Boolean {
        _showAbout.value = false
        return _showAbout.value
    }

}