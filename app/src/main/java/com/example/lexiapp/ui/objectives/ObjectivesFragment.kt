package com.example.lexiapp.ui.objectives

import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lexiapp.R
import com.example.lexiapp.databinding.FragmentObjectivesBinding
import com.example.lexiapp.domain.model.Objective
import com.example.lexiapp.ui.adapter.ObjectivesAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalTime
import java.time.temporal.ChronoUnit

@AndroidEntryPoint
class ObjectivesFragment : Fragment() {
    private var _binding: FragmentObjectivesBinding? = null
    private val binding get() = _binding!!
    private lateinit var rvObjectives: RecyclerView
    private lateinit var objectivesAdapter: ObjectivesAdapter

    private val objectivesViewModel: ObjectivesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentObjectivesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        loadObjectivesWithDelay()
        setListener()
    }

    private fun setListener() {
        binding.imbCompletedObjectives.setOnClickListener {
            startActivity(Intent(activity, CompletedObjectiveActivity::class.java))
        }
    }

    private fun setUpRecyclerView() {
        objectivesAdapter = ObjectivesAdapter()
        rvObjectives = binding.rvObjectives
        rvObjectives.layoutManager = LinearLayoutManager(requireContext())
        rvObjectives.adapter = objectivesAdapter

    }

    private fun loadObjectivesWithDelay() {
        Handler(Looper.getMainLooper()).postDelayed({
            saveObjectives()
            loadObjectives()
        }, 300)
    }

    private fun loadObjectives() {
        objectivesViewModel.objectives.observe(viewLifecycleOwner) { objectives ->
            objectivesAdapter.updateObjectiveList(objectives)
            objectivesAdapter.notifyDataSetChanged()
            checkObjectivesCompletion(objectives)
        }
        objectivesViewModel.daysLeft.observe(viewLifecycleOwner) { daysLeft ->
            val daysLeftText = resources.getQuantityString(R.plurals.days_left, daysLeft, daysLeft)
            binding.txtDaysLeft.text = if(daysLeftText == "0") calculateHoursLeftToMonday() else daysLeftText
        }
    }

    private fun saveObjectives() {
            objectivesViewModel.saveObjectives()
    }

    private fun checkObjectivesCompletion(objectives: List<Objective>) {
        for (i in 0 until objectivesAdapter.itemCount) {
            Handler(Looper.getMainLooper()).postDelayed({
                val viewHolder = rvObjectives.findViewHolderForAdapterPosition(i)
                if (viewHolder is ObjectivesAdapter.ObjectiveViewHolder) {
                    val button = viewHolder.button
                    val objective = objectives[i]
                    if (objective.progress >= objective.goal!!) {
                        val greenColor = Color.parseColor("#71dea2")
                        val newDrawable = GradientDrawable()
                        newDrawable.shape = GradientDrawable.RECTANGLE
                        newDrawable.cornerRadius = 30f
                        newDrawable.setColor(greenColor)
                        button.background = newDrawable
                        val translationXAnimation = ObjectAnimator.ofFloat(
                            button, "translationX",0f, -30f, 30f, -30f, 30f, 0f
                        )
                        translationXAnimation.duration = 1000
                        translationXAnimation.start()
                    }
                }
            }, 200)
        }
    }

    private fun calculateHoursLeftToMonday(): String {
        val currentTime = LocalTime.now()
        val midnight = LocalTime.MAX
        val timeDifference = currentTime.until(midnight, ChronoUnit.MINUTES)

        val hoursUntilMidnight = timeDifference / 60
        val minutesUntilMidnight = timeDifference % 60

        return "Restan $hoursUntilMidnight horas \n $minutesUntilMidnight minutos"
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
