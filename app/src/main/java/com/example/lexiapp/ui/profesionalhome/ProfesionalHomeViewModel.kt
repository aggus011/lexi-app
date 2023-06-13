package com.example.lexiapp.ui.profesionalhome

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lexiapp.data.repository.patient.PatientMocks
import com.example.lexiapp.domain.model.Patient
import com.example.lexiapp.domain.useCases.CodeQRUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfesionalHomeViewModel @Inject constructor(
    private val codeQRUseCases: CodeQRUseCases
): ViewModel() {

    private val _listPatient = MutableLiveData<List<Patient>>()
    val listPatient: LiveData<List<Patient>> = _listPatient
    private var _listFilterPatient= MutableLiveData<List<Patient>>()
    val listFilterPatient: LiveData<List<Patient>> = _listFilterPatient
    private val _patientSelected = MutableLiveData<Patient?>()
    val patientSelected: LiveData<Patient?> = _patientSelected

    var validater = false

    init {
        _listPatient.value = emptyList()
        _listPatient.value = getPatient()
        _listFilterPatient.value = _listPatient.value
    }

    private fun getPatient() = PatientMocks.getPatientMocks()

    fun getScanOptions() = codeQRUseCases.getScanOptions()

    fun filter(patientSearch: String?){
        val filteredList = mutableListOf<Patient>()
        if(patientSearch!=null){
            _listPatient.value?.forEach {
                if (it.user.userName!=null &&
                    (it.user.userName!!.contains(patientSearch) || it.user.email.contains(patientSearch)))
                    filteredList.add(it)
            }
        }
        _listFilterPatient.value = filteredList
    }

    fun getPatientEmail(contents: String?): String?{
        if (contents==null) return null
        return codeQRUseCases.getEmailFromQR(contents)
    }

    fun setPatientSelected(patient: Patient){
        _patientSelected.value=patient
    }

    fun cleanPatient(){
        _patientSelected.value=null
    }

    fun unbindPatient(email: String): Boolean {
        /*val isUnbindSuccess = linkUseCase.unbindPatient(email)
        if (isUnbinndSuccess) {
            updateListRecycler(email)
            return FirebaseResult.TaskSuccess
        }else{
            return FirebaseResult.TaskFailure
        }*/
        validater = !validater
        updateListRecycler(email)
        return validater
    }

    private fun updateListRecycler(email: String) {
        cleanFilerList(email)
        cleanCompleteList(email)
    }

    private fun cleanCompleteList(email: String) {
        val helpList = mutableListOf<Patient>()
        _listPatient.value?.forEach { patient->
            if(patient.user.email!=email) helpList.add(patient)
        }
        _listPatient.value= helpList
    }

    private fun cleanFilerList(email: String) {
        val helpList = mutableListOf<Patient>()
        _listFilterPatient.value?.forEach { patient->
            if(patient.user.email!=email) helpList.add(patient)
        }
        _listFilterPatient.value= helpList    }
}
