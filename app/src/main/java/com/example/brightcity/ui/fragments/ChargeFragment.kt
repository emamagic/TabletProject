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
import com.example.brightcity.ui.viewmodels.ChargeViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.math.BigDecimal
import java.text.DecimalFormat

@Suppress("JAVA_CLASS_ON_COMPANION")
@AndroidEntryPoint
class ChargeFragment: DialogFragment() {

    private val TAG = ChargeFragment.javaClass.simpleName

    private val viewModel: ChargeViewModel by viewModels()
    private var _binding: FragmentChargeBinding? = null
    private val binding get() = _binding
    private var userId: Long? = null
    private var factorId: Long? = null

    companion object {
        fun newInstance(userId: Long): ChargeFragment{
            val args = Bundle()
            args.putLong("userId" ,userId)
            val fragment = ChargeFragment()
            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId = arguments?.getLong("userId")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        subscribeOnUserInfo()
        subscribeOnGetFactor()
        subscribeOnAddCharge()
        subscribeOnAddOffCode()
        subscribeOnDelete()
        subscribeOnPause()
        subscribeOnPlay()

        getUserInfo(userId)
        getFactor(userId!!)

        binding?.btnChargeFCancel?.setOnClickListener { dismiss() }

        binding?.btnChargeFPay?.setOnClickListener {

        }

        binding?.include1?.btnChargeFSubmitCodeBon?.setOnClickListener {

        }

        binding?.include1?.btnPessonalProileFAdd?.setOnClickListener {

        }

        binding?.include1?.editChargeFGetCharge?.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                splitDigitNumber(binding?.include1?.editChargeFGetCharge!! ,this)
            }
        })
        binding?.include1?.editChargeFCodeBon?.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                splitDigitNumber(binding?.include1?.editChargeFCodeBon!! ,this)
            }
        })


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChargeBinding.inflate(inflater, container, false)
        return _binding?.root
    }


    private fun getUserInfo(userId: Long?) {
        showLoading()
        viewModel.getUserInfo(userId)
    }

    private fun getFactor(userId: Long) {
        viewModel.getFactor(userId)
    }

    private fun addCharge(price: String ,factorId: Long) {
        viewModel.addCharge(price, factorId)
    }

    private fun addOffCode(price: String ,factorId: Long) {
        viewModel.addOffCode(price, factorId)
    }

    private fun play(factorismId: Long ,factorId: Long) {
        viewModel.play(factorismId, factorId)
    }

    private fun pause(factorismId: Long ,factorId: Long) {
        viewModel.pause(factorismId, factorId)
    }

    private fun delete(factorismId: Long ,factorId: Long) {
        viewModel.delete(factorismId, factorId)
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

    private fun subscribeOnUserInfo() {
        viewModel.userInfo.observe(viewLifecycleOwner){ response ->
            hideLoading()
            when(response){
                is ApiWrapper.Success -> {
                    response.data?.let {

                    }
                }
                is ApiWrapper.ApiError -> {
                    response.error?.let {
                        Log.e("TAG", "subscribeOnPayDevices: $it")
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                }
                is ApiWrapper.UnknownError -> {
                    Log.e("TAG", "subscribeOnPayDevices: ${response.message}")
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

    private fun subscribeOnGetFactor() {
        viewModel.factor.observe(viewLifecycleOwner){ response ->
            hideLoading()
            when(response){
                is ApiWrapper.Success -> {
                    response.data?.let {
                        factorId = it.id
                    }
                }
                is ApiWrapper.ApiError -> {
                    response.error?.let {
                        Log.e("TAG", "subscribeOnPayDevices: $it")
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                }
                is ApiWrapper.UnknownError -> {
                    Log.e("TAG", "subscribeOnPayDevices: ${response.message}")
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

    private fun subscribeOnAddCharge(){
        viewModel.addCharge.observe(viewLifecycleOwner){ response ->
            hideLoading()
            when(response){
                is ApiWrapper.Success -> {
                    response.data?.let {

                    }
                }
                is ApiWrapper.ApiError -> {
                    response.error?.let {
                        Log.e("TAG", "subscribeOnAddCharge: $it")
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                }
                is ApiWrapper.UnknownError -> {
                    Log.e("TAG", "subscribeOnAddCharge: ${response.message}")
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

    private fun subscribeOnAddOffCode() {
        viewModel.addOffCode.observe(viewLifecycleOwner){ response ->
            hideLoading()
            when(response){
                is ApiWrapper.Success -> {
                    response.data?.let {

                    }
                }
                is ApiWrapper.ApiError -> {
                    response.error?.let {
                        Log.e("TAG", "subscribeOnAddOffCode: $it")
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                }
                is ApiWrapper.UnknownError -> {
                    Log.e("TAG", "subscribeOnAddOffCode: ${response.message}")
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

    private fun subscribeOnPlay() {
        viewModel.play.observe(viewLifecycleOwner){ response ->
            hideLoading()
            when(response){
                is ApiWrapper.Success -> {
                    response.data?.let {

                    }
                }
                is ApiWrapper.ApiError -> {
                    response.error?.let {
                        Log.e("TAG", "subscribeOnPlay: $it")
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                }
                is ApiWrapper.UnknownError -> {
                    Log.e("TAG", "subscribeOnPlay: ${response.message}")
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

    private fun subscribeOnPause() {
        viewModel.pause.observe(viewLifecycleOwner){ response ->
            hideLoading()
            when(response){
                is ApiWrapper.Success -> {
                    response.data?.let {

                    }
                }
                is ApiWrapper.ApiError -> {
                    response.error?.let {
                        Log.e("TAG", "subscribeOnPause: $it")
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                }
                is ApiWrapper.UnknownError -> {
                    Log.e("TAG", "subscribeOnPause: ${response.message}")
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

    private fun subscribeOnDelete() {
        viewModel.delete.observe(viewLifecycleOwner){ response ->
            hideLoading()
            when(response){
                is ApiWrapper.Success -> {
                    response.data?.let {

                    }
                }
                is ApiWrapper.ApiError -> {
                    response.error?.let {
                        Log.e("TAG", "subscribeOnDelete: $it")
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                }
                is ApiWrapper.UnknownError -> {
                    Log.e("TAG", "subscribeOnDelete: ${response.message}")
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


    private fun showLoading() {
        binding?.loading?.visibility = View.VISIBLE
        binding?.btnChargeFPay?.visibility = View.GONE
        binding?.btnChargeFCancel?.visibility = View.GONE
    }

    private fun hideLoading() {
        binding?.loading?.visibility = View.GONE
        binding?.btnChargeFPay?.visibility = View.VISIBLE
        binding?.btnChargeFCancel?.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}