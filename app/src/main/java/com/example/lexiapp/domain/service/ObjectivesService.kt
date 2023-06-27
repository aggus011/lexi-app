package com.example.lexiapp.domain.service

import com.example.lexiapp.domain.model.MiniObjective
import com.example.lexiapp.domain.model.Objective
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface ObjectivesService {
    fun getObjectives(startDate: LocalDate): List<Objective>

    suspend fun getCompleteObjectives(uid: String): Flow<List<MiniObjective>>
}
