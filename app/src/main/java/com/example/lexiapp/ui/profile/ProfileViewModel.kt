package com.example.lexiapp.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lexiapp.domain.model.User
import com.example.lexiapp.domain.model.UserSignUp
import com.example.lexiapp.domain.useCases.SignUpUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val signUpUseCases: SignUpUseCases): ViewModel() {
    private val _perfilLiveData = MutableLiveData<User>()
    val perfilLiveData: LiveData<User> = _perfilLiveData

    fun getProfile(email: String) {
        viewModelScope.launch {
            val user = signUpUseCases.getUser(email)
            Log.v("USER_NAME_FORM_FIRESTORE", "${user?.userName}")
            if (user!=null) _perfilLiveData.value = user!!
        }
    }

}