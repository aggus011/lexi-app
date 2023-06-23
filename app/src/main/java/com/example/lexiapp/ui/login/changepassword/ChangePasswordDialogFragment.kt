package com.example.lexiapp.ui.login.changepassword

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.lexiapp.databinding.FragmentChangePasswordDialogBinding
import com.example.lexiapp.domain.model.FirebaseResult
import com.example.lexiapp.ui.login.LoginViewModel
import kotlinx.coroutines.handleCoroutineException

class ChangePasswordDialogFragment : DialogFragment() {
    private var _binding: FragmentChangePasswordDialogBinding? = null
    private val binding get() = _binding!!
    private val vM: LoginViewModel by activityViewModels<LoginViewModel>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setTitle("Recuperar contraseña")
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setDimAmount(0.5F)
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChangePasswordDialogBinding.inflate(inflater, container, false)
        handleProgressBar(false)
        setListeners()
        setObserver()
        return binding.root
    }

    private fun setObserver() {
        vM.recoverResult.observe(viewLifecycleOwner){
            handleProgressBar(false)
            when (it) {
                FirebaseResult.TaskFailure-> {
                    Toast.makeText(
                        activity,
                        "Ocurrió un error, compruebe el correo o intentelo mas tarde",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.v("UI_RESPONSE", "NO_EXITOSO")
                }
                FirebaseResult.TaskSuccess -> {
                    Toast.makeText(
                        activity,
                        "Se envió el correo de recuperación",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.v("UI_RESPONSE", "EXITOSO")
                    dismiss()
                }
                null -> {
                    Toast.makeText(
                        activity,
                        "Debe completar el campo correo",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {}
            }
        }
    }

    private fun handleProgressBar (isLoading: Boolean){
        with(binding){
            if (isLoading){
                this.pbWaiting.visibility=View.VISIBLE
                this.txtLoading.visibility=View.VISIBLE
                this.btnCancel.visibility=View.INVISIBLE
                this.btnCancel.isEnabled=false
                this.btnRecoverPassword.visibility=View.INVISIBLE
                this.btnRecoverPassword.isEnabled=false
                this.etEmailForRecover.visibility=View.INVISIBLE
                this.txtInfoMessage.visibility=View.INVISIBLE
            }else{
                this.pbWaiting.visibility=View.INVISIBLE
                this.txtLoading.visibility=View.INVISIBLE
                this.btnCancel.visibility=View.VISIBLE
                this.btnCancel.isEnabled=true
                this.btnRecoverPassword.visibility=View.VISIBLE
                this.btnRecoverPassword.isEnabled=true
                this.etEmailForRecover.visibility=View.VISIBLE
                this.txtInfoMessage.visibility=View.VISIBLE
            }
        }
    }

    private fun setListeners() {
        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        binding.btnRecoverPassword.setOnClickListener {
            val email = binding.etEmailForRecover.text.toString()/*.trim()*/
            if (!validateFormatEmail(email)) {
                binding.etEmailForRecover.error = "Debe ingresar un correo valido"
                return@setOnClickListener
            }
            handleProgressBar(true)
            sendEmailRecoverPassword(email)
        }
    }

    private fun sendEmailRecoverPassword(email: String) {
        vM.sendRecoverEmail(email)
    }

    private fun validateFormatEmail(email: String) = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    override fun onDestroy() {
        super.onDestroy()
        vM.cleanRecoverResult()
    }
}
