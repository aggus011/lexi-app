package com.example.lexiapp.ui.profesionalhome

import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lexiapp.R
import com.example.lexiapp.databinding.ActivityProfesionalHomeBinding
import com.example.lexiapp.domain.model.Patient
import com.example.lexiapp.domain.useCases.ProfileUseCases
import com.example.lexiapp.ui.adapter.PatientAdapter
import com.example.lexiapp.ui.profesionalhome.detailpatient.DetailPatientFragment
import com.example.lexiapp.ui.profile.professional.ProfessionalProfileFragment
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfesionalHomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfesionalHomeBinding
    private val vM: ProfesionalHomeViewModel by viewModels()
    private val TAG_FRAGMENT_DETAIL = "detail_fragment_tag"
    private var detailFragment: DetailPatientFragment? = null

    @Inject
    lateinit var profileUseCases: ProfileUseCases

    private val barcodeLauncher: ActivityResultLauncher<ScanOptions> = registerForActivityResult(ScanContract()){ result->
        val email = vM.getPatientEmail(result.contents)
        Toast.makeText(this, "$email", Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfesionalHomeBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        getViews()
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

    private fun getViews(){
        setIconAccount()
    }

    private fun setIconAccount() {
        setTextIcon()
        setColors()
    }

    private fun setTextIcon() {
        binding.tvUserInitials.text = profileUseCases.userInitials()
    }

    private fun setColors(){
        val icColor = profileUseCases.getColorRandomForIconProfile()

        setTextColor(icColor)
        setBackgroundIconColor(icColor)
    }

    private fun setTextColor(icColor: Int) {
        binding.tvUserInitials.setTextColor(ContextCompat.getColor(this, icColor))
    }

    private fun setBackgroundIconColor(icColor: Int) {
        val sizeInDp = 50
        val density = resources.displayMetrics.density
        val sizeInPx = (sizeInDp * density).toInt()

        val shapeDrawable = ShapeDrawable(OvalShape())
        shapeDrawable.paint.color = icColor
        shapeDrawable.setBounds(0,0,sizeInPx, sizeInPx)

        binding.vBackgroundUserIcon.background = shapeDrawable
    }

    private fun setListener() {
        binding.btnAddPatient.setOnClickListener{
            barcodeLauncher.launch(vM.getScanOptions())
        }
        binding.clIconAccount.setOnClickListener{
            val accountFragment = ProfessionalProfileFragment()

            supportFragmentManager
                .beginTransaction()
                .replace(R.id.flHomeProfessional, accountFragment)
                .commit()
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