package com.example.lexiapp.data.repository.challengereading

import com.example.lexiapp.data.network.FireStoreServiceImpl
import com.example.lexiapp.domain.service.ChallengeReadingService
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ChallengeReadingServiceImpl @Inject constructor(private val firestoreService: FireStoreServiceImpl) :
    ChallengeReadingService {

    /*private suspend fun getFirestoreInstance() = flow {
        firestoreService.getFirestoreInstance().collect{
            emit(it)
        }
    }*/

    override suspend fun getFirestoreOpenAICollectionDocumentReference(document: String) = flow {
        firestoreService.getOpenAICollectionDocumentReference(document).collect {
            emit(it)
        }
    }
}