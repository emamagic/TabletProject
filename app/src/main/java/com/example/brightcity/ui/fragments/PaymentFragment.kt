package com.example.brightcity.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.brightcity.R
import com.example.brightcity.api.safe.ApiWrapper
import com.example.brightcity.databinding.FragmentChargeBinding
import com.example.brightcity.databinding.FragmentPaymentBinding
import com.example.brightcity.ui.viewmodels.PaymentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentFragment: DialogFragment() {

    private val viewModel: PaymentViewModel by viewModels()
    private var _binding: FragmentPaymentBinding? = null
    private val binding get() = _binding

    companion object {
        fun newInstance(): PaymentFragment{
            val args = Bundle()

            val fragment = PaymentFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPaymentBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeOnTransactionAdd()


    }

    private fun transactionAdd(userID: Long ,user_factorId: Long ,title: String ,price: String ,cash: String ,cart: String ,offCodID: String ,paydeviceId: Int? = null) {
        viewModel.transactionAdd(userID, user_factorId, title, price, cash, cart, offCodID, paydeviceId)
    }


    private fun subscribeOnTransactionAdd() {
        viewModel.transactionAdd.observe(viewLifecycleOwner){ response ->

            when(response){
                is ApiWrapper.Success -> {
                    response.data?.let {
                        Log.e("TAG", "subscribeOnTransactionAdd: ${it.status}")
                    }
                }
                is ApiWrapper.ApiError -> {
                    response.error?.let {
                        Log.e("TAG", "subscribeOnTransactionAdd: $it")
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                }
                is ApiWrapper.UnknownError -> {
                    Log.e("TAG", "subscribeOnTransactionAdd: ${response.message}")
                    Toast.makeText(
                        requireContext(),
                        requireContext().resources.getString(R.string.toastyError),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is ApiWrapper.NetworkError -> {
                    Toast.makeText(
                        requireContext(),
                        requireContext().resources.getString(R.string.toastyNet),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }
}