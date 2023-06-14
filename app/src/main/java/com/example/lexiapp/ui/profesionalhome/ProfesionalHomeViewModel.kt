package com.example.lexiapp.ui.profesionalhome

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lexiapp.data.repository.patient.PatientMocks
import com.example.lexiapp.domain.model.FirebaseResult
import com.example.lexiapp.domain.model.Patient
import com.example.lexiapp.domain.model.User
import com.example.lexiapp.domain.useCases.CodeQRUseCases
import com.example.lexiapp.domain.useCases.LinkUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfesionalHomeViewModel @Inject constructor(
    private val codeQRUseCases: CodeQRUseCases,
    private val linkUseCase: LinkUseCase
): ViewModel() {

    private val _listPatient = MutableLiveData<List<User>>()
    val listPatient: LiveData<List<User>> = _listPatient
    private var _listFilterPatient= MutableLiveData<List<User>>()
    val listFilterPatient: LiveData<List<User>> = _listFilterPatient
    private val _patientSelected = MutableLiveData<User?>()
    val patientSelected: LiveData<User?> = _patientSelected
    private val _resultAddPatient = MutableLiveData<FirebaseResult>()
    val resultAddPatient: LiveData<FirebaseResult> = _resultAddPatient
    private val _resultDeletePatient = MutableLiveData<FirebaseResult>()
    val resultDeletePatient: LiveData<FirebaseResult> = _resultDeletePatient

    init {
        _listPatient.value = emptyList()
        getPatient()
    }

    private fun getPatient(){
        viewModelScope.launch {
            linkUseCase.getListLinkPatientOfProfessional().collect{ list->
                _listPatient.value=list
                _listFilterPatient.value = _listPatient.value
            }
        }
    }

    fun getScanOptions() = codeQRUseCases.getScanOptions()

    fun filter(patientSearch: String?){
        val filteredList = mutableListOf<User>()
        if(patientSearch!=null){
            _listPatient.value?.forEach {
                if (it.userName!=null &&
                    (it.userName!!.contains(patientSearch) || it.email.contains(patientSearch)))
                    filteredList.add(it)
            }
        }
        _listFilterPatient.value = filteredList
        filteredList.forEach {  Log.v("VALIDATE_FILTER_USERS", "${it.userName}//${it.email}") }
    }

    fun getPatientEmail(contents: String?): String?{
        if (contents==null) return null
        return codeQRUseCases.getEmailFromQR(contents)
    }

    fun setPatientSelected(patient: User){
        _patientSelected.value=patient
    }

    fun cleanPatient(){
        _patientSelected.value=null
    }

    fun unbindPatient(emailPatient: String){
        viewModelScope.launch {
            linkUseCase.deletePatientFromProfessional(emailPatient).collect{
                _listPatient.value=it
                //_listFilterPatient.value = _listPatient.value
                linkUseCase.unBindProfessionalFromPatient(emailPatient).collect{}
            }
        }
    }

    fun addPatientToProfessional(emailPatient: String) {
        viewModelScope.launch {
            try {
                val result = linkUseCase.addPatientToProfessional(emailPatient)
                _listPatient.value=result
                //_listFilterPatient.value = _listPatient.value
                linkUseCase.bindProfessionalToPatient(emailPatient).collect{}
            } catch (e: Exception) {}
        }
    }

    private fun updateListRecycler(email: String) {
        cleanFilerList(email)
        cleanCompleteList(email)
    }

    private suspend fun addToCompleteList(email: String) {
        val helpList = mutableListOf<User>()
        helpList.addAll(_listPatient.value!!)
        helpList.add(linkUseCase.getUser(email))
        _listPatient.value= helpList
    }

    private fun cleanCompleteList(email: String) {
        val helpList = mutableListOf<User>()
        _listPatient.value?.forEach { user->
            if(user.email!=email) helpList.add(user)
        }
        _listPatient.value= helpList
    }

    private fun cleanFilerList(email: String) {
        val helpList = mutableListOf<User>()
        _listFilterPatient.value?.forEach { user->
            if(user.email!=email) helpList.add(user)
        }
        _listFilterPatient.value= helpList
    }
}
