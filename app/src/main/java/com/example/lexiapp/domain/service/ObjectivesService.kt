package com.example.lexiapp.domain.service

import com.example.lexiapp.domain.model.MiniObjective
import com.example.lexiapp.domain.model.Objective
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface ObjectivesService {
    fun getObjectives(startDate: LocalDate): List<Objective>

    suspend fun getCompleteObjectives(): Flow<List<MiniObjective>>

    suspend fun checkObjectivesExist(lastMondayDate: String): Boolean

    suspend fun saveObjectives(objectives: List<Objective>)

    suspend fun getIncompleteGameNames(lastMondayDate: String): List<String>

    suspend fun increaseGoalForGames(games: List<String>)

    suspend fun getObjectivesActual(lastMondayDate: String, listener: (List<Objective>) -> Unit)
}
