package com.example.lexiapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lexiapp.domain.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class LetterGameViewModel(repository: Repository) : ViewModel() {

    private var _words = MutableStateFlow<List<String>>(listOf())
    val words = _words.asStateFlow()
    private var basicWord: String? = null

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getWordToLetterGame()
                .collect {
                    basicWord = it
                }
            generateWordVariants()
        }
    }

    private fun generateWordVariants() {
        TODO()
    }


}