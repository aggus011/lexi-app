package com.example.lexiapp.ui.games

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lexiapp.R
import com.example.lexiapp.databinding.ActivityLetterGameBinding
import com.example.lexiapp.viewModel.LetterGameViewModel

class LetterGameActivity(val viewModel: LetterGameViewModel) : AppCompatActivity() {

    lateinit var binding: ActivityLetterGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLetterGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}