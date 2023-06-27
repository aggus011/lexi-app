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
    private val linkUseCases: LinkUseCases,
    private val resultGamesUseCases: ResultGamesUseCases
) : ViewModel() {

    private var _wasNotPlayedCW = MutableLiveData<Boolean>()
    val wasNotPlayedCW = _wasNotPlayedCW as LiveData<Boolean>
    private var _wasNotPlayedWITL = MutableLiveData<Boolean>()
    val wasNotPlayedWITL = _wasNotPlayedWITL as LiveData<Boolean>

    private var _errorWordsLR = MutableLiveData<List<String>>()
    val errorWordsLR = _errorWordsLR as LiveData<List<String>>

    private var _totalTimesPlayedLR = MutableLiveData<Int>()
    val totalTimesPlayedLR = _totalTimesPlayedLR as LiveData<Int>

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

    private var _resultsLastWeekLR = MutableLiveData<Map<String, Triple<Int, Float, Int>>>()
    val resultsLastWeekLR: LiveData<Map<String, Triple<Int, Float, Int>>> = _resultsLastWeekLR
    private var _resultsLastWeekWITL = MutableLiveData<Map<String, Triple<Int, Float, Int>>>()
    val resultsLastWeekWITL: LiveData<Map<String, Triple<Int, Float, Int>>> = _resultsLastWeekWITL
    private var _resultsLastWeekCW = MutableLiveData<Map<String, Triple<Int, Float, Int>>>()
    val resultsLastWeekCW: LiveData<Map<String, Triple<Int, Float, Int>>> = _resultsLastWeekCW

    private var _totalPieLR = MutableLiveData<Pair<Float, Float>>()
    val totalPieLR: LiveData<Pair<Float, Float>> = _totalPieLR
    private var _totalPieCW = MutableLiveData<Pair<Float, Float>>()
    val totalPieCW: LiveData<Pair<Float, Float>> = _totalPieCW
    private var _totalPieWITL = MutableLiveData<Pair<Float, Float>>()
    val totalPieWITL: LiveData<Pair<Float, Float>> = _totalPieWITL

    private var _weekPieLR = MutableLiveData<Pair<Float, Float>>()
    val weekPieLR: LiveData<Pair<Float, Float>> = _weekPieLR
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
            this.launch {
                setLRStats(patient)
            }
        }
    }

    private suspend fun setLRStats(patient: User) {
        resultGamesUseCases.getLRResults(patient.email).collect {
            _totalTimesPlayedLR.value = if (it.isNotEmpty()) {
                setWrongWordsLR(it)
                setLastResultsLR(it)
                setDataPiesLR(it)
                it.size
            } else {
                0
            }
        }
    }

    private fun setLastResultsLR(results: List<LetsReadGameResult>) {
        _resultsLastWeekLR.value = resultGamesUseCases.getResultsLastWeek(results)
    }

    private fun setWrongWordsLR(results: List<LetsReadGameResult>) {
        val words = mutableListOf<String>()
        for (result in results) {
            words.addAll(result.wrongWords)
        }
        _errorWordsLR.value = words
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

    private fun setDataPiesLR(results: List<LetsReadGameResult>) {
        _totalPieLR.value = setTotalPie(results)
        _weekPieLR.value = setWeekPie(results)
    }

    private fun setResultsLastWeekCW(results: List<CorrectWordGameResult>) {
        _resultsLastWeekCW.value = resultGamesUseCases.getResultsLastWeek(results)
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
        _resultsLastWeekWITL.value = resultGamesUseCases.getResultsLastWeek(results)
    }

    private fun setBlankResults() {
        _hardLettersWITL.value = emptyList()
        _resultsLastWeekWITL.value = emptyMap()
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
