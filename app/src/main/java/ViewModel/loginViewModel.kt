package ViewModel

import android.provider.ContactsContract.CommonDataKinds.Email
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class loginViewModel : ViewModel() {
    //logica del email
    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _isEnableLogin = MutableLiveData<Boolean>()
    val isEnable: LiveData<Boolean> = _isEnableLogin

    fun onLoginChange(email: String, password: String) {
        _email.value = email
        _password.value = password
        _isEnableLogin.value = isEnableLogin(email, password)
    }

    fun isEnableLogin(email: String, password: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.length >= 8
    }

}