package com.example.lexiapp.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lexiapp.domain.useCases.LoginUseCases
import com.example.lexiapp.utils.FirebaseResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginUseCases: LoginUseCases): ViewModel() {

    val authResult: MutableLiveData<FirebaseResult> by lazy {
        MutableLiveData<FirebaseResult>()
    }

    fun loginEmail(email: String, password: String)= flow<FirebaseResult> {
        viewModelScope.launch(Dispatchers.IO) {
            loginUseCases.loginEmail(email, password)
                .addOnSuccessListener { authResult.value = FirebaseResult.TaskSuccess }
                .addOnFailureListener { authResult.value = FirebaseResult.TaskFaliure }
        }
    }
}

