package com.example.myprojectfinancia.Home.UI.home.ViewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myprojectfinancia.Home.Data.UserFinancia
import com.example.myprojectfinancia.Home.UI.home.Models.MovementsItem
import com.example.myprojectfinancia.Login.Data.DI.AuthService
import com.example.myprojectfinancia.Model.Routes
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject


@HiltViewModel
class homeViewModel @Inject constructor(
    private val authService: AuthService,
    private val googleSignInClient: GoogleSignInClient,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

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
    val naturaleza: LiveData<String> = _naturaleza

    fun onNaturalezaChange(naturaleza: String) {
        _naturaleza.value = naturaleza
    }

    fun clearfields() {
        _naturaleza.value = ""
        _montoString.value = ""
        _categoria.value = ""
        _montoDouble.value = 0.0
        _egresosIsPressed.value = false
        _ingrsosPressed.value = false
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


    //interaccion Ingresos y egresos
    private val _ingrsosPressed = MutableStateFlow(false)
    val ingrsosPressed: StateFlow<Boolean> = _ingrsosPressed

    fun ingresosIsPressed() {
        _ingrsosPressed.value = true
        _egresosIsPressed.value = false
        _naturaleza.value = "Ingreso"

    }

    private val _egresosIsPressed = MutableStateFlow(false)
    val egresosIsPressed: StateFlow<Boolean> = _egresosIsPressed

    fun gastosIsPressed() {
        _egresosIsPressed.value = true
        _ingrsosPressed.value = false
        _naturaleza.value = "Gasto"

    }


    fun logout(): String {
        googleSignInClient.revokeAccess()
        authService.logout()
        return Routes.LoginScreen.routes
    }

    //Movimientos
    private val _showMovements = MutableStateFlow<Boolean>(false)
    val showMovements: MutableStateFlow<Boolean> = _showMovements

    fun showMovements(show: Boolean) {
        _showMovements.value = show
    }


    init {
        mostrarNombre()
    }

    fun mostrarNombre() {
        viewModelScope.launch {
            val userCurrent = authService.getCurrentUser()
            Log.i("user", "user para SALUDO: $userCurrent")
            try {


                val user = authService.getUser(userCurrent?.uid ?: "")


                if (user.isSuccess) {

                    val userName = user.getOrNull()?.name ?: ""
                    _name.value = userName

                    Log.i("user", "user para SALUDO: $userName")

                } else {
                    Log.i("user", " error user para SALUDO: ${user.exceptionOrNull()}")
                    _name.value = ""
                }
            } catch (ex: Exception) {
                Log.i("user", " error user para SALUDO: ${ex.message}")

            }

        }

    }

    fun guardarMovimiento() {
        viewModelScope.launch {
            val movimientos = MovementsItem(
                Fecha = fechaActual ?: "",
                Categoria = _categoria.value ?: "",
                Naturaleza = _naturaleza.value ?: "",
                Monto = _montoDouble.value ?: 0.0
            )

            val userCurrent = authService.getCurrentUser()
            val user = UserFinancia(uid = userCurrent?.uid ?: "", name = _name.value ?: "")

            val resultado = authService.saveMovements(movimientos, user)

            if (resultado) {
                Log.i("movimientos", "Movimiento guardado")
                ocultarDialog()
            }

        }
    }

}