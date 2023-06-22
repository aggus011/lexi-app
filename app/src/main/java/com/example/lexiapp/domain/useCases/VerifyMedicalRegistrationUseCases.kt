package com.example.lexiapp.domain.useCases

import android.content.SharedPreferences
import android.util.Log
import com.example.lexiapp.domain.service.FireStoreService
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class VerifyMedicalRegistrationUseCases @Inject constructor(
    private val sharedPrefs: SharedPreferences,
    private val firestoreServiceImpl: FireStoreService
) {
    private val editor = sharedPrefs.edit()

    suspend fun getProfessionalAccountState(email: String): Pair<Boolean, Date> {
        val professional = firestoreServiceImpl.getProfessional(email)
        Log.v(TAG, "Professional state: ${professional.isVerifiedAccount}  Date registration: ${professional.registrationDate}")
        return Pair(professional.isVerifiedAccount, professional.registrationDate ?: Date())
    }

    fun isBeenTwoDaysSinceRegistration(
        registrationDate: Date,
        currentDate: Date = Date()
    ): Boolean {
        val diffInMillis = currentDate.time - registrationDate.time
        return TimeUnit.MILLISECONDS.toDays(diffInMillis) > 2
    }

    fun getCurrentUserEmail(): String? {
        return sharedPrefs.getString("email", null)
    }

    fun changeProfessionalAccountStateToVerified() {
        editor.putInt("professional_account_state", 2).apply()
    }

    companion object {
        private const val TAG = "VerifyMedicalRegistrationUseCases"
    }
}