package com.example.lexiapp.ui.games.isitsocalled

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.lexiapp.databinding.ActivityIsItSoCalledBinding

class IsItSoCalledActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIsItSoCalledBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityIsItSoCalledBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
    }
}