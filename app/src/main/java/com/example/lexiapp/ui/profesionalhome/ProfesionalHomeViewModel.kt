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

    private var _resultSLastWeekWITL = MutableLiveData<Map<String, Triple<Int, Float, Int>>>()
    val resultSLastWeekWITL: LiveData<Map<String,Triple<Int, Float, Int>>> = _resultSLastWeekWITL
    private var _resultSLastWeekCW = MutableLiveData<Map<String, Triple<Int, Float, Int>>>()
    val resultSLastWeekCW: LiveData<Map<String,Triple<Int, Float, Int>>> = _resultSLastWeekCW

    private var _totalPieCW = MutableLiveData<Pair<Float, Float>>()
    val totalPieCW : LiveData<Pair<Float, Float>> = _totalPieCW
    private var _totalPieWITL = MutableLiveData<Pair<Float, Float>>()
    val totalPieWITL : LiveData<Pair<Float, Float>> = _totalPieWITL
    private var _weekPieCW = MutableLiveData<Pair<Float, Float>>()
    val weekPieCW : LiveData<Pair<Float, Float>> = _weekPieCW
    private var _weekPieWITL = MutableLiveData<Pair<Float, Float>>()
    val weekPieWITL : LiveData<Pair<Float, Float>> = _weekPieWITL

    init {
        getPatient()
    }

    fun arePiesCWEquals()=_totalPieCW.value==_weekPieCW.value

    fun arePiesWITLEquals()=_totalPieWITL.value==_weekPieWITL.value

    fun getPatient() {
        viewModelScope.launch {
            linkUseCases.getListLinkPatientOfProfessional { list ->
                val helpList = users(list)
                _listPatient.value = helpList
                _listFilterPatient.value = _listPatient.value
            }
        }
    }

    private fun users(list: List<String>?): List<User> {
        val helpList = mutableListOf<User>()
        viewModelScope.launch {
            list?.forEach {
                val patient = linkUseCases.getUser(it)
                Log.v("VALIDATE_FILTER_USERS", "${patient.userName}//${patient.email}")
                helpList.add(patient)
            }
        }
        return helpList
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
                setHardWordsCW(it)
                setResultsLastWeekCW(it)
                setDataPiesCW(it)
            }else {
                setBlankResultsCW()
            }
        }
    }

    private fun setDataPiesCW(results: List<CorrectWordGameResult>){
        _totalPieCW.value = Pair(results.size.toFloat(),getCountError(results))
        val weekList=resultGamesUseCases.filterResultsByWeek(results)
        _weekPieCW.value = Pair(weekList.size.toFloat(), getCountError(weekList))
    }

    private fun setResultsLastWeekCW(results: List<CorrectWordGameResult>) {
        _resultSLastWeekCW.value=resultGamesUseCases.getResultsLastWeek(results)
    }

    private fun setErrorAvgCW(results: List<CorrectWordGameResult>){
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
                setResultsLastWeekWITL(it)
                setDataPiesWITL(it)
            } else {
                setBlankResults()
            }
        }
    }

    private fun setDataPiesWITL(results: List<WhereIsTheLetterResult>){
        _totalPieWITL.value = Pair(results.size.toFloat(),getCountError(results))
        val weekList=resultGamesUseCases.filterResultsByWeek(results)
        _weekPieWITL.value = Pair(weekList.size.toFloat(), getCountError(weekList))
    }

    private fun setResultsLastWeekWITL(results: List<WhereIsTheLetterResult>) {
        _resultSLastWeekWITL.value=resultGamesUseCases.getResultsLastWeek(results)
    }

    private fun setBlankResults() {
        _avgWITL.value = "0"
        _countWordsPlayWITL.value = 0
        _hardLettersWITL.value = emptyList()
    }

    private fun getCountError(results: List<ResultGame>): Float{
        val subList = results.filter { !it.success }
        return subList.size.toFloat()
    }

    private fun setErrorAvgWITL(results: List<WhereIsTheLetterResult>) {
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
