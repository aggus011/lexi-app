package com.example.lexiapp.ui.games

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lexiapp.R
import com.example.lexiapp.databinding.ActivityLetterGameBinding

class LetterGameActivity : AppCompatActivity() {

    lateinit var binding : ActivityLetterGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLetterGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}