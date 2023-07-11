import android.content.SharedPreferences
import com.example.lexiapp.data.api.difference_text.model.Rows
import com.example.lexiapp.domain.model.gameResult.LetsReadGameResult
import com.example.lexiapp.domain.service.DifferenceService
import com.example.lexiapp.domain.useCases.DifferenceUseCases
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class DifferenceUseCasesTest {

    private lateinit var service: DifferenceService
    private lateinit var prefs: SharedPreferences
    private lateinit var differenceUseCases: DifferenceUseCases

    @Before
    fun setup() {
        service = mockk()
        prefs = mockk(relaxed = true)
        differenceUseCases = DifferenceUseCases(service, prefs)
    }

    @Test
    fun `getDifference returns response from service`() = runBlocking {
        // Given
        val originalText = "Texto correcto"
        val revisedText = "Texto erroneo"

        val response = mockk<Response<Rows>>()
        coEvery { service.getDifference(originalText, revisedText) } returns response

        // When
        val result = differenceUseCases.getDifference(originalText, revisedText)

        // Then
        assertEquals(response, result)
        coVerify { service.getDifference(originalText, revisedText) }
    }

    @Test
    fun `saveWrongWords updates result and calls service`() = runBlocking {
        // Given
        val result = LetsReadGameResult(
            email = "asd@lexi.com",
            success = false,
            wrongWords = listOf("MANZANA", "BANANA", "FRUTILLA"),
            totalWords = 10
        )
        val isChallengeReading = 1

        every { prefs.getString("email", null) } returns "asd@lexi.com"
        coEvery { service.saveLetsReadResult(result.copy(email = "asd@lexi.com"), true) } just runs

        // When
        differenceUseCases.saveWrongWords(result, isChallengeReading)

        // Then
        assertEquals("asd@lexi.com", result.email)
        coVerify { service.saveLetsReadResult(result.copy(email = "asd@lexi.com"), true) }
    }


    @Test
    fun `normalizeWords normalizes list of wrong words`() {
        // Given
        val wrongWords = listOf("manzana,", "banana!", "frutilla.")

        // When
        val result = differenceUseCases.normalizeWords(wrongWords)

        // Then
        val expected = listOf("MANZANA", "BANANA", "FRUTILLA")
        assertEquals(expected, result)
    }

    @Test
    fun `generateNotificationForObjectives calls service`() = runBlocking {
        // Given
        val game = "CW"
        val type = "hit"
        val gameName = "CorrectWord"

        coEvery { service.generateNotificationIfObjectiveHasBeenCompleted(game, type, gameName) } just runs

        // When
        differenceUseCases.generateNotificationForObjectives(game, type, gameName)

        // Then
        coVerify { service.generateNotificationIfObjectiveHasBeenCompleted(game, type, gameName) }
    }


}