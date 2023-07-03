package com.example.lexiapp.ui.objectives

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lexiapp.databinding.ActivityCompletedObjectiveBinding
import com.example.lexiapp.ui.adapter.ObjectiveAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CompletedObjectiveActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCompletedObjectiveBinding
    private val vM: ObjectivesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompletedObjectiveBinding.inflate(LayoutInflater.from(this))
        setListener()
        setContentView(binding.root)
        setRecycler()
    }

    private fun setListener() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun setRecycler() {
        binding.rvCompleteObjectives.layoutManager= LinearLayoutManager(this)
        suscribeToVM()
    }

    private fun suscribeToVM() {
        vM.completedObjectives.observe(this) { objectives ->
            if (objectives.isEmpty()) binding.txtNotHaveObjectives.visibility = View.VISIBLE
                else binding.txtNotHaveObjectives.visibility = View.GONE
            binding.rvCompleteObjectives.adapter = ObjectiveAdapter(objectives)
        }
    }
}