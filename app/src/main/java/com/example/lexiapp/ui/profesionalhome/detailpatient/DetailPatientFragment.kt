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
import androidx.fragment.app.Fragment
import com.example.lexiapp.databinding.FragmentDetailPatientBinding
import androidx.fragment.app.activityViewModels
import com.example.lexiapp.R
import com.example.lexiapp.domain.model.FirebaseResult
import com.example.lexiapp.domain.model.User
import com.example.lexiapp.ui.profesionalhome.ProfesionalHomeViewModel
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
        setVisibilityCards()
        setObservers()
    }

    private fun setVisibilityCards() {
        binding.cvMetricsCW.visibility = View.GONE
        binding.cvMetricsWITL.visibility = View.GONE
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
    }

    private fun setLRObservers() {
        viewModel.totalTimesPlayedLR.observe(viewLifecycleOwner) {
            binding.cvMetricsLR.visibility =
                if(it != 0) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
        }
        viewModel.totalPieLR.observe(viewLifecycleOwner) { (total, percentError) ->
            setPieGraph(
                total,
                percentError,
                binding.pieTotalChartLR.id,
                binding.txtTitlePieTotalGraphLR
            )
        }
        viewModel.weekPieLR.observe(viewLifecycleOwner) { (total, percentError) ->
            setPieGraph(
                total,
                percentError,
                binding.pieWeekChartLR.id,
                binding.txtTitlePieWeekGraphLR
            )
        }
        viewModel.resultsLastWeekLR.observe(viewLifecycleOwner) { resultsLastWeek ->
            setBarGraph(resultsLastWeek, binding.barChartLR.id)
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
                binding.pieTotalChartCW.id,
                binding.txtTitlePieTotalGraphCW
            )
        }
        viewModel.weekPieCW.observe(viewLifecycleOwner) { (total, percentError) ->
            setPieGraph(
                total,
                percentError,
                binding.pieWeekChartCW.id,
                binding.txtTitlePieWeekGraphCW
            )
        }
        viewModel.hardWordsCW.observe(viewLifecycleOwner) { letter ->
            binding.txtValueLettersDificultsCW.text = letter.toString()
        }
        viewModel.resultsLastWeekCW.observe(viewLifecycleOwner) { resultsLastWeek ->
            setBarGraph(resultsLastWeek, binding.barChartCW.id)
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
                binding.pieTotalChartWITL.id,
                binding.txtTitlePieTotalGraphWITL
            )
        }
        viewModel.weekPieWITL.observe(viewLifecycleOwner) { (total, percentError) ->
            setPieGraph(
                total,
                percentError,
                binding.pieWeekChartWITL.id,
                binding.txtTitlePieWeekGraphWITL
            )
        }
        viewModel.hardLettersWITL.observe(viewLifecycleOwner) { letter ->
            binding.txtValueLettersDificultsWITL.text = letter.toString()
        }
        viewModel.resultsLastWeekWITL.observe(viewLifecycleOwner) { resultsLastWeek ->
            setBarGraph(resultsLastWeek, binding.barChartWITL.id)
        }
    }

    private fun setPieGraph(
        total: Float,
        errorPercentage: Float,
        idChart: Int,
        titleGraph: TextView
    ) {
        val pieChart = when (idChart) {
            binding.pieTotalChartWITL.id -> binding.pieTotalChartWITL
            binding.pieWeekChartWITL.id -> binding.pieWeekChartWITL
            binding.pieTotalChartCW.id -> binding.pieTotalChartCW
            binding.pieWeekChartCW.id -> binding.pieWeekChartCW
            else -> null
        } ?: return
        try {
            when (idChart) {
                binding.pieTotalChartWITL.id, binding.pieTotalChartCW.id ->
                    titleGraph.text =
                        "${getString(R.string.total_progress)} (${(total + errorPercentage).toInt()})"

                binding.pieWeekChartWITL.id, binding.pieWeekChartCW.id ->
                    titleGraph.text =
                        "${getString(R.string.progress_last_week)} (${(total + errorPercentage).toInt()})"
            }
            val entries = listOf(
                PieEntry(total, "Total correctos"),
                PieEntry(errorPercentage, "Total error")
            )
            val colors = intArrayOf(Color.CYAN, Color.RED)
            val dataSet = PieDataSet(entries, "")
            dataSet.colors = colors.asList()
            dataSet.valueTextSize = 12f
            val data = PieData(dataSet)
            pieChart.apply {
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

    private fun setBarGraph(resultsLastWeek: Map<String, Triple<Int, Float, Int>>, idChart: Int) {
        val barChart = when (idChart) {
            binding.barChartWITL.id -> binding.barChartWITL
            binding.barChartCW.id -> binding.barChartCW
            else -> null
        } ?: return
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
            val barDataSet = BarDataSet(entries, "Ejercícios por día")
            barDataSet.setColors(Color.CYAN)
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
            viewModel.unbindPatient(patient.email)
            viewModel.resultDeletePatient.observe(viewLifecycleOwner) {
                if (it == FirebaseResult.TaskSuccess)
                    requireActivity().supportFragmentManager.popBackStack()
                else Toast.makeText(
                    activity,
                    "No se pudo desvincular al paciente, intentelo mas tarde",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
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