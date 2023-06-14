package com.example.lexiapp.ui.profesionalhome.detailpatient

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.lexiapp.databinding.FragmentDetailPatientBinding
import com.example.lexiapp.domain.model.FirebaseResult
import com.example.lexiapp.domain.model.Patient
import com.example.lexiapp.domain.model.User
import com.example.lexiapp.ui.profesionalhome.ProfesionalHomeViewModel

class DetailPatientFragment : Fragment() {
    private var _binding: FragmentDetailPatientBinding? = null
    private val binding get() = _binding!!

    val vM: ProfesionalHomeViewModel by activityViewModels<ProfesionalHomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailPatientBinding.inflate(inflater, container, false)
        setObserver()
        return binding.root
    }

    private fun setObserver() {
        vM.patientSelected.observe(viewLifecycleOwner) { patient ->
            if (patient != null) bind(patient)
        }
    }

    private fun bind(patient: User) {
        binding.txtName.text = patient.userName
        binding.txtEmail.text = patient.email
        binding.tvUserInitials.text = if (patient.userName!=null && patient.userName!!.isNotEmpty())
            patient.userName!![0].uppercase()
        else patient.email[0].uppercase()
        binding.btnTrash.setOnClickListener {
            vM.unbindPatient(patient.email)
            vM.resultDeletePatient.observe(viewLifecycleOwner){
                if (it==FirebaseResult.TaskSuccess)
                    requireActivity().supportFragmentManager.popBackStack()
                else Toast.makeText(activity,
                    "No se pudo desvincular al paciente, intentelo mas tarde",
                    Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDetach() {
        super.onDetach()
        vM.cleanPatient()
    }
}