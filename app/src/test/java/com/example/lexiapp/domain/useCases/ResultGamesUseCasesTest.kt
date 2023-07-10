import com.example.lexiapp.data.model.LetsReadGameDataResult
import com.example.lexiapp.data.model.WhereIsTheLetterDataResult
import com.example.lexiapp.domain.model.gameResult.CorrectWordGameResult
import com.example.lexiapp.domain.model.gameResult.LetsReadGameResult
import com.example.lexiapp.domain.model.gameResult.WhereIsTheLetterResult
import com.example.lexiapp.domain.service.ResultGamesService
import com.example.lexiapp.domain.useCases.ResultGamesUseCases
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ResultGamesUseCasesTest {

    private lateinit var resultGamesService: ResultGamesService
    private lateinit var resultGamesUseCases: ResultGamesUseCases

    @Before
    fun setup() {
        resultGamesService = mockk(relaxed = true)
        resultGamesUseCases = ResultGamesUseCases(resultGamesService)
    }

    @Test
    fun `testGetWhereIsCWResults calls service and returns the results`() = runBlocking {
        // Given
        val email = "test@example.com"
        val results = listOf(
            CorrectWordGameResult(email, "Perro", "Perro", true, "2023-07-09"),
            CorrectWordGameResult(email, "Perro", "Perra", false, "2023-07-09")
        )
        coEvery { resultGamesService.getCorrectWordResults(email) } returns flowOf(results)

        // When
        val result = resultGamesUseCases.getWhereIsCWResults(email).toList().flatten()

        // Then
        assertEquals(results, result)
    }

    @Test
    fun `testGetWhereIsCWResults returns empty list when service returns empty list`() = runBlocking {
        // Given
        val email = "test@example.com"
        val emptyList = emptyList<CorrectWordGameResult>()
        coEvery { resultGamesService.getCorrectWordResults(email) } returns flowOf(emptyList)

        // When
        val result = resultGamesUseCases.getWhereIsCWResults(email).toList().flatten()

        // Then
        assertEquals(emptyList, result)
    }


    @Test
    fun `testGetLRResults calls service and returns the results`() = runBlocking {
        // Given
        val email = "test@example.com"
        val results = listOf(
            LetsReadGameResult(email, true, emptyList(), 0, "2023-07-09"),
            LetsReadGameResult(email, false, listOf("a", "e"), 2, "2023-07-08")
        )
        coEvery { resultGamesService.getLRResults(email) } returns flowOf(results)

        // When
        val result = resultGamesUseCases.getLRResults(email).toList().flatten()

        // Then
        assertEquals(results, result)
    }

    @Test
    fun `testGetTSResults calls service and returns the results`() = runBlocking {
        // Given
        val email = "test@example.com"
        val results = listOf("result1", "result2", "result3")
        coEvery { resultGamesService.getTSResults(email) } returns flowOf(results)

        // When
        val result = resultGamesUseCases.getTSResults(email).toList().flatten()

        // Then
        assertEquals(results, result)
    }



    @Test
    fun `testGetWhereIsTheLetterResults calls service and returns the results`() = runBlocking {
        // Given
        val email = "test@example.com"
        val results = listOf(
            WhereIsTheLetterDataResult(true, "A", "A", "Apple", "2023-07-09"),
            WhereIsTheLetterDataResult(false, "A", "B", "Banana", "2023-07-08")
        )
        val expectedResults = results.map {
            WhereIsTheLetterResult(
                email,
                it.mainLetter.single(),
                it.selectedLetter.single(),
                it.word,
                it.result,
                it.date
            )
        }
        coEvery { resultGamesService.getWhereIsTheLetterResults(email) } returns flowOf(expectedResults)

        // When
        val result = resultGamesUseCases.getWhereIsTheLetterResults(email).toList().flatten()

        // Then
        assertEquals(expectedResults, result)
    }



}
