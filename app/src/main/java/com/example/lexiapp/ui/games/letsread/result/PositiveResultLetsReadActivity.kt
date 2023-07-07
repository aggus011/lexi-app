package com.example.lexiapp.ui.games.letsread.result

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.lexiapp.databinding.ActivityPositiveResultLetsReadBinding
import com.example.lexiapp.ui.games.letsread.DifferenceViewModel
import com.example.lexiapp.ui.games.letsread.ListTextActivity
import com.example.lexiapp.ui.patienthome.HomePatientActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PositiveResultLetsReadActivity : AppCompatActivity() {
    lateinit var binding: ActivityPositiveResultLetsReadBinding
    private val differenceVM: DifferenceViewModel by viewModels()

    private lateinit var nextTextBtn: Button
    private lateinit var homeBtn: Button

    private var isChallengeReading: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPositiveResultLetsReadBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        getExtra()
        getViews()
        setButton()
        checkObjectives()
    }

    private fun getExtra() {
        val hasChallengeReadingExtras = intent.hasExtra("challenge")
        isChallengeReading = if(hasChallengeReadingExtras) {
            intent.getIntExtra("challenge", 0)
        } else {
            0
        }
    }

    private fun getViews() {
        nextTextBtn = binding.btnNextText
        homeBtn = binding.btnGoInit
    }

    private fun setButton() {
        nextTextBtn.setOnClickListener {
            startActivity(Intent(this, ListTextActivity::class.java))
            finish()
        }
        homeBtn.setOnClickListener {
            startActivity(Intent(this, HomePatientActivity::class.java))
            finish()
        }
    }

    private fun checkObjectives() {
        if(isChallengeReading == 1) {
            differenceVM.checkIfObjectivesHasBeenCompleted("RC", "hit", "Lectura Desafío")
        }
        differenceVM.checkIfObjectivesHasBeenCompleted("LR", "hit", "¡Vamos a Leer!")
    }
}