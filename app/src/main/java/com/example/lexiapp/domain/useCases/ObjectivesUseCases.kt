package com.example.lexiapp.domain.useCases

import com.example.lexiapp.domain.model.MiniObjective
import com.example.lexiapp.domain.model.Objective
import com.example.lexiapp.domain.service.FireStoreService
import com.example.lexiapp.domain.service.ObjectivesService
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.*
import javax.inject.Inject


class ObjectivesUseCases @Inject constructor(
    private val objectivesService: ObjectivesService,
    private val fireStoreService: FireStoreService
) {


    fun getObjectives(startDate: LocalDate): List<Objective> {
        return objectivesService.getObjectives(startDate)
    }

    fun calculateDaysLeft(startDate: LocalDate): Int {
        val today = LocalDate.now(ZoneId.of("America/Argentina/Buenos_Aires"))
        val nextMonday = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY))

        val daysLeft = nextMonday.dayOfWeek.value - today.dayOfWeek.value

        return if (daysLeft < 0) {
            7 - (today.dayOfWeek.value - nextMonday.dayOfWeek.value)
        } else {
            daysLeft
        }
    }

    suspend fun listenerCompleteObjectives(uid: String) = objectivesService.getCompleteObjectives(uid)

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

    fun getCurrentUser (): String? {
        val currentUser = FirebaseAuth.getInstance().currentUser
        var uid = currentUser?.uid
        return uid
    }
    suspend fun saveObjectives(email: String) {
        val lastMondayDate = getMondayDateOfPreviousWeeks(0)
        val objectivesExist = fireStoreService.checkObjectivesExist(email, lastMondayDate)
        if (!objectivesExist) {
            val today = LocalDate.now()
            val monday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            val objectives = getObjectives(monday)
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

    suspend fun getObjectives(uid: String, lastMondayDate: String, listener: (List<Objective>) -> Unit) {
        fireStoreService.getObjectives(uid, lastMondayDate, listener)
    }

}
