package com.example.lexiapp.domain.useCases

import android.content.SharedPreferences
import android.util.Log
import com.example.lexiapp.domain.exceptions.FirestoreException
import com.example.lexiapp.domain.exceptions.UserNotFoundException
import com.example.lexiapp.domain.service.FireStoreService
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class VerifyMedicalRegistrationUseCases @Inject constructor(
    private val sharedPrefs: SharedPreferences,
    private val firestoreServiceImpl: FireStoreService
) {
    private val editor=sharedPrefs.edit()

    suspend fun getProfessionalAccountState(email:String): Pair<Boolean, Date?>{
        return try{
            val professional = firestoreServiceImpl.getProfessional(email)
            Pair(professional.isVerifiedAccount, professional.registrationDate)
        }catch (e: UserNotFoundException){
            Log.v(TAG, "Error to found professional")
            Pair(false, null)
        }catch (e: FirestoreException){
            Log.v(TAG, "Error in firestore to get data")
            Pair(false, null)
        }
    }

    fun isBeenTwoDaysSinceRegistration(registrationDate: Date, currentDate: Date = Date()): Boolean {
        val diffInMillis = currentDate.time - registrationDate.time
        return TimeUnit.MILLISECONDS.toDays(diffInMillis) > 2
    }

    fun getCurrentUserEmail(): String?{
        return sharedPrefs.getString("email", null)
    }

    fun changeProfessionalAccountStateToVerified(){
        editor.putInt("professional_account_state", 2).apply()
    }

    companion object{
        private const val TAG = "VerifyMedicalRegistrationUseCases"
    }
}