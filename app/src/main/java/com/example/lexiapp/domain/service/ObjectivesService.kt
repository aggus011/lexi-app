package com.example.lexiapp.domain.service

import com.example.lexiapp.domain.model.Objective
import java.time.LocalDate

interface ObjectivesService {
    fun getObjectives(startDate: LocalDate): List<Objective>

}
