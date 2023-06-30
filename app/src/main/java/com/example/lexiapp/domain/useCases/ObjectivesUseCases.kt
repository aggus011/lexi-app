package com.example.lexiapp.domain.useCases

import com.example.lexiapp.domain.model.MiniObjective
import com.example.lexiapp.domain.model.Objective
import com.example.lexiapp.domain.service.FireStoreService
import com.example.lexiapp.domain.service.ObjectivesService
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters
import java.util.*
import javax.inject.Inject


class ObjectivesUseCases @Inject constructor(
    private val objectivesService: ObjectivesService,
) {
    data class TimeLeft(val daysLeft: Int, val hoursLeft: Int)

    private fun getObjectives(startDate: LocalDate): List<Objective> {
        return objectivesService.getObjectives(startDate)
    }

    fun calculateDaysLeft(): TimeLeft {
        val today = LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires"))
        val nextMonday = today.with(TemporalAdjusters.next(DayOfWeek.MONDAY))
            .withHour(0).withMinute(0).withSecond(0)
        val hoursLeft = ChronoUnit.HOURS.between(today, nextMonday)
        val daysLeft = ChronoUnit.DAYS.between(today.toLocalDate(), nextMonday.toLocalDate())

        return TimeLeft(daysLeft.toInt(), hoursLeft.toInt())
    }



    suspend fun listenerCompleteObjectives() = objectivesService.getCompleteObjectives()


    fun filterBeforeActualWeek(results: List<MiniObjective>): List<MiniObjective> {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.WEEK_OF_YEAR, -1)
        val filteredResults = results.filter { result ->
            val resultDate = Date(result.date.toLong())
            resultDate.before(calendar.time)
        }
        filteredResults.map { it.copy(date = formatDate(it.date)) }
        return filteredResults
    }

    private fun formatDate(timeInMillis: String): String {
        val date = Date(timeInMillis.toLong())
        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return format.format(date)
    }

    suspend fun saveObjectives() {
        val lastMondayDate = getMondayDateOfPreviousWeeks(0)
        val objectivesExist = objectivesService.checkObjectivesExist(lastMondayDate)
        if (!objectivesExist) {
            val today = LocalDate.now()
            val monday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            val objectives = getObjectives(monday)
            objectivesService.saveObjectives(objectives ?: emptyList())
            val loadObjectivesFromTwoWeeksAgo = getMondayDateOfPreviousWeeks(1)
            val lastObjectivesExist = objectivesService.checkObjectivesExist(loadObjectivesFromTwoWeeksAgo)
            if (lastObjectivesExist) {
                val incompleteGames = objectivesService.getIncompleteGameNames(loadObjectivesFromTwoWeeksAgo)
                objectivesService.increaseGoalForGames(incompleteGames)
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

    suspend fun getObjectivesActual(lastMondayDate: String, listener: (List<Objective>) -> Unit) {
        objectivesService.getObjectivesActual(lastMondayDate, listener)
    }


}
