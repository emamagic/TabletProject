package com.example.brightcity.ui.fragments

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.brightcity.R
import com.example.brightcity.api.safe.ApiWrapper
import com.example.brightcity.databinding.FragmentChargeBinding
import com.example.brightcity.databinding.FragmentPaymentBinding
import com.example.brightcity.interfaces.OnCallBackCharge
import com.example.brightcity.ui.viewmodels.PaymentViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.math.BigDecimal
import java.text.DecimalFormat

@AndroidEntryPoint
class PaymentFragment(private val userID: Long ,private val factorID: Long ,private val totalPrice: String ,private val call: OnCallBackCharge): DialogFragment() {

    private val viewModel: PaymentViewModel by viewModels()
    private var _binding: FragmentPaymentBinding? = null
    private var cashPrice: String? = ""
    private var cardPrice: String? = ""
    private val binding get() = _binding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPaymentBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        subscribeOnTransactionAdd()

        binding?.editPaymentFCostForCart?.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                splitDigitNumber(binding?.editPaymentFCostForCart!!,this)
            }
        })
        binding?.editPaymentFCostForCash?.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                splitDigitNumber(binding?.editPaymentFCostForCash!!,this)
            }
        })
        binding?.txtPaymentFCost?.text = totalPrice
        binding?.btnPaymentFCancel?.setOnClickListener { dismiss() }
        binding?.btnPaymentFPay?.setOnClickListener {
            cashPrice = binding?.editPaymentFCostForCash?.text?.toString()?.filter { it != ',' }
          //  cardPrice = binding?.editPaymentFCostForCart?.text?.toString()?.filter { it != ',' }
            transactionAdd(userID ,factorID ," ",totalPrice ,cashPrice!! ," " ,null ,null)
        }

    }

    private fun transactionAdd(userID: Long ,user_factorId: Long ,title: String ,price: String ,cash: String ,cart: String ,offCodID: String? = null ,paydeviceId: Int? = null) {
        viewModel.transactionAdd(userID, user_factorId, title, price, cash, cart, offCodID, paydeviceId)
    }


    private fun subscribeOnTransactionAdd() {
        viewModel.transactionAdd.observe(viewLifecycleOwner){ response ->

            when(response){
                is ApiWrapper.Success -> {
                    response.data?.let {
                        Log.e("TAG", "subscribeOnTransactionAdd: ${it.status}")
                        Toast.makeText(requireContext(), it.status, Toast.LENGTH_SHORT).show()
                        dismiss()
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

    private fun splitDigitNumber(editText: EditText, watcher: TextWatcher) {
        editText.removeTextChangedListener(watcher)
        val text = editText.text.toString()
        if (text.isNotEmpty()) {
            val decimalFormat = DecimalFormat("#,###")
            val format = decimalFormat.format(
                java.lang.Double.valueOf(BigDecimal(text.replace(",".toRegex(), "")).toString())
            )
            editText.setText(format)
            editText.setSelection(format.length)
        }
        editText.addTextChangedListener(watcher)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        call.onViewStarted()
    }
}