package com.example.lexiapp.ui.profesionalhome

import android.content.Intent
import android.graphics.drawable.GradientDrawable
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
import com.example.lexiapp.domain.model.FirebaseResult
import com.example.lexiapp.domain.model.User
import com.example.lexiapp.domain.useCases.ProfileUseCases
import com.example.lexiapp.ui.adapter.UserAdapter
import com.example.lexiapp.ui.profesionalhome.detailpatient.DetailPatientFragment
import com.example.lexiapp.ui.profesionalhome.note.CreateNoteActivity
import com.example.lexiapp.ui.profesionalhome.note.RecordNoteActivity
import com.example.lexiapp.ui.profesionalhome.resultlink.SuccessfulLinkActivity
import com.example.lexiapp.ui.profesionalhome.resultlink.UnsuccessfulLinkActivity
import com.example.lexiapp.ui.profile.professional.ProfessionalProfileFragment
import com.google.gson.Gson
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
        if(result.contents != null) {
            try {
                val email = vM.getPatientEmail(result.contents)
                vM.addPatientToProfessional(email!!)
                startActivity(Intent(this, SuccessfulLinkActivity::class.java))
            } catch (e: Exception) {
                startActivity(Intent(this, UnsuccessfulLinkActivity::class.java))
            }
        }
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
                binding.apply {
                    rvPatient.visibility = View.VISIBLE
                    btnAddPatient.visibility = View.VISIBLE
                    svFilter.visibility = View.VISIBLE
                    ivMiniLogo.visibility = View.VISIBLE
                    clIconAccount.visibility = View.VISIBLE
                }
            } else {
                binding.apply {
                    rvPatient.visibility = View.GONE
                    btnAddPatient.visibility = View.GONE
                    svFilter.visibility = View.GONE
                    ivMiniLogo.visibility = View.GONE
                    clIconAccount.visibility = View.GONE
                }
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

        //setTextColor(icColor)
        setBackgroundIconColor(icColor)
    }

    private fun setTextColor(icColor: Int) {
        binding.tvUserInitials.setTextColor(ContextCompat.getColor(this, icColor))
    }

    private fun setBackgroundIconColor(icColor: Int) {
        val color = ContextCompat.getColor(this, icColor)

        val newDrawable = GradientDrawable()
        newDrawable.shape = GradientDrawable.OVAL
        newDrawable.setColor(color)

        binding.vBackgroundUserIcon.background = newDrawable
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
            binding.rvPatient.adapter = UserAdapter(
                list,
                ::viewDetails,
                ::unbindPatient,
                ::startCreateNoteActivity,
                ::startRecordNoteActivity
            )
        }
        vM.resultAddPatient.observe(this) { result ->
            if (result == FirebaseResult.TaskSuccess) {
                Toast.makeText(this,"Se agregó con éxito", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this,"No se pudo agregar", Toast.LENGTH_SHORT).show()
            }
        }
        vM.resultDeletePatient.observe(this) { result ->
            if (result == FirebaseResult.TaskSuccess) {
                Toast.makeText(this,"Se eliminó con éxito", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this,"No se pudo eliminar", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun unbindPatient(email: String){
        vM.unbindPatient(email)
    }

    private fun viewDetails(patient: User){
        vM.setPatientSelected(patient)
        supportFragmentManager.beginTransaction()
            .show(detailFragment!!)
            .addToBackStack(TAG_FRAGMENT_DETAIL)
            .commit()
    }

    private fun startCreateNoteActivity(emailPatient: String){
        val intent = Intent(applicationContext, CreateNoteActivity::class.java)
        intent.putExtra("emailPatient", emailPatient)
        startActivity(intent)
    }

    private fun startRecordNoteActivity(patient: User){
        val intent = Intent(applicationContext, RecordNoteActivity::class.java)
        intent.putExtra("patient", Gson().toJson(patient))
        startActivity(intent)
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