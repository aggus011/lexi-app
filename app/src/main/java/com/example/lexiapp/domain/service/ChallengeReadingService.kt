package com.example.lexiapp.domain.service

import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.flow.Flow

interface ChallengeReadingService {

    suspend fun getFirestoreOpenAICollectionDocumentReference(document: String): Flow<DocumentReference>

}
