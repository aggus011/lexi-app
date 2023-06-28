package com.example.lexiapp.domain.useCases

import com.example.lexiapp.domain.service.FireStoreService
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

class GetObjectiveCases @Inject constructor(
    private val fireStoreService: FireStoreService,
    private val objectivesUseCases: ObjectivesUseCases
) {
    suspend fun saveObjectivesToFirestore(email: String) {
        val lastMondayDate = getMondayDateOfPreviousWeeks(0)

        val objectivesExist = fireStoreService.checkObjectivesExist(email, lastMondayDate)
        if (!objectivesExist) {
            val today = LocalDate.now()
            val monday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            val objectives = objectivesUseCases.getObjectives(monday)
            fireStoreService.saveObjectives(email, objectives ?: emptyList())

            val loadObjectivesFromTwoWeeksAgo = getMondayDateOfPreviousWeeks(1)
            val lastObjectivesExist = fireStoreService.checkObjectivesExist(email, loadObjectivesFromTwoWeeksAgo)
            if (lastObjectivesExist) {
                val incompleteGames = fireStoreService.getIncompleteGameNames(email, loadObjectivesFromTwoWeeksAgo)
                fireStoreService.increaseGoalForGames(email, incompleteGames)
            }
        }
    }

    private fun getMondayDateOfPreviousWeeks(weeks: Int): String {
        val timeZone = ZoneId.of("America/Argentina/Buenos_Aires")
        val currentDate = LocalDate.now(timeZone)
        val lastMonday = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            .atStartOfDay(timeZone).toLocalDate()
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        return lastMonday.minusWeeks(weeks.toLong()).format(formatter)
    }
}
