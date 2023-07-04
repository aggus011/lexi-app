package com.example.lexiapp.ui.profesionalhome.detailpatient

import android.content.ContentValues.TAG
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.example.lexiapp.databinding.FragmentDetailPatientBinding
import androidx.fragment.app.activityViewModels
import com.example.lexiapp.R
import com.example.lexiapp.domain.model.FirebaseResult
import com.example.lexiapp.domain.model.User
import com.example.lexiapp.ui.profesionalhome.ProfesionalHomeViewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class DetailPatientFragment : Fragment() {

    private var _binding: FragmentDetailPatientBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfesionalHomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailPatientBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //setVisibilityCards()
        setObservers()
        setBtnClose()
    }

    private fun setVisibilityCards() {
        binding.cvMetricsCW.visibility = View.GONE
        binding.cvMetricsWITL.visibility = View.GONE
        binding.cvMetricsCW.visibility = View.GONE
    }

    private fun setView() {
        binding.txtDate.text = SimpleDateFormat("HH:mm:ss").format(Date())
    }

    private fun setObservers() {
        viewModel.patientSelected.observe(viewLifecycleOwner) { patient ->
            if (patient != null) bind(patient)
            setView()
        }
        setWITLObservers()
        setCWObservers()
        setLRObservers()
        setTSObservers()
    }

    private fun setTSObservers() {
        viewModel.allDataTS.observe(viewLifecycleOwner) { (total, dataMap) ->
            setVisibilityTSCard(total, dataMap)
        }
    }

    private fun setVisibilityTSCard(total: Int, dataMap: Map<String, Int>) {
        Log.v("LOG_TEXT_SCANN_FRAGMENT", "${total}")
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
        viewModel.totalTimesPlayedLR.observe(viewLifecycleOwner) {
            binding.cvMetricsLR.visibility =
                if (it != 0) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
        }
        viewModel.totalPieLR.observe(viewLifecycleOwner) { (total, percentError) ->
            setPieGraph(
                total,
                percentError,
                binding.pieTotalChartLR,
                binding.txtTitlePieTotalGraphLR
            )
        }
        viewModel.weekPieLR.observe(viewLifecycleOwner) { (countCorrect, countError) ->
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
        viewModel.resultsLastWeekLR.observe(viewLifecycleOwner) { resultsLastWeek ->
            setBarGraph(resultsLastWeek, binding.barChartLR)
        }
    }

    private fun setCWObservers() {
        viewModel.wasNotPlayedCW.observe(viewLifecycleOwner) {
            binding.cvMetricsCW.visibility = if (it) View.VISIBLE else View.GONE
        }
        viewModel.totalPieCW.observe(viewLifecycleOwner) { (total, percentError) ->
            setPieGraph(
                total,
                percentError,
                binding.pieTotalChartCW,
                binding.txtTitlePieTotalGraphCW
            )
        }
        viewModel.weekPieCW.observe(viewLifecycleOwner) { (countCorrect, countError) ->
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
        viewModel.hardWordsCW.observe(viewLifecycleOwner) { letter ->
            binding.txtValueLettersDificultsCW.text = letter.toString()
        }
        viewModel.resultsLastWeekCW.observe(viewLifecycleOwner) { resultsLastWeek ->
            setBarGraph(resultsLastWeek, binding.barChartCW)
        }
    }

    private fun setWITLObservers() {
        viewModel.wasNotPlayedWITL.observe(viewLifecycleOwner) {
            binding.cvMetricsWITL.visibility = if (it) View.VISIBLE else View.GONE
        }
        viewModel.totalPieWITL.observe(viewLifecycleOwner) { (total, percentError) ->
            setPieGraph(
                total,
                percentError,
                binding.pieTotalChartWITL,
                binding.txtTitlePieTotalGraphWITL
            )
        }
        viewModel.weekPieWITL.observe(viewLifecycleOwner) { (countCorrect, countError) ->
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
        viewModel.hardLettersWITL.observe(viewLifecycleOwner) { letter ->
            binding.txtValueLettersDificultsWITL.text = letter.toString()
        }
        viewModel.resultsLastWeekWITL.observe(viewLifecycleOwner) { resultsLastWeek ->
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
            Log.d(TAG, "Fallo: ${e.javaClass}")
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
            Log.d(TAG, "Fallo2: ${e.javaClass}")
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
        val builder = AlertDialog.Builder(requireActivity())
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
        viewModel.resultDeletePatient.observe(viewLifecycleOwner) {
            if (it == FirebaseResult.TaskSuccess)
                requireActivity().supportFragmentManager.popBackStack()
            else Toast.makeText(
                activity,
                "No se pudo desvincular al paciente, inténtelo más tarde",
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    private fun setBtnClose() {
        binding.icClose.setOnClickListener {
            requireActivity()
                .supportFragmentManager
                .popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDetach() {
        super.onDetach()
        viewModel.cleanPatient()
    }
}