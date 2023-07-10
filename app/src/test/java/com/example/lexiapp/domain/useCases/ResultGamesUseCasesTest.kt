import android.icu.util.Calendar
import com.example.lexiapp.data.model.LetsReadGameDataResult
import com.example.lexiapp.data.model.WhereIsTheLetterDataResult
import com.example.lexiapp.data.network.ResultGamesServiceImpl
import com.example.lexiapp.domain.model.gameResult.CorrectWordGameResult
import com.example.lexiapp.domain.model.gameResult.LetsReadGameResult
import com.example.lexiapp.domain.model.gameResult.WhereIsTheLetterResult
import com.example.lexiapp.domain.service.ResultGamesService
import com.example.lexiapp.domain.useCases.ResultGamesUseCases
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.sql.Date
import java.util.concurrent.TimeUnit

class ResultGamesUseCasesTest {

    @RelaxedMockK
    private lateinit var serviceImpl: ResultGamesServiceImpl

    lateinit var resultGamesUseCases: ResultGamesUseCases

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        resultGamesUseCases = ResultGamesUseCases(serviceImpl)
    }

    @Test
    fun `que si tengo mas resultados que no sean de esta semana los filtre en juego LR`() {
        //Given

        //When
        val responseUseCase = resultGamesUseCases.filterResultsByWeek(resultsLR)

        assert(responseUseCase.size == 1)

    }

    @Test
    fun `que si no tengo resultados esta semana me devuelva la lista de la ultima semana vacia`() {

        val responseUseCase = resultGamesUseCases.getResultsLastWeek(emptyList())

        assert(responseUseCase.entries.size == 7)
    }

    @Test
    fun `que sin importar los resultados de esta semana me devuelva la lista de la ultima semana en orden descendiente`() {

        val responseUseCase = resultGamesUseCases.getResultsLastWeek(emptyList())
        val calendar = Calendar.getInstance()
        val actualDay = calendar.get(Calendar.DAY_OF_YEAR)
        //calendar.add(java.util.Calendar.DAY_OF_YEAR, -1)
        val day6 = calendar.time
        val day5 = calendar.add(java.util.Calendar.DAY_OF_YEAR, -2)
        val day4 = calendar.add(java.util.Calendar.DAY_OF_YEAR, -3)
        val day3 = calendar.add(java.util.Calendar.DAY_OF_YEAR, -4)
        val day2 = calendar.add(java.util.Calendar.DAY_OF_YEAR, -5)
        val day1 = calendar.add(java.util.Calendar.DAY_OF_YEAR, -6)

        val actualResult = Calendar.getInstance().apply {
            time = Date(responseUseCase.keys.toList()[6].toLong())
        }.get(Calendar.DAY_OF_YEAR)
        val day6result = Date(responseUseCase.keys.toList()[5].toLong())
        val day5result = Date(responseUseCase.keys.toList()[4].toLong())
        val day4result = Date(responseUseCase.keys.toList()[3].toLong())
        val day3result = Date(responseUseCase.keys.toList()[2].toLong())
        val day2result = Date(responseUseCase.keys.toList()[1].toLong())
        val day1result = Date(responseUseCase.keys.toList()[0].toLong())

        assert(actualDay == actualResult)

    }

    private val resultsLR = listOf(
        LetsReadGameResult(
            email = "algo@fake.com",
            success = true,
            wrongWords = listOf("CASA", "PATO", "BOTA"),
            totalWords = 20,
            date = (System.currentTimeMillis() - TimeUnit.DAYS.toMillis(9)).toString()
        ),
        LetsReadGameResult(
            email = "algo@fake.com",
            success = true,
            wrongWords = listOf("CASA", "PATO", "BOTA", "CANGURO"),
            totalWords = 20,
            date = (System.currentTimeMillis() - TimeUnit.DAYS.toMillis(2)).toString()
        ),
        LetsReadGameResult(
            email = "algo@fake.com",
            success = true,
            wrongWords = listOf("CASA", "PATO", "BOTA"),
            totalWords = 20,
            date = (System.currentTimeMillis() - TimeUnit.DAYS.toMillis(8)).toString()
        )
    )

    @Test
    fun `testGetWhereIsCWResults calls service and returns the results`() = runBlocking {
        // Given
        val email = "test@example.com"
        val results = listOf(
            CorrectWordGameResult(email, "Perro", "Perro", true, "2023-07-09"),
            CorrectWordGameResult(email, "Perro", "Perra", false, "2023-07-09")
        )
        coEvery { serviceImpl.getCorrectWordResults(email) } returns flowOf(results)

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
        coEvery { serviceImpl.getCorrectWordResults(email) } returns flowOf(emptyList)

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
        coEvery { serviceImpl.getLRResults(email) } returns flowOf(results)

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
        coEvery { serviceImpl.getTSResults(email) } returns flowOf(results)

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
        coEvery { serviceImpl.getWhereIsTheLetterResults(email) } returns flowOf(expectedResults)

        // When
        val result = resultGamesUseCases.getWhereIsTheLetterResults(email).toList().flatten()

        // Then
        assertEquals(expectedResults, result)
    }


}
