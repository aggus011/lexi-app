package com.example.lexiapp.ui.games.letsread

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import com.example.lexiapp.R
import com.example.lexiapp.databinding.ActivityResultsLetsReadBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResultActivity() : AppCompatActivity() {

    private lateinit var binding: ActivityResultsLetsReadBinding
    private val vM: DifferenceViewModel by viewModels()

    private lateinit var resultTextView: TextView
    private lateinit var homeBtn: Button
    private lateinit var originalText: String
    private lateinit var results: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResultsLetsReadBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        resultTextView = binding.tvTitle
        homeBtn = binding.btnHome

        setButtonHome()

        getExtras()
        getResults(originalText, results)
        manageResult()



    }

    private fun setButtonHome() {
        homeBtn.setOnClickListener {
            //goToFragment(PatientHomeFragment())
            finish()
        }
    }

    private fun goToFragment(destinationFragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.flMainActivity, destinationFragment)
            .commit()
    }

    private fun getExtras() {
        originalText = intent.getStringExtra("originalText").toString()
        results = intent.getStringExtra("results").toString()
    }

    private fun getResults(originalText: String, results: String) {
        vM.getDifference(originalText = originalText, results = results)
    }

    private fun manageResult() {
        vM.difference.observe(this) {
            val result = vM.convertToText()
            if (result == "Correct"){
                resultTextView.text = "¡Tu respuesta es correcta!"
                vM.checkIfObjectivesHasBeenCompleted("LR", "hit", "Vamos a leer")
            } else {
                resultTextView.text = HtmlCompat.fromHtml(result, HtmlCompat.FROM_HTML_MODE_LEGACY)
                vM.checkIfObjectivesHasBeenCompleted("RL", "play", "Vamos a leer")

            }
        }
    }

}