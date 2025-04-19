package com.example.myprojectfinancia.Login.ui.ViewModel

import androidx.lifecycle.ViewModel
import com.example.myprojectfinancia.Login.Data.DI.AuthService
import com.example.myprojectfinancia.Model.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class homeViewModel @Inject constructor(private val authService: AuthService) : ViewModel() {

    fun logout():String{
        authService.logout()
        return Routes.LoginScreen.routes

    }
}