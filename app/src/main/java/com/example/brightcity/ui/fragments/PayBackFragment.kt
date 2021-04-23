package com.example.brightcity.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.brightcity.databinding.FragmentChargeBinding
import com.example.brightcity.databinding.PayBackFragmentBinding
import com.example.brightcity.ui.viewmodels.ChargeViewModel

class PayBackFragment: DialogFragment() {

    private val TAG = "PayBackFragment"
    private var _binding: PayBackFragmentBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = PayBackFragmentBinding.inflate(inflater, container, false)
        return _binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}