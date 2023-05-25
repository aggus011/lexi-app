package com.example.lexiapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lexiapp.core.Event
import com.example.lexiapp.data.response.LoginResult
import com.example.lexiapp.domain.useCases.LoginUseCases
import com.example.lexiapp.domain.model.UserLogin
import com.example.lexiapp.utils.FirebaseResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginUseCases: LoginUseCases) : ViewModel() {

    val authResult = MutableLiveData<FirebaseResult>()

    private val _navigateToHome = MutableLiveData<Event<Boolean>>()
    val navigateToHome: LiveData<Event<Boolean>>
        get() = _navigateToHome

    private var _showErrorDialog = MutableLiveData(UserLogin())
    val showErrorDialog: LiveData<UserLogin>
        get() = _showErrorDialog

    /*
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
         */

    fun loginTest(email: String, password: String) {
        viewModelScope.launch {
            //_viewState.value = LoginViewState(isLoading = true)
            when (val result = loginUseCases(email, password)) {
                LoginResult.Error -> {
                    _showErrorDialog.value =
                        UserLogin(email = email, password = password, showErrorDialog = true)
                    //_viewState.value = LoginViewState(isLoading = false)
                }

                is LoginResult.Success -> {
                    _navigateToHome.value = Event(true)
                }
            }
            //_viewState.value = LoginViewState(isLoading = false)
        }
    }
}
