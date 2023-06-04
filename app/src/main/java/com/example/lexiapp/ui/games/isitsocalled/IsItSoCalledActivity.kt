package com.example.lexiapp.ui.games.isitsocalled

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.example.lexiapp.databinding.ActivityIsItSoCalledBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class IsItSoCalledActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIsItSoCalledBinding
    private val vM:IsItSoCalledViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIsItSoCalledBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        setObserverVisibility()
        setListeners()
    }

    private fun setListeners() {
        binding.btnRec.setOnClickListener { vM.setVisibility() }
        binding.btnShowResults.setOnClickListener { vM.setVisibility() }
        binding.btnListenRecording.setOnClickListener { vM.setVisibility() }
        binding.btnReRecordAudio.setOnClickListener { vM.setVisibility() }
    }

    private fun setObserverVisibility() {
        vM.visibilityBtnsResult.observe(this){
            setViews(it)
        }
    }

    private fun setViews(visibility: Boolean) {
        if(visibility) invisibleRestultsViews() else visibleRestultsViews()
    }

    private fun visibleRestultsViews(){
        lifecycleScope.launch {
            binding.btnRec.visibility= View.INVISIBLE
            binding.btnShowResults.visibility= View.VISIBLE
            binding.btnListenRecording.visibility= View.VISIBLE
            binding.btnReRecordAudio.visibility= View.VISIBLE
        }
    }

    private fun invisibleRestultsViews(){
        lifecycleScope.launch {
            binding.btnRec.visibility= View.VISIBLE
            binding.btnShowResults.visibility= View.INVISIBLE
            binding.btnListenRecording.visibility= View.INVISIBLE
            binding.btnReRecordAudio.visibility= View.INVISIBLE
        }
    }
}