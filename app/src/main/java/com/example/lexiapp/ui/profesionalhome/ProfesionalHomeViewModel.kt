package com.example.lexiapp.ui.profesionalhome

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lexiapp.domain.model.FirebaseResult
import com.example.lexiapp.domain.model.User
import com.example.lexiapp.domain.model.gameResult.CorrectWordGameResult
import com.example.lexiapp.domain.model.gameResult.ResultGame
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

    private var _wasNotPlayedCW = MutableLiveData<Boolean>()
    val wasNotPlayedCW = _wasNotPlayedCW as LiveData<Boolean>
    private var _wasNotPlayedWITL = MutableLiveData<Boolean>()
    val wasNotPlayedWITL = _wasNotPlayedWITL as LiveData<Boolean>

    private var _hardWordsCW = MutableLiveData<List<String>>()
    val hardWordsCW = _hardWordsCW as LiveData<List<String>>
    private var _hardLettersWITL = MutableLiveData<List<Char>>()
    val hardLettersWITL = _hardLettersWITL as LiveData<List<Char>>

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

    private var _resultSLastWeekWITL = MutableLiveData<Map<String, Triple<Int, Float, Int>>>()
    val resultSLastWeekWITL: LiveData<Map<String, Triple<Int, Float, Int>>> = _resultSLastWeekWITL
    private var _resultSLastWeekCW = MutableLiveData<Map<String, Triple<Int, Float, Int>>>()
    val resultSLastWeekCW: LiveData<Map<String, Triple<Int, Float, Int>>> = _resultSLastWeekCW

    private var _totalPieCW = MutableLiveData<Pair<Float, Float>>()
    val totalPieCW: LiveData<Pair<Float, Float>> = _totalPieCW
    private var _totalPieWITL = MutableLiveData<Pair<Float, Float>>()
    val totalPieWITL: LiveData<Pair<Float, Float>> = _totalPieWITL
    private var _weekPieCW = MutableLiveData<Pair<Float, Float>>()
    val weekPieCW: LiveData<Pair<Float, Float>> = _weekPieCW
    private var _weekPieWITL = MutableLiveData<Pair<Float, Float>>()
    val weekPieWITL: LiveData<Pair<Float, Float>> = _weekPieWITL

    init {
        _wasNotPlayedWITL.value = false
        _wasNotPlayedCW.value = false
        listenerOfPatients()
    }

    fun listenerOfPatients() {
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
            _wasNotPlayedCW.value = if (it.isNotEmpty()) {
                setHardWordsCW(it)
                setResultsLastWeekCW(it)
                setDataPiesCW(it)
                true
            } else {
                false
            }
        }
    }

    private fun setDataPiesCW(results: List<CorrectWordGameResult>) {
        _totalPieCW.value = setTotalPie(results)
        _weekPieCW.value = setWeekPie(results)
    }

    private fun setResultsLastWeekCW(results: List<CorrectWordGameResult>) {
        _resultSLastWeekCW.value = resultGamesUseCases.getResultsLastWeek(results)
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
        var maxCount: Int? = null
        try {
            maxCount = list.values.toSet().max()
        } catch (e: NoSuchElementException) {
        }
        val wordList = mutableListOf<String>()
        if (maxCount != null) {
            for (word in list.keys) {
                if (list[word] == maxCount) {
                    wordList.add(word)
                }
            }
        }
        _hardWordsCW.value = wordList
    }

    private suspend fun setWITLStats(patient: User) {
        resultGamesUseCases.getWhereIsTheLetterResults(patient.email).collect {
            _wasNotPlayedWITL.value = if (it.isNotEmpty()) {
                setHardLetters(it)
                setResultsLastWeekWITL(it)
                setDataPiesWITL(it)
                true
            } else {
                false
            }
        }
    }

    private fun setDataPiesWITL(results: List<WhereIsTheLetterResult>) {
        _totalPieWITL.value = setTotalPie(results)
        _weekPieWITL.value = setWeekPie(results)
    }

    private fun setResultsLastWeekWITL(results: List<WhereIsTheLetterResult>) {
        _resultSLastWeekWITL.value = resultGamesUseCases.getResultsLastWeek(results)
    }

    private fun setBlankResults() {
        _hardLettersWITL.value = emptyList()
        _resultSLastWeekWITL.value = emptyMap()
    }

    private fun getCountError(results: List<ResultGame>): Float {
        val subList = results.filter { !it.success }
        return subList.size.toFloat()
    }

    private fun setTotalPie(results: List<ResultGame>): Pair<Float, Float> {
        val countError = getCountError(results)
        val countCorrect = (results.size - countError)
        return Pair(countCorrect, countError)
    }

    private fun setWeekPie(results: List<ResultGame>): Pair<Float, Float> {
        val weekList = resultGamesUseCases.filterResultsByWeek(results)
        val countError = getCountError(weekList)
        val countCorrect = (weekList.size - countError)
        return Pair(countCorrect, countError)
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
        var maxCount: Int? = null
        try {
            maxCount = list.values.toSet().max()
        } catch (e: NoSuchElementException) {
        }
        val letterList = mutableListOf<Char>()
        if (maxCount != null) {
            for (letter in list.keys) {
                if (list[letter] == maxCount) {
                    letterList.add(letter)
                }
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
