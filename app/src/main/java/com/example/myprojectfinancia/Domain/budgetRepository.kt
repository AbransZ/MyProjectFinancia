package com.example.myprojectfinancia.Domain

import android.util.Log
import com.example.myprojectfinancia.Data.BD.AuthService
import com.example.myprojectfinancia.Index.home.Models.Movements.MovementsItemSave
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class budgetRepository @Inject constructor(private val authService: AuthService) {

    private val _budgetFree = MutableStateFlow<Double>(0.00)
    val budgetFree: StateFlow<Double> = _budgetFree

    private val _planBudget = MutableStateFlow<Double>(0.00)
    val planBudget: StateFlow<Double> = _planBudget

    private val _totalBudget = MutableStateFlow<Double>(0.00)
    val totalBudget: StateFlow<Double> = _totalBudget

    //Ingresos y Gastos variables
    private val _TotalIngresos = MutableStateFlow<Double>(0.0)
    val TotalIngresos: StateFlow<Double> = _TotalIngresos

    //total de gastos
    private val _TotalGastos = MutableStateFlow<Double>(0.0)
    val TotalGastos: StateFlow<Double> = _TotalGastos

    //total de asignaciones
    private val _TotalAsignacion = MutableStateFlow<Double>(0.0)
    val TotalAsignacion: StateFlow<Double> = _TotalAsignacion

    //Formatear Presupuesto
    fun formatBudget(budget: Double): String {
        return String.format("%.2f", budget)
    }

    //Calcular Presupuesto
    fun calculateBudget(movements: List<MovementsItemSave>) {
        var ingresos = 0.0
        var gasto = 0.0
        var asignacion = 0.0

        movements.forEach { movement ->
            when (movement.Naturaleza) {
                "Ingreso" -> ingresos += movement.MontoBs
                "Gasto" -> gasto += movement.MontoBs
                "Asignacion" -> asignacion += movement.MontoBs
            }
        }
        _TotalIngresos.value = ingresos
        _TotalGastos.value = gasto
        _TotalAsignacion.value = asignacion

        val budgetCalculated = ingresos - gasto
        _totalBudget.value = formatBudget(budgetCalculated).toDoubleOrNull() ?: 0.00
        _planBudget.value = asignacion
        _budgetFree.value = formatBudget(_totalBudget.value - _planBudget.value).toDoubleOrNull() ?: 0.00



        Log.i(
            "presupuesto",
            "ingresos: $ingresos , gastos: $gasto , asignaciones: $asignacion, presupuesto: ${budgetCalculated}"
        )


        Log.i(
            "presupuesto",
            "ingresos: $ingresos , gastos: $gasto , presupuesto: ${budgetCalculated}"
        )

    }

    // Actualizar presupuesto disponible
    fun updateBudget(newBudget: Double) {
        _budgetFree.value = newBudget
    }

    // Validar si hay suficiente dinero en el presupuesto disponible
    fun validateBudget(amount: Double): Boolean {
        return _budgetFree.value >= amount
    }

    // Actualizar presupuesto total
    fun updateTotalBudget(newTotal: Double) {
        _totalBudget.value = newTotal
        _budgetFree.value = newTotal - _planBudget.value
        Log.i("testpresu", "presupuesto disponible: ${_budgetFree.value}")
        Log.i("testpresu", "presupuesto asignado: ${_planBudget.value}")
    }

    // Transferir dinero a un plan
    fun transferToPlan(amount: Double): Boolean {
        if (_budgetFree.value >= amount) {
            _planBudget.value += amount
            _budgetFree.value -= amount

            Log.i("testpresu", "presupuesto disponible: ${_budgetFree.value}")
            Log.i("testpresu", "presupuesto asignado: ${_planBudget.value}")
            return true
        }
        return false
    }

// Transferir dinero de planes a presupuesto disponible

    // Transferir dinero al presupuesto disponible
    fun transferToBudget(amount: Double): Boolean {
        if (_planBudget.value >= amount) {
            _planBudget.value -= amount
            _budgetFree.value += amount
            Log.i("testpresu", "presupuesto disponible: ${_budgetFree.value}")
            Log.i("testpresu", "presupuesto asignado: ${_planBudget.value}")
            return true
        }
        return false
    }
}