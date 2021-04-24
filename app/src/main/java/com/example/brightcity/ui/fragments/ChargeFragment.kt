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
import com.example.brightcity.api.responses.ItemsListResponse
import com.example.brightcity.api.responses.ProductListResponse
import com.example.brightcity.api.responses.TransactionListResponse
import com.example.brightcity.api.safe.ApiWrapper
import com.example.brightcity.databinding.FragmentChargeBinding
import com.example.brightcity.ui.adapter.ItemsAdapter
import com.example.brightcity.ui.adapter.ProductAdapter
import com.example.brightcity.ui.viewmodels.ChargeViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.math.BigDecimal
import java.text.DecimalFormat
import javax.inject.Inject
import kotlin.math.abs

@AndroidEntryPoint
class ChargeFragment: DialogFragment() ,ProductAdapter.Interaction ,ItemsAdapter.Interaction {

    private val TAG = "ChargeFragment"

    private val viewModel: ChargeViewModel by viewModels()
    private var _binding: FragmentChargeBinding? = null
    private val binding get() = _binding
    private var userId: Long? = null
    private var factorId: Long? = null
    private lateinit var productAdapter: ProductAdapter
    private lateinit var itemsAdapter: ItemsAdapter
    @Inject
    lateinit var picasso: Picasso

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
        productAdapter = ProductAdapter(this ,picasso)
        itemsAdapter = ItemsAdapter(this ,picasso)
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
        subscribeOnTransactionAdd()
        subscribeOnProductList()
        subscribeOnItemsList()

        getUserInfo(userId)
        getFactor(userId!!)

        binding?.btnChargeFCancel?.setOnClickListener { dismiss() }

        binding?.btnChargeFPay?.setOnClickListener {
            // transactionAdd()
        }

        binding?.include1?.btnChargeFSubmitCodeBon?.setOnClickListener {
            val offCode = binding?.include1?.editChargeFCodeBon?.text?.toString()?.filter { it != ',' }
            if (offCode?.isNotEmpty()!!) addOffCode(offCode ,factorId!!)
        }

        binding?.include1?.btnPessonalProileFAdd?.setOnClickListener {
            val charge = binding?.include1?.editChargeFGetCharge?.text?.toString()?.filter { it != ',' }
            if (charge?.isNotEmpty()!!) addCharge(charge ,factorId!!)
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

        binding?.relativeLayout2?.button?.setOnClickListener {
            AddPersonFragment.newInstance(userId!!).show(childFragmentManager ,null)
        }

        binding?.relativeLayout2?.relativeLayout55?.setOnClickListener {
            PayBackFragment().show(childFragmentManager ,null)
        }


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

    private fun transactionAdd(userID: Long ,user_factorId: Long ,title: String ,price: String ,cash: String ,cart: String ,offCodID: String ,paydeviceId: Int) {
        viewModel.transactionAdd(userID, user_factorId, title, price, cash, cart, offCodID, paydeviceId)
    }

    private fun productList() {
        viewModel.productList()
    }

    private fun itemsList(factorId: Long) {
        viewModel.itemsList(factorId)
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
                        binding?.relativeLayout2?.txtChargeFGetName?.text = it.name
                        binding?.relativeLayout2?.txtChargeFGetAge?.text = it.age.toString()
                        binding?.relativeLayout2?.txtChargeFGetPhoneNumber?.text = it.mobile
                        binding?.relativeLayout2?.txtChargeFGetNationalId?.text = it.national_id
                        binding?.relativeLayout2?.textView16?.text = abs((it.credit)-(it.gift)).toString()
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
                        binding?.relativeLayout2?.textView13?.text = it.sumprice.toString()
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

    private fun subscribeOnTransactionAdd() {
        viewModel.transactionAdd.observe(viewLifecycleOwner){ response ->
            hideLoading()
            when(response){
                is ApiWrapper.Success -> {
                    response.data?.let {

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

    private fun subscribeOnProductList() {
        viewModel.productList.observe(viewLifecycleOwner){ response ->
            hideLoading()
            when(response){
                is ApiWrapper.Success -> {
                    response.data?.let {

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

    private fun subscribeOnItemsList() {
        viewModel.itemsList.observe(viewLifecycleOwner){ response ->
            hideLoading()
            when(response){
                is ApiWrapper.Success -> {
                    response.data?.let {

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

    override fun onItemSelectedProduct(position: Int, item: ProductListResponse) {

    }

    override fun onItemSelectedItem(position: Int, item: ItemsListResponse) {

    }


}