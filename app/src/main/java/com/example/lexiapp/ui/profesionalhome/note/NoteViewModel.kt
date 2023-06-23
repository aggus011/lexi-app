package com.example.lexiapp.ui.profesionalhome.note

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lexiapp.domain.model.FirebaseResult
import com.example.lexiapp.domain.model.Note
import com.example.lexiapp.domain.model.User
import com.example.lexiapp.domain.useCases.CodeQRUseCases
import com.example.lexiapp.domain.useCases.LinkUseCases
import com.example.lexiapp.domain.useCases.NotesUseCases
import com.example.lexiapp.domain.useCases.ResultGamesUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val notesUseCases: NotesUseCases
) : ViewModel(){
    private var _patient = MutableLiveData<User?>()
    val patient = _patient as LiveData<User?>
    private var _notes = MutableLiveData<List<Note>>()
    val notes = _notes as LiveData<List<Note>>
    private var _resultSave = MutableLiveData<FirebaseResult>()
    val resultSave = _resultSave as LiveData<FirebaseResult>
    private var _resultDelete = MutableLiveData<FirebaseResult>()
    val resultDelete = _resultDelete as LiveData<FirebaseResult>

    fun setPatient(patient: User) {
        _patient.value=patient
    }

    fun cleanPatient() {
        _patient.value=null
    }

    fun saveNote(email: String, text: String){
        val note = Note(emailPatient = email, text = text)
        viewModelScope.launch {
            notesUseCases.saveNote(note).collect{ result ->
                _resultSave.value = result
            }
        }
    }

    fun startListenerOfNotes(){
        viewModelScope.launch {
            notesUseCases.getNotes(_patient.value!!.email).collect{ notes->
                _notes.value=notes
            }
        }
    }

    fun deleteNote(note: Note){
        viewModelScope.launch {
            notesUseCases.deleteNote(note).collect{ result->
                _resultDelete.value = result
            }
        }
    }
}