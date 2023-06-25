package com.example.lexiapp.ui.games.correctword

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.lexiapp.databinding.ActivityCorrectWordBinding
import com.example.lexiapp.ui.games.correctword.result.NegativeResultCorrectWordActivity
import com.example.lexiapp.ui.games.correctword.result.PositiveResultCorrectWordActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class CorrectWordActivity : AppCompatActivity() {

    private val viewModel: CorrectWordViewModel by viewModels()
    private lateinit var binding: ActivityCorrectWordBinding
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCorrectWordBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        setListeners()
        setObservers()
    }

    private fun setObservers() {
        viewModel.basicWords.observe(this) {
            if (!it.isNullOrEmpty()) {
                waitForValues()
                progressBarOff()
            } else {
                progressBarOn()
            }
        }
    }

    private fun resetGame() {
        viewModel.generateWords()
        progressBarOn()
        waitForValues()
    }

    private fun setListeners() {
        binding.wordOne.setOnClickListener { checkAnswer(binding.wordOne) }
        binding.wordTwo.setOnClickListener { checkAnswer(binding.wordTwo) }
        binding.wordThree.setOnClickListener { checkAnswer(binding.wordThree) }
        binding.wordFour.setOnClickListener { checkAnswer(binding.wordFour) }

        binding.iconVolume.setOnClickListener {
            playWordSound()
        }
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnOtherWord.setOnClickListener {
            resetGame()
        }
    }


    private fun playWordSound() {
        val wordSoundResId = resources.getIdentifier("word_sound.xml", "raw", packageName)
        mediaPlayer = MediaPlayer.create(this, wordSoundResId)
        mediaPlayer?.start()
    }


    override fun onStop() {
        super.onStop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun checkAnswer(selectedButton: Button) {
        if (viewModel.validateAnswer(selectedButton.text.toString())) {
            goToCorrectWordPositiveResultActivity()
        } else {
            goToNegativeResultCorrectActivity(binding.txtVariableWord.text.toString(), selectedButton.text.toString())
        }
    }

    private fun waitForValues() {
        lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                try {
                    setValues()
                } catch (e: Exception) {
                    binding.progressBar3.visibility = View.GONE
                    binding.txtWord.visibility = View.GONE
                    binding.txtVariableWord.visibility = View.GONE
                    binding.iconVolume.visibility = View.GONE
                    Toast.makeText(
                        applicationContext,
                        "NO SE PUDO CARGAR LA PALABRA",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
        }
    }

    private fun progressBarOn() {
        lifecycleScope.launch {
            binding.progressBar3.visibility = View.VISIBLE
            binding.optionWord.visibility = View.GONE
            binding.txtVariableWord.visibility = View.GONE
            binding.iconVolume.visibility = View.GONE
            binding.btnOtherWord.visibility = View.GONE
        }
    }

    private fun progressBarOff() {
        binding.progressBar3.visibility = View.GONE
        binding.optionWord.visibility = View.VISIBLE
        binding.txtVariableWord.visibility = View.VISIBLE
        binding.iconVolume.visibility = View.VISIBLE
        binding.btnOtherWord.visibility = View.VISIBLE
    }

    private fun setValues() {
        val words = viewModel.basicWords.value!!
        binding.txtVariableWord.text = words[0]
        val shuffledArray = words.shuffled().toTypedArray()

        binding.wordOne.text = shuffledArray[0]
        binding.wordTwo.text = shuffledArray[1]
        binding.wordThree.text = shuffledArray[2]
        binding.wordFour.text = shuffledArray[3]
    }

    private fun goToCorrectWordPositiveResultActivity(){
        startActivity(Intent(this, PositiveResultCorrectWordActivity::class.java))
        finish()
    }

    private fun goToNegativeResultCorrectActivity(correctWord: String, selectedWord: String) {
        val intent = Intent(this, NegativeResultCorrectWordActivity::class.java)
        intent.putExtra("correctWord", correctWord)
        intent.putExtra("selectedWord", selectedWord)

        startActivity(intent)
        finish()
    }

}
