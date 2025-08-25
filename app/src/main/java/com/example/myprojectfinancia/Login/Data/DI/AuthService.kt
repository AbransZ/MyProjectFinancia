package com.example.myprojectfinancia.Login.Data.DI

import android.util.Log
import com.example.myprojectfinancia.Home.Data.UserFinancia
import com.example.myprojectfinancia.Home.UI.Plans.ModelsPlans.DataPlans
import com.example.myprojectfinancia.Home.UI.home.Models.Movements.MovementsItemSave
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthService @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    googleSignInClient: GoogleSignInClient,
    private val firestore: FirebaseFirestore
) {

    suspend fun login(email: String, password: String): FirebaseUser? {
        return firebaseAuth.signInWithEmailAndPassword(email, password)
            .await().user
    }

    suspend fun register(email: String, password: String): FirebaseUser? {
        val result = firebaseAuth.createUserWithEmailAndPassword(email, password)
        return result.await().user
    }

    suspend fun validUser(email: String): Boolean {
        return try {
            val result = firebaseAuth.fetchSignInMethodsForEmail(email)
                .await()
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
        return firebaseAuth.signInWithCredential(credential)
            .await().user
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
            firebaseAuth.sendPasswordResetEmail(email)
                .await()
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
    suspend fun saveMovements(movement: MovementsItemSave, user: UserFinancia): Boolean {
        val useruid = user.uid
        val movimiento = firestore.collection("users")
            .document(useruid)
            .collection("movimientos")
            .document()

        val movementWithID = movement.copy(Id = movimiento.id)
        return try {
            movimiento.set(movementWithID)
                .await()
            Log.i("firestore", "Movimiento guardado")
            true
        } catch (ex: Exception) {
            Log.i("firestore", "Movimiento  No guardad Motivo: ${ex.message}")
            false
        }
    }

    //Metodo para Guardar planes
    suspend fun savePlan(plan: DataPlans, user: UserFinancia): Boolean {
        val useruid = user.uid
        val planSave = firestore.collection("users")
            .document(useruid)
            .collection("planes")
            .document()

        val planWithId = plan.copy(id = planSave.id)
        return try {
            planSave.set(planWithId)
                .await()
            Log.i("firestore", "Plan guardado")
            true
        } catch (ex: Exception) {
            Log.i("firestore", "Plan No guardado Motivo: ${ex.message}")
            false
        }
    }

    //Metodo para obtener movimientos
    fun getMovementsByNature(
        nature: String, onResult: (List<MovementsItemSave>) -> Unit, onError: (Exception) -> Unit
    ) {
        val userId = firebaseAuth.currentUser?.uid

        try {
            if (userId != null) {
                Log.i("Movimientos", "Usuario encontrado : ${userId}")
                firestore.collection("users")
                    .document(userId)
                    .collection("movimientos")
                    .whereEqualTo("naturaleza", nature)
                    .addSnapshotListener { snapshot, error ->

                        if (error != null) {
                            onError(error)
                            return@addSnapshotListener
                        }

                        if (snapshot != null) {
                            val movemets = snapshot.documents.map { doc ->
                                MovementsItemSave(
                                    Id = doc.id,
                                    Fecha = doc.getString("fecha") ?: "",
                                    Categoria = doc.getString("categoria") ?: "",
                                    Naturaleza = doc.getString("naturaleza") ?: "",
                                    Monto = doc.getDouble("monto") ?: 0.0
                                )
                            }
                            onResult(movemets)
                            Log.i("Movimientos", "Movimientos encontrados : ${movemets}")
                        }


                    }
            } else {
                Log.i("Movimientos", "Usuario no encontrado")
                onError(Exception("Ususario no encontrado"))
                null
            }


        } catch (e: Exception) {
            Log.i("Movimientos", "Usuario Nulo : ${e}")
            onError(e)
            null
        }
    }

    //Metodo para obtener todos los movimientos
    fun getAllMovements(onResult: (List<MovementsItemSave>) -> Unit, onError: (Exception) -> Unit) {

        val userId = firebaseAuth.currentUser?.uid
        try {
            if (userId != null) {
                Log.i("AllMovements", "Usuario encontrado : ${userId}")

                firestore.collection("users")
                    .document(userId)
                    .collection("movimientos")
                    .addSnapshotListener { snapshot, error ->
                        if (error != null) {
                            onError(error)
                            Log.i("AllMovements", "error al obtener movimientos ${error}")
                        }
                        if (snapshot != null) {
                            val allMovements = snapshot.documents.map { doc ->
                                MovementsItemSave(
                                    Id = doc.id,
                                    Fecha = doc.getString("fecha") ?: "",
                                    Naturaleza = doc.getString("naturaleza") ?: "",
                                    Monto = doc.getDouble("monto") ?: 0.0,
                                    Categoria = doc.getString("categoria") ?: ""
                                )
                            }
                            onResult(allMovements)
                            Log.i("AllMovements", "movimientos encontrados ${allMovements}")
                        } else {
                            Log.i("AllMovements", "snapshot nulo")
                            onResult(emptyList())
                            onError(Exception("Snapshot nulo"))
                        }
                    }
            }
        } catch (e: Exception) {
            Log.i("AllMovements", "Error al obtener movimientos")
            onError(e)
        }
    }

    suspend fun getPlans(onResult: (List<DataPlans>) -> Unit, onError: (Exception) -> Unit) {
        val userId = getCurrentUser()?.uid

        if (userId != null) {
            Log.i("Plans", "Usuario encontrado : ${userId}")
            firestore.collection("users")
                .document(userId)
                .collection("planes")
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        onError(error)
                        Log.i("Plans", "error al obtener planes ${error}")
                        return@addSnapshotListener
                    }
                    if (snapshot != null) {
                        val plans = snapshot.documents.map { plan ->
                            DataPlans(
                                id = plan.getString("id") ?: "",
                                Name = plan.getString("name") ?: "",
                                Description = plan.getString("description") ?: "",
                                Category = plan.getString("category") ?: "",
                                Date = plan.getString("date") ?: "",
                                Objective = plan.getString("objective") ?: "",
                                Advice = plan.getString("advice") ?: "",
                                Actualy = plan.getString("actualy") ?: "",
                            )
                        }
                        onResult(plans)
                        Log.i("Plans", "planes encontrados ${plans}")
                    } else {
                        Log.i("Plans", "snapshot nulo")
                        onResult(emptyList())
                        onError(Exception("Snapshot nulo"))
                    }
                }
        }
    }
}
