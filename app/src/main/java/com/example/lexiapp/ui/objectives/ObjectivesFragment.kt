package com.example.lexiapp.ui.objectives

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lexiapp.databinding.FragmentObjectivesBinding


class ObjectivesFragment : Fragment() {
    private var _binding: FragmentObjectivesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentObjectivesBinding.inflate(inflater, container, false)
        return binding.root
    }

}