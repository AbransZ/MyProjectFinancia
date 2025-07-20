package com.example.myprojectfinancia.Login.Data.DI

import android.util.Log
import com.example.myprojectfinancia.Home.Data.UserFinancia
import com.example.myprojectfinancia.Home.UI.home.Models.MovementsItem
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class AuthService @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    googleSignInClient: GoogleSignInClient,
    private val firestore: FirebaseFirestore
) {

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
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        return firebaseAuthWithCredential(credential)
    }

    //autenticar con credenciales
    private suspend fun firebaseAuthWithCredential(credential: AuthCredential): FirebaseUser? {
        return firebaseAuth.signInWithCredential(credential).await().user
    }

    fun isUsedLogged(): Boolean {
        return getCurrentUser() != null
    }

    fun logout() {
        firebaseAuth.signOut()
    }

    fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    suspend fun forgotPassword(email: String): Boolean {
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            true
        } catch (ex: Exception) {
            Log.e("AuthService", "Error al enviar correo: ${ex.message}")
            false
        }

    }

    //metodo para guardar usuarios en la BD
    suspend fun saveUser(
        user: UserFinancia
    ): Boolean {
        return try {
            Log.i("loginViewModel", "=== INICIANDO GUARDADO EN FIRESTORE ===")
            Log.i("loginViewModel", "UID: ${user.uid}")
            Log.i("loginViewModel", "Name: ${user.name}")

            Log.i("AuthService", "Llamando a firestore.collection...")

            firestore.collection("users")
                .document(user.uid)
                .set(user)
                .await()

            Log.i("firestore", "usuario guardado")
            true
        } catch (ex: Exception) {
            Log.e("firestore", "Error al guardar el usuario: ${ex.message}")
            Log.e("firestore", "Tipo de error: ${ex.javaClass.simpleName}")
            ex.printStackTrace()
            false
        }
    }

    //Metodo para obtener datos del usuario
    suspend fun getUser(uid: String): Result<UserFinancia> {
        return try {
            val document = firestore.collection("users")
                .document(uid)
                .get()
                .await()
            if (document.exists()) {
                val usuario = UserFinancia(
                    uid = document.getString("uid") ?: "",
                    name = document.getString("name") ?: "deberia decir un nombre aqui"
                )
                Result.success(usuario)
            } else {
                Result.failure(Exception("Usuario no encontrado"))
            }
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }

    //Metodo para guardar movimientos
    suspend fun saveMovements(movement: MovementsItem,user: UserFinancia): Boolean {
        val useruid = user.uid
        val movementId = UUID.randomUUID().toString()
        return try {

            firestore.collection("users")
                .document(useruid)
                .collection("movimientos")
                .document(movementId)
                .set(movement)
                .await()
            Log.i("firestore", "Movimiento guardado")
            true
        } catch (ex: Exception) {
            Log.i("firestore", "Movimiento  No guardad Motivo: ${ex.message}")
            false
        }
    }

}
