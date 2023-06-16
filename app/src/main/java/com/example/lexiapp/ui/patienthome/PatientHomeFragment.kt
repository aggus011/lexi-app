package com.example.lexiapp.ui.patienthome

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.model.KeyPath
import com.example.lexiapp.R
import com.example.lexiapp.databinding.FragmentPatientHomeBinding
import com.example.lexiapp.ui.games.correctword.CorrectWordActivity
import com.example.lexiapp.ui.games.isitsocalled.IsItSoCalledActivity
import com.example.lexiapp.ui.games.letsread.LetsReadActivity
import com.example.lexiapp.ui.games.letsread.ListTextActivity
import com.example.lexiapp.ui.games.whereistheletter.WhereIsTheLetterActivity
import com.example.lexiapp.ui.textscanner.TextScannerActivity
import com.google.android.material.button.MaterialButton

class PatientHomeFragment : Fragment() {
    private var _binding: FragmentPatientHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var ibGameCorrectWord: LottieAnimationView
    private lateinit var ibGameLetsRead: LottieAnimationView
    private lateinit var ibGameWhereIsTheLetter: LottieAnimationView
    private lateinit var btnTextScanner: LottieAnimationView

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
        setInputImageDialog()
        setGamesListeners()
    }

    private fun getViews() {
        ibGameCorrectWord = binding.animationCorrectWord
        ibGameLetsRead = binding.animationLetsRead
        ibGameWhereIsTheLetter = binding.animationWhereIsTheLetter
        btnTextScanner = binding.animationTextScanner
    }

    private fun setInputImageDialog() {
        val popUpMenu = PopupMenu(requireContext(), btnTextScanner)

        popUpMenu.inflate(R.menu.input_image)

        setBtnScanListener(popUpMenu)

        popUpMenu.setOnMenuItemClickListener { menuItem ->
            goToScannerActivity(menuItem.itemId)
            true
        }
    }

    @SuppressLint("DiscouragedPrivateApi")
    private fun setBtnScanListener(popUpMenu: PopupMenu) {
        btnTextScanner.setOnClickListener {
            try {
                val popup = PopupMenu::class.java.getDeclaredField("mPopup")
                popup.isAccessible = true
                val menu = popup.get(popUpMenu)
                menu.javaClass
                    .getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                    .invoke(menu, true)

            } catch (e: Exception) {
                Log.e("MainError", e.toString())
            } finally {
                popUpMenu.show()
            }
        }
    }

    private fun goToScannerActivity(itemId: Int) {
        val intent = Intent(activity, TextScannerActivity::class.java)

        when (itemId) {
            R.id.camera -> intent.putExtra("InputImage", 1)
            R.id.gallery -> intent.putExtra("InputImage", 2)
        }

        startActivity(intent)
    }

    private fun setGamesListeners() {
        gameCorrectWordClick()
        gameLetsReadClick()
        gameWhereIsTheLetterClick()
    }

    private fun gameCorrectWordClick() {
        ibGameCorrectWord.setOnClickListener {
            startActivity(Intent(activity, CorrectWordActivity::class.java))
        }
    }

    private fun gameLetsReadClick() {
        ibGameLetsRead.setOnClickListener {
            startActivity(Intent(activity, ListTextActivity::class.java))
        }
    }

    private fun gameWhereIsTheLetterClick() {
        ibGameWhereIsTheLetter.setOnClickListener {
            startActivity(Intent(activity, WhereIsTheLetterActivity::class.java))
        }
    }

}