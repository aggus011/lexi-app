package com.example.lexiapp.ui.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lexiapp.domain.useCases.LoginUseCases
import com.example.lexiapp.utils.FirebaseResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(private val loginUseCases: LoginUseCases): ViewModel() {

    val registerResult: MutableLiveData<FirebaseResult> by lazy {
        MutableLiveData<FirebaseResult>()
    }

    fun singUpWithEmail(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            loginUseCases.loginEmail(email, password)
                .addOnSuccessListener { registerResult.value = FirebaseResult.TaskSuccess }
                .addOnFailureListener { registerResult.value = FirebaseResult.TaskFaliure }
        }
    }
}