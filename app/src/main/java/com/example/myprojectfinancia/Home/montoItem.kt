package com.example.myprojectfinancia.Home

import java.util.Date

data class montoItem(
    val fecha: Long = System.currentTimeMillis(),
    var monto: Float,
    var tipo: String,
    var Categoria: String
)
