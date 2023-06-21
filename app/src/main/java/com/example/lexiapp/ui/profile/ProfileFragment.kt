package com.example.lexiapp.ui.profile

import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.lexiapp.ui.profile.edit.EditProfileActivity
import com.example.lexiapp.ui.profile.link.LinkPatientActivity
import com.example.lexiapp.domain.model.User
import androidx.fragment.app.viewModels
import com.example.lexiapp.databinding.FragmentProfileBinding
import com.example.lexiapp.domain.useCases.ProfileUseCases
import com.example.lexiapp.ui.login.LoginActivity
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var logout: MaterialButton

    @Inject
    lateinit var profileUseCases: ProfileUseCases

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getViews()
        setListeners()
        setObserver()
    }

    private fun setObserver() {
        viewModel.profileLiveData.observe(viewLifecycleOwner) { user ->
                setProfile(user!!)
        }
    }

    private fun setProfile(user: User) {
        binding.txtEmail.text = user!!.email
        Log.v("USER_NAME_FIRESTORE_FRAGMENT", "${user.userName}")
        user.userName.let { binding.txtName.text = it }
        user.birthDate.let { binding.txtBirthDate.text = it }
    }

    private fun getViews() {
        logout = binding.btnLogOut
        setIconUserInitials()
    }

    private fun setIconUserInitials() {
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
        binding.tvUserInitials.setTextColor(ContextCompat.getColor(requireContext(), icColor))
    }

    private fun setBackgroundIconColor(icColor: Int) {
        val color = ContextCompat.getColor(requireContext(), icColor)

        val newDrawable = GradientDrawable()
        newDrawable.shape = GradientDrawable.OVAL
        newDrawable.setColor(color)

        binding.vBackgroundUserIcon.background = newDrawable
    }

    private fun setListeners() {
        btnLogoutClick()
        btnEditProfile()
        btnLink()
    }

    private fun btnEditProfile() {
        binding.btnEditProfile.setOnClickListener {
            startActivity(Intent(activity, EditProfileActivity::class.java))
        }
    }

    private fun btnLink() {
        binding.btnLinkAccount.setOnClickListener {
            viewModel.cleanIsLinked()
            viewModel.isLinked()
            viewModel.isLinked.observe(viewLifecycleOwner) { isLink ->
                if (isLink == null) {
                    Toast.makeText(
                        activity,
                        "Estamos validando que no se encuentre asociado",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (!isLink) {
                    startActivity(Intent(activity, LinkPatientActivity::class.java))
                    viewModel.isLinked.removeObservers(viewLifecycleOwner)
                } else {
                    Toast.makeText(
                        activity,
                        "Ya se encuentra vinculado a un profesional",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun btnLogoutClick() {
        logout.setOnClickListener {
            viewModel.closeSesion()
            requireActivity().finish()
            startActivity(Intent(activity, LoginActivity::class.java))
        }
    }
}
