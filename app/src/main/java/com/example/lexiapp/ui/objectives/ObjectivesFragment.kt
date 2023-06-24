package com.example.lexiapp.ui.objectives

import ObjectivesAdapter
import android.animation.ObjectAnimator
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lexiapp.R
import com.example.lexiapp.databinding.FragmentObjectivesBinding
import dagger.hilt.android.AndroidEntryPoint
import com.google.firebase.auth.FirebaseAuth
import android.os.Looper
import android.util.Log
import android.widget.Button
import com.example.lexiapp.data.network.FireStoreServiceImpl
import com.example.lexiapp.domain.model.Objective

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
        }, 200)
    }

    private fun loadObjectives() {
        objectivesViewModel.objectives.observe(viewLifecycleOwner) { objectives ->
            objectivesAdapter.updateObjectiveList(objectives)
            objectivesAdapter.notifyDataSetChanged()
            checkObjectivesCompletion(objectives)
        }
        objectivesViewModel.daysLeft.observe(viewLifecycleOwner) { daysLeft ->
            val daysLeftText = resources.getQuantityString(R.plurals.days_left, daysLeft, daysLeft)
            binding.txtDaysLeft.text = daysLeftText
        }
    }

    private fun saveObjectives() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userid = currentUser?.uid
        if (userid != null) {
            objectivesViewModel.saveObjectivesToFirestore(userid)
        }

    }

    private fun checkObjectivesCompletion(objectives: List<Objective>) {
        for (i in 0 until objectivesAdapter.itemCount) {
            Handler(Looper.getMainLooper()).postDelayed({
                val viewHolder = rvObjectives.findViewHolderForAdapterPosition(i)
                if (viewHolder is ObjectivesAdapter.ObjectiveViewHolder) {
                    val button = viewHolder.button
                    val objective = objectives[i]
                    if (objective.progress == objective.goal) {
                        val greenColor = Color.parseColor("#71dea2")
                        button.setBackgroundColor(greenColor)
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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
