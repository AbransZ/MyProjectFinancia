package com.example.myprojectfinancia.Index.home.ViewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myprojectfinancia.Data.BD.AuthService
import com.example.myprojectfinancia.Index.Data.UserFinancia
import com.example.myprojectfinancia.Index.Plans.Domain.budgetRepository
import com.example.myprojectfinancia.Index.home.Models.Movements.MovementsItemSave
import com.example.myprojectfinancia.Index.home.Models.Movements.Movimiento
import com.example.myprojectfinancia.Model.Routes
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject


@HiltViewModel
class homeViewModel @Inject constructor(
    private val authService: AuthService,
    private val googleSignInClient: GoogleSignInClient,
    private val budgetRepository: budgetRepository
) : ViewModel() {

//VARIABLES DE DATOS

    //variable para el nombre
    private val _name = MutableStateFlow<String>("")
    val name: StateFlow<String> = _name

    private val _categoria = MutableLiveData<String>("")
    val category: LiveData<String> = _categoria

    // naturaleza de la transaccion
    private val _naturaleza = MutableStateFlow<String>("")
    val naturaleza: StateFlow<String> = _naturaleza

    // valor del monto en string
    private val _montoString = MutableLiveData<String>("")
    val monto: LiveData<String> = _montoString

    // valor del monto en double
    private val _montoDouble = MutableLiveData<Double?>(0.0)
    val montoDouble: LiveData<Double?> = _montoDouble

    // fecha de la transaccion
    var fechaActual by mutableStateOf(fechaActual())

    //variable para el nombre
    private val _nameEdit = MutableStateFlow<String>("")
    val nameEdit: StateFlow<String> = _nameEdit

    private val _categoriaEdit = MutableStateFlow<String>("")
    val categoryEdit: StateFlow<String> = _categoriaEdit

    // naturaleza de la transaccion
    private val _naturalezaEdit = MutableStateFlow<String>("")
    val naturalezaEdit: StateFlow<String> = _naturalezaEdit

    // valor del monto en string
    private val _montoStringEdit = MutableStateFlow<String>("")
    val montoEdit: StateFlow<String> = _montoStringEdit

    // valor del monto en double
    private val _montoDoubleEdit = MutableLiveData<Double?>(0.0)
    val montoDoubleEdit: LiveData<Double?> = _montoDoubleEdit


    //interaccion Ingresos y egresos Editables
    private val _ingrsosPressedEdit = MutableStateFlow(true)
    val ingrsosPressedEdit: StateFlow<Boolean> = _ingrsosPressedEdit

    //boton de gastos presionado Edit
    private val _egresosIsPressedEdit = MutableStateFlow(false)
    val egresosIsPressedEdit: StateFlow<Boolean> = _egresosIsPressedEdit


    // mostrar dialog
    private val _showDialog = MutableLiveData<Boolean>(false)
    val showDialog: LiveData<Boolean> = _showDialog

    //interaccion Ingresos y egresos
    private val _ingrsosPressed = MutableStateFlow(true)
    val ingrsosPressed: StateFlow<Boolean> = _ingrsosPressed

    //boton de gastos presionado
    private val _egresosIsPressed = MutableStateFlow(false)
    val egresosIsPressed: StateFlow<Boolean> = _egresosIsPressed


    //Movimientos
    private val _allMovements = MutableStateFlow<List<Movimiento>>(emptyList())
    val allMovements: StateFlow<List<Movimiento>> = _allMovements

    //Error de todos los movimientos
    private val _ErrorAllMovements = MutableStateFlow<String?>(null)
    val ErrorAllMovements: StateFlow<String?> = _ErrorAllMovements


    //Movimientos Dialogo Variables
    private val _showMovements = MutableStateFlow<Boolean>(false)
    val showMovements: MutableStateFlow<Boolean> = _showMovements

    //Mostrar Dialogo para editar movimientos
    private val _showMovementsEdit = MutableStateFlow<Boolean>(false)
    val showMovementsEdit: MutableStateFlow<Boolean> = _showMovementsEdit


    //variable presupuesto
    val presupuesto: StateFlow<Double> = budgetRepository.totalBudget

    //variable de movimiento
    private val selectedMovement = MutableStateFlow<Movimiento?>(null)
    val selectedMovimiento: StateFlow<Movimiento?> = selectedMovement

    //variable que controla la seleccion de listado de movimientos
    private val _currentSelectedTab = MutableStateFlow(0) // 0 = Ingresos, 1 = Gastos
    val currentSelectedTab: StateFlow<Int> = _currentSelectedTab

    //dialogo agg presupuesto
    private val _showBudget = MutableStateFlow<Boolean>(false)
    val showBudget: StateFlow<Boolean> = _showBudget

    //monto de dialogo
    private val _bufgetMount = MutableStateFlow<String>("")
    val budgetMount: StateFlow<String> = _bufgetMount

    private val _bufgetMountNum = MutableStateFlow<Double>(0.00)
    val budgetMountNum: StateFlow<Double> = _bufgetMountNum

    //categoria de dialogo
    private val _budgetCategory = MutableStateFlow<String>("")
    val budgetCategory: StateFlow<String> = _budgetCategory

    //Movimientos filtrados
    private val _Movements = MutableStateFlow<List<Movimiento>>(emptyList())
    val Movements: StateFlow<List<Movimiento>> = _Movements

    private val _Error = MutableStateFlow<String?>(null)
    var Error: StateFlow<String?> = _Error


    //FUNCIONESSS

    // Función para actualizar el tab seleccionado
    fun updateSelectedTab(tabIndex: Int) {
        _currentSelectedTab.value = tabIndex
    }

    // Función auxiliar para obtener la naturaleza actual
    private fun getCurrentNature(): String {
        return if (_currentSelectedTab.value == 0) "Ingreso" else "Gasto"
    }

    // funcion para seleccionar un movimiento
    fun selectedMovement(movimiento: Movimiento) {
        selectedMovement.value = movimiento

        _montoStringEdit.value = movimiento.monto
        _montoDoubleEdit.value = movimiento.monto.toDoubleOrNull() ?: 0.0
        _categoriaEdit.value = movimiento.categoria
        _naturalezaEdit.value = movimiento.naturaleza

        when (movimiento.naturaleza) {
            "Ingreso" -> {
                _ingrsosPressedEdit.value = true
                _egresosIsPressedEdit.value = false
                _naturalezaEdit.value = "Ingreso"
            }

            "Gasto" -> {
                _ingrsosPressedEdit.value = false
                _egresosIsPressedEdit.value = true
                _naturalezaEdit.value = "Gasto"
            }

            else -> {
                _ingrsosPressedEdit.value = false
                _egresosIsPressedEdit.value = false
                _naturalezaEdit.value = "Asignacion"
            }
        }
        _showMovementsEdit.value = true
    }


    fun onBudgetCategoryChange(categoria: String) {
        _budgetCategory.value = categoria
    }

    fun aumentarPresupuesto() {
        val presupuestoFinal = presupuesto.value + _bufgetMountNum.value
        guardarPresupuestoTotal()
        getAllMovements()
        budgetRepository.updateBudget(presupuestoFinal)

    }

    private fun guardarPresupuestoTotal() {
        viewModelScope.launch {
            val aumentoPresupuesto = MovementsItemSave(
                Fecha = fechaActual,
                Categoria = _budgetCategory.value,
                Naturaleza = "Ingreso",
                Monto = _bufgetMountNum.value ?: 0.0
            )

            val userCurrent = authService.getCurrentUser()
            val user = UserFinancia(uid = userCurrent?.uid ?: "", name = _name.value ?: "")

            val resultado = authService.saveMovements(aumentoPresupuesto, user)

            if (resultado) {
                Log.i("Presupuesto", "Aumento de presupuesto guardado")

            }
        }
    }

    //limpiar dialogo presupuesto
    fun clearBudget() {
        _bufgetMount.value = ""
        _bufgetMountNum.value = 0.0
        _budgetCategory.value = ""
    }

    //mostrar dialogo de presupuesto
    fun mostrarDialogBudget() {
        _showBudget.value = true
        Log.i("presupuesto", "Mostrar dialogo presionado")
    }

    //ocultar dialogo de presupuesto
    fun ocultarDialogBudget() {
        _showBudget.value = false
    }

    fun onBudgetChange(monto: String) {
        _bufgetMount.value = monto
        val montoNum = monto.toDoubleOrNull()
        if (montoNum != null) {
            _bufgetMountNum.value = montoNum
        }
    }

    fun onNaturalezaChange(naturaleza: String) {
        _naturaleza.value = naturaleza
    }

    fun onCategoriaChange(categoria: String) {
        _categoria.value = categoria
    }

    //ocultar dialogo de movimientos
    fun ocultarDialog() {
        _showDialog.value = false
        clearfields()
    }

    //mostrar dialogo de movimientos
    fun mostrarDialog() {
        _showDialog.value = true
        _ingrsosPressed.value = true
    }

    //mostrar dialogo de editar movimientos
    fun showMovementsEdit(movimiento: Movimiento) {
        _showMovementsEdit.value = true
        selectedMovement(movimiento)

    }

    //ocultar dialogo de editar  movimientos
    fun ocultarDialogEdit() {
        _showMovementsEdit.value = false
        clearfields()
    }

    //Limpieza de datos
    private fun clearAllData() {
        _name.value = ""
        _Movements.value = emptyList()
        _allMovements.value = emptyList()
        _Error.value = null
        _ErrorAllMovements.value = null
        _isLoading.value = false
        clearfields()
    }

    fun clearfields() {
        _naturaleza.value = ""
        _montoString.value = ""
        _categoria.value = ""
        _montoDouble.value = 0.0
        _nameEdit.value = ""
        _categoriaEdit.value = ""
        _naturalezaEdit.value = ""
        _montoStringEdit.value = ""
        _montoDoubleEdit.value = 0.0
        _egresosIsPressed.value = false
        _ingrsosPressed.value = false
    }

    fun onMontoChange(monto: String) {
        _montoString.value = monto
        val montoNum = monto.toDoubleOrNull()

        if (montoNum != null) {
            _montoDouble.value = montoNum
        }
    }

    companion object {
        private fun fechaActual(): String {
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            return sdf.format(Date())
        }
    }

    //INTERACCION CON LOS BOTONES DE INGRESOS Y GASTOS
    fun ingresosIsPressed() {
        _ingrsosPressed.value = true
        _egresosIsPressed.value = false
        _naturaleza.value = "Ingreso"

    }

    fun gastosIsPressed() {
        _egresosIsPressed.value = true
        _ingrsosPressed.value = false
        _naturaleza.value = "Gasto"

    }

    //INTERACCION CON LOS BOTONES DE INGRESOS Y GASTOS EDITABLES
    fun ingresosIsPressedEdit() {
        _ingrsosPressedEdit.value = true
        _egresosIsPressedEdit.value = false
        _naturalezaEdit.value = "Ingreso"

    }

    fun gastosIsPressedEdit() {
        _egresosIsPressedEdit.value = true
        _ingrsosPressedEdit.value = false
        _naturalezaEdit.value = "Gasto"

    }


    //cerrar sesion
    fun logout(): String {
        viewModelScope.launch {
            try {
                clearAllData()
                googleSignInClient.revokeAccess()
                authService.logout()
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error al cerrar sesion: ${e.message}")
            }
        }
        return Routes.LoginScreen.routes
    }

    //carga de datos cada vez que se abre la app
    fun initializeNewUser() {
        viewModelScope.launch {
            _isLoading.value = true

            clearAllData()

            loadUser()

            initializeData()
        }
    }

    private suspend fun loadUser() {
        mostrarNombre()
    }

    fun showMovements(show: Boolean) {
        _showMovements.value = show
    }

    //Fucnion Mostrar Nombre de usuario
    fun mostrarNombre() {
        viewModelScope.launch {
            val userCurrent = authService.getCurrentUser()
            Log.i("user", "user para SALUDO: $userCurrent")
            try {
                _isLoading.value = true
                val user = authService.getUser(userCurrent?.uid ?: "")
                if (user.isSuccess) {


                    val userName = user.getOrNull()?.name ?: ""
                    _name.value = userName
                    _isLoading.value = false

                    Log.i("user", "user para SALUDO: $userName")

                } else {
                    Log.i("user", " error user para SALUDO: ${user.exceptionOrNull()}")
                    _name.value = ""
                    _isLoading.value = false
                }
            } catch (ex: Exception) {
                Log.i("user", " error user para SALUDO: ${ex.message}")
                _isLoading.value = false

            }

        }

    }


    //INICIO DE INTERACCION CON LA BD


    fun initializeData() {
        viewModelScope.launch {
            try {
                Log.i("HomeViewModel", "Iniciando carga de datos")
                getMovements("Ingreso")
                ingresosIsPressed()
                getAllMovements()
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error inicializando datos: ${e.message}")
                _isLoading.value = false
                _Error.value = e.message
            }
        }
    }


    //Guarda Movimientos
    fun guardarMovimiento() {
        viewModelScope.launch {
            val movimientos = MovementsItemSave(
                Fecha = fechaActual,
                Categoria = _categoria.value ?: "",
                Naturaleza = _naturaleza.value,
                Monto = _montoDouble.value ?: 0.0
            )

            val userCurrent = authService.getCurrentUser()
            val user = UserFinancia(uid = userCurrent?.uid ?: "", name = _name.value ?: "")

            val resultado = authService.saveMovements(movimientos, user)

            if (resultado) {
                Log.i("movimientos", "Movimiento guardado")
                ocultarDialog()
            }

        }
    }

    // Obtiene movimientos filtrados
    fun getMovements(nature: String) {
        _isLoading.value = true

        authService.getMovementsByNature(
            nature = nature,
            onResult = { movemets ->
                _Movements.value = movemets.map {
                    Movimiento(
                        id = it.Id,
                        fecha = it.Fecha,
                        monto = it.Monto.toString(),
                        categoria = it.Categoria,
                        naturaleza = it.Naturaleza
                    )
                }
                _Error.value = null
                _isLoading.value = false
            },
            onError = { e ->
                _Movements.value = emptyList()
                _ErrorAllMovements.value = e.message ?: "Error desconocido"
                _Error.value = e.message ?: "Error desconocido"
                _isLoading.value = false
                Log.e("HomeViewModel", "Error obteniendo movimientos: ${e.message}")
            }
        )
    }

    //variables de control cargando de los datos
    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    //Obtener todos los movimientos
    fun getAllMovements() {


        _isLoading.value = true
        authService.getAllMovements(
            onResult = { movements ->
                _allMovements.value = movements.map {
                    Movimiento(
                        id = it.Id,
                        fecha = it.Fecha,
                        monto = it.Monto.toString(),
                        categoria = it.Categoria,
                        naturaleza = it.Naturaleza
                    )
                }
                budgetRepository.calculateBudget(movements)

                _isLoading.value = false
                _ErrorAllMovements.value = null
                Log.i("Allmovimientos", "movimientos: $movements")

            },
            onError = { e ->
                _allMovements.value = emptyList()
                _ErrorAllMovements.value = e.message ?: "Error desconocido"
                _isLoading.value = false
                Log.i("Allmovimientos", "error: ${e.message}")
            }
        )
    }


    //funcion de limbiar  error
    fun clearError() {
        _Error.value = null
    }

    //funcion de limbiar  error de movimientos
    fun clearErrorMovements() {
        _ErrorAllMovements.value = null
    }

    fun onMontoChangeEdit(monto: String) {
        _montoStringEdit.value = monto
        val montoNum = monto.toDoubleOrNull()
        if (montoNum != null) {
            _montoDoubleEdit.value = montoNum
        }

    }

    fun onCategoriaChangeEdit(category: String) {
        _categoriaEdit.value = category
    }

    fun EditarMovimiento() {
        viewModelScope.launch {
            val _selectedMovimiento = selectedMovimiento.value
            val user = authService.getCurrentUser()
            val userId = user?.uid ?: ""
            val movementID = _selectedMovimiento?.id ?: ""
            val oldAmount = _selectedMovimiento?.monto?.toDoubleOrNull() ?: 0.0
            val newAmount = _montoDoubleEdit.value ?: 0.0
            val oldNature = _selectedMovimiento?.naturaleza ?: ""
            val newNature = _naturalezaEdit.value
            val difference = newAmount - oldAmount
            _isLoading.value = true

            try {

                if (_categoriaEdit.value.isBlank()) {
                    _Error.value = "La categoria del movimiento es requerida"
                    return@launch
                }
                if (_naturalezaEdit.value.isBlank()) {
                    _Error.value = "La naturaleza del movimiento es requerida"
                    return@launch
                }
                if (_montoStringEdit.value.isBlank()) {
                    _Error.value = "El monto del movimiento es requerido"
                    return@launch
                }
                if (newAmount <= 0.0) {
                    _Error.value = "El monto debe ser mayor a cero"
                    _isLoading.value = false
                    return@launch
                }

                val movementUpdate = MovementsItemSave(
                    Id = movementID,
                    Fecha = fechaActual,
                    Categoria = _categoriaEdit.value,
                    Naturaleza = _naturalezaEdit.value,
                    Monto = _montoDoubleEdit.value ?: 0.0
                )

                val result = authService.updateMovements(
                    movementID,
                    userId,
                    movementUpdate,
                    onError = { error ->
                        _Error.value = error?.message ?: "Error desconocido"
                        _isLoading.value = false
                    }
                )

                if (result) {
                    val budgetImpact = calculateBudgetImpact(oldAmount, oldNature, newAmount, newNature)
                    budgetRepository.transferToBudget(budgetImpact)
                    _showMovementsEdit.value = false
                    clearfields()
                    getAllMovements()
                    val currentNature = getCurrentNature()
                    getMovements(currentNature)
                    _isLoading.value = false
                }
            } catch (ex: Exception) {
                Log.e("HomeViewModel", "Error al editar movimiento: ${ex.message}")
                _isLoading.value = false
                _Error.value = ex.message
            }
        }
    }


    private fun calculateBudgetImpact(
        oldAmount: Double,
        oldNature: String,
        newAmount: Double,
        newNature: String
    ): Double {


        val revertOldImpact = when (oldNature) {
            "Ingreso" -> -oldAmount
            "Gasto" -> oldAmount
            else -> 0.0
        }


        val applyNewImpact = when (newNature) {
            "Ingreso" -> newAmount
            "Gasto" -> -newAmount
            else -> 0.0
        }

        val totalImpact = revertOldImpact + applyNewImpact

        Log.i("BudgetCalculation", "Revert old ($oldNature $oldAmount): $revertOldImpact")
        Log.i("BudgetCalculation", "Apply new ($newNature $newAmount): $applyNewImpact")
        Log.i("BudgetCalculation", "Total impact: $totalImpact")

        return totalImpact
    }


}