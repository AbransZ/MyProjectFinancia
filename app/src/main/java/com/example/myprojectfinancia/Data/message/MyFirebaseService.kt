package com.example.myprojectfinancia.Data.message/*
    package com.example.myprojectfinancia.Home.Data

    import android.app.NotificationManager
    import android.content.Context
    import android.os.Build
    import android.util.Log
    import androidx.core.app.NotificationCompat
    import com.example.myprojectfinancia.R
    import com.google.firebase.messaging.FirebaseMessagingService
    import com.google.firebase.messaging.RemoteMessage

    class MyFirebaseService : FirebaseMessagingService() {

        override fun onNewToken(token: String) {
            super.onNewToken(token)
            Log.d("FCM", "Nuevo token: $token")
        }

        override fun onMessageReceived(remoteMessage: RemoteMessage) {
            super.onMessageReceived(remoteMessage)
            Log.d("FCM", "¡Mensaje recibido!")

            remoteMessage.notification?.let {
                mostrarNotificacion(it.title, it.body)
            }
        }

        private fun mostrarNotificacion(titulo: String?, mensaje: String?) {
            val builder = NotificationCompat.Builder(this, "canal_principal")
                .setContentTitle(titulo ?: "Notificación")
                .setContentText(mensaje ?: "Tienes una nueva notificación")
                .setSmallIcon(R.drawable.logo)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)

            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.notify(System.currentTimeMillis().toInt(), builder.build())
        }
    }
*/
