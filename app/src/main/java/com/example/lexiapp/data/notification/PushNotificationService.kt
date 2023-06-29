package com.example.lexiapp.data.notification

import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.lexiapp.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.text.SimpleDateFormat
import java.util.*

class PushNotificationService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Log.v(TAG, "new token registered: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        if(message.notification != null &&
                tiramisuPermissionsCheck()){
            val notification = message.notification

            val title = notification?.title
            val body = notification?.body

            showNotification(title, body)
        }

    }

    private fun showNotification(title: String?, body: String?) {
        val channelId = "Default"
        val uniqueID = Integer.parseInt(
            SimpleDateFormat("ddHHmmss",  Locale.getDefault()).format(
            Date()
        ))
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(body)
            .setColor(resources.getColor(R.color.purple, this.theme))
            .setColorized(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        if(title?.contains("Felicitaciones") == true){
            notificationBuilder
                .setSmallIcon(R.drawable.ic_cup)
        }

        if(title?.contains("Vinculación") == true){
            notificationBuilder
                .setSmallIcon(R.drawable.ic_account_linked)
        }


        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(
            channelId,
            "Default channel",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)
        notificationManager.notify(uniqueID, notificationBuilder.build())
    }

    private fun tiramisuPermissionsCheck(): Boolean {
        // If we are above level 33, check permissions
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                this,
                POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    companion object{
        private const val TAG = "PushNotificationService"
    }

}