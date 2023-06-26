package com.example.lexiapp.data.network

import android.util.Log
import com.example.lexiapp.data.notification.NotificationData
import com.example.lexiapp.data.notification.NotificationRequest
import com.example.lexiapp.domain.service.FirebaseNotificationService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseNotificationServiceImpl @Inject constructor(
    firebaseCloudMessagingClient: FirebaseNotificationClient
) : FirebaseNotificationService {
    private val cloudMessaging = firebaseCloudMessagingClient

    override suspend fun sendNotificationToProfessional(
        professionalToken: String,
        patientEmail: String
    ) {
        try {
            val notificationData = NotificationData(
                title = "Vinculación exitosa",
                body = "Te has vinculado con éxito al jugador $patientEmail"
            )
            val notificationRequest = NotificationRequest(
                token = professionalToken,
                notification = notificationData
            )

            val response = cloudMessaging.sendNotification(notificationRequest)

            Log.v(TAG, "notification response: ${response.body()}")

            if (response.isSuccessful) {
                Log.v(TAG, "notification sent successful to professional")
            } else {
                Log.v(TAG, "error to send notification to professional")
            }
        } catch (e: Exception) {
            Log.v(TAG, "an exception has ocurred: ${e.message}")
        }
    }

    override suspend fun sendNotificationToPatient(
        patientToken: String,
        professionalEmail: String
    ) {
        try {
            val notificationData = NotificationData(
                title = "Vinculación exitosa",
                body = "Te has vinculado con éxito al profesional $professionalEmail"
            )
            val notificationRequest = NotificationRequest(
                token = patientToken,
                notification = notificationData
            )

            val response = cloudMessaging.sendNotification(notificationRequest)

            Log.v(TAG, "notification response: ${response.body()}")

            if (response.isSuccessful) {
                Log.v(TAG, "notification sent successful to patient")
            } else {
                Log.v(TAG, "error to send notification to patient")
            }
        } catch (e: Exception) {
            Log.v(TAG, "an exception has ocurred: ${e.message}")
        }
    }

    companion object {
        private const val TAG = "FirebaseNotificationServiceImpl"
    }

}