package com.example.lexiapp.ui.profile

import androidx.lifecycle.ViewModel
import com.example.lexiapp.domain.useCases.LoginUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val loginUseCases: LoginUseCases): ViewModel() {
}