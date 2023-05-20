package com.example.lexiapp.ui.login

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lexiapp.domain.useCases.LoginUseCases
import com.example.lexiapp.utils.FirebaseResult
import com.example.lexiapp.utils.SignUpException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginUseCases: LoginUseCases) : ViewModel() {

    val authResult = MutableLiveData<FirebaseResult>()

    fun loginEmail(email: String, password: String) {
        try {
            loginUseCases.loginEmail(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        authResult.value = FirebaseResult.TaskSuccess
                    } else {
                        authResult.value = FirebaseResult.TaskFaliure
                    }
                }

        } catch (e: SignUpException) {
            authResult.value = FirebaseResult.EmailError
        }
    }
}

