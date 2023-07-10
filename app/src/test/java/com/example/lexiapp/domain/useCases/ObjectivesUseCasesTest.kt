package com.example.lexiapp.domain.useCases

import com.example.lexiapp.domain.model.MiniObjective
import com.example.lexiapp.domain.model.Objective
import com.example.lexiapp.domain.service.ObjectivesService
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.invoke
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month
import java.time.temporal.TemporalAdjusters

class ObjectivesUseCasesTest {

    @RelaxedMockK
    private lateinit var objectivesService: ObjectivesService

    lateinit var objectivesUseCases: ObjectivesUseCases

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        objectivesUseCases = ObjectivesUseCases(objectivesService)
    }

    @Test
    fun `given a last Monday date, getObjectivesActual should call objectivesService getObjectivesActual`() =
        runBlocking {
            // Given
            val lastMondayDate = "20230626"
            val objectives = listOf(
                Objective(
                    id = 1, title = "Objective test", description = "Description",
                    progress = 0, goal = 10, game = "Game", type = "Type",
                    completed = false, date = "2023-06-26"
                )
            )

            coEvery {
                objectivesService.getObjectivesActual(lastMondayDate, captureLambda())
            } answers {
                val capturedListener = lambda<(List<Objective>) -> Unit>()
                capturedListener.invoke(objectives)
            }

            // When
            objectivesUseCases.getObjectivesActual(lastMondayDate) { result ->
                // Then
                assert(result == objectives)
            }
        }

    @Test
    fun `given objectivesService returns a list of objectives, listenerCompleteObjectives should invoke the listener with the same list`() =
        runBlocking {
            // Given
            val objectives = listOf(
                MiniObjective(date = "2023-06-26", title = "Objective 1", count = 0),
                MiniObjective(date = "2023-06-26", title = "Objective 2", count = 2),
                MiniObjective(date = "2023-06-26", title = "Objective 3", count = 4)
            )

            coEvery { objectivesService.getCompleteObjectives() } returns flow {
                emit(objectives)
            }

            // When
            var result: List<MiniObjective>? = null
            objectivesUseCases.listenerCompleteObjectives().collect { emittedObjectives ->
                result = emittedObjectives
            }

            // Then
            assert(result == objectives)
        }

    @Test
    fun `given objectivesService returns an empty list of objectives, listenerCompleteObjectives should invoke the listener with an empty list`() =
        runBlocking {
            // Given
            val objectives = emptyList<MiniObjective>()

            coEvery { objectivesService.getCompleteObjectives() } returns flow {
                emit(objectives)
            }

            // When
            var result: List<MiniObjective>? = null
            objectivesUseCases.listenerCompleteObjectives().collect { emittedObjectives ->
                result = emittedObjectives
            }

            // Then
            assert(result == objectives)
        }

    @Test
    fun `given no objectives exist for the last Monday, saveObjectives should save the objectives`() =
        runBlocking {
            // Given
            val lastMondayDate = "20230626"
            val today = LocalDate.now()
            val monday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            val objectives = listOf(
                Objective(
                    id = 1, title = "Objective test", description = "Description",
                    progress = 0, goal = 10, game = "Game 1", type = "Type",
                    completed = false, date = "2023-06-26"
                ),
                Objective(
                    id = 2, title = "Objective test 2", description = "Description",
                    progress = 0, goal = 10, game = "Game 2", type = "Type",
                    completed = false, date = "2023-06-26"
                ),
                Objective(
                    id = 3, title = "Objective test 3", description = "Description",
                    progress = 0, goal = 10, game = "Game 3", type = "Type",
                    completed = false, date = "2023-06-26"
                ),
                Objective(
                    id = 4, title = "Objective test 4", description = "Description",
                    progress = 0, goal = 10, game = "Game 4", type = "Type",
                    completed = false, date = "2023-06-26"
                )
            )
            val loadObjectivesFromTwoWeeksAgo = "20230612"
            val incompleteGames = listOf("Game 5", "Game 6")

            coEvery { objectivesService.checkObjectivesExist(lastMondayDate) } returns false
            coEvery { objectivesService.getObjectives(monday) } returns objectives
            coEvery { objectivesService.saveObjectives(objectives) } returns Unit
            coEvery { objectivesService.checkObjectivesExist(loadObjectivesFromTwoWeeksAgo) } returns true
            coEvery { objectivesService.getIncompleteGameNames(loadObjectivesFromTwoWeeksAgo) } returns incompleteGames

            // When
            objectivesUseCases.saveObjectives()

            // Then
            coVerify(exactly = 1) { objectivesService.saveObjectives(objectives) }
        }


}
