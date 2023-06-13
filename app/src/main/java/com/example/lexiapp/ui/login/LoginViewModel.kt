package com.example.lexiapp.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lexiapp.ui.customDialog.Event
import com.example.lexiapp.domain.model.LoginResult
import com.example.lexiapp.domain.useCases.LoginUseCases
import com.example.lexiapp.domain.model.UserLogin
import com.example.lexiapp.domain.useCases.ProfileUseCases
import com.example.lexiapp.domain.model.FirebaseResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCases: LoginUseCases,
    private val profileUseCases: ProfileUseCases
) : ViewModel() {

    private val _recoverResult = MutableLiveData<Boolean>()
    val recoverResult: LiveData<Boolean> = _recoverResult

    private val _navigateToHome = MutableLiveData<Event<Boolean>>()
    val navigateToHome: LiveData<Event<Boolean>>
        get() = _navigateToHome

    private var _showErrorDialog = MutableLiveData(UserLogin())
    val showErrorDialog: LiveData<UserLogin>
        get() = _showErrorDialog

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            //_viewState.value = LoginViewState(isLoading = true)
            when (val result = loginUseCases(email, password)) {
                LoginResult.Error -> {
                    _showErrorDialog.value =
                        UserLogin(email = email, password = password, showErrorDialog = true)
                    //_viewState.value = LoginViewState(isLoading = false)
                }

                is LoginResult.Success -> {
                    profileUseCases.saveProfile(UserLogin(email=email))
                    _navigateToHome.value = Event(true)
                }
            }
            //_viewState.value = LoginViewState(isLoading = false)
        }
    }

    fun sendRecoverEmail(email: String){
        viewModelScope.launch {
            loginUseCases.sendRecoverEmail(email).onEach {
                _recoverResult.value = it
            }.launchIn(viewModelScope)
            /*loginUseCases.sendRecoverEmail(email).collect{
                _recoverResult.value = it
            }*/
            /*_recoverResult.value=loginUseCases.sendRecoverEmail(email)*/
        }

    }
}

