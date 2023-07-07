package com.example.lexiapp.ui.profesionalhome.detailpatient

import android.content.ContentValues
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.lexiapp.R
import com.example.lexiapp.databinding.ActivityDetailPatientBinding
import com.example.lexiapp.domain.model.FirebaseResult
import com.example.lexiapp.domain.model.User
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class DetailPatientActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailPatientBinding
    private val viewModel: DetailPatientViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPatientBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        setPatientData(getPatient())
        setObservers()
        setBtnClose()
    }

    private fun getPatient(): User {
        val patient = intent.getStringExtra("patient")
        return Gson().fromJson(patient, User::class.java)
    }

    private fun setPatientData(patient: User) {
        viewModel.setPatientSelected(patient)
    }

    private fun setView() {
        binding.txtDate.text = SimpleDateFormat("HH:mm:ss").format(Date())
    }

    private fun setObservers() {
        viewModel.patientSelected.observe(this) { patient ->
            if (patient != null) bind(patient)
            setView()
        }
        setWITLObservers()
        setCWObservers()
        setLRObservers()
        setTSObservers()
    }

    private fun setTSObservers() {
        viewModel.allDataTS.observe(this) { (total, dataMap) ->
            setVisibilityTSCard(total, dataMap)
        }
    }

    private fun setVisibilityTSCard(total: Int, dataMap: Map<String, Int>) {
        progressBarOff()
        if (total > 0 && dataMap.entries.isNotEmpty()) {
            binding.txtValueTotalUsesTS.text = "$total"
            binding.txtNotUseTS.visibility = View.GONE
            binding.txtTitleGraphTS.visibility = View.VISIBLE
            binding.barChartTS.visibility = View.VISIBLE
            binding.cvMetricsTS.visibility = View.VISIBLE
            setTSGraph(dataMap)
            return
        }
        if (total == 0) {
            binding.cvMetricsTS.visibility = View.GONE
        } else {
            binding.txtNotUseTS.visibility = View.VISIBLE
            binding.txtTitleGraphTS.visibility = View.GONE
            binding.barChartTS.visibility = View.GONE
        }
    }

    private fun setLRObservers() {
        viewModel.totalTimesPlayedLR.observe(this) {
            binding.cvMetricsLR.visibility =
                if (it != 0) {
                    progressBarOff()
                    View.VISIBLE
                } else {
                    View.GONE
                }
        }
        viewModel.totalPieLR.observe(this) { (total, percentError) ->
            setPieGraph(
                total,
                percentError,
                binding.pieTotalChartLR,
                binding.txtTitlePieTotalGraphLR
            )
        }
        viewModel.weekPieLR.observe(this) { (countCorrect, countError) ->
            if ((countCorrect + countError).toInt() == 0) {
                setGraphsVisibility(
                    binding.pieWeekChartLR,
                    binding.txtTitlePieWeekGraphLR,
                    binding.barChartLR,
                    binding.txtTitleGraphLR,
                    binding.txtNotHaveWeekProgresLR,
                    binding.txtTitlePieTotalGraphLR
                )
            } else {
                setPieGraph(
                    countCorrect,
                    countError,
                    binding.pieWeekChartLR,
                    binding.txtTitlePieWeekGraphLR
                )
            }
        }
        viewModel.resultsLastWeekLR.observe(this) { resultsLastWeek ->
            setBarGraph(resultsLastWeek, binding.barChartLR)
        }
        viewModel.errorWordsLR.observe(this) { errorWords ->
            binding.txtValueWordsDificultsLR.text = errorWords.toString()
        }
    }

    private fun setCWObservers() {
        viewModel.wasNotPlayedCW.observe(this) {
            binding.cvMetricsCW.visibility = if (it) {
                progressBarOff()
                View.VISIBLE
            } else View.GONE
        }
        viewModel.totalPieCW.observe(this) { (total, percentError) ->
            setPieGraph(
                total,
                percentError,
                binding.pieTotalChartCW,
                binding.txtTitlePieTotalGraphCW
            )
        }
        viewModel.weekPieCW.observe(this) { (countCorrect, countError) ->
            if ((countCorrect + countError).toInt() == 0) {
                setGraphsVisibility(
                    binding.pieWeekChartCW,
                    binding.txtTitlePieWeekGraphCW,
                    binding.barChartCW,
                    binding.txtTitleGraphCW,
                    binding.txtNotHaveWeekProgresCW,
                    binding.txtTitlePieTotalGraphCW
                )
            } else {
                setPieGraph(
                    countCorrect,
                    countError,
                    binding.pieWeekChartCW,
                    binding.txtTitlePieWeekGraphCW
                )
            }
        }
        viewModel.hardWordsCW.observe(this) { letter ->
            binding.txtValueLettersDificultsCW.text = letter.toString()
        }
        viewModel.resultsLastWeekCW.observe(this) { resultsLastWeek ->
            setBarGraph(resultsLastWeek, binding.barChartCW)
        }
    }

    private fun setWITLObservers() {
        viewModel.wasNotPlayedWITL.observe(this) {
            binding.cvMetricsWITL.visibility = if (it) {
                progressBarOff()
                View.VISIBLE
            } else View.GONE
        }
        viewModel.totalPieWITL.observe(this) { (total, percentError) ->
            setPieGraph(
                total,
                percentError,
                binding.pieTotalChartWITL,
                binding.txtTitlePieTotalGraphWITL
            )
        }
        viewModel.weekPieWITL.observe(this) { (countCorrect, countError) ->
            if ((countCorrect + countError).toInt() == 0) {
                setGraphsVisibility(
                    binding.pieWeekChartWITL,
                    binding.txtTitlePieWeekGraphWITL,
                    binding.barChartWITL,
                    binding.txtTitleGraphWITL,
                    binding.txtNotHaveWeekProgresWITL,
                    binding.txtTitlePieTotalGraphWITL
                )
            } else {
                setPieGraph(
                    countCorrect,
                    countError,
                    binding.pieWeekChartWITL,
                    binding.txtTitlePieWeekGraphWITL
                )
            }
        }
        viewModel.hardLettersWITL.observe(this) { letter ->
            binding.txtValueLettersDificultsWITL.text = letter.toString()
        }
        viewModel.resultsLastWeekWITL.observe(this) { resultsLastWeek ->
            setBarGraph(resultsLastWeek, binding.barChartWITL)
        }
    }

    private fun setPieGraph(
        countCorrect: Float,
        countError: Float,
        chart: PieChart,
        titleGraph: TextView
    ) {
        try {
            when (chart) {
                binding.pieTotalChartWITL, binding.pieTotalChartCW, binding.pieTotalChartLR ->
                    titleGraph.text =
                        "${getString(R.string.total_progress)} (${(countCorrect + countError).toInt()})"

                binding.pieWeekChartWITL, binding.pieWeekChartCW, binding.pieWeekChartLR ->
                    titleGraph.text =
                        "${getString(R.string.progress_last_week)} (${(countCorrect + countError).toInt()})"
            }
            val entries = listOf(
                PieEntry(countCorrect, "Total correctos"),
                PieEntry(countError, "Total error")
            )
            val colors = intArrayOf(Color.GREEN, Color.RED)
            val dataSet = PieDataSet(entries, "")
            dataSet.colors = colors.asList()
            dataSet.valueTextSize = 12f
            val data = PieData(dataSet)
            chart.apply {
                this.data = data
                description.isEnabled = false
                setDrawEntryLabels(false)
                setEntryLabelTextSize(12f)
                legend.isEnabled = true
                setDrawMarkers(false)
                animateY(300)
            }
        } catch (e: Exception) {
            Log.d(ContentValues.TAG, "Fallo: ${e.javaClass}")
        }
    }

    private fun setBarGraph(
        resultsLastWeek: Map<String, Triple<Int, Float, Int>>,
        barChart: BarChart
    ) {
        try {
            val entries = mutableListOf<BarEntry>()
            val errorEntries = mutableListOf<BarEntry>()
            val labels = mutableListOf<String>()
            var maxCount = 0
            resultsLastWeek.forEach { (date, thrid) ->
                val countError = thrid.second
                val countDay = thrid.first
                if (maxCount < countDay) maxCount = countDay
                val entry = BarEntry(entries.size.toFloat(), countDay.toFloat())
                entries.add(entry)
                val errorEntry = BarEntry(errorEntries.size.toFloat(), countError)
                errorEntries.add(errorEntry)
                labels.add(date)
            }
            labels[labels.lastIndex] = "Hoy"
            val barDataSet = BarDataSet(entries, "Ejercicios por día")
            barDataSet.setColors(Color.GREEN)
            val errorBarDataSet = BarDataSet(errorEntries, "Errores por día")
            errorBarDataSet.setColors(Color.RED)
            barDataSet.valueTextSize = 12f
            errorBarDataSet.valueTextSize = 12f
            val barData = BarData(barDataSet, errorBarDataSet)
            val granularity = if ((maxCount / 10) > 1) maxCount / 10 else 1
            barData.isHighlightEnabled = false
            barChart.data = barData
            barChart.setFitBars(true)
            barChart.animateY(300)
            barChart.setDrawValueAboveBar(true)
            barChart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
            barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
            barChart.axisLeft.axisMinimum = 0f
            barChart.axisLeft.axisMaximum =
                if (maxCount > 1) (maxCount + granularity).toFloat() else maxCount.toFloat()
            barChart.axisLeft.granularity = granularity.toFloat()
            barChart.axisRight.isEnabled = false
            barChart.legend.isEnabled = true
            barChart.xAxis.setDrawGridLines(false)
            barChart.setScaleEnabled(false)
            barChart.isDragEnabled = false
            barChart.description = null

            barChart.invalidate()
        } catch (e: Exception) {
            Log.d(ContentValues.TAG, "Fallo2: ${e.javaClass}")
        }
    }

    private fun setTSGraph(dataMap: Map<String, Int>) {
        try {
            Log.v("LOG_TEXT_SCANN_FRAGMENT_GRAPH", "${dataMap.keys}")
            Log.v("LOG_TEXT_SCANN_FRAGMENT_GRAPH", "${dataMap.values}")
            val barChart = binding.barChartTS
            var countWeek = 0
            var maxCount = 0
            val entries = mutableListOf<BarEntry>()
            val labels = mutableListOf<String>()
            dataMap.forEach { (date, count) ->
                if (maxCount < count) maxCount = count
                countWeek += count
                entries.add(BarEntry(entries.size.toFloat(), count.toFloat()))
                labels.add(date)
            }
            labels[labels.lastIndex] = "Hoy"
            binding.txtTitleGraphTS.text = "Uso de la última semana ($countWeek)"
            val barDataSet = BarDataSet(entries, "Escaneos por día")
            barDataSet.setColors(Color.GREEN)
            barDataSet.valueTextSize = 12f
            val barData = BarData(barDataSet)
            val granularity = if ((maxCount / 10) > 1) maxCount / 10 else 1
            barData.isHighlightEnabled = false
            barChart.data = barData
            barChart.setFitBars(true)
            barChart.animateY(300)
            barChart.setDrawValueAboveBar(true)
            barChart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
            barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
            barChart.axisLeft.axisMinimum = 0f
            barChart.axisLeft.axisMaximum =
                if (maxCount > 1) (maxCount + granularity).toFloat() else maxCount.toFloat()
            barChart.axisLeft.granularity = granularity.toFloat()
            barChart.axisRight.isEnabled = false
            barChart.legend.isEnabled = true
            barChart.xAxis.setDrawGridLines(false)
            barChart.setScaleEnabled(false)
            barChart.isDragEnabled = false
            barChart.description = null

            barChart.invalidate()
        } catch (e: Exception) {
        }
    }

    private fun setGraphsVisibility(
        pieWeek: PieChart,
        titlePieWeek: TextView,
        barChart: BarChart,
        titleBarGraph: TextView,
        notProgressWeek: TextView,
        titlePieTotal: TextView
    ) {
        pieWeek.visibility = View.GONE
        titleBarGraph.visibility = View.GONE
        barChart.visibility = View.GONE
        titlePieWeek.visibility = View.GONE
        val layoutParams = titlePieTotal.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
        titlePieTotal.layoutParams = layoutParams
        notProgressWeek.visibility = View.VISIBLE
    }

    private fun bind(patient: User) {
        binding.apply {
            txtName.text = patient.userName
            txtEmail.text = patient.email
            tvUserInitials.text =
                if (patient.userName != null && patient.userName!!.isNotEmpty())
                    patient.userName!![0].uppercase()
                else patient.email[0].uppercase()
        }
        binding.btnTrash.setOnClickListener {
            showConfirmationDialog(patient.email)
        }
    }

    private fun showConfirmationDialog(emailPatient: String) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("¿Está segura/o de que quiere desvincular al paciente: $emailPatient?")
            .setPositiveButton("Confirmar") { _, _ ->
                // Llama a la acción de confirmación
                unbindPatient(emailPatient)
            }
            .setNegativeButton("Cancelar", null)
        builder.create().show()
    }

    private fun unbindPatient(email: String) {
        viewModel.unbindPatient(email)
        viewModel.resultDeletePatient.observe(this) {
            if (it == FirebaseResult.TaskSuccess)
                finish()
            else Toast.makeText(
                this,
                "No se pudo desvincular al paciente, inténtelo más tarde",
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    private fun setBtnClose() {
        binding.icClose.setOnClickListener {
            finish()
        }
    }

    private fun progressBarOff(){
        binding.progressBar.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.cleanPatient()
    }
}