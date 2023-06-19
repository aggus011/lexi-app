package com.example.lexiapp.ui.games.letsread

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lexiapp.data.api.difference_text.model.Rows
import com.example.lexiapp.domain.useCases.DifferenceUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Error
import java.lang.NullPointerException
import javax.inject.Inject

@HiltViewModel
class DifferenceViewModel @Inject constructor(
    private val differenceUseCases: DifferenceUseCases
) : ViewModel() {

    val difference: MutableLiveData<Rows> = MutableLiveData()
    private lateinit var original: String
    private val wrongWords = mutableListOf<String>()

    fun getDifference(originalText: String, results: String) =
        CoroutineScope(Dispatchers.IO).launch {
            try {
                original = originalText
                val result = differenceUseCases.getDifference(originalText, results)
                if (result.body() != null)
                    difference.postValue(result.body()!!)
            } catch (e: NullPointerException) {
                Log.e(SpeechToTextViewModel.TAG, "Call is null $e")
            } catch (e: Error) {
                Log.e(SpeechToTextViewModel.TAG, "Error! $e")
            }
        }

    fun convertToText(): String = getStringBuilder(original)

    private fun getStringBuilder(originalText: String): String {
        val diffBuilder = StringBuilder()
        var errors = 0
        for (f in difference.value!!.rows[0].right.chunks) {
            when (f.type) {
                "equal" -> diffBuilder.append(f.value)
                "insert" -> {
                    wrongWords.add(f.value)
                    diffBuilder.append("<font color='#FF0000'>${f.value}</font>")
                    errors += countErrors(f.value)
                }
                "removed" -> {
                    wrongWords.add(f.value)
                    diffBuilder.append("<font color='#FF0000'>${f.value}</font>")
                    errors += countErrors(f.value)
                }
                else -> break
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            differenceUseCases.saveWrongWords(wrongWords.toList())
        }
        val total = wordCounter(originalText)
        val average = errors * 100 / total
        return if (average >= 10)
            diffBuilder.toString()
        else "Correct"
    }

    private fun countErrors(errors: String): Int {
        val amount = errors.split(" ")
        return amount.size
    }

    private fun wordCounter(text: String): Int {
        val words = text.trim().split("\\s+".toRegex())
        return words.size
    }

}