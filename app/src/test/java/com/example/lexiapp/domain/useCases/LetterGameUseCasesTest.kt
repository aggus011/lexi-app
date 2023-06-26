package com.example.lexiapp.domain.useCases

import android.content.ContentValues.TAG
import android.content.SharedPreferences
import android.util.Log
import com.example.lexiapp.data.api.LetterServiceImpl
import com.example.lexiapp.data.network.FireStoreServiceImpl
import com.example.lexiapp.domain.exceptions.OversizeException
import com.example.lexiapp.domain.service.FireStoreService
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.util.Random
import java.util.stream.IntStream.range

class LetterGameUseCasesTest {

    @RelaxedMockK
    private lateinit var repository: LetterServiceImpl

    @RelaxedMockK
    private lateinit var db: FireStoreServiceImpl

    @RelaxedMockK
    private lateinit var prefs: SharedPreferences

    lateinit var letterGameUseCases: LetterGameUseCases

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        letterGameUseCases = LetterGameUseCases(repository, prefs)
    }

    @Test
    fun `when requesting a word, returns a word with the correct lenght`() = runBlocking {
        //Given
        val numberOfWords = 1
        val length = 5
        val lang = "es"
        coEvery { repository.getWord(numberOfWords, length, lang) } returns flowOf(wordsMock)
        //When
        letterGameUseCases.getWord().collect {
            //Then
            assertTrue(it.length in 4..7)
        }
    }

    @Test
    fun `when requesting for one word and you bring two, returns only one`() = runBlocking {
        //Given
        val numberOfWords = 1
        val length = 5
        val lang = "es"
        coEvery { repository.getWord(numberOfWords, length, lang) } returns flowOf("PERRO MUERDE")
        //When
        letterGameUseCases.getWord().collect {
            assert(it.split(" ").size == 1)
        }
    }

    @Test
    fun `when requesting a word and it is too long, throws a OversizeException`() = runBlocking {
        //Given
        val numberOfWords = 1
        val length = 5
        val lang = "es"
        coEvery { repository.getWord(numberOfWords, length, lang) } returns flowOf("OTOLARYNGOLOGIES")
        //When
        try {
            letterGameUseCases.getWord().collect()
        } catch (e: Exception) {
            assertEquals(e::class, OversizeException::class)
        }
    }
}

val wordsMock =
    listOf("PERRO", "LORO", "PANCHO", "NACHO").shuffled(Random(System.currentTimeMillis() % 4)).take(1).toString()