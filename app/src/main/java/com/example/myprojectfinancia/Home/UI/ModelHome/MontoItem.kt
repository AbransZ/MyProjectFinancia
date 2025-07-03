package com.example.myprojectfinancia.Home.UI.ModelHome

data class montoItem(
    val fecha: Long = System.currentTimeMillis(),
    var monto: Float,
    var tipo: String,
    var Categoria: String
)
