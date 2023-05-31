package com.example.lexiapp.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lexiapp.core.Event
import com.example.lexiapp.domain.model.UserSignUp
import com.example.lexiapp.domain.useCases.LoginUseCases
import com.example.lexiapp.domain.useCases.SignUpUseCases
import com.example.lexiapp.utils.FirebaseResult
import com.example.lexiapp.utils.SignUpException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(private val signUpUseCases: SignUpUseCases): ViewModel() {

    val registerResult: MutableLiveData<FirebaseResult> by lazy {
        MutableLiveData<FirebaseResult>()
    }
    private val _navigateToLogin = MutableLiveData<Event<Boolean>>()
    val navigateToLogin: LiveData<Event<Boolean>>
        get() = _navigateToLogin
    private var _showErrorDialog = MutableLiveData(false)
    val showErrorDialog: LiveData<Boolean>
        get() = _showErrorDialog

    fun singUpWithEmail(user: UserSignUp) {
        viewModelScope.launch(Dispatchers.IO) {
            //_viewState.value = SignInViewState(isLoading = true)
            val accountCreated = signUpUseCases(user)
            if (accountCreated) {
                _navigateToLogin.value = Event(true)
            } else {
                _showErrorDialog.value = true
            }
            //_viewState.value = SignInViewState(isLoading = false)
        }
    }
}