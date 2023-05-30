package com.example.lexiapp.ui.games.letsread

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.lexiapp.R
import com.example.lexiapp.databinding.ActivityResultsLetsReadBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResultActivity() : AppCompatActivity() {

    private lateinit var binding: ActivityResultsLetsReadBinding
    private val vM: SpeechToTextViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResultsLetsReadBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        makeVisibleGone()

        vM.difference.observe(this) {
            val result = vM.convertToText()
            manageResult(result)
        }


    }

    private fun manageResult(result: String) {
        if (result == "Correct")
            setUpPositiveResult()
        else setUpNegativeResult()
    }

    private fun setUpNegativeResult() {
        binding.btnHome.visibility = View.VISIBLE
        binding.btnNextText.visibility = View.VISIBLE
        binding.btnTryAgain.visibility = View.VISIBLE
        binding.ivCenterIcon.visibility = View.VISIBLE
        binding.tvTitle.visibility = View.VISIBLE
        binding.btnWordsToExercise.visibility = View.VISIBLE
        binding.tvPhraseNegativeResult.visibility = View.VISIBLE
    }

    private fun setUpPositiveResult() {
        binding.btnHome.visibility = View.VISIBLE
        binding.btnNextText.visibility = View.VISIBLE
        binding.ivCenterIcon.setImageResource(R.drawable.ic_hand_clap)
        binding.ivCenterIcon.visibility = View.VISIBLE
        binding.tvTitle.setText(R.string.phrase_positive_result)
        binding.tvTitle.visibility = View.VISIBLE

    }

    private fun makeVisibleGone() {
        binding.btnHome.visibility = View.GONE
        binding.btnNextText.visibility = View.GONE
        binding.btnTryAgain.visibility = View.GONE
        binding.ivCenterIcon.visibility = View.GONE
        binding.tvTitle.visibility = View.GONE
        binding.btnWordsToExercise.visibility = View.GONE
        binding.tvPhraseNegativeResult.visibility = View.GONE
    }

}