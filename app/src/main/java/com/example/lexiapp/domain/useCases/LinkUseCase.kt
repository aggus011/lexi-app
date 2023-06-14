package com.example.lexiapp.domain.useCases

import android.content.SharedPreferences
import android.util.Log
import com.example.lexiapp.domain.model.FirebaseResult
import com.example.lexiapp.domain.model.User
import com.example.lexiapp.domain.service.AuthenticationService
import com.example.lexiapp.domain.service.FireStoreService
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LinkUseCase @Inject constructor(
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
    ): List<User> {
        val deferredList = firestoreService.addPatientToProfessional(emailPatient, getEmail()!!)
        val emailList = deferredList.await()
        val userList = mutableListOf<User>()
        emailList.forEach {
            val user = firestoreService.getUser(it)
            userList.add(user)
        }
        return userList
    }

    suspend fun getListLinkPatientOfProfessional() = flow {
        val helpList = mutableListOf<User>()
        val list = firestoreService.getListLinkPatientOfProfessional(getEmail()!!)
        list.forEach {
            val patient =firestoreService.getUser(it)
            Log.v("VALIDATE_FILTER_USERS", "${patient.userName}//${patient.email}")
            helpList.add(patient)
        }
        emit(helpList)
    }

    suspend fun unBindProfessionalFromPatient(
        emailPatient: String
    ) = flow {
        emit(firestoreService.unBindProfessionalFromPatient(emailPatient))
    }

    suspend fun deletePatientFromProfessional(
        emailPatient: String,
    ) = flow {
        val helpList = mutableListOf<User>()
        val list = firestoreService.deletePatientFromProfessional(emailPatient, getEmail()!!)
        list.forEach {
            val patient =firestoreService.getUser(it)
            Log.v("VALIDATE_FILTER_USERS", "${patient.userName}//${patient.email}")
            helpList.add(patient)
        }
        emit(helpList)
    }

    suspend fun getUser (emailPatient: String) = firestoreService.getUser(emailPatient)

    private fun getEmail()= sharedPrefs.getString("email", null)
}