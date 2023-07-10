package com.example.lexiapp.domain.useCases

import android.content.SharedPreferences
import com.example.lexiapp.domain.service.FireStoreService
import com.example.lexiapp.domain.service.FirebaseNotificationService
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LinkUseCases @Inject constructor(
    private val firestoreService: FireStoreService,
    private val sharedPrefs: SharedPreferences,
    private val firebaseNotificationService: FirebaseNotificationService
) {

    suspend fun bindProfessionalToPatient(
        emailPatient: String
    ) = flow {
        val professionalToken = firestoreService.getProfessionalToken(getEmail()!!)
        val patientToken = firestoreService.getPatientToken(emailPatient)

        if(professionalToken != null && patientToken != null){
            firebaseNotificationService.sendNotificationToProfessional(
                professionalToken = professionalToken,
                patientEmail = emailPatient
            )

            firebaseNotificationService.sendNotificationToPatient(
                patientToken = patientToken,
                professionalEmail = getEmail()!!
            )
        }
        emit(firestoreService.bindProfessionalToPatient(emailPatient, getEmail()!!))
    }

    suspend fun addPatientToProfessional(
        emailPatient: String,
    ) = flow {
        val result = firestoreService.addPatientToProfessional(emailPatient, getEmail()!!).await()
        emit(result)
    }

    suspend fun getListLinkPatientOfProfessional() =
        firestoreService.getListLinkPatientOfProfessional(getEmail()!!)

    suspend fun unBindProfessionalFromPatient(
        emailPatient: String
    ) = flow {
        emit(firestoreService.unBindProfessionalFromPatient(emailPatient))
    }

    suspend fun deletePatientFromProfessional(
        emailPatient: String,
    ) = flow {
        val result =
            firestoreService.deletePatientFromProfessional(emailPatient, getEmail()!!).await()
        emit(result)
    }

    suspend fun getUser(emailPatient: String) = firestoreService.getUser(emailPatient)

    private fun getEmail() = sharedPrefs.getString("email", null)
}