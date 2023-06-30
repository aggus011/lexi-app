package com.example.lexiapp.ui.games.whereistheletter.result

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import com.example.lexiapp.databinding.ActivityPositiveResultWhereIsTheLetterBinding
import com.example.lexiapp.ui.games.whereistheletter.WhereIsTheLetterActivity
import com.example.lexiapp.ui.games.whereistheletter.WhereIsTheLetterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PositiveResultWhereIsTheLetterActivity : AppCompatActivity() {
    lateinit var binding: ActivityPositiveResultWhereIsTheLetterBinding
    private val vM: WhereIsTheLetterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPositiveResultWhereIsTheLetterBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        checkObjectives()
        setListeners()
    }

    private fun checkObjectives() {
        vM.checkIfObjectivesHasBeenCompleted("WL", "hit", "¿Dónde está la letra?")
    }

    private fun setListeners() {
        goToNextWord()
        goToHome()
    }

    private fun goToNextWord() {
        binding.btnNextWord.setOnClickListener {
            startActivity(Intent(this, WhereIsTheLetterActivity::class.java))
            finish()
        }
    }

    private fun goToHome() {
        binding.btnGoInit.setOnClickListener {
            finish()
        }
    }
}