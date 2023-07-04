package com.example.lexiapp.domain.service

interface FirebaseNotificationService {

    suspend fun sendNotificationToProfessional(professionalToken: String, patientEmail: String)

    suspend fun sendNotificationToPatient(patientToken: String, professionalEmail: String)

    suspend fun sendNotificationObjectiveCompleted(patientToken: String, game: String)

    suspend fun sendNotificationWeeklyObjectivesCompleted(patientToken: String)
}