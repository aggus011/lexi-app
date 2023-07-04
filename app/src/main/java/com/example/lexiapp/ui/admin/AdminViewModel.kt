package com.example.lexiapp.ui.admin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lexiapp.domain.model.ProfessionalValidation
import com.example.lexiapp.domain.useCases.AdminUseCases
import com.example.lexiapp.domain.useCases.ProfileUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val profileUseCases: ProfileUseCases,
    private val adminUseCases: AdminUseCases
): ViewModel() {

    private val _professionals = MutableLiveData<List<ProfessionalValidation>>()
    val professionals = _professionals as LiveData<List<ProfessionalValidation>>

    init {
        getProfessionals()
    }

    fun setApproval (emailProfessional: String, approval: Boolean) {
        viewModelScope.launch {
            adminUseCases.saveApprovalToProfessional(emailProfessional, approval)
        }
    }

    fun closeSession(){
        profileUseCases.closeSesion()
    }

    private fun getProfessionals() {
        viewModelScope.launch {
            adminUseCases.getRegisteredProfessionals().collect{
                _professionals.value = it
            }
        }
    }

}