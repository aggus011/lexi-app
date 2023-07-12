package com.example.lexiapp.domain.useCases

import com.example.lexiapp.data.network.FireStoreServiceImpl
import com.example.lexiapp.domain.model.FirebaseResult
import com.example.lexiapp.domain.model.Note
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class NotesUseCasesTest{
    @RelaxedMockK
    private lateinit var db: FireStoreServiceImpl

    val email = "test@gmail.com"
    lateinit var notesUseCases: NotesUseCases

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `when requesting notes, returns all notes`() = runBlocking {
        //Given
        coEvery{ db.getNotes(email) } returns flowOf(listOf(Note(textNote, email, System.currentTimeMillis().toString())))
        notesUseCases = NotesUseCases(db)
        //When - Then
        notesUseCases.getNotes(email).collect{
            assert(it.size==1)
            assert(it[0].emailPatient == email)
            assert(it[0].text == textNote)
        }
    }

    @Test
    fun `when try save a note, that have a permited length, and save it, returns TaskSuccess`() = runBlocking {
        //Given
        val note = Note(textNote, email, "1687717047605")
        coEvery{ db.saveNote(note) } returns flowOf(FirebaseResult.TaskSuccess)
        val useCases = NotesUseCases(db)
        //When - Then
        useCases.saveNote(note).collect{
            assert(it::class == FirebaseResult.TaskSuccess::class)
        }
    }

    @Test
    fun `when the length note have more than 200 caracters, but not more than 200 letters, return TaskSuccess`() = runBlocking {
        //Given
        val note = Note(validTextNote, email, "1687717047605")
        coEvery{ db.saveNote(note) } returns flowOf(FirebaseResult.TaskSuccess)
        val useCases = NotesUseCases(db)
        //When - Then
        useCases.saveNote(note).collect{
            assert(it::class == FirebaseResult.TaskSuccess::class)
        }
    }

    @Test
    fun `when the length note have more than 200 letters, return TaskFailure`() = runBlocking {
        //Given
        val note = Note(invalidTextNote, email, "1687717047605")
        coEvery{ db.saveNote(note) } returns flowOf(FirebaseResult.TaskFailure)
        val useCases = NotesUseCases(db)
        //When - Then
        useCases.saveNote(note).collect{
            assert(it::class == FirebaseResult.TaskFailure::class)
        }
    }

    @Test
    fun `when try to save a valid note, but an error happen, returns TaskFailure`() = runBlocking {
        //Given
        val note = Note(textNote, email, "1687717047605")
        coEvery{ db.saveNote(note) } returns flowOf(FirebaseResult.TaskFailure)
        val useCases = NotesUseCases(db)
        //When - Then
        useCases.saveNote(note).collect{
            assert(it::class == FirebaseResult.TaskFailure::class)
        }
    }

    @Test
    fun `when try to delete a note, but an error happen, returns TaskFailure`() = runBlocking {
        //Given
        val note = Note(textNote, email, "1687717047605")
        coEvery{ db.deleteNote(note.emailPatient, note.date!!) } returns flowOf(FirebaseResult.TaskFailure)
        val useCases = NotesUseCases(db)
        //When - Then
        useCases.deleteNote(note).collect{
            assert(it::class == FirebaseResult.TaskFailure::class)
        }
    }

    @Test
    fun `when try to delete a note and it is success, returns TaskFailure`() = runBlocking {
        //Given
        val note = Note(textNote, email, "1687717047605")
        coEvery{ db.deleteNote(note.emailPatient, note.date!!) } returns flowOf(FirebaseResult.TaskSuccess)
        val useCases = NotesUseCases(db)
        //When - Then
        useCases.saveNote(note).collect{
            assert(it::class == FirebaseResult.TaskSuccess::class)
        }
    }
}

const val textNote = "LasfloresbailabanalcompásdelsuavevientoprimaveralLospájaroscantabanmelodíasalegresmientraselsoliluminabaelpaisajeconsucálidoresplandorElaromadelasfloresimpregnabaelairecreandounasinfoníadecoloresyluz"
const val validTextNote = "Las flores bailaban al compás del suave viento primaveral. Los pájaros cantaban melodías alegres mientras el sol iluminaba el paisaje con su cálido resplandor. El aroma de las flores impregnaba el aire, creando una sinfonía de colores y luz"
const val invalidTextNote = "$textNote some caracters more"