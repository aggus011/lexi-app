package com.example.lexiapp.ui.games.letsread

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import com.example.lexiapp.databinding.ActivityLetsReadBinding
import com.example.lexiapp.domain.model.TextToRead
import com.google.gson.Gson
import java.util.*

class LetsReadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLetsReadBinding
    private lateinit var textToSpeech: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLetsReadBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        val gson = Gson()
        val textToRead = gson.fromJson(intent.getStringExtra("TextToRead"), TextToRead::class.java)
        binding.txtNameText.text = textToRead.title
        binding.tvText.text = textToRead.text

        val language = Locale("es", "US")
        textToSpeech = TextToSpeech(this) {
            if (it != TextToSpeech.ERROR) {
                textToSpeech.language = language
            }
        }

        binding.volume.setOnClickListener {
            if (binding.tvText.text.isNotEmpty()) {
                textToSpeech
                    .speak(binding.tvText.text.toString(), TextToSpeech.QUEUE_FLUSH, null)
            }
        }
    }
}