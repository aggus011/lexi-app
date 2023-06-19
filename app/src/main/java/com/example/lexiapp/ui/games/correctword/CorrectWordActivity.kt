package com.example.lexiapp.ui.games.correctword

import android.animation.ObjectAnimator
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import android.widget.Toast.makeText
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import com.example.lexiapp.R
import com.example.lexiapp.databinding.ActivityCorrectWordBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.media.MediaPlayer
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
        setObservers()
    }

    private fun setObservers() {
        viewModel.basicWords.observe(this) {
            if (!it.isNullOrEmpty()) {
                setListeners()
                waitForValues()
                progressBarOff()
            } else {
                progressBarOn()
            }
        }
    }

    private fun desactivateButton() {
        binding.wordOne.isEnabled = false
        binding.wordTwo.isEnabled = false
        binding.wordThree.isEnabled = false
        binding.wordFour.isEnabled = false
    }

    private fun activateButton() {
        binding.apply {
            wordOne.isEnabled = true
            wordOne.setTextColor(Color.WHITE)
            wordTwo.isEnabled = true
            wordTwo.setTextColor(Color.WHITE)
            wordThree.isEnabled = true
            wordThree.setTextColor(Color.WHITE)
            wordFour.isEnabled = true
            wordFour.setTextColor(Color.WHITE)
        }
    }

    private fun resetGame() {
        val constraintLayout = findViewById<ConstraintLayout>(R.id.constraintLayout)
        constraintLayout.setBackgroundColor(Color.WHITE)
        binding.btnOtherWord.visibility = View.GONE
        activateButton()
        viewModel.generateWords()
        progressBarOn()
        setListeners()
        waitForValues()
    }

    private fun setListeners() {
        binding.wordOne.setOnClickListener { checkAnswer(binding.wordOne) }
        binding.wordTwo.setOnClickListener { checkAnswer(binding.wordTwo) }
        binding.wordThree.setOnClickListener { checkAnswer(binding.wordThree) }
        binding.wordFour.setOnClickListener { checkAnswer(binding.wordFour) }
        binding.btnOtherWord.setOnClickListener {
            resetGame()
        }
        binding.iconVolume.setOnClickListener {
            playWordSound()
        }
        binding.btnBack.setOnClickListener {
            finish()
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
            animateCorrectButton(selectedButton)
            binding.btnOtherWord.visibility = View.VISIBLE
        } else {
            animateIncorrectButton(selectedButton)
        }
    }

    private fun animateCorrectButton(button: Button) {
        val constraintLayout = findViewById<ConstraintLayout>(R.id.constraintLayout)
        desactivateButton()
        Handler(Looper.getMainLooper()).postDelayed({
            makeText(
                binding.txtSelectWord.context,
                "Felicidades, Elegiste la palabra correcta",
                LENGTH_SHORT
            ).show()
            progressBarOff()
            constraintLayout.setBackgroundColor(Color.GREEN)
            button.setTextColor(Color.BLUE)
            // button.setBackgroundColor(Color.WHITE)
        }, 1000)
    }

    private fun animateIncorrectButton(button: Button) {
        val translationXAnimation = ObjectAnimator.ofFloat(
            button, "translationX",
            0f, -20f, 20f, -20f, 20f, 0f
        )
        translationXAnimation.duration = 500
        translationXAnimation.start()
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
            binding.wordOne.visibility = View.GONE
            binding.wordTwo.visibility = View.GONE
            binding.wordThree.visibility = View.GONE
            binding.wordFour.visibility = View.GONE
            binding.txtWordToPlay.visibility = View.GONE
        }
    }

    private fun progressBarOff() {
        binding.progressBar3.visibility = View.GONE
        binding.wordOne.visibility = View.VISIBLE
        binding.wordTwo.visibility = View.VISIBLE
        binding.wordThree.visibility = View.VISIBLE
        binding.wordFour.visibility = View.VISIBLE
        binding.txtWordToPlay.visibility = View.VISIBLE
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

}
