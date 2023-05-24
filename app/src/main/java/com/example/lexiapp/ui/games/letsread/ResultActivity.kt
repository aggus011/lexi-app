package com.example.lexiapp.ui.games.letsread

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.example.lexiapp.databinding.ActivityResultsLetsReadBinding

class ResultActivity(): Activity() {

    private lateinit var binding: ActivityResultsLetsReadBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResultsLetsReadBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        makeVisibleGone()
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