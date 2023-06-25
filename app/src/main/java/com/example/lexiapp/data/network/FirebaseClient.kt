package com.example.lexiapp.data.network

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseClient @Inject constructor(
    val auth: FirebaseAuth,
    val firestore: FirebaseFirestore,
    val firebaseMessaging: FirebaseMessaging
)