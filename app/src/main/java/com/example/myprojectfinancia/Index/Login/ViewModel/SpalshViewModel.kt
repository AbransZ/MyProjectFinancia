package com.example.myprojectfinancia.Index.Login.ViewModel

import androidx.lifecycle.ViewModel
import com.example.myprojectfinancia.Data.BD.AuthService
import com.example.myprojectfinancia.Model.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class SpalshViewModel @Inject constructor(private val authService: AuthService) : ViewModel() {


    private fun isUsedLogged(): Boolean {
        return authService.isUsedLogged()
    }

    fun checkDestination(): String {
        val isLogged = isUsedLogged()
        return if (isLogged) {
            Routes.StartingScreen.routes
        } else {
            Routes.LoginScreen.routes
        }
    }
}