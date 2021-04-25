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
import com.example.brightcity.databinding.PayBackFragmentBinding
import com.example.brightcity.ui.viewmodels.PayBackViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.math.BigDecimal
import java.text.DecimalFormat

@AndroidEntryPoint
class PayBackFragment: DialogFragment() {

    private val TAG = "PayBackFragment"
    private val viewModel: PayBackViewModel by viewModels()
    private var _binding: PayBackFragmentBinding? = null
    private val binding get() = _binding
    private var userId: Long? = 0
    private var factorId: Long? = 0
    private var payBack: String? = ""

    companion object {
        fun newInstance(userId: Long ,factorId: Long ,payBack: String): PayBackFragment{
            val args = Bundle()
            args.putLong("userId" ,userId)
            args.putLong("factorId" ,factorId)
            args.putString("payBack" ,payBack)
            val fragment = PayBackFragment()
            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId = arguments?.getLong("userId")
        factorId = arguments?.getLong("factorId")
        payBack = arguments?.getString("payBack")
    }

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
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        subscribeOnTransactionAdd()

        binding?.editPaymentFCostForCart?.hint = payBack
        binding?.editPaymentFCostForCart?.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                splitDigitNumber(binding?.editPaymentFCostForCart!! ,this)
            }
        })

        binding?.btnPaymentFCancel?.setOnClickListener { dismiss() }

        binding?.btnPaymentFPay?.setOnClickListener {
            val price = binding?.editPaymentFCostForCart?.text?.toString()?.filter { it != ',' }
            transactionAdd(userId!! ,factorId!! ," ","-$price" ,"-$price"," "," ",null)
        }

    }

    private fun transactionAdd(userID: Long ,user_factorId: Long ,title: String ,price: String ,cash: String ,cart: String ,offCodID: String ,paydeviceId: Int? = null) {
        viewModel.transactionAdd(userID, user_factorId, title, price, cash, cart, offCodID, paydeviceId)
    }

    private fun subscribeOnTransactionAdd() {
        viewModel.transactionAdd.observe(viewLifecycleOwner){ response ->
            when(response){
                is ApiWrapper.Success -> {
                    response.data?.let {
                        Log.e(TAG, "subscribeOnTransactionAdd: ${response.data}")
                        Toast.makeText(requireContext(), response.data.status, Toast.LENGTH_SHORT).show()
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
    }
}