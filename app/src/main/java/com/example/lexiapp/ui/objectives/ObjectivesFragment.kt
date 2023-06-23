package com.example.lexiapp.ui.objectives

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
import com.example.lexiapp.ui.adapter.ObjectivesAdapter
import dagger.hilt.android.AndroidEntryPoint
import com.google.firebase.auth.FirebaseAuth
import android.os.Looper

@AndroidEntryPoint
class ObjectivesFragment : Fragment() {
    private var _binding: FragmentObjectivesBinding? = null
    private val binding get() = _binding!!
    private lateinit var rvObjectives: RecyclerView
    private lateinit var objectivesAdapter: ObjectivesAdapter
   // private val handler = Handler()

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
        }, 200) // Delay de 1 segundo (1000 milisegundos)
    }

    private fun loadObjectives() {
        objectivesViewModel.objectives.observe(viewLifecycleOwner) { objectives ->
            objectivesAdapter.updateObjectiveList(objectives)
            objectivesAdapter.notifyDataSetChanged()
        }

        objectivesViewModel.daysLeft.observe(viewLifecycleOwner) { daysLeft ->
            binding.txtDaysLeft.text = resources.getQuantityString(R.plurals.days_left, daysLeft, daysLeft)
        }
    }

    private fun saveObjectives() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val email = currentUser?.email
        if (email != null) {
            objectivesViewModel.saveObjectivesToFirestore(email)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
