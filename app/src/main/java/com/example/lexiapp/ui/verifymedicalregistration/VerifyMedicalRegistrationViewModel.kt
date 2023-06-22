package com.example.lexiapp.ui.verifymedicalregistration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lexiapp.domain.useCases.VerifyMedicalRegistrationUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class VerifyMedicalRegistrationViewModel @Inject constructor(
    private val verifyMedicalRegistrationUseCases: VerifyMedicalRegistrationUseCases
) : ViewModel() {

    private var _verificationState = MutableLiveData(false)
    val verificationState: LiveData<Boolean>
        get() = _verificationState
    private var _errorVerification = MutableLiveData(false)
    val errorVerification: LiveData<Boolean>
        get() = _errorVerification

    fun getLastUpdateAccountVerification() {
        viewModelScope.launch(Dispatchers.IO) {
            val email = verifyMedicalRegistrationUseCases.getCurrentUserEmail()
            if (email != null) {
                val state = verifyMedicalRegistrationUseCases.getProfessionalAccountState(email)
                if (state.first) {
                    withContext(Dispatchers.Main) {
                        _verificationState.value = true
                    }
                }else{
                    withContext(Dispatchers.Main) {
                        _verificationState.value = false
                    }
                }
                state.second.let {
                    val daysDifference =
                        verifyMedicalRegistrationUseCases.isBeenTwoDaysSinceRegistration(it)
                    if (daysDifference) {
                        withContext(Dispatchers.Main) {
                            _errorVerification.value = true
                        }
                    }
                }
            }
        }
    }

    fun changeProfessionalAccountStateToNotBeenVerified(){
        verifyMedicalRegistrationUseCases.changeProfessionalAccountStateToVerified()
    }

}