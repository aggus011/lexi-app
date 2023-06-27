package com.example.lexiapp.ui.patienthome

import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.lexiapp.R
import com.example.lexiapp.databinding.ActivityCategoriesBinding
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
    private lateinit var notificationPermission: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomePatientBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        //To handle when user do back gesture
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        initArrayPermission()
        verifyNotificationPermission()
        getViews()
        setDefaultFragment()
        setBottomNavigationListener()
    }

    private fun initArrayPermission(){
        notificationPermission = if(verifyIsApiVersionIsHigherThan33()){
            arrayOf(android.Manifest.permission.POST_NOTIFICATIONS)
        }else{
            emptyArray()
        }
        if(notificationPermission.isNotEmpty()) {
            verifyNotificationPermission()
        }
    }

    private fun verifyNotificationPermission(){
        if(!checkNotificationPermission()){
            requestNotificationPermission()
        }
    }

    private fun checkNotificationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestNotificationPermission() {
        ActivityCompat
            .requestPermissions(this, notificationPermission, NOTIFICATIONS_REQUEST_CODE)
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

    private fun verifyIsApiVersionIsHigherThan33(): Boolean{
        return Build.VERSION.SDK_INT >=
                Build.VERSION_CODES.TIRAMISU
    }

    private val onBackPressedCallback: OnBackPressedCallback = object: OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }
    }

    private companion object {
        private const val NOTIFICATIONS_REQUEST_CODE = 100
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        //handle permission(s) results

        if(requestCode == NOTIFICATIONS_REQUEST_CODE) {
            if(grantResults.isNotEmpty()) {
                val notificationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED

                if(notificationAccepted){
                    Toast.makeText(this, "Gracias! Ahora recibirás notificaciones", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, "Entendido, no te enviaremos notificaciones", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}