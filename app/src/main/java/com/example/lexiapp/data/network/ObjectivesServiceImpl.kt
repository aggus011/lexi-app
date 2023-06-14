package com.example.lexiapp.data.network

import com.example.lexiapp.data.repository.objectives.ObjectiveMocks
import com.example.lexiapp.domain.model.Objective
import com.example.lexiapp.domain.service.ObjectivesService
import java.time.LocalDate
import javax.inject.Inject

class ObjectivesServiceImpl @Inject constructor() : ObjectivesService {

    private val objectiveMocks = ObjectiveMocks

    override fun getObjectives(startDate: LocalDate): List<Objective> {
        return objectiveMocks.getObjectiveMocks()
    }

}
