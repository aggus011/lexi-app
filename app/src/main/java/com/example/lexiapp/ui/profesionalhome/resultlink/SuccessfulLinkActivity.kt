package com.example.lexiapp.ui.profesionalhome.resultlink

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import com.example.lexiapp.databinding.ActivitySuccessfulLinkBinding
import com.example.lexiapp.ui.profesionalhome.ProfesionalHomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SuccessfulLinkActivity : AppCompatActivity() {

    lateinit var binding: ActivitySuccessfulLinkBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySuccessfulLinkBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        binding.btnGoInit.setOnClickListener{
            finish()
        }
    }
}