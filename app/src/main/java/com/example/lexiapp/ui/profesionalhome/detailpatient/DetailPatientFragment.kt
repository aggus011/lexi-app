package com.example.lexiapp.ui.profesionalhome.detailpatient

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.lexiapp.databinding.FragmentDetailPatientBinding
import com.example.lexiapp.domain.model.FirebaseResult
import com.example.lexiapp.domain.model.User
import com.example.lexiapp.ui.profesionalhome.ProfesionalHomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailPatientFragment : Fragment() {

    private var _binding: FragmentDetailPatientBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfesionalHomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailPatientBinding.inflate(inflater, container, false)
        setWiew()
        setObservers()
        return binding.root
    }

    private fun setWiew() {
        binding.cardContainer.isVerticalScrollBarEnabled = true
    }

    private fun setObservers() {
        viewModel.patientSelected.observe(viewLifecycleOwner) { patient ->
            if (patient != null) bind(patient)
        }
        setWITLObservers()
        setCWObservers()
    }

    private fun setCWObservers() {
        viewModel.countWordsPlayCW.observe(viewLifecycleOwner) { wordsCount ->
            binding.txtValueCountWordsCW.text = wordsCount.toString()
        }
        viewModel.hardLettersCW.observe(viewLifecycleOwner) { letter ->
            binding.txtValueLettersDificultsCW.text = letter.toString()
        }
        viewModel.avgCW.observe(viewLifecycleOwner) {average ->
            binding.txtValuePercentErrorCW.text = average
        }
    }

    private fun setWITLObservers() {
        viewModel.countWordsPlayWITL.observe(viewLifecycleOwner) { wordsCount ->
            binding.txtValueCountWordsWITL.text = wordsCount.toString()
        }
        viewModel.hardLettersWITL.observe(viewLifecycleOwner) { letter ->
            binding.txtValueLettersDificultsWITL.text = letter.toString()
        }
        viewModel.avgWITL.observe(viewLifecycleOwner) { average ->
            binding.txtValuePercentErrorWITL.text = average
        }
    }

    private fun bind(patient: User) {
        binding.apply {
            txtName.text = patient.userName
            txtEmail.text = patient.email
            tvUserInitials.text =
                if (patient.userName != null && patient.userName!!.isNotEmpty())
                    patient.userName!![0].uppercase()
                else patient.email[0].uppercase()
        }
        binding.btnTrash.setOnClickListener {
            viewModel.unbindPatient(patient.email)
            viewModel.resultDeletePatient.observe(viewLifecycleOwner) {
                if (it == FirebaseResult.TaskSuccess)
                    requireActivity().supportFragmentManager.popBackStack()
                else Toast.makeText(
                    activity,
                    "No se pudo desvincular al paciente, intentelo mas tarde",
                    Toast.LENGTH_SHORT
                )
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
        viewModel.cleanPatient()
    }
}