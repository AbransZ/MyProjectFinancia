package com.example.myprojectfinancia.Login.ui.ViewModel

import androidx.lifecycle.ViewModel
import com.example.myprojectfinancia.Login.Data.DI.AuthService
import com.example.myprojectfinancia.Model.Routes
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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