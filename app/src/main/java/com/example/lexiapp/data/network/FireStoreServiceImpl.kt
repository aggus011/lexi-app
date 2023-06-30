package com.example.lexiapp.data.network

import android.util.Log
import com.example.lexiapp.data.model.CorrectWordDataResult
import com.example.lexiapp.data.model.Game
import com.example.lexiapp.data.model.LetsReadGameDataResult
import com.example.lexiapp.data.model.WhereIsTheLetterDataResult
import com.example.lexiapp.data.model.toCorrectWordGameResult
import com.example.lexiapp.data.model.toWhereIsTheLetterResult
import com.example.lexiapp.data.repository.categories_words.*
import com.example.lexiapp.data.model.*
import com.example.lexiapp.domain.exceptions.FirestoreException
import com.example.lexiapp.domain.model.FirebaseResult
import com.example.lexiapp.domain.model.Professional
import com.example.lexiapp.domain.model.User
import com.example.lexiapp.domain.model.*
import com.example.lexiapp.domain.service.FireStoreService
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.math.ceil

@Singleton
class FireStoreServiceImpl @Inject constructor(
    firebase: FirebaseClient
) : FireStoreService {

    private val userCollection = firebase.firestore.collection("user")
    private val whereIsTheLetterCollection =
        firebase.firestore.collection(Game.WHERE_IS_THE_LETTER.toString().lowercase())
    private val correctWordCollection = firebase.firestore.collection(Game.CORRECT_WORD.toString().lowercase())
    private val letsReadCollection = firebase.firestore.collection(Game.LETS_READ.toString().lowercase())
    private val openaiCollection = firebase.firestore.collection("openai_api_use")
    private val professionalCollection = firebase.firestore.collection("professional")
    private val objectivesCollection = firebase.firestore.collection("objectives")
    private val resultGameCollection: (String, String) -> CollectionReference =
        { collection: String, email: String -> firebase.firestore.collection("${collection}/${email}/results") }
    private val notesDocument = firebase.firestore.collection("profesional_notes")
    private val notesCollection: (String) -> CollectionReference =
        {email: String -> firebase.firestore.collection("profesional_notes/${email}/notes")}
    private val errorWordsDocument: (String) -> DocumentReference =
        { email: String -> firebase.firestore.collection("error_words").document(email) }
    private val db = firebase.firestore
    private val categoryCollection = firebase.firestore.collection("category")
    private val completedObjectivesCollectionReference =
        { uid: String -> firebase.firestore.collection("history_objectives/${uid}/objectives") }
    private val firebaseCloudMessaging = firebase.firebaseMessaging

    override suspend fun saveAccount(user: User) {
        val data = hashMapOf(
            "user_name" to user.userName,
            "birth_date" to user.birthDate,
            "professional_link" to user.profesional as String?,
            "token" to getDeviceToken()
        )
        userCollection.document(user.email).set(data).await()
    }

    override suspend fun getUser(email: String): User {
        val user = User(null, email, null, null)
        userCollection.document(email).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val documentSnapshot = task.result
                    if (documentSnapshot.exists()) {
                        user.userName = documentSnapshot.data?.get("user_name") as String?
                        user.birthDate = documentSnapshot.data?.get("birth_date") as String?
                        user.profesional =
                            documentSnapshot.data?.get("professional_link") as String?
                    } else {
                        // El usuario no fue encontrado
                    }
                } else {
                    // Hubo un error al obtener la información del usuario
                }
            }.await()
        return user
    }

    override suspend fun saveWhereIsTheLetterResult(
        result: WhereIsTheLetterDataResult,
        email: String
    ) {
        Log.v("SAVE_ANSWER_FIRESTORE_IMPL_pre", "${result.result}//${result.word}")
        //SAVE WORDS WHEN RESULT=false
        if (!result.result) saveErrorWord(email, listOf(result.word))
        Log.v("SAVE_ANSWER_FIRESTORE_IMPL_post", "${result.result}")
        val data = hashMapOf(
            "result" to result.result,
            "mainLetter" to result.mainLetter,
            "selectedLetter" to result.selectedLetter,
            "word" to result.word
        )
        whereIsTheLetterCollection.document(email).collection("results")
            .document(System.currentTimeMillis().toString()).set(data).await()
    }

    override suspend fun saveTextScanResult(email: String) {
        resultGameCollection("text_scan",email)
            .document(System.currentTimeMillis().toString())
            .set(mapOf<String, Any>())
    }

    override suspend fun getLastResultsWhereIsTheLetterGame(userMail: String) = flow {
        val result = mutableListOf<WhereIsTheLetterDataResult>()
        resultGameCollection(Game.WHERE_IS_THE_LETTER.toString().lowercase(), userMail).get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    val documentId = document.id
                    val data = document.data
                    result.add(
                        WhereIsTheLetterDataResult(
                            mainLetter = data["mainLetter"] as String,
                            result = data["result"] as Boolean,
                            selectedLetter = data["selectedLetter"] as String,
                            word = data["word"] as String,
                            date = documentId
                        )
                    )
                }
            }.await()
        emit(result.map { it.toWhereIsTheLetterResult(userMail) })
    }

    override suspend fun getLastResultsCorrectWordGame(userMail: String) = flow {
        val result = mutableListOf<CorrectWordDataResult>()
        resultGameCollection(Game.CORRECT_WORD.toString().lowercase(), userMail).get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    val documentId = document.id
                    val data = document.data
                    result.add(
                        CorrectWordDataResult(
                            mainWord = data["mainWord"] as String,
                            result = data["result"] as Boolean,
                            selectedWord = data["selectedWord"] as String,
                            date = documentId
                        )
                    )
                }
            }.await()
        emit(result.map { it.toCorrectWordGameResult(userMail) })
    }

    override suspend fun getLastResultsTextScan(email: String) = flow{
        val result = mutableListOf<String>()
        Log.v("LOG_TEXT_SCANN_SERVICE_IMPL", "${email}")
        resultGameCollection("text_scan",email).get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    Log.v("LOG_TEXT_SCANN_SERVICE_IMPL", "${document.id}")
                    result.add(document.id)
                }
            }.await()
        emit(result)
    }

    override suspend fun getLastResultsLetsReadGame(email: String) = flow {
        val result = mutableListOf<LetsReadGameDataResult>()
        resultGameCollection(Game.LETS_READ.toString().lowercase(), email).get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    val documentId = document.id
                    val data = document.data
                    result.add(
                        LetsReadGameDataResult(
                            success = data["success"] as Boolean,
                            email = email,
                            wrongWords = data["wrongWords"] as List<String>,
                            date = documentId,
                            totalWords = (data["totalWords"] as Long).toInt()
                        )
                    )
                }
            }.await()
        emit(result.map { it.toLetsReadGameResult() })
    }

    override suspend fun getOpenAICollectionDocumentReference(document: String) = flow {
        emit(openaiCollection.document(document))
    }

    override suspend fun saveProfessionalAccount(
        professional: Professional,
        registrationDate: Date
    ) {
        val data = hashMapOf(
            "user_name" to professional.user!!.userName,
            "medical_registration" to professional.medicalRegistration,
            "patients" to professional.patients,
            "is_verificated_account" to professional.isVerifiedAccount,
            "registration_date" to registrationDate,
            "token" to getDeviceToken()
        )

        professionalCollection.document(professional.user.email)
            .set(data)
            .addOnSuccessListener {
                Log.v(TAG, "New professional saved in firestore")
            }
            .addOnFailureListener {
                throw FirestoreException("Failure to save a new professional")
            }
    }

    override suspend fun getProfessional(email: String): Professional {
        return suspendCoroutine { continuation ->
            professionalCollection
                .document(email)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    if(documentSnapshot != null &&
                        documentSnapshot.exists()){
                        val professional = Professional.Builder()
                            .user(documentSnapshot.data?.get("user_name").toString(), email)
                            .medicalRegistration(
                                documentSnapshot.data?.get("medical_registration").toString()
                            )
                            .patients(documentSnapshot.data?.get("patients") as List<String>)
                            .isVerifiedAccount(documentSnapshot.data?.get("is_verificated_account") as Boolean)
                            .registrationDate((documentSnapshot.data?.get("registration_date") as Timestamp).toDate())
                            .build()
                        continuation.resume(professional)
                    }else{
                        val professional = Professional(User(null, email), null, null, false, null)
                        continuation.resume(professional)
                    }
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }
    }

    override suspend fun getIsLinked(email: String): Boolean? {
        var linkProfessional: Boolean? = null
        userCollection.document(email).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val documentSnapshot = task.result
                    if (documentSnapshot.exists()) {
                        linkProfessional =
                            documentSnapshot.data?.get("professional_link") as String? != null
                        Log.v("FSSImpl_PRE_LINK_PROF", "$linkProfessional")
                    }
                } else {
                    throw FirestoreException("Problema en firestore para obtener datos")
                }
            }.await()
        Log.v("FSSImpl_POST_LINK_PROF", "$linkProfessional")
        return linkProfessional
    }

    override suspend fun bindProfessionalToPatient(
        emailPatient: String,
        emailProfessional: String
    ): FirebaseResult {
        var result: FirebaseResult = FirebaseResult.TaskFailure
        val data = mapOf(
            "professional_link" to emailProfessional
        )
        userCollection.document(emailPatient).update(data)
            .addOnSuccessListener {
                result = FirebaseResult.TaskSuccess
            }
            .addOnFailureListener {
                throw FirestoreException("Failure to save a new professional")
            }.await()
        return result
    }

    override suspend fun addPatientToProfessional(
        emailPatient: String,
        emailProfessional: String
    ): CompletableDeferred<FirebaseResult> {
        val completableDeferred = CompletableDeferred<FirebaseResult>()
        db.runTransaction { transaction ->
            val documentSnapshot =
                transaction.get(professionalCollection.document(emailProfessional))
            val list = documentSnapshot.get("patients") as List<String>?
            val newList = list?.toMutableList() ?: mutableListOf()
            newList.add(emailPatient)
            transaction.update(
                professionalCollection.document(emailProfessional),
                "patients", newList
            )
        }.addOnSuccessListener {
            completableDeferred.complete(FirebaseResult.TaskSuccess)
        }.addOnFailureListener {
            completableDeferred.completeExceptionally(FirestoreException("Failure to save a new patient"))
        }
        return completableDeferred
    }

    override suspend fun getListLinkPatientOfProfessional(
        emailProfessional: String
    )= callbackFlow {
        val docRef = professionalCollection.document(emailProfessional)
        val listener = docRef.addSnapshotListener { querySnapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            val result = mutableListOf<String>()
            if (querySnapshot != null && querySnapshot.exists()) {
                val list: List<String> = querySnapshot.get("patients") as List<String>
                result.addAll(list)
            }
            trySend(result.toList()).isSuccess
        }
        awaitClose { listener.remove() }
    }

    override suspend fun unBindProfessionalFromPatient(
        emailPatient: String
    ): FirebaseResult {
        var result: FirebaseResult = FirebaseResult.TaskFailure
        val data = mapOf(
            "professional_link" to null
        )
        userCollection.document(emailPatient).update(data)
            .addOnSuccessListener {
                result = FirebaseResult.TaskSuccess
            }
            .addOnFailureListener {
                throw FirestoreException("Failure to save a new professional")
            }.await()
        return result
    }

    override suspend fun deletePatientFromProfessional(
        emailPatient: String,
        emailProfessional: String
    ): CompletableDeferred<FirebaseResult> {
        val completableDeferred = CompletableDeferred<FirebaseResult>()
        db.runTransaction { transaction ->
            val documentSnapshot =
                transaction.get(professionalCollection.document(emailProfessional))
            val list = documentSnapshot.get("patients") as List<String>?
            val newList = list?.toMutableList() ?: mutableListOf()
            newList.remove(emailPatient)
            transaction.update(
                professionalCollection.document(emailProfessional),
                "patients", newList
            )
            newList
        }.addOnSuccessListener {
            //THIS LINE CLEAN ALL THE NOTES OF PATIENT, WHEN IT IS UNLINK
            cleanNotesFromPatient(emailPatient)
            completableDeferred.complete(FirebaseResult.TaskSuccess)
        }.addOnFailureListener {
            completableDeferred.completeExceptionally(FirestoreException("Failure to save a new patient"))
        }
        return completableDeferred
    }

    override suspend fun saveCorrectWordResult(result: CorrectWordDataResult, email: String) {
        //SAVE ERROR WORDS, OF RESULT
        if (!result.result) saveErrorWord(email, listOf(result.mainWord))
        val data = hashMapOf(
            "result" to result.result,
            "mainWord" to result.mainWord,
            "selectedWord" to result.selectedWord
        )
        correctWordCollection.document(email).collection("results")
            .document(System.currentTimeMillis().toString()).set(data).await()
    }

    override suspend fun saveObjectives(email: String, objectives: List<Objective>) {
        val timeZone = ZoneId.of("America/Argentina/Buenos_Aires")
        val currentDate = LocalDate.now(timeZone)
        val lastMonday = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).atStartOfDay(timeZone).toLocalDate()
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        val lastMondayDate = lastMonday.format(formatter)

        val document = objectivesCollection.document(email).collection(lastMondayDate)
        val objectiveMap = hashMapOf<String, Any?>()
        objectives.forEachIndexed { index, objective ->
            val objectiveFields = hashMapOf<String, Any?>(
                "id" to objective.id,
                "title" to objective.title,
                "description" to objective.description,
                "progress" to objective.progress,
                "goal" to objective.goal,
                "completed" to objective.completed,
                "game" to objective.game,
                "type" to objective.type,
                "date" to objective.date
            )
            document.document("objective$index")
                .set(objectiveFields)
                .addOnSuccessListener {
                    // Éxito al guardar el objetivo
                }
                .addOnFailureListener { e ->
                    // Error al guardar el objetivo
                }
                .await()
        }
    }
    override suspend fun increaseGoalForGames(email: String, games: List<String>) {
        val timeZone = ZoneId.of("America/Argentina/Buenos_Aires")
        val currentDate = LocalDate.now(timeZone)
        val lastMonday = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).atStartOfDay(timeZone).toLocalDate()
        val dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        val lastMondayDate = lastMonday.format(dateFormatter)
        val document = objectivesCollection.document(email).collection(lastMondayDate)
        val snapshot = document.get().await()
        val batch = db.batch()


        for (documentSnapshot in snapshot.documents) {
            val objectiveFields = documentSnapshot.data
            if (objectiveFields is Map<*, *>) {
                val game = objectiveFields["game"] as String?
                if (game != null && game in games) {
                    val goal = objectiveFields["goal"] as Long?
                    if (goal != null) {
                        val newGoal = ceil(goal * 1.5).toInt()
                        batch.update(documentSnapshot.reference, "goal", newGoal)
                    }
                }
            }
        }

        batch.commit().await()
    }


    override suspend fun checkObjectivesExist(email: String, lastMondayDate: String): Boolean {
        val document = objectivesCollection.document(email).collection(lastMondayDate)
        return try {
            val snapshot = document.get().await()
            !snapshot.isEmpty
        } catch (e: Exception) {
            false
        }
    }
    override suspend fun getIncompleteGameNames(email: String, lastMondayDate: String): List<String> {
        return suspendCancellableCoroutine { continuation ->
            val document = objectivesCollection.document(email).collection(lastMondayDate)
            val registration = document.addSnapshotListener { snapshot, exception ->
                try {
                    if (exception != null || snapshot == null) {
                        continuation.resume(emptyList())
                        return@addSnapshotListener
                    }
                    val gameNames = mutableListOf<String>()
                    for (documentSnapshot in snapshot.documents) {
                        val objectiveFields = documentSnapshot.data
                        if (objectiveFields is Map<*, *>) {
                            val completed = objectiveFields["completed"]
                            if (completed == false) {
                                val game = objectiveFields["game"] as String?
                                val progress = objectiveFields["progress"] as String?
                                if (game != null) {
                                    gameNames.add(game)
                                }
                            }
                        }
                    }
                    continuation.resume(gameNames)
                } catch (e: Exception) {
                    Log.v("TAG", "$e")
                    continuation.resume(emptyList())
                }
            }
            continuation.invokeOnCancellation {
                registration.remove()
            }
        }
    }


    override suspend fun getObjectives(uid: String, lastMondayDate: String, listener: (List<Objective>) -> Unit
    ) {
        val document = objectivesCollection.document(uid).collection(lastMondayDate)
        val registration = document.addSnapshotListener { snapshot, exception ->
            try {
                if (exception != null || snapshot == null) {
                    listener(emptyList())
                    return@addSnapshotListener

                }

                val objectives = mutableListOf<Objective>()
                for (documentSnapshot in snapshot.documents) {
                    val objectiveFields = documentSnapshot.data
                    if (objectiveFields is Map<*, *>) {
                        val id = objectiveFields["id"] as Long?
                        val title = objectiveFields["title"] as String?
                        val description = objectiveFields["description"] as String?
                        val progress = (objectiveFields["progress"] as Long?)?.toInt() ?: 0
                        val goal = (objectiveFields["goal"] as Long?)?.toInt()
                        val game = objectiveFields["game"] as String?
                        val type = objectiveFields["type"] as String?
                        val completed = objectiveFields["completed"] as Boolean?
                        val date = objectiveFields["date"] as String?
                        objectives.add(Objective(id, title, description, progress, goal, game, type, completed, date))
                    }
                }

                listener(objectives)
            } catch (e: Exception) {
                listener(emptyList())
            }
        }
    }

    override suspend fun saveLetsReadResult(result: LetsReadGameDataResult) {
        //SAVE WORDS, OF RESULT=false
        saveErrorWord(result.email, result.wrongWords)
        val data = hashMapOf(
            "wrongWords" to result.wrongWords,
            "totalWords" to result.totalWords,
            "success" to result.success
        )
        letsReadCollection.document(result.email).collection("results")
            .document(System.currentTimeMillis().toString()).set(data).await()
    }

    override suspend fun saveCategoriesFromPatient(email: String, categories: List<String>) {
        val data = hashMapOf(
            "categories" to categories
        )

        categoryCollection
            .document(email)
            .set(data)
            .addOnSuccessListener {
                Log.v(TAG, "Categories saved for patient $email")
            }
            .addOnFailureListener {
                throw FirestoreException("Failure to save categories for patient $email")
            }
    }

    override suspend fun getPatientCategories(email: String): List<String> {

        return suspendCoroutine { continuation ->
            categoryCollection
                .document(email)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        val categories = documentSnapshot.data?.get("categories") as List<String>
                        continuation.resume(categories)
                    } else {
                        continuation.resume(emptyList())
                    }
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }
    }

    override suspend fun getWordCategories(email: String): List<String> {
        val categoriesNames = getPatientCategories(email)
        val wordsCategories = mutableListOf<String>()
        categoriesNames.forEach { categoryName ->
            val category: Category? = when {
                (categoryName == "Animales") -> Animals
                (categoryName == "Insectos") -> Insects
                (categoryName == "Frutas") -> Fruits
                (categoryName == "Verduras") -> Vegetables
                (categoryName == "Colores" )-> Colors
                (categoryName == "Nombres" )-> Names
                (categoryName == "Lugares" )-> Places
                (categoryName == "Países" )-> Countries
                (categoryName == "Vehículos")-> Vehicles
                else -> null
            }
            if (category!=null) wordsCategories.addAll(category.stimulus())
        }
        return wordsCategories
    }

    override suspend fun saveNote(note: Note) = flow {
        try {
            val data = hashMapOf(
                "note" to note.text
            )
            val documentRef = notesDocument.document(note.emailPatient)
                .collection("notes")
                .document(System.currentTimeMillis().toString())
            documentRef.set(data).await()
            emit(FirebaseResult.TaskSuccess)
        } catch (e: Exception) {
            emit(FirebaseResult.TaskFailure)
        }
    }

    override suspend fun deleteNote (emailPatient: String, date: String) = flow {
        try {
            val notes = notesCollection(emailPatient).document(date).delete().await()
            emit(FirebaseResult.TaskSuccess)
        } catch (e: Exception) {
            emit(FirebaseResult.TaskFailure)
        }
    }

    override suspend fun getNotes(emailPatient: String) = callbackFlow {
        val notesCollectionRef = notesDocument.document(emailPatient).collection("notes")
        val listener = notesCollectionRef.addSnapshotListener { querySnapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }

            val result = mutableListOf<Note>()
            for (document in querySnapshot!!.documents) {
                val date = document.id
                val data = document.data
                result.add(
                    Note(
                        text = data?.get("note")!! as String,
                        emailPatient = emailPatient,
                        date = date
                    )
                )
            }
            trySend(result.toList()).isSuccess
        }

        awaitClose { listener.remove() }
    }

    private fun cleanNotesFromPatient(emailPatient: String){
        notesCollection(emailPatient).get().addOnSuccessListener { querySnapshot ->
            for (document in querySnapshot.documents) {
                document.reference.delete()
            }
        }
    }

    private fun saveErrorWord(email: String, errorWords: List<String>){
        val ref = errorWordsDocument(email)
        ref.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists() && documentSnapshot.contains("words")) {
                val currentErrorWords = documentSnapshot["words"] as List<String>
                val updatedErrorWords = currentErrorWords.toMutableSet()
                updatedErrorWords.addAll(errorWords)
                ref.update("words", updatedErrorWords.toList())
            } else {
                ref.set(hashMapOf("words" to errorWords))
            }
        }
    }

    override suspend fun getWordPlayed(email: String): Pair<Boolean, List<String>> {
        Log.v("GET_ERROR", "${email}")
        val documentSnapshot = errorWordsDocument(email).get().await()
        return try{
            if (documentSnapshot.exists()) {
                val errorWords = documentSnapshot.get("words") as List<String>
                Log.v("ERRORS", "${errorWords}")
                val validation = errorWords.size >= 15
                Pair(validation, errorWords)
            } else {
                Pair(false, emptyList())
            }
        }catch (e: Exception){
            Pair(false, emptyList())
        }

    }

    override suspend fun saveTokenToPatient(emailPatient: String) {
        try{
            val token = getDeviceToken()
            userCollection
                .document(emailPatient)
                .update("token", token)
                .addOnSuccessListener {
                    Log.v(TAG, "new token saved to patient $emailPatient")
                }
        }catch (e: FirestoreException){
            Log.v(TAG, "error to save token to patient $emailPatient with exception ${e.message}")
        }
    }

    override suspend fun saveTokenToProfessional(emailProfessional: String) {
        try{
            val token = getDeviceToken()
            professionalCollection
                .document(emailProfessional)
                .update("token", token)
                .addOnSuccessListener {
                    Log.v(TAG, "new token saved to professional $emailProfessional")
                }
        }catch (e: FirestoreException){
            Log.v(TAG, "error to save token to professional $emailProfessional with exception ${e.message}")
        }
    }

    override suspend fun getDeviceToken(): String {
        return firebaseCloudMessaging.token.await()
    }

    override suspend fun getPatientToken(patientEmail: String): String? {
        return suspendCoroutine { continuation ->
            userCollection
                .document(patientEmail)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    if(documentSnapshot.exists() &&
                        documentSnapshot.contains("token")){
                        val token = documentSnapshot.data?.get("token") as String

                        continuation.resume(token)
                    }else{
                        continuation.resume(null)
                    }
                }
                .addOnFailureListener{ exception ->
                    Log.v(TAG, "failed to get token for patient $patientEmail and exception ${exception.message}")
                    continuation.resumeWithException(exception)
                }
        }
    }

    override suspend fun getProfessionalToken(professionalEmail: String): String? {
        return suspendCoroutine { continuation ->
            professionalCollection
                .document(professionalEmail)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    if(documentSnapshot.exists() &&
                        documentSnapshot.contains("token")){
                        val token = documentSnapshot.data?.get("token") as String

                        continuation.resume(token)
                    }else{
                        continuation.resume(null)
                    }
                }
                .addOnFailureListener{ exception ->
                    Log.v(TAG, "failed to get token for patient $professionalEmail and exception ${exception.message}")
                    continuation.resumeWithException(exception)
                }
        }
    }

    override suspend fun updateObjectiveProgress(game: String, type: String) {
        val timeZone = ZoneId.of("America/Argentina/Buenos_Aires")
        val currentDate = LocalDate.now(timeZone)
        val lastMonday = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        val dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        val lastMondayDate = lastMonday.format(dateFormatter)
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid
        if (userId != null) {
            val document = objectivesCollection.document(userId).collection(lastMondayDate)
            val snapshot = document.get().await()
            if (!snapshot.isEmpty) {
                snapshot.documents.forEach { documentSnapshot ->
                    val objectiveFields = documentSnapshot.data
                    if (objectiveFields is Map<*, *>) {
                        val gameValue = objectiveFields["game"] as String?
                        val typeValue = objectiveFields["type"] as String?
                        val goal = objectiveFields["goal"] as Long
                        val progress = objectiveFields["progress"] as Long?
                        var completed = objectiveFields["completed"] as Boolean?
                        if (gameValue == game && typeValue == type && goal != progress && !completed!!) {
                            val updatedProgress = (objectiveFields["progress"] as Long?)?.toInt()?.plus(1)
                            if (updatedProgress != null) {
                                if (updatedProgress >= goal) {
                                    completed = true
                                    saveCompleteObjectives(userId ,goal, objectiveFields["title"] as String)
                                }
                                documentSnapshot.reference.update(
                                    "progress", updatedProgress.toLong(),
                                    "completed", completed
                                ).await()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun saveCompleteObjectives(uid: String,count: Long, title: String) {
        val data = mapOf(
            "title" to title,
            "count" to count
        )
        completedObjectivesCollectionReference(uid)
            .document(System.currentTimeMillis().toString()).set(data)
    }

    override suspend fun getObjectivesHistory(uid: String) = callbackFlow {
        val listener =
            completedObjectivesCollectionReference(uid).addSnapshotListener { querySnapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val completeObjectives = mutableListOf<MiniObjective>()
                for (document in querySnapshot!!.documents) {
                    val date = document.id
                    val data = document.data
                    completeObjectives.add(
                        MiniObjective(
                            date = date,
                            title = data?.get("title")!! as String,
                            count = data?.get("count")!! as Long
                        )
                    )
                }
                trySend(completeObjectives.toList()).isSuccess
            }

        awaitClose { listener.remove() }
    }

    companion object {
        private const val TAG = "FireStoreServiceImpl"
    }
}
