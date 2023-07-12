package com.example.lexiapp.ui.signup.patient

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lexiapp.ui.customDialog.Event
import com.example.lexiapp.domain.model.LoginResult
import com.example.lexiapp.domain.model.UserSignUp
import com.example.lexiapp.domain.useCases.SignUpUseCases
import com.example.lexiapp.domain.model.FirebaseResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(private val signUpUseCases: SignUpUseCases) :
    ViewModel() {

    val registerResult: MutableLiveData<FirebaseResult> by lazy {
        MutableLiveData<FirebaseResult>()
    }
    private val _navigateToLogin = MutableLiveData<Event<Boolean>>()
    val navigateToLogin: LiveData<Event<Boolean>>
        get() = _navigateToLogin
    private var _showErrorDialog = MutableLiveData(false)
    var errorMessage = "No hemos podido crear tu cuenta, intenta denuevo mas tarde"
    val showErrorDialog: LiveData<Boolean>
        get() = _showErrorDialog

    fun singUpWithEmail(user: UserSignUp) {
        viewModelScope.launch(Dispatchers.IO) {
            //_viewState.value = SignInViewState(isLoading = true)
            when (signUpUseCases(user)) {
                LoginResult.Error -> {
                    withContext(Dispatchers.Main) {
                        errorMessage = "No hemos podido crear tu cuenta, intenta denuevo mas tarde"
                        _showErrorDialog.value = true
                    }
                }

                LoginResult.Success -> {
                    withContext(Dispatchers.Main) {
                        signUpUseCases.savePatientAccount(user)
                        _navigateToLogin.value = Event(true)
                    }
                }

                LoginResult.DistinctEmail -> {
                    withContext(Dispatchers.Main) {
                        errorMessage = "Los emails no coinciden"
                        _showErrorDialog.value = true
                    }
                }

                LoginResult.EmailInvalid -> {
                    withContext(Dispatchers.Main) {
                        errorMessage = "El formato de email es incorrecto"
                        _showErrorDialog.value = true
                    }
                }

                LoginResult.DistinctPassword -> {
                    withContext(Dispatchers.Main) {
                        errorMessage = "Las contraseñas no coinciden"
                        _showErrorDialog.value = true
                    }
                }

                LoginResult.PasswordInvalid -> {
                    withContext(Dispatchers.Main) {
                        errorMessage = "La contraseña debe tenel por lo menos 6 caracteres"
                        _showErrorDialog.value = true
                    }
                }
            }

            //_viewState.value = SignInViewState(isLoading = false)
        }
    }
}