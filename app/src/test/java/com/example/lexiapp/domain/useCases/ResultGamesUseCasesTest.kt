package com.example.lexiapp.domain.useCases

import android.icu.util.Calendar
import com.example.lexiapp.data.network.ResultGamesServiceImpl
import com.example.lexiapp.domain.model.gameResult.LetsReadGameResult
import com.example.lexiapp.domain.model.gameResult.ResultGame
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.sql.Date
import java.sql.Time
import java.util.concurrent.TimeUnit


internal class ResultGamesUseCasesTest {

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
}