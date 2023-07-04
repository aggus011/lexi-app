package com.example.lexiapp.ui.games.letsread.result

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import androidx.activity.viewModels
import com.example.lexiapp.databinding.ActivityNegativeResultLetsReadBinding
import com.example.lexiapp.ui.games.letsread.ListTextActivity
import com.example.lexiapp.ui.games.letsread.TextViewModel
import com.example.lexiapp.ui.patienthome.HomePatientActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NegativeResultLetsReadActivity : AppCompatActivity() {
    lateinit var binding: ActivityNegativeResultLetsReadBinding
    private val vM: TextViewModel by viewModels()

    private lateinit var homeBtn: Button
    private lateinit var tryAgainBtn: Button
    private lateinit var nextTextBtn: Button
    private lateinit var seeWordsBtn: Button

    private lateinit var result: String
    private lateinit var title: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNegativeResultLetsReadBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        getViews()
        getExtra()
        setButtons()
    }

    private fun getViews() {
        homeBtn = binding.btnGoInit
        tryAgainBtn = binding.btnTryAgain
        nextTextBtn = binding.btnNextText
        seeWordsBtn = binding.btnSeeWords
    }

    private fun getExtra() {
        result = intent.getStringExtra("results").toString()
        title = intent.getStringExtra("title").toString()
    }

    private fun setButtons() {
        goHome()
        goToTryAgain()
        goToNextText()
        goToSeeResult()
    }

    private fun goHome() {
        homeBtn.setOnClickListener {
            startActivity(Intent(this, HomePatientActivity::class.java))
        }
    }

    private fun goToTryAgain() = finish()

    private fun goToNextText() {
        nextTextBtn.setOnClickListener {
            startActivity(Intent(this, ListTextActivity::class.java))
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

}