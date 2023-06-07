package com.example.lexiapp.ui.profile

import android.graphics.Bitmap
import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lexiapp.domain.useCases.CodeQRUseCases
import com.example.lexiapp.domain.useCases.LoginUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val loginUseCases: LoginUseCases,
    private val qrUseCases: CodeQRUseCases
): ViewModel() {


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
}