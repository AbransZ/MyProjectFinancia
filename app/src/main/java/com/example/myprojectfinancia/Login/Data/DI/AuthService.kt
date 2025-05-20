package com.example.myprojectfinancia.Login.Data.DI

import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthService @Inject constructor(private val firebaseAuth: FirebaseAuth,googleSignInClient: GoogleSignInClient) {

    suspend fun login(email: String, password: String): FirebaseUser? {
        return firebaseAuth.signInWithEmailAndPassword(email, password).await().user
    }

    suspend fun register(email: String, password: String): FirebaseUser? {
        val result = firebaseAuth.createUserWithEmailAndPassword(email, password)
        return result.await().user
    }

    suspend fun validUser(email: String): Boolean {
        return try {
            val result = firebaseAuth.fetchSignInMethodsForEmail(email).await()
            result.signInMethods?.isNotEmpty() ?: false
        } catch (ex: Exception) {
            false
        }
    }

    // auth con google
    suspend fun loginWithGoogle(idToken: String): FirebaseUser? {
        val credential = GoogleAuthProvider.getCredential(idToken,null)
        return firebaseAuthWithCredential(credential)
    }
//autenticar con credenciales
    private suspend fun firebaseAuthWithCredential(credential: AuthCredential): FirebaseUser? {
        return firebaseAuth.signInWithCredential(credential).await().user
    }

    fun isUsedLogged():Boolean {
        return getCurrentUser() != null
    }

    fun logout() {
        firebaseAuth.signOut()
    }

    private fun getCurrentUser() = firebaseAuth.currentUser

    suspend fun forgotPassword(email: String):Boolean{
      return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            true
        }catch (ex:Exception){
            Log.e("AuthService", "Error al enviar correo: ${ex.message}")
            false
        }

    }


}