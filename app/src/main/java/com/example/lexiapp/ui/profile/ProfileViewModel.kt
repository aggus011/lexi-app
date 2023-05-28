package com.example.lexiapp.ui.profile

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lexiapp.R
import com.example.lexiapp.domain.model.User
import com.example.lexiapp.domain.model.UserSignUp
import com.example.lexiapp.domain.useCases.SignUpUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val signUpUseCases: SignUpUseCases,
    private val prefs: SharedPreferences
): ViewModel() {

    private val _perfilLiveData = MutableLiveData<User>()
    val perfilLiveData: LiveData<User> = _perfilLiveData

    init {

    }

    fun getProfile(email: String) {
        viewModelScope.launch {
            _perfilLiveData.value = signUpUseCases.getUser(email)
            //Log.v("USER_NAME_FORM_FIRESTORE", "${user?.userName}")
        }
    }

}