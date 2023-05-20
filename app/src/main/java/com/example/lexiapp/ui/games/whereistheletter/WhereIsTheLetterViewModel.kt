package com.example.lexiapp.ui.games.whereistheletter

import android.util.Log
import androidx.lifecycle.*
import com.example.lexiapp.domain.useCases.LetterGameUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WhereIsTheLetterViewModel @Inject constructor(
    private val letterGameUseCases: LetterGameUseCases
) : ViewModel() {

    private val _selectedPosition = MutableStateFlow<Int?>(null)
    val selectedPosition = _selectedPosition.asLiveData()

    private val _basicWord = MutableStateFlow<String?>(null)
    val basicWord = _basicWord.asLiveData()

    private val _correctPosition = MutableStateFlow(0)
    var correctPosition : LiveData<Int> = _correctPosition.asLiveData()

    private val _correctAnswerSubmitted = MutableStateFlow<Boolean>(false)
    var correctAnswerSubmitted: LiveData<Boolean> = _correctAnswerSubmitted.asLiveData()

    private var _incorrectAnswerSubmitted = MutableStateFlow<Boolean>(false)
    var incorrectAnswerSubmitted: LiveData<Boolean> = _incorrectAnswerSubmitted.asLiveData()


    fun onPositionSelected(position: Int) {
        _selectedPosition.value = position
    }

    fun onPositionDeselected() {
        _selectedPosition.value = null
    }

    fun isItSelected(position: Int)= _selectedPosition.value==position

    fun isAnyLetterSelected()= _selectedPosition.value!=null

    fun onOmitWord() {
        generateWord()
    }

    fun cleanBasicWord(){
        _basicWord.value=null
    }

    fun onSubmitAnswer() {
        if(selectedPosition.value == correctPosition.value) {
            _correctAnswerSubmitted.value = true
        } else {
            _incorrectAnswerSubmitted.value = true
        }
    }

    private fun selectLetter() {
        var position: Int
        do {
            position = (Math.random() * 10).toInt()
        } while (position !in (0..(_basicWord.value!!.length)))
        _correctPosition.value = position
    }

    fun getLetterWithPosition()= _selectedPosition.value?.let { _basicWord.value?.get(it) }

    private fun generateWord() {
        viewModelScope.launch(Dispatchers.IO) {
            letterGameUseCases.getWord()
                .collect {
                    Log.v("data_in_view_model", "response word: $it")
                    _basicWord.value =it
                    Log.v("asignate_data_to_variable",
                        "response _basicWord: ${_basicWord.value} and basicWord: ${basicWord.value}")
                }
            selectLetter()
        }
    }

    fun resetSubmit() {
        cleanBasicWord()
        _selectedPosition.value=null
        _correctAnswerSubmitted.value = false
        _incorrectAnswerSubmitted.value = false
    }

}