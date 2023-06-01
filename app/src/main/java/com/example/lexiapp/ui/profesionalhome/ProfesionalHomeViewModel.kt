package com.example.lexiapp.ui.profesionalhome

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lexiapp.domain.model.Patient
import com.example.lexiapp.domain.model.TextToRead
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfesionalHomeViewModel @Inject constructor(): ViewModel() {
    private val _listPatient = MutableLiveData<List<Patient>>()
    val listPatient: LiveData<List<Patient>> = _listPatient
    private val _patientSelected = MutableLiveData<Patient>()
    val patientSelected: LiveData<Patient> = _patientSelected

    init {
        _listPatient.value = emptyList()
        _listPatient.value = getText()
    }

    private fun getText() = listOf<Patient>()
}
