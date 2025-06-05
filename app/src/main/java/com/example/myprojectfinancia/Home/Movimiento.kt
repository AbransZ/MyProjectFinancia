package com.example.myprojectfinancia.Home

import androidx.compose.runtime.State

data class Movimiento(
    val fecha: String,
    val monto: State<String>,
    val categoria: State<String>,
    val naturaleza: State<String>
)
