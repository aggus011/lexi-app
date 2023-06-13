package com.example.lexiapp.ui.games.letsread.result

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import com.example.lexiapp.databinding.ActivityNegativeResultCorrectWordBinding
import com.example.lexiapp.databinding.ActivityPositiveResultLetsReadBinding
import com.example.lexiapp.ui.games.correctword.CorrectWordViewModel
import com.example.lexiapp.ui.games.letsread.TextViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PositiveResultLetsReadActivity : AppCompatActivity() {
    lateinit var binding: ActivityPositiveResultLetsReadBinding
    private val vM: TextViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPositiveResultLetsReadBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
    }
}