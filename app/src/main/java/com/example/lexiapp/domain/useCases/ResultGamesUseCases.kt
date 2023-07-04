package com.example.lexiapp.domain.useCases

import com.example.lexiapp.domain.model.gameResult.ResultGame
import com.example.lexiapp.domain.service.ResultGamesService
import java.text.SimpleDateFormat
import java.util.*
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
        val dateKeysMap = mutableMapOf<String, Triple<Int, Float, Int>>()
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

        return sortMapResultGameByDate(dateKeysMap)
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

    private fun sortMapResultGameByDate(map: Map<String, Triple<Int, Float, Int>>): Map<String, Triple<Int, Float, Int>> {
        val dateFormatter = SimpleDateFormat("dd/MM", Locale.getDefault())
        val sortedKeys = map.keys.sortedBy { dateFormatter.parse(it) }
        return sortedKeys.associateWith { key -> map[key]!! }
    }

    private fun sortMapScanTextByDate(map: Map<String, Int>): Map<String, Int> {
        val dateFormatter = SimpleDateFormat("dd/MM", Locale.getDefault())
        val sortedKeys = map.keys.sortedBy { dateFormatter.parse(it) }
        return sortedKeys.associateWith { key -> map[key]!! }
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
        val results = getScansLastWeek(dateList)
        return Pair(dateList.size, results)
    }

    private fun getScansLastWeek(allScans: List<String>): Map<String, Int>{
        val calendar = Calendar.getInstance()
        val dateKeysMap = mutableMapOf<String, Int>()
        val resultsLastWeek = filterScansByWeek(allScans)
        for (i in 0 until 7) {
            val currentDate = calendar.time
            val filteredResults = resultsLastWeek.filter { scanDate ->
                val resultDate = convertStringToDate(scanDate)
                isSameDay(resultDate, currentDate)
            }
            val totalDayCount = filteredResults.size
            dateKeysMap[formatDateToString(currentDate)] = totalDayCount
            calendar.add(Calendar.DAY_OF_YEAR, -1)
        }
        return sortMapScanTextByDate(dateKeysMap)
    }

    private fun filterScansByWeek(allScans: List<String>): List<String> {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.WEEK_OF_YEAR, -1)
        val filteredResults = allScans.filter { scanDate ->
            val resultDate = Date(scanDate.toLong())
            resultDate.after(calendar.time)
        }
        return filteredResults
    }
}
