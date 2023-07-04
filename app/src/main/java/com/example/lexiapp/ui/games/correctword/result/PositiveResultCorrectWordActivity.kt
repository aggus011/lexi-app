package com.example.lexiapp.ui.games.correctword.result

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import com.example.lexiapp.databinding.ActivityCorrectWordBinding
import com.example.lexiapp.databinding.ActivityPositiveResultCorrectWordBinding
import com.example.lexiapp.ui.games.correctword.CorrectWordActivity
import com.example.lexiapp.ui.games.correctword.CorrectWordViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PositiveResultCorrectWordActivity : AppCompatActivity() {

    lateinit var binding: ActivityPositiveResultCorrectWordBinding
    private val vM: CorrectWordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPositiveResultCorrectWordBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        checkObjectives()
        setListeners()
    }

    private fun checkObjectives() {
        vM.checkIfObjectivesHasBeenCompleted("CW", "hit", "Palabra Correcta")
    }

    private fun setListeners() {
        goToNextWord()
        goToHome()
    }

    private fun goToNextWord() {
        binding.btnNextWord.setOnClickListener {
            startActivity(Intent(this, CorrectWordActivity::class.java))
            finish()
        }
    }

    private fun goToHome() {
        binding.btnGoInit.setOnClickListener {
            finish()
        }
    }
}