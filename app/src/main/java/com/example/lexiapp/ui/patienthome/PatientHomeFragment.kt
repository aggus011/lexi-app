package com.example.lexiapp.ui.patienthome

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.example.lexiapp.databinding.FragmentPatientHomeBinding
import com.example.lexiapp.ui.games.correctword.CorrectWordActivity
import com.example.lexiapp.ui.games.isitsocalled.IsItSoCalledActivity
import com.example.lexiapp.ui.games.letsread.LetsReadActivity
import com.example.lexiapp.ui.games.whereistheletter.WhereIsTheLetterActivity

class PatientHomeFragment : Fragment() {
    private var _binding: FragmentPatientHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var ibGameCorrectWord: ImageButton
    private lateinit var ibGameLetsRead: ImageButton
    private lateinit var ibGameWhereIsTheLetter: ImageButton
    private lateinit var ibGameIsItSoCalled: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPatientHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getViews()
        setGamesListeners()
    }

    private fun getViews() {
        ibGameCorrectWord = binding.gameCorrectWord
        ibGameLetsRead = binding.gameLetsRead
        ibGameWhereIsTheLetter = binding.gameWhereIsTheLetter
        ibGameIsItSoCalled = binding.gameIsItSoCalled
    }

    private fun setGamesListeners() {
        gameCorrectWordClick()
        gameLetsReadClick()
        gameWhereIsTheLetterClick()
        gameIsItSoCalledClick()
    }

    private fun gameCorrectWordClick() {
        ibGameCorrectWord.setOnClickListener {
            startActivity(Intent(activity, CorrectWordActivity::class.java))
        }
    }

    private fun gameLetsReadClick() {
        ibGameLetsRead.setOnClickListener {
            startActivity(Intent(activity, LetsReadActivity::class.java))
        }
    }

    private fun gameWhereIsTheLetterClick() {
        ibGameWhereIsTheLetter.setOnClickListener {
            startActivity(Intent(activity, WhereIsTheLetterActivity::class.java))
        }
    }

    private fun gameIsItSoCalledClick() {
        ibGameIsItSoCalled.setOnClickListener {
            startActivity(Intent(activity, IsItSoCalledActivity::class.java))
        }
    }

}