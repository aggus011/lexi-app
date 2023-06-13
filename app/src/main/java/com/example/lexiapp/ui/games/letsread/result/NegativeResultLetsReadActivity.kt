package com.example.lexiapp.ui.games.letsread.result

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import com.example.lexiapp.databinding.ActivityNegativeResultLetsReadBinding
import com.example.lexiapp.ui.games.letsread.TextViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NegativeResultLetsReadActivity : AppCompatActivity() {
    lateinit var binding: ActivityNegativeResultLetsReadBinding
    private val vM: TextViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNegativeResultLetsReadBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
    }
}