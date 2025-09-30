package com.example.myprojectfinancia.Data.API.repository

import com.example.myprojectfinancia.Data.API.network.DolarOficial
import com.example.myprojectfinancia.Data.API.network.DolarOficialService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DolarOficialRepository @Inject constructor(private val apiService: DolarOficialService) {

    suspend fun getDolarPrice(): Result<DolarOficial> {
        return try {
            val response = apiService.getDolarOficial()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al obtener el precio del dólar"))
            }

        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }
}