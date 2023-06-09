package com.example.lexiapp.ui.profesionalhome

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lexiapp.databinding.ActivityProfesionalHomeBinding
import com.example.lexiapp.ui.adapter.PatientAdapter
import com.example.lexiapp.utils.CaptureAct
import com.journeyapps.barcodescanner.CaptureActivity
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfesionalHomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfesionalHomeBinding
    private val vM: ProfesionalHomeViewModel by viewModels()

    val barcodeLauncher: ActivityResultLauncher<ScanOptions> = registerForActivityResult(ScanContract()){result->
        val email = vM.getPatientEmail(result.contents)
        Toast.makeText(this, "${email}", Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfesionalHomeBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        setListener()
        setRecyclerView()
        setSearch()
    }

    private fun setListener() {
        binding.btnAddPatient.setOnClickListener{
            val options = ScanOptions()
            options
                .setDesiredBarcodeFormats(ScanOptions.QR_CODE)
                .setPrompt("Escaneá el QR del paciente a vincular")
                .setBeepEnabled(true)
                .setOrientationLocked(true)
                .captureActivity = CaptureAct::class.java
            barcodeLauncher.launch(options)
        }
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
            binding.rvPatient.adapter = PatientAdapter(list) {
                //Set vM.unbindPatient()
            }
        }
    }
}