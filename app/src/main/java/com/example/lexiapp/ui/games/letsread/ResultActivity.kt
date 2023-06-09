package com.example.lexiapp.ui.games.letsread

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.lexiapp.R
import com.example.lexiapp.databinding.ActivityResultsLetsReadBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResultActivity() : AppCompatActivity() {

    private lateinit var binding: ActivityResultsLetsReadBinding
    private val vM: SpeechToTextViewModel by viewModels()

    private lateinit var resultTextView: TextView
    private lateinit var originalText: String
    private lateinit var results: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResultsLetsReadBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        resultTextView = binding.tvTitle

        getExtras()
        getResults(originalText, results)
        manageResult()


    }

    private fun getExtras() {
        originalText = intent.getStringExtra("originalText").toString()
        results = intent.getStringExtra("results").toString()
    }

    private fun getResults(originalText: String, results: String) {
        vM.getDifference(originalText = originalText, results = results)
    }

    private fun manageResult() {
        vM.difference.observe(this) {
            val result = vM.convertToText()
            if (result == "Correct")
                resultTextView.text = "¡Tu respuesta es correcta!"
            else resultTextView.text = HtmlCompat.fromHtml(result, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }
    }

}