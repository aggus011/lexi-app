package com.example.lexiapp.domain.useCases

import android.content.SharedPreferences
import android.util.Log
import com.example.lexiapp.R
import com.example.lexiapp.domain.exceptions.UserNotFoundException
import com.example.lexiapp.domain.model.User
import com.example.lexiapp.domain.model.UserLogin
import com.example.lexiapp.domain.model.UserSignUp
import com.example.lexiapp.domain.model.UserType
import com.example.lexiapp.domain.service.FireStoreService
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class ProfileUseCases @Inject constructor(
    private val prefs: SharedPreferences,
    private val fireStoreService: FireStoreService
) {
    private val editor=prefs.edit()

    suspend fun saveProfile(user: UserType) {
        when (user) {
            is UserSignUp -> saveSignUpUser(user.mapToUser())
            is UserLogin -> saveLoginUser(user.mapToUser())
        }
    }

    private suspend fun saveLoginUser(user: User) {
        try{
            val patient = fireStoreService.getUser(user.email)
            val professional = fireStoreService.getProfessional(user.email)
            if(patient.userName != null){
                editor.putString("email", user.email).apply()
                editor.putString("user_name", patient.userName).apply()
                editor.putString("professional_link", patient.profesional).apply()
                editor.putString("user_type", "patient").apply()
                fireStoreService.saveTokenToPatient(user.email)
            }
            if(professional.user?.userName != null){
                editor.putString("email", professional.user.email).apply()
                editor.putString("user_name", professional.user.userName).apply()
                editor.putString("user_type", "professional").apply()
                editor.putString("medical_registration", professional.medicalRegistration).apply()
                editor.putInt("professional_account_state", if(professional.isVerifiedAccount) 2 else 1).apply()
                fireStoreService.saveTokenToProfessional(professional.user.email)
            }
            if(isAdmin(user.email)){
                editor.putString("email", user.email).apply()
                editor.putString("user_type", "admin").apply()
            }
        }catch (e: UserNotFoundException){
            Log.v(TAG, "User not found in collections")
        }
    }

    fun getUserType() = prefs.getString("user_type", null)

    fun getProfessionalVerificationState() = prefs.getInt("professional_account_state", 0)


    private suspend fun saveSignUpUser(user: User) {
        fireStoreService.saveAccount(user)
    }

    suspend fun editProfile(userName: String?, professional: String?, birthDate: Calendar?): User{
        setUserName(userName)
        setBirthDate(birthDate)
        setProfessional(professional)
        val modifyUser=getProfile()
        saveInFirestore(modifyUser)
        return modifyUser
    }

    private suspend fun saveInFirestore(profile: User?) {
        profile.let{fireStoreService.saveAccount(it!!)}
    }

    private fun setProfessional(professional: String?) {
        //if (professional!=null && professional.isNotBlank() && professional.isNotEmpty())
            editor.putString("professional_link", professional).apply()
    }

    private fun setBirthDate(birthDate: Calendar?) {
        val bD = birthDate?.time?.let {
            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(it)
        }
        if(bD!=null && bD.isNotBlank() && bD.isNotEmpty())
            editor.putString("birth_date", bD).apply()
    }

    private fun setUserName(userName: String?) {
        if (userName!=null && userName.isNotBlank() && userName.isNotEmpty())
            editor.putString("user_name", userName).apply()
    }


    fun getProfile(): User {
        return User(
            userName = prefs.getString("user_name", null),
            email = getEmail()!!,
            birthDate = prefs.getString("birth_date", null),
            profesional = prefs.getString("professional_link", null)
        )
    }

    fun getColorRandomForIconProfile(): Int {
        val colors = listOf(
            R.color.icon_profile1,
            R.color.icon_profile2,
            R.color.icon_profile3,
            R.color.icon_profile4,
            R.color.icon_profile5,
            R.color.icon_profile6,
            R.color.icon_profile7,
            R.color.icon_profile8,
            R.color.icon_profile9,
            R.color.icon_profile10
        )

        return colors.random()
    }


    fun userInitials(name: String? = prefs.getString("user_name", null)): String{
        val words = name?.split(" ")
        val initials = StringBuilder()
        words?.let {
            for(word in it){
                val initial = word.firstOrNull()
                if(initial != null){
                    initials.append(initial)
                }
            }
        }
        return initials.toString()
    }

    fun getEmail()= prefs.getString("email", null)

    fun haveAccount()= getEmail()!=null

    suspend fun isPatientLinked() = fireStoreService.getIsLinked(getEmail()!!)

    fun closeSesion()=editor.clear().apply()

    fun verifyIfPatientHasRegisteredButNotChooseCategories() = prefs.contains("categories")

    private fun isAdmin(email: String): Boolean {
        return email == "lexiapp.2023@gmail.com"
    }

    fun getMedicalRegistration() = prefs.getString("medical_registration", null)

    companion object{
        private const val TAG = "ProfileUseCases"
    }

}