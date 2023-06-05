package com.example.lexiapp.data.repository.challengereading

import com.example.lexiapp.data.network.FirestoreService
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ChallengeReadingRepositoryImpl @Inject constructor(private val firestoreService: FirestoreService) {

    private suspend fun getFirestoreInstance() = flow {
        firestoreService.getFirestoreInstance().collect{
            emit(it)
        }
    }

    suspend fun getFirestoreOpenAiCompletionDocumentReference() = flow {
        getFirestoreInstance().collect{
            emit(it.collection("openai_api_use").document("completions"))
        }
    }
}