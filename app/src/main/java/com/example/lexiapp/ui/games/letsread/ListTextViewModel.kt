package com.example.lexiapp.ui.games.letsread

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.lexiapp.domain.model.TextToRead
import com.example.lexiapp.domain.useCases.CategoriesUseCases
import com.example.lexiapp.domain.useCases.ChallengeReadingUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ListTextViewModel @Inject constructor(
    private val challengeReadingUseCases: ChallengeReadingUseCases,
    private val categoriesUseCases: CategoriesUseCases
) : ViewModel(){
    private var _challengeReading = MutableStateFlow<TextToRead?>(null)
    var challengeReading: LiveData<TextToRead?> = _challengeReading.asLiveData()
    var isPossibleGenerateChallengeReading = MutableLiveData(true)

    private var _textToReadList = MutableLiveData<List<TextToRead>>()
    val textToReadList: LiveData<List<TextToRead>>
    get() = _textToReadList

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

    fun getTextsFromCategories(patientEmail: String){
        viewModelScope.launch(Dispatchers.IO) {
            val categories = categoriesUseCases.getCategoriesFromPatient(email = patientEmail)

            if(categories.isNotEmpty()){
                val texts = categoriesUseCases.getListTextToReadWithCategories(categories)

                withContext(Dispatchers.Main){
                    _textToReadList.value = texts
                }
            }
        }
    }

    private fun setIsNotPossibleGenerateChallengeReading(){
        isPossibleGenerateChallengeReading.value = false
    }
}