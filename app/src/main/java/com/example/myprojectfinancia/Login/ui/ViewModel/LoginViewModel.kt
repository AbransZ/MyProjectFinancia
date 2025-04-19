package com.example.myprojectfinancia.Login.ui.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.myprojectfinancia.Login.Data.DI.AuthService
import com.example.myprojectfinancia.Model.Routes
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
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
    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    //logica del email
    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    //logica de navegaciom
    private val _NavController = MutableSharedFlow<String?>()
    val navController = _NavController.asSharedFlow()

    //logica para mensajes de error
    private val _errorMessage = MutableSharedFlow<String?>()
    val message = _errorMessage.asSharedFlow()

    //logica para contraseñas
    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _Confirm = MutableLiveData<String>()
    val passconfirm: LiveData<String> = _Confirm

    private val _onPassMatch = MutableLiveData<Boolean>()
    val onPassMatch: LiveData<Boolean> = _onPassMatch

    //logica para activar botones
    private val _isEnableLogin = MutableLiveData<Boolean>()
    val isEnable: LiveData<Boolean> = _isEnableLogin

    // logica de fields para loginscreen
    fun onLoginChange(email: String, password: String) {
        _email.value = email
        _password.value = password
        _isEnableLogin.value = isEnableLogin(email, password)
    }

    // logica de fields para register screen
    fun onRememberChangeConfirm(name: String, email: String, password: String, confirm: String) {
        _name.value = name
        _email.value = email
        _password.value = password
        _Confirm.value = confirm
    }


    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                Log.i("LoginViewModel", "Iniciando login para $email")
                val user = authService.login(email, password)
                if (user != null) {
                    Log.i("LoginViewModel", "Usuario autenticado con UID: ${user.uid}")

                    Log.i("LoginViewModel", "Login CORRECTOOOOOOOOOO!!!!!!!")
                    _NavController.emit(Routes.StartingScreen.routes)

                } else {
                    Log.e("LoginViewModel", "Autenticación exitosa pero usuario es null")
                }
            } catch (ex: Exception) {
                Log.e("LoginViewModel", "Error en login: ${ex.message}")

                val errorMessage = when (ex) {
                    is FirebaseAuthInvalidUserException ->
                        "No existe una cuenta con este correo electrónico"

                    is FirebaseAuthInvalidCredentialsException ->
                        "Email o contraseña son invalidas"

                    is FirebaseNetworkException ->
                        "Error de conexión. Verifica tu conexión a internet"

                    else -> ex.message ?: "Error al iniciar sesión"
                }
                _errorMessage.emit(errorMessage)
            }
         }
    }

    fun isEnableLogin(email: String, password: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.length >= 8
    }

    fun onRememberChange(email: String) {
        _email.value = email
    }

    // registra al usuario
    fun register(email: String, password: String) {
        viewModelScope.launch {
            try {
                Log.i("LoginViewModel", "Iniciando registro para $email")
                val user = authService.register(email, password)
                if (user != null) {
                    _NavController.emit(Routes.StartingScreen.routes)

                } else {
                    Log.e("LoginViewModel", "Autenticación exitosa pero usuario es null")
                }

            } catch (ex: Exception) {
                Log.e("LoginViewModel", "Error en registro: ${ex.message}")
                Log.i("abrahan", "este es el mensaje del register")
                val errorMessage = when {
                    ex.message?.contains("email address is already in use") == true ->

                        "Este correo electrónico ya está registrado"

                    ex.message?.contains("password is invalid") == true ->
                        "La contraseña no cumple con los requisitos de seguridad"

                    ex.message?.contains("network") == true ->
                        "Error de conexión. Verifica tu conexión a internet"

                    else -> ex.message ?: "Error al registrar el usuario"
                }
                _errorMessage.emit(errorMessage)
            }
        }
    }

    //Confirma la contraseña y registra al usuario
    fun onConnfirmPass(email: String, password: String, confirm: String) {
        val isvalid = onButtonEnable(email, password, confirm)
        if (isvalid) {
            viewModelScope.launch {
                if (authService.validUser(email)) {
                    Log.i("abrahan", "este es el mensaje del confirm")
                    _errorMessage.emit("Este correo electrónico ya está registrado")
                } else {
                    register(email, password)
                }
            }
        } else {
            viewModelScope.launch {
                _errorMessage.emit("Las contraseñas no coinciden")
            }
        }
    }

    fun onButtonEnable(email: String, password: String, confirm: String): Boolean {

        val isValidEmail = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val isValidPassword = password.length >= 8
        val passwordsMatch = password == confirm

        val isValid = isValidEmail && isValidPassword && passwordsMatch
        _onPassMatch.value = isValid
        return isValid
    }


}



