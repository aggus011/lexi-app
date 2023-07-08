package com.example.lexiapp.ui.games.letsread.result

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.lexiapp.databinding.ActivityNegativeResultLetsReadBinding
import com.example.lexiapp.ui.games.letsread.DifferenceViewModel
import com.example.lexiapp.ui.games.letsread.ListTextActivity
import com.example.lexiapp.ui.patienthome.HomePatientActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NegativeResultLetsReadActivity : AppCompatActivity() {
    lateinit var binding: ActivityNegativeResultLetsReadBinding

    private lateinit var homeBtn: Button
    private lateinit var nextTextBtn: Button
    private lateinit var seeWordsBtn: Button

    private lateinit var result: String
    private lateinit var title: String
    private var isChallengeReading: Int = 0

    private val differenceVM: DifferenceViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNegativeResultLetsReadBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        getViews()
        getExtra()
        setButtons()
        checkObjectives()
    }

    private fun getViews() {
        homeBtn = binding.btnGoInit
        nextTextBtn = binding.btnNextText
        seeWordsBtn = binding.btnSeeWords
    }

    private fun getExtra() {
        result = intent.getStringExtra("results").toString()
        title = intent.getStringExtra("title").toString()

        val hasChallengeReadingExtras = intent.hasExtra("challenge")
        isChallengeReading = if(hasChallengeReadingExtras) {
            intent.getIntExtra("challenge", 0)
        } else {
            0
        }
    }

    private fun setButtons() {
        goHome()
        goToNextText()
        goToSeeResult()
    }

    private fun goHome() {
        homeBtn.setOnClickListener {
            startActivity(Intent(this, HomePatientActivity::class.java))
            finish()
        }
    }

    private fun goToNextText() {
        nextTextBtn.setOnClickListener {
            startActivity(Intent(this, ListTextActivity::class.java))
            finish()
        }
    }

    private fun goToSeeResult() {
        seeWordsBtn.setOnClickListener {
            val intent = Intent(this, ResultActivity::class.java)
            intent.putExtra("results", result)
            intent.putExtra("title", title)
            startActivity(intent)
        }
    }

    private fun checkObjectives() {
        if(isChallengeReading == 1) {
            differenceVM.checkIfObjectivesHasBeenCompleted("RC", "play", "Lectura Desafío")
        }
        differenceVM.checkIfObjectivesHasBeenCompleted("LR", "play", "¡Vamos a Leer!")
    }

}