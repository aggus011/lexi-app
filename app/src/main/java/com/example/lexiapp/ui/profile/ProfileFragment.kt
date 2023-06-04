package com.example.lexiapp.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.lexiapp.databinding.FragmentProfileBinding
import com.example.lexiapp.ui.login.LoginActivity
import com.example.lexiapp.ui.profesionalhome.ProfesionalHomeActivity
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var logout: MaterialButton
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.getProfile()
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
            binding.txtEmail.text = user.email
            Log.v("USER_NAME_FIRESTORE_FRAGMENT", "${user.userName}")
            user.userName.let { binding.txtName.text = it }
            //user.birthDate.let { binding.txtBirthDate.text=it.toString() }
        }
    }

    private fun getViews() {
        logout = binding.btnLogOut
    }

    private fun setListeners() {
        btnLogoutClick()
    }

    private fun btnLogoutClick() {
        logout.setOnClickListener {
            viewModel.closeSesion()
            requireActivity().finish()
            startActivity(Intent(activity, LoginActivity::class.java))
        }
    }
}