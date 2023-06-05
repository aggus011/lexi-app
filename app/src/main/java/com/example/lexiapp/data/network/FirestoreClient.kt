package com.example.lexiapp.data.network

import com.google.firebase.firestore.FirebaseFirestore

object FirestoreClient {
    val firestoreClient: FirebaseFirestore get() = FirebaseFirestore.getInstance()
}