package com.example.lexiapp.ui.games.whereistheletter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.lexiapp.databinding.ActivityWhereIsTheLetterBinding

class WhereIsTheLetterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWhereIsTheLetterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWhereIsTheLetterBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
    }
}