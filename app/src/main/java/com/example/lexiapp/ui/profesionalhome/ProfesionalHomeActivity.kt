package com.example.lexiapp.ui.profesionalhome

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lexiapp.databinding.ActivityProfesionalHomeBinding
import com.example.lexiapp.domain.model.Patient
import com.example.lexiapp.ui.adapter.PatientAdapter
import com.example.lexiapp.ui.profesionalhome.detailpatient.DetailPatientFragment
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfesionalHomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfesionalHomeBinding
    private val vM: ProfesionalHomeViewModel by viewModels()
    private val TAG_FRAGMENT_DETAIL = "detail_fragment_tag"
    private var detailFragment: DetailPatientFragment? = null

    private val barcodeLauncher: ActivityResultLauncher<ScanOptions> = registerForActivityResult(ScanContract()){ result->
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
        addFragment()
        visibilityDetailFragment()
    }

    private fun visibilityDetailFragment() {
        supportFragmentManager.addOnBackStackChangedListener {
            val fragment = supportFragmentManager.findFragmentByTag(TAG_FRAGMENT_DETAIL)
            if (fragment == null || !fragment.isVisible) {
                // El fragmento no está visible, así que podemos mostrar el RecyclerView nuevamente
                binding.rvPatient.visibility = View.VISIBLE
                binding.btnAddPatient.visibility = View.VISIBLE
                binding.svFilter.visibility = View.VISIBLE
                binding.ivMiniLogo.visibility = View.VISIBLE
            } else {
                // El fragmento está visible, ocultamos el RecyclerView
                binding.rvPatient.visibility = View.GONE
                binding.btnAddPatient.visibility = View.GONE
                binding.svFilter.visibility = View.GONE
                binding.ivMiniLogo.visibility = View.GONE
            }
        }
    }

    private fun addFragment() {
        detailFragment = DetailPatientFragment()
        supportFragmentManager.beginTransaction()
            .add(binding.lyFragment.id, detailFragment!!, TAG_FRAGMENT_DETAIL)
            .hide(detailFragment!!)
            .commit()
    }

    private fun setListener() {
        binding.btnAddPatient.setOnClickListener{
            barcodeLauncher.launch(vM.getScanOptions())
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
            binding.rvPatient.adapter = PatientAdapter(list, ::viewDetails, ::unbindPatient)
        }
    }

    private fun unbindPatient(email: String){
        vM.unbindPatient(email)
    }

    private fun viewDetails(patient: Patient){
        vM.setPatientSelected(patient)
        supportFragmentManager.beginTransaction()
            .show(detailFragment!!)
            .addToBackStack(TAG_FRAGMENT_DETAIL)
            .commit()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        supportFragmentManager.findFragmentByTag(TAG_FRAGMENT_DETAIL).let{
            when (it?.isVisible) {
                true -> supportFragmentManager.popBackStack()
                else -> super.onBackPressed()
            }
        }
    }
}