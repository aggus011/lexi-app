package com.example.lexiapp.ui.profesionalhome

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lexiapp.databinding.ActivityProfesionalHomeBinding
import com.example.lexiapp.ui.adapter.PatientAdapter

class ProfesionalHomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfesionalHomeBinding
    private val vM: ProfesionalHomeViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfesionalHomeBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        setRecyclerView()
        setSearch()
    }

    private fun setSearch() {
        binding.search.setOnQueryTextListener (object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(patientSearch: String?): Boolean {
                vM.filter(patientSearch)
                return true
            }

        })
    }

    private fun setRecyclerView() {
        binding.rvPatient.layoutManager= LinearLayoutManager(this)
        suscribeToVM()
    }

    private fun suscribeToVM() {
        vM.listFilterPatient.observe(this) { list ->
            binding.rvPatient.adapter = PatientAdapter(list){
                /*val intent = Intent(this, DetailProfilePatientActivity::class.java)
                intent.putExtra("info_patient", gson.toJson(it))
                startActivity(intent)*/
            }
        }
    }
}