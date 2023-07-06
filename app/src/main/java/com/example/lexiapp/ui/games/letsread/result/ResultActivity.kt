package com.example.lexiapp.ui.games.letsread.result

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.SeekBar
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
import java.io.File
import java.util.Locale

@AndroidEntryPoint
class ResultActivity() : AppCompatActivity() {

    private lateinit var binding: ActivityResultsLetsReadBinding

    private lateinit var nameTextTextView: TextView
    private lateinit var resultTextView: TextView
    private lateinit var nextTextBtn: Button
    private lateinit var backBtn: Button
    private lateinit var homeBtn: Button
    private lateinit var playAudioIb: ImageButton
    private lateinit var seekBarAudio: SeekBar
    private lateinit var audioTextDurationTv: TextView
    private var mediaPlayerAudioRecord: MediaPlayer? = null
    private lateinit var runnableAudioRecord: Runnable
    private var handlerAudioRecord = Handler()

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
        setMediaPlayerAudioRecord()

    }

    private fun getViews() {
        nameTextTextView = binding.txtNameText
        resultTextView = binding.textScrollView
        nextTextBtn = binding.btnNextText
        backBtn = binding.goBackBtn
        homeBtn = binding.homeBtn
        playAudioIb = binding.ibPlayAudioText
        seekBarAudio = binding.seekBarAudioText
        audioTextDurationTv = binding.tvAudioTextDuration
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

    private fun setMediaPlayerAudioRecord() {

        val recordingFile = File(
            this.filesDir,
            "Lexi $title record.mp3"
        )
        mediaPlayerAudioRecord = MediaPlayer.create(this, Uri.fromFile(recordingFile))

        if (mediaPlayerAudioRecord != null) {
            setSeekBar(mediaPlayerAudioRecord!!, seekBarAudio, audioTextDurationTv)
            setPlayButtonAudioRecordListener(mediaPlayerAudioRecord!!, playAudioIb)
            setRunnableMediaPlayerAudioRecord(mediaPlayerAudioRecord!!, seekBarAudio)

            mediaPlayerAudioRecord!!.setOnCompletionListener {
                playAudioIb.setImageResource(R.drawable.ic_play)
                seekBarAudio.progress = 0
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

        seekBarAudioText.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, position: Int, changed: Boolean) {
                if (changed) {
                    mediaPlayer.seekTo(position)
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })
    }

    private fun setAudioTextDuration(duration: Int): String {
        val seconds = (duration / 1000) % 60
        val minutes = (duration / (1000 * 60)) % 60

        return String.format(Locale.getDefault(), "%d:%02d", minutes, seconds)
    }

    private fun setPlayButtonAudioRecordListener(
        mediaPlayerAudioRecord: MediaPlayer,
        btnPlayAudioRecord: ImageButton
    ) {
        btnPlayAudioRecord.setOnClickListener {
            if (!mediaPlayerAudioRecord.isPlaying) {
                mediaPlayerAudioRecord.start()
                btnPlayAudioRecord.setImageResource(R.drawable.ic_pause)
            } else {
                pauseMediaPlayerAudioRecord()
            }
        }
    }

    private fun pauseMediaPlayerAudioRecord() {
        if (mediaPlayerAudioRecord != null && mediaPlayerAudioRecord!!.isPlaying) {
            mediaPlayerAudioRecord!!.pause()
            playAudioIb.setImageResource(R.drawable.ic_play)
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


}