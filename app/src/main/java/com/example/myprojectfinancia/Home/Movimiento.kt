package com.example.myprojectfinancia.Home

import androidx.compose.runtime.State

data class Movimiento(
    val fecha: String,
    val monto: String,
    val categoria: String,
    val naturaleza: String
)
