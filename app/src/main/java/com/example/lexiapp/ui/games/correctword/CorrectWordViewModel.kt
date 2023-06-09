package com.example.lexiapp.ui.games.correctword

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.lexiapp.domain.useCases.CorrectWordUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CorrectWordViewModel @Inject constructor(
    private val correctWordUseCases: CorrectWordUseCases
) : ViewModel() {

    init {
        generateWords()
    }


    private var _basicWords = MutableStateFlow(emptyList<String>())
    var basicWords = _basicWords.asLiveData()

    fun generateWords() {
        viewModelScope.launch(Dispatchers.IO) {
            correctWordUseCases.getWords()
                .collect {
                    Log.v("data_in_view_model", "response word: $it")
                    _basicWords.value = it
                    Log.v(
                        "asignate_data_to_variable",
                        "response _basicWord: ${_basicWords.value} and basicWord: ${basicWords.value}"
                    )
                }
        }
    }

}