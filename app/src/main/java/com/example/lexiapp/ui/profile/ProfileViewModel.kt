package com.example.lexiapp.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import android.graphics.Bitmap
import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lexiapp.domain.model.User
import com.example.lexiapp.domain.useCases.CodeQRUseCases
import com.example.lexiapp.domain.useCases.ProfileUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileUseCases: ProfileUseCases,
    private val qrUseCases: CodeQRUseCases
): ViewModel() {

    private var _profileLiveData = MutableLiveData<User?>()
    val profileLiveData: LiveData<User?> = _profileLiveData

    private val _calendarBirthDate = MutableLiveData<Calendar?>()

    private val _timeLeftInMillis = MutableLiveData<Long>()
    val timeLeftInMillis: LiveData<Long> = _timeLeftInMillis

    private var countdownTimer: CountDownTimer? = null
    private val initialTimeInMillis: Long = 60000 // Tiempo inicial en milisegundos (60 segundos)

    private val _isTimerRunning = MutableLiveData<Boolean>()
    val isTimerRunning: LiveData<Boolean> = _isTimerRunning

    private var myQR:Bitmap?=null

    init{
        _timeLeftInMillis.value = initialTimeInMillis
        _isTimerRunning.value = true
        //Set in ProfileFragment
        setQR()
        getProfile()
        _calendarBirthDate.value=null
    }

    private fun setQR() {
        myQR = qrUseCases.generateQR(getEmail())
    }

    fun getQR()=myQR

    //Should be from SharedPreferences (or UseCases)
    private fun getEmail()="aaaa@gmail.com"

    fun startTimer() {
        if (countdownTimer != null) {
            countdownTimer?.cancel()
            countdownTimer = null
        }

        countdownTimer = object : CountDownTimer(_timeLeftInMillis.value ?: initialTimeInMillis, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                _timeLeftInMillis.value = millisUntilFinished
            }

            override fun onFinish() {
                _isTimerRunning.value = false
                // Acción a realizar cuando el contador llegue a cero
            }
        }

        _isTimerRunning.value = true
        countdownTimer?.start()
    }

    fun cleared()=onCleared()

    override fun onCleared() {
        super.onCleared()
        countdownTimer?.cancel()
    }

    private fun getProfile(){
        viewModelScope.launch {
            _profileLiveData.value=profileUseCases.getProfile()
            Log.v("USER_NAME_FIRESTORE_VIEW_MODEL", "${profileLiveData.value?.userName}")
        }
    }

    fun setBirthDate (calendar: Calendar){
        _calendarBirthDate.value = calendar
    }

    fun modifiedProfile(userName: String?,uri: String?){
        viewModelScope.launch{
            _profileLiveData.value=null
            _profileLiveData.value = profileUseCases.editProfile(userName,uri,_calendarBirthDate.value)
            _calendarBirthDate.value=null
        }
    }

    fun closeSesion(){
        profileUseCases.closeSesion()
    }
}