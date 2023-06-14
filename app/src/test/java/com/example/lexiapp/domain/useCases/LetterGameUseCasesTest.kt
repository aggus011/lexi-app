package com.example.lexiapp.domain.useCases

import com.example.lexiapp.data.api.LetterServiceImpl
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
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

    lateinit var letterGameUseCases: LetterGameUseCases

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        letterGameUseCases = LetterGameUseCases(repository)
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
            assertTrue(it.length.minus(4)<=3)
        }
        //Then


        //validar!!
    }
}

val wordsMock =
    listOf("PERRO", "LORO", "PANCHO", "NACHO").shuffled(Random(System.currentTimeMillis() % 4)).take(1).toString()