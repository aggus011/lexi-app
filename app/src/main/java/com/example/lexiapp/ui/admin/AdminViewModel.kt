package com.example.lexiapp.ui.admin

import androidx.lifecycle.ViewModel
import com.example.lexiapp.domain.useCases.ProfileUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val profileUseCases: ProfileUseCases
): ViewModel() {

    fun closeSession(){
        profileUseCases.closeSesion()
    }

}