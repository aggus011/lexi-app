package com.example.lexiapp.ui.profesionalhome

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lexiapp.domain.model.FirebaseResult
import com.example.lexiapp.domain.model.User
import com.example.lexiapp.domain.model.gameResult.CorrectWordGameResult
import com.example.lexiapp.domain.model.gameResult.WhereIsTheLetterResult
import com.example.lexiapp.domain.useCases.CodeQRUseCases
import com.example.lexiapp.domain.useCases.LinkUseCases
import com.example.lexiapp.domain.useCases.ResultGamesUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class ProfesionalHomeViewModel @Inject constructor(
    private val codeQRUseCases: CodeQRUseCases,
    private val linkUseCases: LinkUseCases,
    private val resultGamesUseCases: ResultGamesUseCases
) : ViewModel() {

    private var _avgCW = MutableLiveData<String>()
    val avgCW = _avgCW as LiveData<String>
    private var _hardLettersCW = MutableLiveData<List<String>>()
    val hardLettersCW = _hardLettersCW as LiveData<List<String>>
    private var _countWordsPlayCW = MutableLiveData<Int>()
    val countWordsPlayCW = _countWordsPlayCW as LiveData<Int>
    private var _avgWITL = MutableLiveData<String>()
    val avgWITL = _avgWITL as LiveData<String>
    private var _hardLettersWITL = MutableLiveData<List<Char>>()
    val hardLettersWITL = _hardLettersWITL as LiveData<List<Char>>
    private var _countWordsPlayWITL = MutableLiveData<Int>()
    val countWordsPlayWITL = _countWordsPlayWITL as LiveData<Int>
    private var _listPatient = MutableLiveData<List<User>>()
    val listPatient: LiveData<List<User>> = _listPatient
    private var _listFilterPatient = MutableLiveData<List<User>>()
    val listFilterPatient: LiveData<List<User>> = _listFilterPatient
    private var _patientSelected = MutableLiveData<User?>()
    val patientSelected: LiveData<User?> = _patientSelected
    private var _resultAddPatient = MutableLiveData<FirebaseResult>()
    val resultAddPatient: LiveData<FirebaseResult> = _resultAddPatient
    private var _resultDeletePatient = MutableLiveData<FirebaseResult>()
    val resultDeletePatient: LiveData<FirebaseResult> = _resultDeletePatient

    init {
        listenerOfPatients()
    }

    fun listenerOfPatients() {
        viewModelScope.launch {
            linkUseCases.getListLinkPatientOfProfessional().collect { list ->
                patients(list).collect{ patients ->
                    _listPatient.value = patients
                    _listFilterPatient.value = _listPatient.value
                }
            }
        }
    }

    suspend fun patients(emails: List<String>) = flow{
            val patients = mutableListOf<User>()
            emails.forEach { email ->
                patients.add(linkUseCases.getUser(email))
            }
            emit (patients)
        }

    fun getScanOptions() = codeQRUseCases.getScanOptions()

    fun filter(patientSearch: String?) {
        val filteredList = mutableListOf<User>()
        if (patientSearch != null) {
            _listPatient.value?.forEach {
                if (it.userName != null &&
                    (it.userName!!.contains(patientSearch) || it.email.contains(patientSearch))
                )
                    filteredList.add(it)
            }
        }
        _listFilterPatient.value = filteredList
        filteredList.forEach { Log.v("VALIDATE_FILTER_USERS", "${it.userName}//${it.email}") }
    }

    fun getPatientEmail(contents: String?): String? {
        if (contents == null) return null
        return codeQRUseCases.getEmailFromQR(contents)
    }

    fun setPatientSelected(patient: User) {
        _patientSelected.value = patient
        viewModelScope.launch {
            this.launch {
                setWITLStats(patient)
            }
            this.launch {
                setCWStats(patient)
            }
        }
    }

    private suspend fun setCWStats(patient: User) {
        resultGamesUseCases.getWhereIsCWResults(patient.email).collect {
            Log.d("CW Result", it.toString())
            if (it.isNotEmpty()) {
                _countWordsPlayCW.value = it.size
                setHardWordsCW(it)
                setErrorAvgCW(it)
            }else {
                setBlankResultsCW()
            }
        }
    }

    private fun setErrorAvgCW(results: List<CorrectWordGameResult>) {
        val subList = results.filter { !it.success }
        _avgCW.value =
            ((subList.size.toDouble() / results.size * 10000.0).roundToInt() / 100.0).toString()
    }

    private fun setBlankResultsCW() {
        _hardLettersCW.value = emptyList()
        _countWordsPlayCW.value = 0
        _avgCW.value = "0"
    }

    private fun setHardWordsCW(results: List<CorrectWordGameResult>) {
        val list = mutableMapOf<String, Int>()
        val filteredList = results.filter { !it.success }
        for (result in filteredList) {
            if (!list.contains(result.correctWord)) {
                list[result.correctWord] = 1
            } else {
                list[result.correctWord]!!.plus(1)
            }
        }
        val maxCount = list.values.toSet().max()
        val wordList = mutableListOf<String>()
        for (word in list.keys) {
            if (list[word] == maxCount) {
                wordList.add(word)
            }
        }
        _hardLettersCW.value = wordList
    }

    private suspend fun setWITLStats(patient: User) {
        resultGamesUseCases.getWhereIsTheLetterResults(patient.email).collect {
            if(it.isNotEmpty()){
                _countWordsPlayWITL.value = it.size
                setHardLetters(it)
                setErrorAvg(it)
            } else {
                setBlankResults()
            }
        }
    }

    private fun setBlankResults() {
        _avgWITL.value = "0"
        _countWordsPlayWITL.value = 0
        _hardLettersWITL.value = emptyList()
    }

    private fun setErrorAvg(results: List<WhereIsTheLetterResult>) {
        val subList = results.filter { !it.success }
        _avgWITL.value =
            ((subList.size.toDouble() / results.size * 10000.0).roundToInt() / 100.0).toString()
    }

    private fun setHardLetters(results: List<WhereIsTheLetterResult>) {
        val list = mutableMapOf<Char, Int>()
        val filteredList = results.filter { !it.success }
        for (result in filteredList) {
            if (!list.contains(result.mainLetter)) {
                list[result.mainLetter] = 1
            } else {
                list[result.mainLetter]!!.plus(1)
            }
        }
        val maxCount = list.values.toSet().max()
        val letterList = mutableListOf<Char>()
        for (letter in list.keys) {
            if (list[letter] == maxCount) {
                letterList.add(letter)
            }
        }
        _hardLettersWITL.value = letterList
    }

    fun cleanPatient() {
        _patientSelected.value = null
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
