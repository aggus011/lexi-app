package com.example.lexiapp.ui.games.letsread

import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
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
    private lateinit var btnBack: ImageButton
    private lateinit var btnRecordAudio: ImageButton
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var runnable: Runnable
    private var handler = Handler()
    private var isRecording = false

    private lateinit var recordAudioPermissions: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLetsReadBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        //To handle when user do back gesture
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        getViews()
        btnBackListener()
        setTextToReadData()
        setTextToSpeech()
        btnRecordAudioListener()
    }

    private fun getViews(){
        tvTextTitle = binding.txtNameText
        tvTextToRead = binding.tvText
        btnPlayAudioText = binding.ibPlayAudioText
        seekBarAudioText = binding.seekBarAudioText
        tvAudioTextDuration = binding.tvAudioTextDuration
        btnBack = binding.btnArrowBack
        btnRecordAudio = binding.btnRec
    }

    private fun btnBackListener(){
        btnBack.setOnClickListener {
            finish()
        }
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

                val audioTextFile = File(this.filesDir, "${tvTextTitle.text}.mp3")
                generateAudioFromTextToRead(audioTextFile)
            }
        }
    }

    private fun generateAudioFromTextToRead(audioTextFile: File) {
        val textToSynthesize =
            tvTextTitle.text.toString().plus("\n").plus(tvTextToRead.text.toString())

        textToSpeech.synthesizeToFile(
            textToSynthesize,
            null,
            audioTextFile,
            "my_utterance_id"
        )

        textToSpeech.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(p0: String?) {
                Log.i("Game Lets Read", "Empezando a convertir texto a audio")
            }

            override fun onDone(p0: String?) {
                Log.i("Game Lets Read", "Texto convertido a audio")
                runOnUiThread {
                    setMediaPlayer(audioTextFile)
                }
            }

            @Deprecated("Deprecated in Java")
            override fun onError(p0: String?) {
            }

        })
    }

    private fun setMediaPlayer(audioTextFile: File) {
        val mediaPlayer =
            MediaPlayer.create(this, Uri.fromFile(audioTextFile))

        if(mediaPlayer != null) {
            this.mediaPlayer = mediaPlayer
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
        Log.i("Lucas2", "seekbar max seteado")
        tvAudioTextDuration.text = setAudioTextDuration(mediaPlayer.duration)
        Log.i("Lucas2", "duracion seteada")

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

    private fun setAudioTextDuration(duration: Int) : String {
        val seconds = (duration / 1000) % 60
        val minutes = (duration / (1000 * 60)) % 60

        return String.format(Locale.getDefault(), "%d:%02d", minutes, seconds)
    }

    private fun btnRecordAudioListener(){
        btnRecordAudio.setOnClickListener {
            initArrayPermissions()
            if(checkRecordAudioPermissions()){
                recordAudio()
            }else{
                requestRecordAudioPermissions()
            }
        }
    }

    private fun initArrayPermissions(){
        recordAudioPermissions =
            arrayOf(android.Manifest.permission.RECORD_AUDIO,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    private fun checkRecordAudioPermissions(): Boolean{
        val recordAudioResult =
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
        val storageResult =
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

        return recordAudioResult && storageResult
    }

    private fun recordAudio(){
        val recordingFile = File(Environment.getExternalStorageDirectory(), "lexiAudio.mp3")
        val mediaRecorder = MediaRecorder()
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        mediaRecorder.setOutputFile(recordingFile.absolutePath)

        mediaRecorder.prepare()

        isRecording = if(!isRecording){

            mediaRecorder.start()
            !isRecording
        }else{
            mediaRecorder.stop()
            !isRecording
        }
        Toast.makeText(this, "Grabando...", Toast.LENGTH_LONG).show()
    }

    private fun requestRecordAudioPermissions(){
        ActivityCompat.requestPermissions(this, recordAudioPermissions, RECORD_AUDIO_REQUEST_CODE)
    }

    private companion object{
         private const val RECORD_AUDIO_REQUEST_CODE = 300
    }

    private val onBackPressedCallback: OnBackPressedCallback = object: OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        //handle permission(s) results
        if(requestCode == RECORD_AUDIO_REQUEST_CODE){
            if(grantResults.isNotEmpty()){
                val recordAudioAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED

                if(recordAudioAccepted && storageAccepted){
                    recordAudio()
                }else{
                    //No record audio and storage permissions granted
                    Toast.makeText(this, "Necesitas darnos permiso para poder usar el micrófono", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if(mediaPlayer != null && mediaPlayer!!.isPlaying){
            mediaPlayer!!.stop()
            btnPlayAudioText.setImageResource(R.drawable.ic_play)
            handler.removeCallbacks(runnable)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(mediaPlayer != null){
            mediaPlayer!!.release()
            handler.removeCallbacks(runnable)
        }
        textToSpeech.shutdown()
    }
}