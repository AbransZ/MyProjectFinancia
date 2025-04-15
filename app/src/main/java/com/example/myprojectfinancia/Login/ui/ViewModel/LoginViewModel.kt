package com.example.myprojectfinancia.Login.ui.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.myprojectfinancia.Login.Data.DI.AuthService
import com.example.myprojectfinancia.Model.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

//etiqueta de dagger hilt
@HiltViewModel

//preparar el view model para ser inyectado
class LoginViewModel @Inject constructor(private val authService: AuthService) : ViewModel() {

    //logica del email
    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _NavController = MutableStateFlow<String?>(null)
    val navController = _NavController.asStateFlow()

    private val _errorMessage = MutableSharedFlow<String?>()
    val message = _errorMessage.asSharedFlow()

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _isEnableLogin = MutableLiveData<Boolean>()
    val isEnable: LiveData<Boolean> = _isEnableLogin

    fun onLoginChange(email: String, password: String) {
        _email.value = email
        _password.value = password
        _isEnableLogin.value = isEnableLogin(email, password)
    }


    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                Log.i("LoginViewModel", "Iniciando login para $email")
                val user = authService.login(email, password)
                if (user != null) {
                    Log.i("LoginViewModel", "Usuario autenticado con UID: ${user.uid}")
                    Log.i("LoginViewModel", "Configurando navegación a ${Routes.StartingScreen.routes}")
                    Log.i("LoginViewModel", "Login CORRECTOOOOOOOOOO!!!!!!!")
                    _NavController.emit(Routes.StartingScreen.routes)
                    Log.i("LoginViewModel", "Valor de _NavController actualizado")
                }else {
                    Log.e("LoginViewModel", "Autenticación exitosa pero usuario es null")
                }

            } catch (ex: Exception) {
                _errorMessage.emit(ex.message ?: "Error al iniciar sesion")


            }
        }
    }

    fun isEnableLogin(email: String, password: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.length >= 8
    }

    fun onRememberChange(email: String) {
        _email.value = email

    }

}