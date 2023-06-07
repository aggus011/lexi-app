package com.example.lexiapp.domain.useCases

import android.content.SharedPreferences
import android.util.Log
import com.example.lexiapp.data.network.FireStoreService
import com.example.lexiapp.domain.model.User
import com.example.lexiapp.domain.model.UserLogin
import com.example.lexiapp.domain.model.UserSignUp
import com.example.lexiapp.domain.model.UserType
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
        val netUser = fireStoreService.getUser(user.email)
        Log.v("USER_NAME_SAVE_PROFILE_USE_CASES", "${netUser.userName} // ${user.email}")
        Log.v("name_profileUC", "${netUser.userName}")
        editor.putString("email", user.email).apply()
        editor.putString("user_name", netUser.userName).apply()
        editor.putString("uri_image", netUser.uri).apply()
    }

    private suspend fun saveSignUpUser(user: User) {
        fireStoreService.saveAccount(user)
        //Uncomment in case the session has already been accessed from the log
        /*
            editor.putString("email", user.email).apply()
            editor.putString("user_name", netUser.userName).apply()
            editor.putString("uri_image", netUser.uri).apply()
        }*/
    }

    suspend fun editProfile(userName: String?, uriImage: String?, birthDate: Calendar?): User{
        setUserName(userName)
        setBirthDate(birthDate)
        setUriImage(uriImage)
        val modifyUser=getProfile()
        saveInFirestore(modifyUser)
        return modifyUser
    }

    private suspend fun saveInFirestore(profile: User?) {
        profile.let{fireStoreService.saveAccount(it!!)}
    }

    private fun setUriImage(uriImage: String?) {
        if (uriImage!=null && uriImage.isNotBlank() && uriImage.isNotEmpty())
            editor.putString("user_name", uriImage).apply()
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
            uri = prefs.getString("uri", null)
        )
    }

    fun getEmail()= prefs.getString("email", null)

    fun haveAccount()= getEmail()!=null

    fun closeSesion()=editor.clear().apply()

}