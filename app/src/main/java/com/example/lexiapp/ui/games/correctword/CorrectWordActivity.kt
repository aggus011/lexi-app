package com.example.lexiapp.ui.games.correctword

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.lexiapp.databinding.ActivityCorrectWordBinding

class CorrectWordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCorrectWordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCorrectWordBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
    }
}