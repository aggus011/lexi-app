package com.example.lexiapp.domain.useCases

import android.content.SharedPreferences
import com.example.lexiapp.domain.model.FirebaseResult
import com.example.lexiapp.domain.model.Note
import com.example.lexiapp.domain.service.FireStoreService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import okhttp3.internal.wait
import javax.inject.Inject

class NotesUseCases @Inject constructor(
    private val fireStoreService: FireStoreService
){
    private val MAX_LENGTH = 200

    suspend fun getNotes(emailPatient: String) = fireStoreService.getNotes(emailPatient)

    suspend fun saveNote (note: Note): Flow<FirebaseResult> {
        if (!havePermitLength(note.text)) return flowOf(FirebaseResult.TaskFaliure)
        return fireStoreService.saveNote(note)
    }

    private fun havePermitLength(text: String): Boolean {
        val characters = text
            .replace("\\.".toRegex(), "")
            .replace("\\s+".toRegex(), "")
            .replace(",".toRegex(), "")
        return characters.length <= MAX_LENGTH
    }

    suspend fun deleteNote (note: Note): Flow<FirebaseResult>{
        return fireStoreService.deleteNote(note.emailPatient!!, note.date!!)
    }
}
