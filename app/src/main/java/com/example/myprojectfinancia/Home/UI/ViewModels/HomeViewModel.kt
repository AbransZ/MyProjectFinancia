package com.example.myprojectfinancia.Home.UI.ViewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myprojectfinancia.Login.Data.DI.AuthService
import com.example.myprojectfinancia.Model.Routes
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject


@HiltViewModel
class homeViewModel @Inject constructor(
    private val authService: AuthService,
    private val googleSignInClient: GoogleSignInClient
) : ViewModel() {

    // mostrar dialog
    private val _showDialog = MutableLiveData<Boolean>()
    val showDialog: LiveData<Boolean> = _showDialog

    fun mostrarDialog() {
        _showDialog.value = true
    }
    fun ocultarDialog() {
        _showDialog.value = false
        clearfields()

    }

    private val _categoria = MutableLiveData<String>("")
    val category: LiveData<String> = _categoria

    fun onCategoriaChange(categoria: String) {
        _categoria.value = categoria
    }


    // naturaleza de la transaccion
    private val _naturaleza = MutableLiveData<String>("")
    private val _natureExpanded = MutableLiveData<Boolean>()
    val naturaleza: LiveData<String> = _naturaleza
    val natureExpanded: LiveData<Boolean> = _natureExpanded

    fun onNaturalezaChange(naturaleza: String) {
        _naturaleza.value = naturaleza
    }

    private fun clearfields() {
        _naturaleza.value=""
        _montoString.value=""
    }

    fun onNatureExpandedChange(expanded: Boolean) {
        _natureExpanded.value = expanded

    }




    // valor del monto en string
    private val _montoString = MutableLiveData<String>("")
    val monto: LiveData<String> = _montoString

    fun onValueChange(value: String) {
        _montoString.value = value
    }

    // valor del monto en double
    private val _montoDouble = MutableLiveData<Double?>(0.0)
    val montoDouble: LiveData<Double?> = _montoDouble

    fun onMontoChange(monto: String) {
        _montoString.value = monto
        val montoNum = monto.toDoubleOrNull()

        if (montoNum != null) {
            _montoDouble.value = montoNum
        }
    }

// fecha de la transaccion
    var fechaActual by mutableStateOf(fechaActual())

    companion object {
        private fun fechaActual(): String {
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            return sdf.format(Date())
        }
    }


    fun logout(): String {
        googleSignInClient.revokeAccess()
        authService.logout()
        return Routes.LoginScreen.routes
    }


}