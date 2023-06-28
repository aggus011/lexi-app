package com.example.lexiapp.domain.useCases

import android.util.Log
import com.example.lexiapp.domain.model.gameResult.ResultGame
import com.example.lexiapp.domain.model.gameResult.WhereIsTheLetterResult
import com.example.lexiapp.domain.service.ResultGamesService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ResultGamesUseCases @Inject constructor(
    private val service: ResultGamesService
) {

    suspend fun getWhereIsTheLetterResults(email: String) =
        service.getWhereIsTheLetterResults(email)

    suspend fun getWhereIsCWResults(email: String) =
        service.getCorrectWordResults(email)

    suspend fun getLRResults(email: String) =
        service.getLRResults(email)

    suspend fun getTSResults(email: String) =
        service.getTSResults(email)

    fun getResultsLastWeek(results: List<ResultGame>): Map<String, Triple<Int, Float, Int>> {
        val calendar = Calendar.getInstance()
        val dateKeysMap = sortedMapOf<String, Triple<Int, Float, Int>>()
        val resultsLastWeek = filterResultsByWeek(results)
        val countOfWeek = resultsLastWeek.size
        for (i in 0 until 7) {
            val currentDate = calendar.time

            val filteredResults = resultsLastWeek.filter { result ->
                val resultDate = convertStringToDate(result.date!!)
                isSameDay(resultDate, currentDate)
            }

            val unsuccessCount = filteredResults.count { !it.success }.toFloat()
            val totalDayCount = filteredResults.size
            //val successPercentage = if (totalDayCount > 0) (successCount.toDouble() / totalDayCount) * 100 else 0.0

            dateKeysMap[formatDateToString(currentDate)] =
                Triple(totalDayCount, unsuccessCount, countOfWeek)

            calendar.add(Calendar.DAY_OF_YEAR, -1)
        }

        return dateKeysMap.toMap()
    }

    fun filterResultsByWeek(results: List<ResultGame>): List<ResultGame> {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.WEEK_OF_YEAR, -1)
        val filteredResults = results.filter { result ->
            val resultDate = Date(result.date!!.toLong())
            resultDate.after(calendar.time)
        }
        return filteredResults
    }

    private fun convertStringToDate(timeString: String): Date {
        val timeInMillis = timeString.toLong()
        return Date(timeInMillis)
    }

    private fun formatDateToString(time: Date): String {
        val format = SimpleDateFormat("dd/MM", Locale.getDefault())
        return format.format(time)
    }

    private fun isSameDay(date1: Date, date2: Date): Boolean {
        val calendar1 = Calendar.getInstance().apply {
            time = date1
        }
        val calendar2 = Calendar.getInstance().apply {
            time = date2
        }
        return calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR) &&
                calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)
    }

    fun generateWeeklyMap(dateList: List<String>): Pair<Int, Map<String, Int>> {
        val currentDate = System.currentTimeMillis()

        val results = getCountForWeeks(dateList, currentDate)
        return Pair(dateList.size, results)
    }

    private fun getCountForWeeks(resultsList: List<String>, dateRef: Long): Map<String, Int> {
        val oneWeekLess = dateRef - TimeUnit.DAYS.toMillis(7)
        val bucleCount = (System.currentTimeMillis() - oneWeekLess) / (24 * 7 * 60 * 60 * 1000)
        val restList = resultsList.filter { it.toLong() < oneWeekLess }
        val result = mutableMapOf<String, Int>()
        val actualList = resultsList.filter {
            it.toLong() >= oneWeekLess
        }.toMutableList()
        result[getWeekFromDate(oneWeekLess, dateRef)] = actualList.size
        if (bucleCount < 5) {
            result.putAll(getCountForWeeks(restList, oneWeekLess))
        }
        return result
    }

    private fun getWeekFromDate(date: Long, currentDate: Long): String {
        val dateFormat = SimpleDateFormat("dd/MM", Locale.getDefault())

        val dateCalendar = Calendar.getInstance().apply {
            timeInMillis = date
        }

        val currentCalendar = Calendar.getInstance().apply {
            timeInMillis = currentDate
        }

        val weekStartDate = dateFormat.format(dateCalendar.time)
        val weekEndDate = dateFormat.format(currentCalendar.time)

        return "$weekStartDate - $weekEndDate"
    }
}
