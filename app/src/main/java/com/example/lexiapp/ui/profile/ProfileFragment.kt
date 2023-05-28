package com.example.lexiapp.ui.profile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.lexiapp.R
import com.example.lexiapp.databinding.FragmentProfileBinding
import com.example.lexiapp.domain.useCases.LoginUseCases
import com.example.lexiapp.ui.login.LoginActivity
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var prefs: SharedPreferences
    @Inject
    lateinit var authProv: LoginUseCases
    private lateinit var btnLogout: MaterialButton
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        setPreferences()
        //setAuthProv()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getProfile()
        getViews()
        setListeners()
        setObserver()
    }

    private fun getProfile() {
        val userEmail=prefs.getString("email", null)
        Log.v("USER_EMAIL_FROM_PREFS", "$userEmail")
        userEmail?.let { viewModel.getProfile(it) }
    }

    private fun setObserver() {
        viewModel.perfilLiveData.observe(viewLifecycleOwner){ user ->
            binding.txtEmail.text=user.email
            user.userName.let { binding.txtName.text=it }
            user.birthDate.let { binding.txtBirthDate.text=it.toString() }
        }
    }
/*
    private fun setAuthProv() {

        @Inject
        authProv = LoginUseCases()
    }
     */

    private fun setPreferences() {
        prefs = requireContext()
            .getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
    }

    private fun getViews() {
        btnLogout = binding.btnLogOut
    }

    private fun setListeners() {
        btnLogoutClick()
    }

    private fun btnLogoutClick() {
        btnLogout.setOnClickListener {
            prefs.edit().clear().apply()
            authProv.signOut()
            requireActivity().finish()
            startActivity(Intent(activity, LoginActivity::class.java))
        }
    }
}