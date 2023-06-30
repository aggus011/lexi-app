package com.example.lexiapp.ui.textscanner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lexiapp.domain.useCases.TextScannerUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TextScannerViewModel @Inject constructor(
    private val scanUseCases: TextScannerUseCases
): ViewModel() {

    fun saveTSResult() {
        viewModelScope.launch {
            scanUseCases.saveTSResult()
        }
    }

    fun checkIfObjectiveHasBeenCompleted(game: String, type: String, gameName: String) {
        viewModelScope.launch {
            scanUseCases.generateNotificationForObjectives(game, type, gameName)
        }
    }

}