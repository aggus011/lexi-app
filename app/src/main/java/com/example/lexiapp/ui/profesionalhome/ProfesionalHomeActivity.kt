package com.example.lexiapp.ui.profesionalhome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lexiapp.databinding.ActivityProfesionalHomeBinding
import com.example.lexiapp.databinding.ActivitySignUpBinding
import com.example.lexiapp.ui.adapter.PatientAdapter
import com.example.lexiapp.ui.adapter.TextAdapter
import com.example.lexiapp.ui.games.letsread.LetsReadActivity
import com.example.lexiapp.ui.signup.SignUpViewModel
import com.google.gson.Gson

class ProfesionalHomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfesionalHomeBinding
    private val vM: ProfesionalHomeViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfesionalHomeBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        setRecyclerView()
    }

    private fun setRecyclerView() {
        binding.rvPatient.layoutManager= LinearLayoutManager(this)
        suscribeToVM()
    }

    private fun suscribeToVM() {
        vM.listPatient.observe(this) { list ->
            binding.rvPatient.adapter = PatientAdapter(list){
                /*val intent = Intent(this, DetailProfilePatientActivity::class.java)
                intent.putExtra("info_patient", gson.toJson(it))
                startActivity(intent)*/
            }
        }
    }
}