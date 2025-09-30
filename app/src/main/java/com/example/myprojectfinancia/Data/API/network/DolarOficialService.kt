package com.example.myprojectfinancia.Data.API.network

import retrofit2.Response
import retrofit2.http.GET

//Interfaz para menu de solicitudes a la API
interface DolarOficialService {

    @GET("v1/dolares/oficial")

    suspend fun getDolarOficial(): Response<DolarOficial>
}