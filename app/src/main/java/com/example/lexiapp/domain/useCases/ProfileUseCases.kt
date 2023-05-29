package com.example.lexiapp.domain.useCases

import android.content.SharedPreferences
import android.util.Log
import com.example.lexiapp.data.network.FireStoreService
import com.example.lexiapp.domain.model.User
import com.example.lexiapp.domain.model.UserLogin
import com.example.lexiapp.domain.model.UserSignUp
import com.example.lexiapp.domain.model.UserType
import okhttp3.internal.wait
import java.text.SimpleDateFormat
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
        editor.apply{
            Log.v("USER_NAME_SAVE_PROFILE_USE_CASES", "${netUser.userName} // ${user.email
            }")
            Log.v("name_profileUC", "${netUser.userName}")
            putString("email", user.email)
            putString("user_name", netUser.userName)
            putString("uri_image", netUser.uri)
        }
    }

    private suspend fun saveSignUpUser(user: User) {
        fireStoreService.saveAccount(user)
        /*editor.apply{
            putString("email", user.email)
            putString("user_name", user.userName)
        }*/
    }

    fun editProfile(userName: String?, uriImage: String?, birthDate: SimpleDateFormat?){

    }


    fun getProfile(): User? {
        Log.v("EMAIL_GET_PROFILE_USE_CASES", "${getEmail()}")
        Log.v("USER_NAME_GET_PROFILE_USE_CASES", "${prefs.getString("user_name", null)}")
        var user: User? = null
        try{
            user =User(
                userName = prefs.getString("user_name", null),
                email = getEmail()!!,
                uri = prefs.getString("uri", null)
            )
        }catch (_: Exception){}
        return user
    }

    private fun getEmail()= prefs.getString("email", null)

    fun haveAccount()= getEmail()!=null

    fun closeSesion()=editor.clear().apply()

}