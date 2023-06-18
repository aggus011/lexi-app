package com.example.lexiapp.ui.objectives

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lexiapp.R
import com.example.lexiapp.data.api.openaicompletions.OpenAICompletionsGateway
import com.example.lexiapp.databinding.FragmentObjectivesBinding
import com.example.lexiapp.ui.adapter.ObjectivesAdapter
import dagger.hilt.android.AndroidEntryPoint
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
        saveObjectives()
        loadObjectives()
    }


    private fun setUpRecyclerView() {
        rvObjectives = binding.rvObjectives
        objectivesAdapter = ObjectivesAdapter()
        rvObjectives.layoutManager = LinearLayoutManager(requireContext())
        rvObjectives.adapter = objectivesAdapter
    }

    private fun loadObjectives() {
        objectivesViewModel.objectives.observe(viewLifecycleOwner) { objectives ->
            objectivesAdapter.updateObjectiveList(objectives)
        }

        objectivesViewModel.daysLeft.observe(viewLifecycleOwner) { daysLeft ->
            binding.txtDaysLeft.text = resources.getQuantityString(R.plurals.days_left, daysLeft, daysLeft)
        }
    }

    private fun saveObjectives() {
        val email = "asd10@asd.com"
        objectivesViewModel.saveObjectivesToFirestore(email)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
