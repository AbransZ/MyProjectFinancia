package com.example.myprojectfinancia.Data.API.network

data class DolarOficial(
    val fuente: String,
    val nombre: String,
    val compra: Double?,
    val venta: Double?,
    val promedio: Double,
    val fechaActualizacion: String
)
