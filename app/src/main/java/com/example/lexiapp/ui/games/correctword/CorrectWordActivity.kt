package com.example.lexiapp.ui.games.correctword

import android.animation.ObjectAnimator
import android.graphics.Color
import android.icu.lang.UCharacter.GraphemeClusterBreak.V
import android.icu.util.TimeUnit.values
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import android.media.MediaPlayer
import kotlinx.coroutines.withContext
@AndroidEntryPoint
class CorrectWordActivity : AppCompatActivity() {

    private val vM: CorrectWordViewModel by viewModels()
    private lateinit var binding: ActivityCorrectWordBinding
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCorrectWordBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        progressBarOn()
        setListeners()
        waitForValues()
    }

    private fun desactivateButton() {
        binding.wordOne.isEnabled = false
        binding.wordTwo.isEnabled = false
        binding.wordThree.isEnabled = false
        binding.wordFour.isEnabled = false
    }

    private fun activateButton() {
        binding.wordOne.isEnabled = true
        binding.wordTwo.isEnabled = true
        binding.wordThree.isEnabled = true
        binding.wordFour.isEnabled = true
    }

    private fun resetGame() {
        val constraintLayout = findViewById<ConstraintLayout>(R.id.constraintLayout)
        constraintLayout.setBackgroundColor(Color.WHITE)
        binding.btnOtherWord.visibility = View.GONE
        activateButton()
        vM.generateWord()
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
        val correctButtonText = binding.txtVariableWord.text.toString()
        if (selectedButton.text == correctButtonText) {
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
            makeText(binding.txtSelectWord.context, "Felicidades, Elegiste la palabra correcta", LENGTH_SHORT).show()
            progressBarOff()
            constraintLayout.setBackgroundColor(Color.GREEN)
            button.setBackgroundColor(Color.WHITE)
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
            delay(1000)
            withContext(Dispatchers.Main) {
                try {
                    setValues()
                }catch (e: Exception){
                    binding.progressBar3.visibility=View.GONE
                    binding.txtWord.visibility= View.GONE
                    binding.txtVariableWord.visibility= View.GONE
                    binding.iconVolume.visibility= View.GONE
                    Toast.makeText(applicationContext ,"NO SE PUDO CARGAR LA PALABRA", Toast.LENGTH_SHORT).show()

                }
            }
            progressBarOff()
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

    fun shuffleString(input: String, numCharsToShuffle: Int): String {
        val charArray = input.toCharArray()
        val indicesToShuffle = (0 until charArray.size).shuffled().take(numCharsToShuffle)
        val shuffledCharArray = charArray.mapIndexed { index, char ->
            if (index in indicesToShuffle) {
                charArray[(index + 1) % charArray.size]
            } else {
                char
            }
        }.toCharArray()
        return String(shuffledCharArray)
    }




    private fun setValues(){
        val word = vM.basicWord.value
        binding.txtVariableWord.text = word
          val shuffledWord1 = shuffleString(word, 2)
          val shuffledWord2 = shuffleString(word, 1)
          val shuffledWord3 = shuffleString(word, 2)
          val words = arrayOf(word, shuffledWord1, shuffledWord2, shuffledWord3)
          val shuffledArray = words.toList().shuffled().toTypedArray()

          binding.wordOne.text = shuffledArray[0]
          binding.wordTwo.text = shuffledArray[1]
          binding.wordThree.text = shuffledArray[2]
          binding.wordFour.text = shuffledArray[3]
    }

}
