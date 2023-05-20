package com.example.lexiapp.ui.signup

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lexiapp.domain.useCases.LoginUseCases
import com.example.lexiapp.utils.FirebaseResult
import com.example.lexiapp.utils.SignUpException
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
            try {
                loginUseCases.singUpWithEmail(email, password)
                    .addOnCompleteListener {
                        Log.d(ContentValues.TAG, "El resultado: $it")
                        if (it.isSuccessful) {
                            registerResult.value = FirebaseResult.TaskSuccess
                        } else {
                            registerResult.value = FirebaseResult.TaskFaliure
                        }
                    }
            } catch (e: SignUpException.EmailException) {
                registerResult.value = FirebaseResult.EmailError
            } catch (e: SignUpException.PasswordException) {
                registerResult.value = FirebaseResult.PasswordError
            }

        }
    }
}