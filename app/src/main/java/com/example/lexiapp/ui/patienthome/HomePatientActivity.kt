package com.example.lexiapp.ui.patienthome

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.example.lexiapp.R
import com.example.lexiapp.databinding.ActivityHomePatientBinding
import com.example.lexiapp.ui.objectives.ObjectivesFragment
import com.example.lexiapp.ui.profile.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomePatientActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomePatientBinding
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var frameLayout: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomePatientBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        //To handle when user do back gesture
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        getViews()
        setDefaultFragment()
        setBottomNavigationListener()
    }

    private fun getViews() {
        bottomNavigationView = binding.bottomNavigation
        frameLayout = binding.flMainActivity
    }

    private fun setDefaultFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.flMainActivity, PatientHomeFragment())
            .commit()
    }

    private fun setBottomNavigationListener() {
        bottomNavigationView.setOnItemSelectedListener {menuItem ->
            when(menuItem.itemId){
                R.id.games -> {
                    goToFragment(PatientHomeFragment())
                    true
                }
                R.id.objetives -> {
                    goToFragment(ObjectivesFragment())
                    true
                }
                R.id.profile -> {
                    goToFragment(ProfileFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun goToFragment(destinationFragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.flMainActivity, destinationFragment)
            .commit()
    }

    private val onBackPressedCallback: OnBackPressedCallback = object: OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }
    }
}