package com.example.myprojectfinancia.Data.message

import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.example.myprojectfinancia.Data.API.network.DolarOficial
import com.example.myprojectfinancia.Data.API.repository.DolarOficialRepository
import com.example.myprojectfinancia.R
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import hilt_aggregated_deps._dagger_hilt_android_internal_modules_ApplicationContextModule
import kotlinx.coroutines.flow.MutableStateFlow

@HiltWorker
class WorkerNotification @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted params: WorkerParameters,
    private val repository: DolarOficialRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        Log.d("WorkerTest", "Iniciando trabajo...")

        return try {
            val result = repository.getDolarPrice()
            Log.d("WorkerTest", "Resultado API: ${result.isSuccess}")

            if (result.isSuccess) {

                val precio = result.getOrNull()?.promedio?.toString()
                Log.d("WorkerTest", "Precio obtenido: $precio")

                showNotification("Dolar BCV", "El Precio del dolar ahora mismo es: Bs.${precio}")
                Result.success()
            } else {
                Log.e("WorkerTest", "Falló la API")
                Result.retry()
            }
        } catch (ex: Exception) {
            Log.e("WorkerTest", "Excepción crítica: ${ex.message}")
            Result.retry()
        }
    }

    private fun showNotification(
        titulo: String,
        contenido: String
    ) {
        val workerManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val builder = NotificationCompat.Builder(context, "canal_principal")
            .setSmallIcon(R.drawable.financia)
            .setContentTitle(titulo)
            .setContentText(contenido)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        workerManager.notify(100,builder.build())

    }


}

