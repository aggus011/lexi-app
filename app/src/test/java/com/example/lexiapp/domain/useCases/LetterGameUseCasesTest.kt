package com.example.lexiapp.domain.useCases

import com.example.lexiapp.data.api.LetterRepositoryImpl
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class LetterGameUseCasesTest {

    @RelaxedMockK
    private lateinit var repository: LetterRepositoryImpl

    lateinit var letterGameUseCases: LetterGameUseCases

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        letterGameUseCases = LetterGameUseCases(repository)
    }

    @Test
    fun `that, when requesting a word, returns a word`() = runBlocking {
        //Given
        val numberOfWords = 1
        val length = 5
        val lang = "es"
        coEvery { repository.getWord(numberOfWords, length, lang) } returns flowOf("PERRO")
        //When
        letterGameUseCases.getWord().collect { wordResult ->
            assert(wordResult != null)
            assert(wordResult.length == length)
        }
        //validar!!
    }
}