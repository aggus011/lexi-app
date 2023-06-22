package com.example.lexiapp.ui.games.letsread

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lexiapp.databinding.ActivityListTextBinding
import com.example.lexiapp.ui.adapter.TextAdapter
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListTextActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListTextBinding
    //Should be inject
    private lateinit var vM: TextViewModel
    private val listTextViewModel: ListTextViewModel by viewModels()
    private lateinit var progressBar: View
    private lateinit var challengeReadingBtn: MaterialButton
    private lateinit var rvReadings: RecyclerView
    private lateinit var backBtn: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListTextBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        //To handle when user do back gesture
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        getViews()
        setVM()
        setObservables()
        setListener()
        setRecyclerView()
    }

    private fun getViews(){
        challengeReadingBtn = binding.btnRandomText
        rvReadings = binding.rvText
        backBtn = binding.btnBack
        progressBar = binding.progressLayout
    }

    private fun setVM(){
        //Should be inject
        val factory = TextViewModel.Factory() // Factory
        vM= ViewModelProvider(this, factory)[TextViewModel::class.java]
    }

    private fun setObservables(){
        listTextViewModel.challengeReading.observe(this){
            if(it != null){
                hideProgressBar()
                enableButtons()
                val gson = Gson()
                val intent = Intent(this, LetsReadActivity::class.java)
                intent.putExtra("TextToRead", gson.toJson(it))
                startActivity(intent)
            }
        }
        listTextViewModel.isPossibleGenerateChallengeReading.observe(this){
            if(!it){
                listTextViewModel.isPossibleGenerateChallengeReading.value = !it
                hideProgressBar()
                enableButtons()
                showRateLimitDialog()
            }
        }
    }

    private fun setListener() {
        backBtn.setOnClickListener {
            finish()
        }
        challengeReadingBtn.setOnClickListener {
            showProgressBar()
            disableButtons()
            listTextViewModel.generateChallengeReading()
        }
    }

    private fun setRecyclerView() {
        rvReadings.layoutManager=LinearLayoutManager(this)
        suscribeToVM()
    }

    private fun suscribeToVM() {
        val gson = Gson()
        vM.listText.observe(this) { list ->
            binding.rvText.adapter = TextAdapter(list){
                val intent = Intent(this, LetsReadActivity::class.java)
                intent.putExtra("TextToRead", gson.toJson(it))
                startActivity(intent)
            }
        }
    }

    private fun disableButtons(){
        challengeReadingBtn.isClickable = false
        backBtn.isClickable = false
    }

    private fun enableButtons(){
        challengeReadingBtn.isClickable = true
        backBtn.isClickable = true
    }

    private fun hideProgressBar(){
        progressBar.visibility = View.GONE
    }

    private fun showProgressBar(){
        progressBar.visibility = View.VISIBLE
    }

    private fun showRateLimitDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Lexi ocupado")
            .setMessage("Estamos con mucho trabajo para ofrecerte un desafío ahora, intentá más tarde por favor")
            .setPositiveButton("OK"){dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private val onBackPressedCallback: OnBackPressedCallback = object: OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }
    }

}