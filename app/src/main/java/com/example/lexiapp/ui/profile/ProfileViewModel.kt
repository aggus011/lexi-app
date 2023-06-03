package com.example.lexiapp.ui.profile

import android.graphics.Bitmap
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

    private var myQR:Bitmap?=null

    init{
        setQR()
    }

    private fun setQR() {
        myQR = qrUseCases.generateQR(getEmail())
    }

    fun getQR()=myQR

    //Should be from SharedPreferences (or UseCases)
    private fun getEmail()="aaaa@gmail.com"
}