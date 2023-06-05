package com.example.lexiapp.data.network

import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreService @Inject constructor(private val firestoreClient: FirestoreClient) {

    suspend fun getFirestoreInstance() = flow {
        emit(firestoreClient.firestoreClient)
    }

}