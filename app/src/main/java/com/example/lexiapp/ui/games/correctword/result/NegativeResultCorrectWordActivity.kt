package com.example.lexiapp.ui.games.correctword.result

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.lexiapp.R
import com.example.lexiapp.databinding.ActivityNegativeResultCorrectWordBinding
import com.example.lexiapp.ui.games.correctword.CorrectWordActivity
import com.example.lexiapp.ui.games.correctword.CorrectWordViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NegativeResultCorrectWordActivity : AppCompatActivity() {
    lateinit var binding: ActivityNegativeResultCorrectWordBinding
    private val vM: CorrectWordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNegativeResultCorrectWordBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        getValues()
        setListeners()
    }

    private fun getValues() {
        val intent = intent
        val correctWord = intent.getStringExtra("correctWord")
        val selectedWord = intent.getStringExtra("selectedWord")

        setWords(correctWord, selectedWord)
    }

    private fun setWords(correctWord: String?, selectedWord: String?) {
        val tvCorrectWord = TextView(this)
        tvCorrectWord.setTextAppearance(R.style.textItemsBottomNavigation)
        tvCorrectWord.setTextColor(ContextCompat.getColor(this, R.color.white))
        tvCorrectWord.textSize = 24F
        tvCorrectWord.text = correctWord
        tvCorrectWord.setBackgroundColor(ContextCompat.getColor(this, R.color.light_purple))
        tvCorrectWord.setPadding(4, 2, 4, 2)
        binding.lyResultCorrect.addView(tvCorrectWord)

        val tvSelectedWord = TextView(this)
        tvSelectedWord.setTextAppearance(R.style.textItemsBottomNavigation)
        tvSelectedWord.setTextColor(ContextCompat.getColor(this, R.color.white))
        tvSelectedWord.textSize = 24F
        tvSelectedWord.text = selectedWord
        tvSelectedWord.setBackgroundColor(ContextCompat.getColor(this, R.color.light_purple))
        tvSelectedWord.setPadding(4, 2, 4, 2)
        binding.lyAnswer.addView(tvSelectedWord)
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