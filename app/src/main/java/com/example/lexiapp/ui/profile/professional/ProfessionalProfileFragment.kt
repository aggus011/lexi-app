package com.example.lexiapp.ui.profile.professional

import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.lexiapp.R
import com.example.lexiapp.databinding.FragmentProfessionalProfileBinding
import com.example.lexiapp.domain.useCases.ProfileUseCases
import com.example.lexiapp.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfessionalProfileFragment : Fragment() {
    private var _binding: FragmentProfessionalProfileBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var profileUseCases: ProfileUseCases

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfessionalProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setProfessionalData()
        setListeners()
    }

    private fun setProfessionalData(){
        val name = arguments?.getString("name")
        val email = arguments?.getString("email")
        val medicalRegistration = arguments?.getString("medical_registration")

        binding.tvProfessionalName.text = name
        binding.tvProfessionalEmail.text = email
        binding.tvProfessionalMedicalRegistration.text = getString(R.string.medical_registration_number).plus(medicalRegistration)

        setTextIcon()
        setColors()
    }

    private fun setTextIcon() {
        val initials = arguments?.getString("initials")
        binding.tvUserInitials.text = initials
    }

    private fun setColors(){
        val icColor = profileUseCases.getColorRandomForIconProfile()
        setBackgroundIconColor(icColor)
    }

    private fun setBackgroundIconColor(icColor: Int) {
        val color = ContextCompat.getColor(
            requireContext(),
            arguments?.getInt("icon_color") ?: icColor
        )

        val newDrawable = GradientDrawable()
        newDrawable.shape = GradientDrawable.OVAL
        newDrawable.setColor(color)

        binding.vBackgroundUserIcon.background = newDrawable
    }

    private fun setListeners() {
        closeProfile()
        signOff()
    }

    private fun closeProfile() {
        binding.icClose.setOnClickListener {
            parentFragmentManager
                .beginTransaction()
                .remove(this)
                .commit()
        }
    }

    private fun signOff(){
        binding.btnLogOut.setOnClickListener {
            profileUseCases.closeSesion()
            requireActivity().finish()
            startActivity(Intent(activity, LoginActivity::class.java))
        }
    }
}