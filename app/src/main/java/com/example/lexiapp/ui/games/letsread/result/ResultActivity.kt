package com.example.lexiapp.ui.games.letsread.result

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import com.example.lexiapp.R
import com.example.lexiapp.databinding.ActivityResultsLetsReadBinding
import com.example.lexiapp.ui.games.letsread.DifferenceViewModel
import com.example.lexiapp.ui.games.letsread.ListTextActivity
import com.example.lexiapp.ui.patienthome.HomePatientActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResultActivity() : AppCompatActivity() {

    private lateinit var binding: ActivityResultsLetsReadBinding
    private val vM: DifferenceViewModel by viewModels()

    private lateinit var nameTextTextView: TextView
    private lateinit var resultTextView: TextView
    private lateinit var listenRecordingBtn: Button
    private lateinit var nextTextBtn: Button
    private lateinit var backBtn: Button
    private lateinit var homeBtn: Button
    private lateinit var originalText: String
    private lateinit var results: String
    private lateinit var title: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResultsLetsReadBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        getViews()
        setButtons()
        getExtras()
        setResults(results)

    }

    private fun getViews() {
        nameTextTextView = binding.txtNameText
        resultTextView = binding.textScrollView
        //listenRecordingBtn = binding.listenMyRecording
        nextTextBtn = binding.btnNextText
        backBtn = binding.goBackBtn
        homeBtn = binding.homeBtn
    }

    private fun setButtons() {
        setBackBtn()
        setNextTextBtn()
        setButtonHome()
    }

    private fun setNextTextBtn() {
        nextTextBtn.setOnClickListener {
            startActivity(Intent(this, ListTextActivity::class.java))
        }
    }

    private fun setBackBtn() {
        backBtn.setOnClickListener {
            finish()
        }
    }

    private fun setButtonHome() {
        homeBtn.setOnClickListener {
            finish()
        }
    }

    private fun getExtras() {
        results = intent.getStringExtra("results").toString()
        title = intent.getStringExtra("title").toString()
    }

    private fun setResults(results: String) {
        resultTextView.text = HtmlCompat.fromHtml(results, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

}