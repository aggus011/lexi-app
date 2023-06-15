package com.example.lexiapp.domain.useCases

import android.content.SharedPreferences
import com.example.lexiapp.domain.model.WhereIsTheLetterResult
import com.example.lexiapp.domain.service.FireStoreService
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LinkUseCases @Inject constructor(
    private val firestoreService: FireStoreService,
    private val sharedPrefs: SharedPreferences
) {

    suspend fun bindProfessionalToPatient(
        emailPatient: String
    ) = flow {
        emit(firestoreService.bindProfessionalToPatient(emailPatient, getEmail()!!))
    }

    suspend fun addPatientToProfessional(
        emailPatient: String,
    ) = flow {
        val result = firestoreService.addPatientToProfessional(emailPatient, getEmail()!!).await()
        emit(result)
    }

    suspend fun getListLinkPatientOfProfessional(listener: (List<String>?) -> Unit) {
        firestoreService.getListLinkPatientOfProfessional(getEmail()!!, listener)
    }

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

    suspend fun getUserGameResults(emailPatient: String): List<WhereIsTheLetterResult> {
        return firestoreService.getLastResultsWhereIsTheLetterGame(emailPatient)
    }

    suspend fun getUser(emailPatient: String) = firestoreService.getUser(emailPatient)

    private fun getEmail() = sharedPrefs.getString("email", null)
}