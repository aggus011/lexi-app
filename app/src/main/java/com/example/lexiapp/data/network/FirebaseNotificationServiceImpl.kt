package com.example.lexiapp.data.network

import android.util.Log
import com.example.lexiapp.data.api.notifications.FirebaseNotificationGateway
import com.example.lexiapp.domain.service.FirebaseNotificationService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseNotificationServiceImpl @Inject constructor(
    private val firebaseNotificationGateway: FirebaseNotificationGateway
) : FirebaseNotificationService {

    override suspend fun sendNotificationToProfessional(
        professionalToken: String,
        patientEmail: String
    ) {
        firebaseNotificationGateway.sendNotificationToProfessional(professionalToken, patientEmail)
        Log.v(TAG, "notification sent to professional")
    }

    override suspend fun sendNotificationToPatient(
        patientToken: String,
        professionalEmail: String
    ) {
        firebaseNotificationGateway.sendNotificationToPatient(patientToken, professionalEmail)
        Log.v(TAG, "notification sent to patient")
    }

    override suspend fun sendNotificationObjectiveCompleted(patientToken: String, game: String) {
        firebaseNotificationGateway.sendNotificationObjectiveCompleted(patientToken, game)
        Log.v(TAG, "notification objective completed sent to patient")
    }

    override suspend fun sendNotificationWeeklyObjectivesCompleted(patientToken: String) {
        firebaseNotificationGateway.sendNotificationWeeklyObjectivesCompleted(patientToken)
        Log.v(TAG, "notification weekly objectives completed sent to patient")
    }

    companion object {
        private const val TAG = "FirebaseNotificationServiceImpl"
    }

}