package com.example.lexiapp.domain.useCases

import com.example.lexiapp.domain.model.Objective
import com.example.lexiapp.domain.service.ObjectivesService
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject


class ObjectivesUseCases @Inject constructor(private val objectivesService: ObjectivesService) {
    fun getObjectives(startDate: LocalDate): List<Objective> {
        return objectivesService.getObjectives(startDate)
    }

    fun calculateDaysLeft(startDate: LocalDate): Int {
        val today = LocalDate.now()
        val nextMonday = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY))

        val daysLeft = nextMonday.dayOfWeek.value - today.dayOfWeek.value

        return if (daysLeft < 0) {
            7 - (today.dayOfWeek.value - nextMonday.dayOfWeek.value)
        } else {
            daysLeft
        }
    }


}
