package com.example.myprojectfinancia.Data.BD

import android.util.Log
import com.example.myprojectfinancia.Index.Data.UserFinancia
import com.example.myprojectfinancia.Index.Plans.ModelsPlans.DataPlans
import com.example.myprojectfinancia.Index.home.Models.Movements.MovementsItemSave
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
                                    MontoBs = doc.getDouble("montoBs") ?: 0.0
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

    //Obtener movimientos por ID
    suspend fun getMovementsById(
        movementId: String, movimiento: (MovementsItemSave) -> Unit, onError: (Exception) ->
        Unit
    ) {
        val userId = firebaseAuth.currentUser?.uid
        try {
            if (userId != null) {
                firestore.collection("users")
                    .document(userId)
                    .collection("movimientos")
                    .document(movementId)
                    .addSnapshotListener { snapshot, error ->
                        if (error != null) {
                            Log.i("Movimientos", "Error al obtener movimiento por ID : ${error}")
                            onError(error)
                            return@addSnapshotListener
                        }
                        if (snapshot != null) {
                            val movementById = MovementsItemSave(
                                Id = snapshot.getString("id") ?: "",
                                Fecha = snapshot.getString("fecha") ?: "",
                                Categoria = snapshot.getString("categoria") ?: "",
                                Naturaleza = snapshot.getString("naturaleza") ?: "",
                                MontoBs = snapshot.getDouble("monto") ?: 0.0
                            )
                            movimiento(movementById)
                        } else {
                            Log.i("Movimientos", "snapshot nulo")
                            onError(Exception("Movimiento nulo"))
                        }
                    }
            }

        } catch (ex: Exception) {
            Log.i("Movimientos", "Error al obtener movimiento : ${ex}")
            onError(ex)
        }


    }


//*******Terminar metodos de movimientos********


    //Actualizar movimientos
    suspend fun updateMovements(
        movementId: String,
        userId: String,
        movement: MovementsItemSave,
        onError: (Exception) -> Unit
    ): Boolean {

        return try {
            if (userId != null) {
                val movementRef = firestore.collection("users")
                    .document(userId)
                    .collection("movimientos")
                    .document(movementId)

                val updates = hashMapOf<String, Any>(
                    "categoria" to movement.Categoria,
                    "montoBs" to movement.MontoBs,
                    "naturaleza" to movement.Naturaleza
                )
                movementRef.update(updates)
                    .await()
                true

            } else {
                Log.i("Movimientos", "snapshot nulo")
                onError(Exception("Movimiento nulo"))
                false
            }

        } catch (ex: Exception) {
            Log.i("Movimientos", "Error al actualizar movimiento : ${ex}")
            onError(ex)
            false
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
                                    MontoBs = doc.getDouble("montoBs") ?: 0.0,
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

    //funcion suspendida para obtener planes
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

    //funcion suspendida para obtener plan especifico para agregar aporte
    suspend fun getPlanById(
        planId: String,
        userId: String,
        contribution: Double,
        onError: (Exception) -> Unit
    ): Boolean {
        return try {
            val planRef = firestore.collection("users")
                .document(userId)
                .collection("planes")
                .document(planId)

            val plan = planRef
                .get()
                .await()

            if (plan.exists()) {
                val amountActualy = plan.getString("actualy") ?: ""
                val amountActuallyNum = amountActualy.toDoubleOrNull() ?: 0.00

                val newAMount = amountActuallyNum + contribution

                val newAdvice = "Se recomienda ahorrar $${newAMount * 50 / 100}"

                val updates = hashMapOf<String, Any>(
                    "actualy" to newAMount.toString(),
                    "advice" to newAdvice
                )
                planRef.update(updates)
                    .await()
                true
            } else {
                Log.i("getplan_service", "No se encontro el plan")
                onError(Exception("No se encontro el plan"))
                false
            }

        } catch (ex: Exception) {
            Log.i("getplan_service", "Error al obtener el plan: ${ex.message}")
            onError(ex)
            false
        }
    }


    suspend fun updatePlanByID(
        planId: String,
        userId: String,
        updatesPlans: DataPlans,
        onError: (Exception) -> Unit
    ): Boolean {
        return try {
            val planRef = firestore.collection("users")
                .document(userId)
                .collection("planes")
                .document(planId)

            val plan = planRef
                .get()
                .await()

            if (plan.exists()) {
                val name = updatesPlans.Name ?: ""
                val description = updatesPlans.Description ?: ""
                val category = updatesPlans.Category ?: ""
                val date = plan.getString("date") ?: ""
                val objective = updatesPlans.Objective ?: ""
                val amountActualy = updatesPlans.Actualy ?: ""

                val updates = hashMapOf<String, Any>(
                    "name" to name,
                    "description" to description,
                    "category" to category,
                    "date" to date,
                    "objective" to objective,
                    "actualy" to amountActualy
                )
                planRef.update(updates)
                    .await()
                true

            } else {
                Log.i("update_service", "No se encontro el plan")
                onError(Exception("No se encontro el plan"))
                false
            }
        } catch (ex: Exception) {
            onError(ex)
            Log.i("update_service", "Error al actualizar el plan: ${ex.message}")
            false
        }

    }


}
