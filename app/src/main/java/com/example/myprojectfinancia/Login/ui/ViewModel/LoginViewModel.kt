package com.example.myprojectfinancia.Login.ui.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.myprojectfinancia.Home.Data.UserFinancia
import com.example.myprojectfinancia.Login.Data.DI.AuthService
import com.example.myprojectfinancia.Model.Routes
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
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
class LoginViewModel @Inject constructor(
    private val authService: AuthService,
    private val googleSignInClient: GoogleSignInClient
) : ViewModel() {

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

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    //muestra el dialogo de recuperacion de contraseña
    private val _isDialogOK = MutableLiveData<Boolean>()
    val isDialogOk: LiveData<Boolean> = _isDialogOK


    //metodo para recuperar contraseña
    fun sendEmailToRevoverPassword(email: String) {

        viewModelScope.launch {

            if (authService.validUser(email)) {
                _errorMessage.emit("Este correo electrónico no está registrado")
                Log.i("abrahan", "correo no enviado porque no esta registrado")
            } else {
                try {
                    val emailSended = authService.forgotPassword(email)
                    if (emailSended) {
                        _isLoading.value = true
                        _isDialogOK.value = true
                        Log.i("abrahan", "correo enviado")

                    } else {
                        _errorMessage.emit("correo no enviado porque no se authentico")
                    }
                } catch (ex: Exception) {
                    _errorMessage.emit("Error al enviar el correo ${ex.message}")
                    Log.i("abrahan", "correo No enviado hubo un error")
                } finally {
                    _isLoading.value = false
                }
            }
        }
    }

    //metodo para manejar el dialog
    fun onDialogChange(show: Boolean) {
        _isDialogOK.value = show
    }

    //metodo para obtener clente signin de google
    fun getGoogleSignInClient() = googleSignInClient

    // logica de fields para loginscreen
    fun onLoginChange(email: String, password: String) {
        _email.value = email
        _password.value = password
        _isEnableLogin.value = isEnableLogin(email, password)
    }

    //metodo para iniciar sesion con google
    fun resultSinginWithGoogle(account: GoogleSignInAccount?) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                account?.let {
                    val idToken = it.idToken
                    if (idToken != null) {
                        val user = authService.loginWithGoogle(idToken)
                        if (user != null) {
                            Log.i("abrahan", "login con Goolgle es exitoso")
                            _NavController.emit(Routes.StartingScreen.routes)
                        } else {
                            Log.i("abrahan", "login con Goolgle es fallido")
                            _errorMessage.emit("login con Goolgle es fallido")
                        }
                    } else {
                        Log.i("abrahan", "no se pudo obtener el token")
                        _errorMessage.emit("no se pudo obtener el token de google")
                    }
                } ?: run {
                    Log.i("abrahan", "no se pudo obtener la cuenta")
                    _errorMessage.emit("no se pudo obtener la cuenta de google")
                }
            } catch (ex: Exception) {
                Log.e("abrahan", "Error en el sing in ${ex.message}")
                _errorMessage.emit("Error en el sing in con Google ${ex.message}")


            } finally {
                _isLoading.value = false
            }
        }
    }

    //metodo para procesar el intent de google sing in
    fun processGoogleSingIn(task: com.google.android.gms.tasks.Task<GoogleSignInAccount>) {
        try {
            val account = task.getResult(ApiException::class.java)
            resultSinginWithGoogle(account)

        } catch (e: ApiException) {
            Log.e("abrahan", "Error en el sing in ${e.message}")
            viewModelScope.launch {
                _errorMessage.emit("Error en el sing in con Google ${e.message}")
            }
        }
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
                   //clearFields()

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

                val uID = user?.uid
                Log.i("LoginViewModel", "id obtenido es $uID")

                val users= UserFinancia(
                    uid= uID.toString(),
                    name= _name.value.toString()
                )
                if (user != null) {
                    val registerUser=authService.saveUser(users)

                    Log.i("LoginViewModel", "user no fue nulo ")
                    if (registerUser){
                        Log.i("firestore", "Registro Completado con exito")
                        _NavController.emit(Routes.StartingScreen.routes)
                        //clearFields()
                    }else{
                        Log.e("firestore", "Error al guardar el usuario")
                    }
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
                _isLoading.value = true
                try {
                    if (authService.validUser(email)) {
                        Log.i("abrahan", "este es el mensaje del confirm")
                        _errorMessage.emit("Este correo electrónico ya está registrado")
                    } else {
                        register(email, password)
                    }
                }catch (ex:Exception){
                    Log.i("abrahan", "este es el mensaje del confirm")
                    _errorMessage.emit("ERROR OnCOnfirm")
                }finally {
                    _isLoading.value = false
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

    //metodo para limpiar campos
    fun clearFields() {
        _name.value = ""
        _email.value = ""
        _password.value = ""
        _Confirm.value = ""
    }


}



