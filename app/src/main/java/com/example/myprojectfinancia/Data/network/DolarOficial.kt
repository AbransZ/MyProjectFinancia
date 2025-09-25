package com.example.myprojectfinancia.Data.network

data class DolarOficial(
    val fuente: String,
    val nombre: String,
    val compra: Double?,
    val venta: Double?,
    val precio: Double,
    val fecha: String
)
