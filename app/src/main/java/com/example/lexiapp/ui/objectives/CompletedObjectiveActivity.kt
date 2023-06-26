package com.example.lexiapp.ui.objectives

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lexiapp.databinding.ActivityCompletedObjectiveBinding
import com.example.lexiapp.ui.adapter.ObjectiveAdapter
import com.example.lexiapp.ui.adapter.TextAdapter
import com.example.lexiapp.ui.games.letsread.LetsReadActivity
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CompletedObjectiveActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCompletedObjectiveBinding
    private val vM: ObjectivesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompletedObjectiveBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        setRecycler()
    }

    private fun setRecycler() {
        binding.rvCompleteObjectives.layoutManager= LinearLayoutManager(this)
        suscribeToVM()
    }

    private fun suscribeToVM() {
        vM.completedObjectives.observe(this) { objectives ->
            binding.rvCompleteObjectives.adapter = ObjectiveAdapter(objectives)
        }
    }
}