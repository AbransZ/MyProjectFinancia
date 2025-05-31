package com.example.myprojectfinancia.Home.UI.ViewModels

import androidx.lifecycle.ViewModel
import com.example.myprojectfinancia.Login.Data.DI.AuthService
import com.example.myprojectfinancia.Model.Routes
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class homeViewModel @Inject constructor(
    private val authService: AuthService,
    private val googleSignInClient: GoogleSignInClient
) : ViewModel() {


    fun logout(): String {
        googleSignInClient.revokeAccess()
        authService.logout()
        return Routes.LoginScreen.routes
    }


}