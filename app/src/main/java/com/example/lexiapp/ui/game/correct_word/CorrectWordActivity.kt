package com.example.lexiapp.ui.game.correct_word

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lexiapp.databinding.ActivityCorrectWordBinding

class CorrectWordActivity : AppCompatActivity() {
    lateinit var binding: ActivityCorrectWordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityCorrectWordBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}