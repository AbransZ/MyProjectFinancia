package com.example.myprojectfinancia.Home.Data

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.app.NotificationCompat
import com.example.myprojectfinancia.R
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import javax.inject.Inject

class MessaguingToken @Inject constructor() : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }




    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d("FCM", "¡Mensaje recibido!")

        // Mostrar notificación (tanto en primer plano como en segundo plano)
        remoteMessage.notification?.let {
            mostrarNotificacion(it.title, it.body)
        }
    }



    // Metodo simple para mostrar una notificación
    private fun mostrarNotificacion(titulo: String?, mensaje: String?) {
        val notificationBuilder = NotificationCompat.Builder(this, "canal_principal")
            .setContentTitle(titulo ?: "Notificación")
            .setContentText(mensaje ?: "Tienes una nueva notificación")
            .setSmallIcon(R.drawable.financia)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
    }
}


// Función composable para solicitar permiso de notificaciones
@Composable
fun SetupFirebaseNotifications() {
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            FirebaseMessaging.getInstance().subscribeToTopic("novedades")
                .addOnCompleteListener { Log.d("FCM", "Permiso concedido, obteniendo token... parte 1")}
            Log.d("FCM", "Permiso concedido, obteniendo token...")
            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("FCM", "TOKEN: ${task.result}")
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            // En versiones anteriores a Android 13, obtener token directamente
            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("FCM", "TOKEN: ${task.result}")
                }
            }
        }
    }
}

