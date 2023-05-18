package com.example.lexiapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.lexiapp.R
import com.example.lexiapp.databinding.ActivityMainBinding
import com.example.lexiapp.ui.objectives.ObjectivesFragment
import com.example.lexiapp.ui.patienthome.PatientHomeFragment
import com.example.lexiapp.ui.profile.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var frameLayout: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))

        setContentView(binding.root)
        
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
}