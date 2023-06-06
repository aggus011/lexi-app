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
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val loginUpUseCases: LoginUseCases,
    private val profileUseCases: ProfileUseCases
): ViewModel() {

    private var _profileLiveData = MutableLiveData<User?>()
    val profileLiveData: LiveData<User?> = _profileLiveData

    private val _calendarBirthDate = MutableLiveData<Calendar?>()

    init {
        getProfile()
        _calendarBirthDate.value=null
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
        loginUpUseCases.logOut()
    }
}