package com.example.lexiapp.ui.games.letsread

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.lexiapp.domain.model.TextToRead
import com.example.lexiapp.domain.useCases.ChallengeReadingUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ListTextViewModel @Inject constructor(
    private val challengeReadingUseCases: ChallengeReadingUseCases
) : ViewModel(){
    private var _challengeReading = MutableStateFlow<TextToRead?>(null)
    var challengeReading: LiveData<TextToRead?> = _challengeReading.asLiveData()
    var isPossibleGenerateChallengeReading = MutableLiveData(true)

    fun generateChallengeReading() {
        viewModelScope.launch(Dispatchers.IO) {
            challengeReadingUseCases.getLastUseOpenAICompletionDate().collect { isPossible ->
                if (isPossible) {
                    challengeReadingUseCases.getChallengeReading()
                        .collect { challengeReading ->
                            withContext(Dispatchers.Main) {
                                if(challengeReading.text!!.isNotEmpty()){
                                    _challengeReading.value = challengeReading
                                    challengeReadingUseCases.updateOpenAiCompletionsLastUse()
                                }else{
                                    setIsNotPossibleGenerateChallengeReading()
                                }
                            }
                        }
                }else{
                    withContext(Dispatchers.Main){
                        setIsNotPossibleGenerateChallengeReading()
                    }
                }
            }
        }
    }

    private fun setIsNotPossibleGenerateChallengeReading(){
        isPossibleGenerateChallengeReading.value = false
    }
}