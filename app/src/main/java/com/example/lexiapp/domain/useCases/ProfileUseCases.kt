package com.example.lexiapp.domain.useCases

import android.content.SharedPreferences
import com.example.lexiapp.domain.model.User
import com.example.lexiapp.domain.model.UserLogin
import com.example.lexiapp.domain.model.UserSignUp
import com.example.lexiapp.domain.model.UserType
import javax.inject.Inject

class ProfileUseCases @Inject constructor(
    private val prefs: SharedPreferences
) {

    fun saveProfile(user: UserType) {
        when (user) {
            is User -> saveUser(user)
            is UserSignUp -> saveUserSingUp(user)
            is UserLogin -> saveUserLogin(user)
        }
    }

    private fun saveUserLogin(userLogin: UserLogin) {}

    private fun saveUserSingUp(userSignUp: UserSignUp) {}

    private fun saveUser(user: User) {}

    fun getEmail() = prefs.getString("email", null)
}