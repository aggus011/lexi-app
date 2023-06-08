package com.example.lexiapp.ui.games.whereistheletter.result

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import com.example.lexiapp.databinding.ActivityNegativeResultWhereIsTheLetterBinding
import com.example.lexiapp.ui.games.whereistheletter.WhereIsTheLetterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NegativeResultWhereIsTheLetterActivity : AppCompatActivity() {
    lateinit var binding: ActivityNegativeResultWhereIsTheLetterBinding
    private val vM: WhereIsTheLetterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNegativeResultWhereIsTheLetterBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
    }
}