package com.example.lexiapp.ui.profesionalhome

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lexiapp.R
import com.example.lexiapp.databinding.ActivityProfesionalHomeBinding
import com.example.lexiapp.domain.model.FirebaseResult
import com.example.lexiapp.domain.model.User
import com.example.lexiapp.domain.useCases.ProfileUseCases
import com.example.lexiapp.ui.adapter.UserAdapter
import com.example.lexiapp.ui.profesionalhome.detailpatient.DetailPatientActivity
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
    private lateinit var notificationPermission: Array<String>
    private var iconUserColor: Int? = null

    @Inject
    lateinit var profileUseCases: ProfileUseCases

    private val barcodeLauncher: ActivityResultLauncher<ScanOptions> =
        registerForActivityResult(ScanContract()) { result ->
            if (result.contents != null) {
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

        initArrayPermission()
        verifyNotificationPermission()
        getViews()
        setListener()
        setRecyclerView()
        setSearch()
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
            .requestPermissions(this, notificationPermission,
                NOTIFICATIONS_REQUEST_CODE
            )
    }

    private fun getViews() {
        setIconAccount()
    }

    private fun setIconAccount() {
        setTextIcon()
        setColors()
    }

    private fun setTextIcon() {
        binding.tvUserInitials.text = profileUseCases.userInitials()
    }

    private fun setColors() {
        val icColor = profileUseCases.getColorRandomForIconProfile()
        iconUserColor = icColor
        setBackgroundIconColor(icColor)
    }

    private fun setBackgroundIconColor(icColor: Int) {
        val color = ContextCompat.getColor(this, icColor)

        val newDrawable = GradientDrawable()
        newDrawable.shape = GradientDrawable.OVAL
        newDrawable.setColor(color)

        binding.vBackgroundUserIcon.background = newDrawable
    }

    private fun setListener() {
        binding.btnAddPatient.setOnClickListener {
            barcodeLauncher.launch(vM.getScanOptions())
        }
        binding.clIconAccount.setOnClickListener {
            val accountFragment = ProfessionalProfileFragment()
            accountFragment.arguments = getProfessionalData()

            supportFragmentManager
                .beginTransaction()
                .replace(R.id.flHomeProfessional, accountFragment)
                .commit()
        }
    }

    private fun setSearch() {
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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
        binding.rvPatient.layoutManager = LinearLayoutManager(this)
        suscribeToVM()
    }

    private fun suscribeToVM() {
        vM.listFilterPatient.observe(this) { list ->
            binding.rvPatient.adapter = UserAdapter(
                list,
                ::viewDetails,
                ::confirmUnbindPatient,
                ::startCreateNoteActivity,
                ::startRecordNoteActivity
            )
        }
        vM.resultAddPatient.observe(this) { result ->
            if (result == FirebaseResult.TaskSuccess) {
                Toast.makeText(this, "Se agregó con éxito", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No se pudo agregar", Toast.LENGTH_SHORT).show()
            }
        }
        vM.resultDeletePatient.observe(this) { result ->
            if (result == FirebaseResult.TaskSuccess) {
                Toast.makeText(this, "Se eliminó con éxito", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No se pudo eliminar", Toast.LENGTH_SHORT).show()
            }
        }
        vM.listPatient.observe(this){ list ->
            binding.clNotPatientsAdded.visibility = if(list.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun confirmUnbindPatient(emailPatient: String) {
        showConfirmationDialog(emailPatient)
    }

    private fun showConfirmationDialog(emailPatient: String) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("¿Esta seguro de que quiere desvincular al paciente: $emailPatient?")
            .setPositiveButton("Confirmar") { _, _ ->
                // Llama a la acción de confirmación
                unbindPatient(emailPatient)
            }
            .setNegativeButton("Cancelar", null)
        builder.create().show()
    }

    private fun unbindPatient(email: String) {
        vM.unbindPatient(email)
    }

    private fun viewDetails(patient: User) {
        val intent = Intent(this, DetailPatientActivity::class.java)
        intent.putExtra("patient", Gson().toJson(patient))
        startActivity(intent)
    }

    private fun startCreateNoteActivity(patient: User) {
        val intent = Intent(applicationContext, CreateNoteActivity::class.java)
        intent.putExtra("patient", Gson().toJson(patient))
        startActivity(intent)
    }

    private fun startRecordNoteActivity(patient: User) {
        val intent = Intent(applicationContext, RecordNoteActivity::class.java)
        intent.putExtra("patient", Gson().toJson(patient))
        startActivity(intent)
    }

    private fun verifyIsApiVersionIsHigherThan33(): Boolean{
        return Build.VERSION.SDK_INT >=
                Build.VERSION_CODES.TIRAMISU
    }

    private fun getProfessionalData(): Bundle{
        val args = Bundle()
        val name = profileUseCases.getProfile().userName
        val email = profileUseCases.getEmail()
        val initials = profileUseCases.userInitials()
        val medicalRegistration = profileUseCases.getMedicalRegistration()

        args.putString("name", name)
        args.putString("email", email)
        args.putString("initials", initials)
        args.putString("medical_registration", medicalRegistration)

        iconUserColor?.let {
            args.putInt("icon_color", it)
        }

        return args
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