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
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
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
import com.google.android.material.button.MaterialButton
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
    private var mediaPlayerText: MediaPlayer? = null
    private lateinit var runnableAudioText: Runnable
    private var handlerAudioText = Handler()
    private var mediaRecorder: MediaRecorder? = null
    private var isRecording = false
    private lateinit var btnPlayAudioRecord: ImageButton
    private lateinit var seekBarAudioRecord: SeekBar
    private lateinit var tvAudioRecordDuration: TextView
    private var mediaPlayerAudioRecord: MediaPlayer? = null
    private lateinit var runnableAudioRecord: Runnable
    private var handlerAudioRecord = Handler()
    private lateinit var btnReRecordAudio: MaterialButton
    private lateinit var btnShowResultsRecordAudio: MaterialButton

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
        btnPlayAudioRecord = binding.ibPlayAudioRecord
        seekBarAudioRecord = binding.seekBarAudioRecord
        tvAudioRecordDuration = binding.tvAudioRecordDuration
        btnReRecordAudio = binding.btnReRecordAudio
        btnShowResultsRecordAudio = binding.btnShowResults
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
        tvTextToRead.movementMethod = ScrollingMovementMethod()
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
                Log.v("Game Lets Read", "Empezando a convertir texto a audio")
            }

            override fun onDone(p0: String?) {
                Log.v("Game Lets Read", "Texto convertido a audio")
                runOnUiThread {
                    setMediaPlayerText(audioTextFile, btnPlayAudioText, seekBarAudioText, tvAudioTextDuration)
                }
            }

            @Deprecated("Deprecated in Java")
            override fun onError(p0: String?) {
            }

        })
    }

    private fun setMediaPlayerText(
        audioTextFile: File,
        btnPlayAudioText: ImageButton,
        seekBarAudioText: SeekBar,
        tvAudioTextDuration: TextView
    ) {
        mediaPlayerText =
            MediaPlayer.create(this, Uri.fromFile(audioTextFile))

        if(mediaPlayerText != null) {
            setSeekBar(mediaPlayerText!!, seekBarAudioText, tvAudioTextDuration)
            setPlayButtonAudioTextListener(mediaPlayerText!!, btnPlayAudioText)
            setRunnableMediaPlayerText(mediaPlayerText!!, seekBarAudioText)

            mediaPlayerText!!.setOnCompletionListener {
                btnPlayAudioText.setImageResource(R.drawable.ic_play)
                seekBarAudioText.progress = 0
            }
        }
    }

    private fun setSeekBar(
        mediaPlayer: MediaPlayer,
        seekBarAudioText: SeekBar,
        tvAudioTextDuration: TextView
    ) {
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

    private fun setPlayButtonAudioTextListener(mediaPlayer: MediaPlayer, btnPlayAudio: ImageButton) {
        btnPlayAudio.setOnClickListener {
            if(!mediaPlayer.isPlaying){
                pauseMediaPlayerAudioRecord()
                mediaPlayer.start()
                btnPlayAudio.setImageResource(R.drawable.ic_pause)
            }else{
                pauseMediaPlayerAudioText()
            }
        }
    }

    private fun setRunnableMediaPlayerText(
        mediaPlayer: MediaPlayer,
        seekBarAudioText: SeekBar,
    ) {
        this.runnableAudioText = Runnable {
            seekBarAudioText.progress = mediaPlayer.currentPosition
            handlerAudioText.postDelayed(this.runnableAudioText, 500)
        }
        handlerAudioText.postDelayed(this.runnableAudioText, 500)
    }

    private fun setAudioTextDuration(duration: Int) : String {
        val seconds = (duration / 1000) % 60
        val minutes = (duration / (1000 * 60)) % 60

        return String.format(Locale.getDefault(), "%d:%02d", minutes, seconds)
    }

    private fun btnRecordAudioListener(){
        initArrayPermissions()
        btnRecordAudio.setOnClickListener {
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
        val recordingFile = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC), "Lexi ${tvTextTitle.text} record.mp3")
        if(!isRecording){
            setMediaRecorder(recordingFile)
            pauseMediaPlayerAudioText()
            enableButtonPlayAudioText(btnPlayAudioText= false)
            isRecording = !isRecording
            Log.v("Game Lets Read", "Grabando audio...")
        }else{
            releaseMediaAudioRecorder()
            enableButtonPlayAudioText(btnPlayAudioText= true)
            setMediaPlayerAudioRecord(recordingFile, btnPlayAudioRecord, seekBarAudioRecord, tvAudioRecordDuration)
            isRecording = !isRecording
            Log.v("Game Lets Read", "Guardando audio...")
        }
    }

    private fun setMediaRecorder(recordingFile: File) {
        mediaRecorder = MediaRecorder()
        mediaRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        mediaRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        mediaRecorder!!.setMaxDuration(120000) // 2 minutes as max recorder time
        mediaRecorder!!.setOutputFile(recordingFile.absolutePath)
        mediaRecorder!!.prepare()
        mediaRecorder!!.start()
    }

    private fun setMediaPlayerAudioRecord(
        audioFile: File,
        btnPlayAudioRecord: ImageButton,
        seekBarAudioRecord: SeekBar,
        tvAudioRecordDuration: TextView) {

        mediaPlayerAudioRecord = MediaPlayer.create(this, Uri.fromFile(audioFile))

        if (mediaPlayerAudioRecord != null) {
            setSeekBar(mediaPlayerAudioRecord!!, seekBarAudioRecord, tvAudioRecordDuration)
            setPlayButtonAudioRecordListener(mediaPlayerAudioRecord!!, btnPlayAudioRecord)
            setRunnableMediaPlayerAudioRecord(mediaPlayerAudioRecord!!, seekBarAudioRecord)

            showMediaPlayerAudioRecord()
            setReRecordAudioListener()
            setShowResultsRecordAudio(audioFile)

            mediaPlayerAudioRecord!!.setOnCompletionListener {
                btnPlayAudioRecord.setImageResource(R.drawable.ic_play)
                seekBarAudioRecord.progress = 0
            }
        }
    }

    private fun setPlayButtonAudioRecordListener(
        mediaPlayerAudioRecord: MediaPlayer,
        btnPlayAudioRecord: ImageButton
    ) {
        btnPlayAudioRecord.setOnClickListener {
            if(!mediaPlayerAudioRecord.isPlaying){
                pauseMediaPlayerAudioText()
                mediaPlayerAudioRecord.start()
                btnPlayAudioRecord.setImageResource(R.drawable.ic_pause)
            }else{
                pauseMediaPlayerAudioRecord()
            }
        }
    }

    private fun setRunnableMediaPlayerAudioRecord(
        mediaPlayerAudioRecord: MediaPlayer,
        seekBarAudioRecord: SeekBar
    ) {
        this.runnableAudioRecord = Runnable {
            seekBarAudioRecord.progress = mediaPlayerAudioRecord.currentPosition
            this.handlerAudioRecord.postDelayed(this.runnableAudioRecord, 500)
        }
        this.handlerAudioRecord.postDelayed(this.runnableAudioRecord, 500)
    }

    private fun showMediaPlayerAudioRecord(){
        btnRecordAudio.visibility = View.GONE
        binding.clAudioRecord.visibility = View.VISIBLE
    }

    private fun hideMediaPlayerAudioRecord(){
        btnRecordAudio.visibility = View.VISIBLE
        binding.clAudioRecord.visibility = View.GONE
    }

    private fun enableButtonPlayAudioText(btnPlayAudioText: Boolean) {
        this.btnPlayAudioText.isEnabled = btnPlayAudioText
    }

    private fun pauseMediaPlayerAudioText() {
        if (mediaPlayerText != null  && mediaPlayerText!!.isPlaying) {
            mediaPlayerText!!.pause()
            btnPlayAudioText.setImageResource(R.drawable.ic_play)
        }
    }

    private fun pauseMediaPlayerAudioRecord() {
        if (mediaPlayerAudioRecord != null && mediaPlayerAudioRecord!!.isPlaying) {
            mediaPlayerAudioRecord!!.pause()
            btnPlayAudioRecord.setImageResource(R.drawable.ic_play)
        }
    }

    private fun setReRecordAudioListener(){
        btnReRecordAudio.setOnClickListener {
            pauseMediaPlayerAudioText()
            pauseMediaPlayerAudioRecord()
            isRecording = false
            hideMediaPlayerAudioRecord()
            recordAudio()
        }
    }

    private fun setShowResultsRecordAudio(audioFile: File) {
        btnShowResultsRecordAudio.setOnClickListener {
            // SEND AUDIO FILE TO ANALYSIS
        }
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

    private fun releaseMediaAudioRecorder(){
        mediaRecorder?.stop()
        mediaRecorder?.release()
        mediaRecorder = null
    }

    private fun releaseMediaPlayerAudioText(){
        mediaPlayerText?.stop()
        mediaPlayerText?.release()

        if(mediaPlayerText != null) {
            handlerAudioText.removeCallbacks(runnableAudioText)
        }

        mediaPlayerText = null
    }

    private fun releaseMediaPlayerAudioRecorder(){
        mediaRecorder?.stop()
        mediaRecorder?.release()

        if(mediaPlayerAudioRecord != null){
            handlerAudioRecord.removeCallbacks(runnableAudioRecord)
        }

        mediaRecorder = null
    }

    override fun onPause() {
        super.onPause()
        releaseMediaAudioRecorder()
        pauseMediaPlayerAudioText()
        releaseMediaPlayerAudioRecorder()
    }

    override fun onDestroy() {
        super.onDestroy()
        releaseMediaAudioRecorder()
        releaseMediaPlayerAudioText()
        releaseMediaPlayerAudioRecorder()
        textToSpeech.shutdown()
    }
}