package com.example.lexiapp.ui.games.letsread

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.lexiapp.databinding.ActivityLetsReadBinding

class LetsReadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLetsReadBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLetsReadBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
    }
}