package com.example.lexiapp.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lexiapp.domain.model.User
import com.example.lexiapp.domain.useCases.LoginUseCases
import com.example.lexiapp.domain.useCases.ProfileUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val loginUpUseCases: LoginUseCases,
    private val profileUseCases: ProfileUseCases
): ViewModel() {

    private val _profileLiveData = MutableLiveData<User>()
    val profileLiveData: LiveData<User> = _profileLiveData

    fun getProfile(){
        viewModelScope.launch {
            delay(3000)
            profileUseCases.getProfile().let{_profileLiveData.value = it }
            Log.v("USER_NAME_FIRESTORE_FRAGMENT", "${profileLiveData.value?.userName}")
        }
    }

    fun closeSesion(){
        profileUseCases.closeSesion()
        loginUpUseCases.logOut()
    }
}