package com.example.myprojectfinancia.Domain

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FinanciaApp : Application() {

    override fun onCreate() {
        super.onCreate()
        // Crear canal de notificaciones
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "canal_principal", // ID del canal
                "Notificaciones Financia", // Nombre visible
                NotificationManager.IMPORTANCE_HIGH // Prioridad alta
            )
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }
}