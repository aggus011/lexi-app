package com.example.lexiapp.domain.useCases

import com.example.lexiapp.domain.model.TextToRead
import com.example.lexiapp.domain.service.ChallengeReadingService
import com.example.lexiapp.domain.service.LetterService
import com.example.lexiapp.domain.service.OpenAICompletionsService
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ChallengeReadingUseCases @Inject constructor(
    private val repositoryImpl: OpenAICompletionsService,
    private val pendingReadingsRepositoryImpl: ChallengeReadingService,
    private val service: LetterService,
) {
    private val promptChallengeReading =
        "Necesito un texto de 150 caracteres para ser leído por niños que incluya las siguientes palabras:"
    private val openAICompletionsDocumentReference = "completions"

    suspend fun getChallengeReading() = flow {

        val challengeWords = mutableListOf("entrenamiento", "desafio", "juego")
        getChallengeWords().collect{
            if(it.isNotEmpty()){
                challengeWords.clear()
                challengeWords.addAll(it)
            }
        }

        repositoryImpl.getChallengeReading(promptChallengeReading, challengeWords)
            .collect{ challengeReading ->
                val text = removeLineBreaks(challengeReading)
                val textToRead = generateTextToRead(text)
                emit(textToRead)
            }
    }

    suspend fun getLastUseOpenAICompletionDate() = callbackFlow {
        pendingReadingsRepositoryImpl
            .getFirestoreOpenAICollectionDocumentReference(openAICompletionsDocumentReference)
            .collect{ docReference ->
                docReference.get()
                    .addOnSuccessListener { documentSnapshot ->
                        val lastUseDate = documentSnapshot.getTimestamp("last_use")
                        val hoursDifference = getHoursDifference(lastUseDate!!.toDate(), Date())

                        trySend(hoursDifference > 0).isSuccess
                        close()
                    }
                    .addOnFailureListener{
                        trySend(false).isSuccess
                        close()
                    }
            }
        awaitClose()
    }

    suspend fun updateOpenAiCompletionsLastUse() {
        pendingReadingsRepositoryImpl
            .getFirestoreOpenAICollectionDocumentReference(openAICompletionsDocumentReference)
            .collect{ documentReference ->
                documentReference.update("last_use", Date())
            }
    }

    private fun getHoursDifference(startDate: Date, endDate: Date): Long {
        val hoursDifference = endDate.time - startDate.time
        return TimeUnit.MILLISECONDS.toHours(hoursDifference)
    }

    private fun removeLineBreaks(textToRemoveLineBreaks: String): String{
        return textToRemoveLineBreaks.trim().replace("\n", "")
    }

    private fun generateTextToRead(text: String): TextToRead{
        return TextToRead.Builder()
            .id(6666)
            .title("Desafío")
            .text(text)
            .build()
    }

    private suspend fun getChallengeWords() = flow {
        service
            .getWordsForChallengeReading(WORDS, MAX_LENGTH, SPANISH_LANGUAGE)
            .collect{
            emit(it)
        }
    }

    private companion object {
        private const val SPANISH_LANGUAGE = "es"
        private const val MAX_LENGTH = 7
        private const val WORDS = 5
    }
}