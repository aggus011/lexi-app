package com.example.lexiapp.ui.profesionalhome

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lexiapp.domain.model.FirebaseResult
import com.example.lexiapp.domain.model.User
import com.example.lexiapp.domain.model.gameResult.CorrectWordGameResult
import com.example.lexiapp.domain.model.gameResult.LetsReadGameResult
import com.example.lexiapp.domain.model.gameResult.ResultGame
import com.example.lexiapp.domain.model.gameResult.WhereIsTheLetterResult
import com.example.lexiapp.domain.useCases.CodeQRUseCases
import com.example.lexiapp.domain.useCases.LinkUseCases
import com.example.lexiapp.domain.useCases.ResultGamesUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfesionalHomeViewModel @Inject constructor(
    private val codeQRUseCases: CodeQRUseCases,
    private val linkUseCases: LinkUseCases
) : ViewModel() {

    private var _listPatient = MutableLiveData<List<User>>()
    val listPatient: LiveData<List<User>> = _listPatient
    private var _listFilterPatient = MutableLiveData<List<User>>()
    val listFilterPatient: LiveData<List<User>> = _listFilterPatient
    private var _resultAddPatient = MutableLiveData<FirebaseResult>()
    val resultAddPatient: LiveData<FirebaseResult> = _resultAddPatient
    private var _resultDeletePatient = MutableLiveData<FirebaseResult>()
    val resultDeletePatient: LiveData<FirebaseResult> = _resultDeletePatient

    init {
        listenerOfPatients()
    }

    private fun listenerOfPatients() {
        viewModelScope.launch {
            linkUseCases.getListLinkPatientOfProfessional().collect { list ->
                patients(list).collect { patients ->
                    _listPatient.value = patients
                    _listFilterPatient.value = _listPatient.value
                }
            }
        }
    }

    suspend fun patients(emails: List<String>) = flow {
        val patients = mutableListOf<User>()
        emails.forEach { email ->
            patients.add(linkUseCases.getUser(email))
        }
        emit(patients)
    }

    fun getScanOptions() = codeQRUseCases.getScanOptions()

    fun filter(patientSearch: String?) {
        val filteredList = mutableListOf<User>()
        if (patientSearch != null) {
            _listPatient.value?.forEach {
                if (it.userName!!.contains(patientSearch) || it.email.contains(patientSearch))
                    filteredList.add(it)
            }
        }
        _listFilterPatient.value = filteredList
    }

    fun getPatientEmail(contents: String?): String? {
        if (contents == null) return null
        return codeQRUseCases.getEmailFromQR(contents)
    }

    fun unbindPatient(emailPatient: String) {
        viewModelScope.launch {
            linkUseCases.deletePatientFromProfessional(emailPatient).collect {
                _resultDeletePatient.value = it
                linkUseCases.unBindProfessionalFromPatient(emailPatient).collect {}
            }
        }
    }

    fun addPatientToProfessional(emailPatient: String) {
        viewModelScope.launch {
            try {
                linkUseCases.addPatientToProfessional(emailPatient).collect {
                    _resultAddPatient.value = it
                    linkUseCases.bindProfessionalToPatient(emailPatient).collect {}
                }
            } catch (e: Exception) {
            }
        }
    }

}
