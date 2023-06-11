package com.example.lexiapp.ui.games.correctword.result

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import com.example.lexiapp.databinding.ActivityNegativeResultCorrectWordBinding
import com.example.lexiapp.databinding.ActivityPositiveResultCorrectWordBinding
import com.example.lexiapp.ui.games.correctword.CorrectWordViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NegativeResultCorrectWordActivity : AppCompatActivity() {
    lateinit var binding: ActivityNegativeResultCorrectWordBinding
    private val vM: CorrectWordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNegativeResultCorrectWordBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
    }
}