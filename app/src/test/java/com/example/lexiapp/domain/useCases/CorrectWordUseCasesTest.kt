package com.example.lexiapp.domain.useCases

import android.content.SharedPreferences
import com.example.lexiapp.domain.model.gameResult.CorrectWordGameResult
import com.example.lexiapp.domain.service.LetterService
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class CorrectWordUseCasesTest {

    @RelaxedMockK
    private lateinit var letterService: LetterService

    @RelaxedMockK
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var correctWordUseCases: CorrectWordUseCases

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        correctWordUseCases = CorrectWordUseCases(letterService, sharedPreferences)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }


    @Test
    fun `testSaveAnswer saves the answer with the correct email`() = runBlocking {
        // Given
        val email = "test@example.com"
        val result = CorrectWordGameResult(email, "PERRO", "PERRO", true, "2023-07-09")
        every { sharedPreferences.getString(any(), null) } returns email
        coEvery { letterService.saveResult(result) } just Runs

        // When
        correctWordUseCases.saveAnswer(result)

        // Then
        assertEquals(email, result.email)
        coVerify(exactly = 1) { letterService.saveResult(result) }
    }

    @Test
    fun `testGenerateNotificationForObjectives generates a notification with the correct parameters`() = runBlocking {
        // Given
        val game = "GAME"
        val type = "TYPE"
        val gameName = "GAME_NAME"
        coEvery { letterService.generateNotificationIfObjectiveHasBeenCompleted(game, type, gameName) } just Runs

        // When
        correctWordUseCases.generateNotificationForObjectives(game, type, gameName)

        // Then
        coVerify(exactly = 1) { letterService.generateNotificationIfObjectiveHasBeenCompleted(game, type, gameName) }
    }

    @Test
    fun `when requesting a word, returns a word with the correct length`() = runBlocking {
        // Given
        val word = "PERRO"
        val numberOfWords = 1
        val length = 5
        val lang = "es"
        coEvery { letterService.getWord(numberOfWords, length, lang) } returns flowOf(word)

        // When
        val result = mutableListOf<String>()
        correctWordUseCases.getWord().collect { result.add(it) }

        // Then
        assertTrue(result.all { it.length in 4..7 })
    }


}
