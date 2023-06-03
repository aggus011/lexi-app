package com.example.lexiapp.ui.profesionalhome

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lexiapp.data.repository.patient.PatientMocks
import com.example.lexiapp.domain.model.Patient
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfesionalHomeViewModel @Inject constructor(): ViewModel() {
    private val _listPatient = MutableLiveData<List<Patient>>()
    val listPatient: LiveData<List<Patient>> = _listPatient
    private var _listFilterPatient= MutableLiveData<List<Patient>>()
    val listFilterPatient: LiveData<List<Patient>> = _listFilterPatient
    private val _patientSelected = MutableLiveData<Patient>()
    val patientSelected: LiveData<Patient> = _patientSelected

    init {
        _listPatient.value = emptyList()
        _listPatient.value = getText()
        _listFilterPatient.value = _listPatient.value
    }

    private fun getText() = PatientMocks.getPatientMocks()

    fun filter(patientSearch: String?){
        val filteredList = mutableListOf<Patient>()
        if(patientSearch!=null){
            _listPatient.value?.forEach {
                val info = it.name!=null && it.email!=null
                if (info && (it.name!!.contains(patientSearch) || it.email!!.contains(patientSearch)))
                    filteredList.add(it)
            }
        }
        _listFilterPatient.value = filteredList
    }
}
