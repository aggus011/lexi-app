package com.example.lexiapp.data.network

import android.util.Log
import com.example.lexiapp.data.notification.NotificationData
import com.example.lexiapp.data.notification.NotificationRequest
import com.example.lexiapp.domain.service.FirebaseNotificationService
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
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
            Log.v(TAG, "an exception has occurred: ${e.message}")
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
            Log.v(TAG, "an exception has occurred: ${e.message}")
        }
    }

    override suspend fun sendNotificationObjectiveCompleted(
        patientToken: String,
        game: String
    ) {
        try {
            val notificationData = NotificationData(
                title = "¡Felicitaciones!",
                body = "Has completado con éxito el objetivo $game"
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
            Log.v(TAG, "an exception has occurred: ${e.message}")
        }
    }

    override suspend fun sendNotificationWeeklyObjectivesCompleted(patientToken: String) {
        val timeZone = ZoneId.of("America/Argentina/Buenos_Aires")
        val currentDate = LocalDate.now(timeZone)
        val lastMonday = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val lastMondayDate = lastMonday.format(dateFormatter)

        try{

            val notificationData = NotificationData(
                title = "¡Felicitaciones!",
                body = "Has completado todos los objetivos de la semana de $lastMondayDate"
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

        } catch (e: Exception){
            Log.v(TAG, "exception has occurred: ${e.message}")
        }
    }

    companion object {
        private const val TAG = "FirebaseNotificationServiceImpl"
    }

}