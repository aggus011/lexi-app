package com.example.lexiapp.ui.profesionalhome.resultlink

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import com.example.lexiapp.databinding.ActivityUnsuccessfulLinkBinding
import com.example.lexiapp.ui.profesionalhome.ProfesionalHomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UnsuccessfulLinkActivity : AppCompatActivity() {
    lateinit var binding: ActivityUnsuccessfulLinkBinding
    private val vM: ProfesionalHomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityUnsuccessfulLinkBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
    }
}