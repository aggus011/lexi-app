package com.example.lexiapp.ui.objectives

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lexiapp.databinding.FragmentObjectivesBinding
import com.example.lexiapp.domain.adapters.objectives.ObjectivesAdapter
import com.example.lexiapp.domain.model.Objective
import com.example.lexiapp.domain.repositories.objectives.ObjectiveMocks


class ObjectivesFragment : Fragment() {
    private var _binding: FragmentObjectivesBinding? = null
    private val binding get() = _binding!!
    private lateinit var objectiveList: List<Objective>
    private lateinit var rvObjective: RecyclerView

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
    }

    private fun getObjectives() {
        objectiveList = ObjectiveMocks.getObjectiveMocks()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setUpRecyclerView() {
        rvObjective = binding.rvObjectives
        rvObjective.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        getObjectives()

        val adapter = ObjectivesAdapter()
        rvObjective.adapter = adapter
        adapter.updateObjectiveList(objectiveList)
        adapter.notifyDataSetChanged()
    }

}