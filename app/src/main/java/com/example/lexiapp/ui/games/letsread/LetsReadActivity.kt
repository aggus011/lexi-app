package com.example.lexiapp.ui.games.letsread

import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.lexiapp.R
import com.example.lexiapp.databinding.ActivityLetsReadBinding
import com.example.lexiapp.domain.model.TextToRead
import com.google.gson.Gson
import java.io.File
import java.util.*

class LetsReadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLetsReadBinding
    private lateinit var textToSpeech: TextToSpeech
    private lateinit var btnPlayAudioText: ImageButton
    private lateinit var seekBarAudioText: SeekBar
    private lateinit var tvAudioTextDuration: TextView
    private lateinit var tvTextTitle: TextView
    private lateinit var tvTextToRead: TextView
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var runnable: Runnable
    private var handler = Handler()
    private lateinit var currentAudioFile: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLetsReadBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        getViews()
        setTextToReadData()
        setTextToSpeech()
    }

    private fun getViews(){
        tvTextTitle = binding.txtNameText
        tvTextToRead = binding.tvText
        btnPlayAudioText = binding.ibPlayAudioText
        seekBarAudioText = binding.seekBarAudioText
        tvAudioTextDuration = binding.tvAudioTextDuration
    }

    private fun setTextToReadData(){
        val gson = Gson()
        val textToRead = gson.fromJson(intent.getStringExtra("TextToRead"), TextToRead::class.java)

        tvTextTitle.text = textToRead.title
        tvTextToRead.text = textToRead.text
    }

    private fun setTextToSpeech() {
        val language = Locale("es", "US")
        textToSpeech = TextToSpeech(this) {
            if (it != TextToSpeech.ERROR) {
                textToSpeech.language = language

                generateAudioFromTextToRead()
            }
        }
    }

    private fun generateAudioFromTextToRead(){
        if(tvTextTitle.text.isNotEmpty() &&
            tvTextToRead.text.isNotEmpty() &&
                checkWriteStoragePermission()){
            saveToFile(tvTextTitle.text.toString(), tvTextToRead.text.toString())
        }
    }

    private fun checkWriteStoragePermission(): Boolean{
        return ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun setAudioTextPlayer(){
        setMediaPlayer()
    }

    private fun setMediaPlayer() {
        currentAudioFile = File(this.filesDir, "${tvTextTitle.text}.mp3")

        if(currentAudioFile.exists()){
            val mediaPlayer = MediaPlayer.create(this, Uri.fromFile(File(currentAudioFile.absolutePath)))


            setSeekBar(mediaPlayer)
            setPlayButtonListener(mediaPlayer)
            setRunnable(mediaPlayer)

            mediaPlayer.setOnCompletionListener {
                btnPlayAudioText.setImageResource(R.drawable.ic_play)
                seekBarAudioText.progress = 0
            }
        }
    }

    private fun setSeekBar(mediaPlayer: MediaPlayer) {
        seekBarAudioText.progress = 0
        seekBarAudioText.max = mediaPlayer.duration
        tvAudioTextDuration.text = setAudioTextDuration(mediaPlayer.duration)

        seekBarAudioText.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, position: Int, changed: Boolean) {
                if(changed){
                    mediaPlayer.seekTo(position)
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })
    }

    private fun setPlayButtonListener(mediaPlayer: MediaPlayer) {
        btnPlayAudioText.setOnClickListener {
            if(!mediaPlayer.isPlaying){
                mediaPlayer.start()
                btnPlayAudioText.setImageResource(R.drawable.ic_pause)
            }else{
                mediaPlayer.pause()
                btnPlayAudioText.setImageResource(R.drawable.ic_play)
            }
        }
    }

    private fun setRunnable(mediaPlayer: MediaPlayer) {
        runnable = Runnable {
            seekBarAudioText.progress = mediaPlayer.currentPosition
            handler.postDelayed(runnable, 500)
        }
        handler.postDelayed(runnable, 500)
    }

    private fun saveToFile(textTitle: String, text:String) {
        val internalDir = this.filesDir
        currentAudioFile = File(internalDir, "$textTitle.mp3")

        if(!currentAudioFile.exists()){
            val result = textToSpeech.synthesizeToFile(textTitle.plus("\n").plus(text), null, currentAudioFile, null)

            if (result == TextToSpeech.SUCCESS) {
                Toast.makeText(this, "Audio saved successfully", Toast.LENGTH_SHORT).show()
                setAudioTextPlayer()
            } else {
                Toast.makeText(this, "Failed to save audio", Toast.LENGTH_SHORT).show()
            }
        }else{
            setAudioTextPlayer()
        }

    }

    private fun setAudioTextDuration(duration: Int) : String {
        val seconds = (duration / 1000) % 60
        val minutes = (duration / (1000 * 60)) % 60

        return String.format(Locale.getDefault(), "%d:%02d", minutes, seconds)
    }

    /*override fun onPause() {
        super.onPause()
        if(mediaPlayer != null && mediaPlayer.isPlaying){
            mediaPlayer.stop()
            btnPlayAudioText.setImageResource(R.drawable.ic_play)
        }
        handler.removeCallbacks(runnable)
    }

    override fun onDestroy() {
        super.onDestroy()
        if(mediaPlayer != null){
            mediaPlayer.release()
        }
        handler.removeCallbacks(runnable)
    }*/
}