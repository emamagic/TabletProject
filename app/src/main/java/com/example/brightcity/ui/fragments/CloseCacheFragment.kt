package com.example.brightcity.ui.fragments

import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.brightcity.R
import com.example.brightcity.api.responses.PayDevicesResponse
import com.example.brightcity.api.safe.ApiWrapper
import com.example.brightcity.databinding.FragmentCloseCashierBinding
import com.example.brightcity.models.SummeryCacheModel
import com.example.brightcity.ui.adapter.CloseCashierAdapter
import com.example.brightcity.ui.viewmodels.CloseCacheViewModel
import com.example.brightcity.util.Constance.USER_ID
import dagger.hilt.android.AndroidEntryPoint
import java.math.BigDecimal
import java.text.DecimalFormat
import javax.inject.Inject

@AndroidEntryPoint
class CloseCacheFragment : DialogFragment(), CloseCashierAdapter.Interaction {

    private val viewModel: CloseCacheViewModel by viewModels()
    private var _binding: FragmentCloseCashierBinding? = null
    private val binding get() = _binding
    private lateinit var closeCashAdapter: CloseCashierAdapter
    private lateinit var priceTypesArray: ArrayList<String>
    private lateinit var cardTypesArray: ArrayList<PayDevicesResponse>
    private lateinit var summeryCacheModels: ArrayList<SummeryCacheModel>
    private lateinit var cardSpinnerAdapter: ArrayAdapter<String>
    private var userId: Long = 0
    private var cardType: String = " "
    private var description: String = " "
    private var amount: String = " "
    // type ->  نقدی(1) - کارتخوان (2)- بن(3)
    private var type: Int = 0
    private var transactionIds: String = ""
    private var isUpdatingMode: Boolean = false
    private var summeryItemDelete: SummeryCacheModel? = null
    private var summeryItemUpdate: SummeryCacheModel? = null
    private var cashInfo: CashInfo? = null

    @Inject
    lateinit var pref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        closeCashAdapter = CloseCashierAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCloseCashierBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        priceTypesArray = ArrayList()
        cardTypesArray = ArrayList()
        summeryCacheModels = ArrayList()
        userId = pref.getLong(USER_ID, 0)

        subscribeOnTransactionDelete()
        subscribeOnTransactionUpdate()
        subscribeOnTransactionAdd()
        subscribeOnTransactionClose()
        subscribeOnPayDevices()

        setUpPriceTypeSpinner()
        getPayDevices()

        binding?.btnCashAndAmountFCancel?.setOnClickListener { dismiss() }

        binding?.editCashAndAmountFReceive?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                splitDigitNumber(binding?.editCashAndAmountFReceive!!, this)
            }
        })

        binding?.btnCashAndAmountFCloseCach?.setOnClickListener {
            if (binding?.editCashAndAmountFDelivered?.text.toString()
                    .isNotEmpty() && binding?.editCashAndAmountFTaken?.text.toString().isNotEmpty()
            ){
                if (summeryCacheModels.size == 0) {
                    Toast.makeText(requireContext(), "تراکنشی ثبت نشده است", Toast.LENGTH_SHORT).show()
                }else{
                    summeryCacheModels.forEachIndexed { index, summeryCacheModel ->
                        transactionIds += if (index == summeryCacheModels.lastIndex) "${summeryCacheModel.id}"
                         else "${summeryCacheModel.id},"
                    }

                    transactionClose(
                        transactionIds,
                        userId.toInt(),
                        binding?.editCashAndAmountFDelivered?.text.toString().toInt(),
                        binding?.editCashAndAmountFTaken?.text.toString().toInt()
                    )
                }

            } else Toast.makeText(
                requireContext(),
                "لطفا تعداد کارت تحویل گرفته شده و تحویل داده شده را وارد نمایید",
                Toast.LENGTH_LONG
            ).show()
        }

        binding?.btnCashAndAmountFSubmit?.setOnClickListener {
            amount = binding?.editCashAndAmountFReceive?.text.toString().filter { it != ',' }
            description = binding?.editCashAndAmountFDescription?.text.toString()
            if (amount.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "لطفا مبلغ دریافتی را وارد نمایید",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            if (description.isEmpty()) description = " "
            showLoading()
            if (!isUpdatingMode) transactionAdd(userId.toInt(), description, amount, type ,cardTypesArray.find { it.name == cardType }?.id?.toInt()!!)
            else {
                isUpdatingMode = false
                transactionUpdate(summeryItemUpdate?.id!!, userId.toInt(), description, amount, type ,cardTypesArray.find { it.name == cardType }?.id?.toInt()!!)
            }

        }

        binding?.recyclerCloseCashier?.adapter = closeCashAdapter
        closeCashAdapter.submitList(summeryCacheModels)

    }

    private fun setUpPriceTypeSpinner() {
        priceTypesArray.add("نقدی")
        priceTypesArray.add("کارتخوان")
        priceTypesArray.add("بن")
        priceType(priceTypesArray)
        priceTypeListener()
    }

    private fun setUpCardTypeSpinner(list: List<PayDevicesResponse>) {
        cardType(list as ArrayList)
        cardTypeListener()
    }

    private fun priceType(priceTypes: ArrayList<String>) {

        val priceTypeAdapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_item,
            android.R.id.text1,
            priceTypes
        )
        priceTypeAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        binding?.spinner1CloseCach?.adapter = priceTypeAdapter
        binding?.spinner1CloseCach?.isEnabled = true
    }

    private fun priceTypeListener() {
        binding?.spinner1CloseCach?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parentView: AdapterView<*>?,
                    selectedItemView: View?,
                    position: Int,
                    id: Long
                ) {
                    type = (id + 1).toInt()
                    if (id != 0L && id != 2L) binding?.spinnerCashAndAmountFReadCart?.visibility = View.VISIBLE
                    else binding?.spinnerCashAndAmountFReadCart?.visibility = View.INVISIBLE


                }

                override fun onNothingSelected(parentView: AdapterView<*>?) {}
            }

    }

    private fun cardType(cardTypes: ArrayList<PayDevicesResponse>) {

        cardSpinnerAdapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_item,
            android.R.id.text1,
            cardTypes.map { it.name }
        )
        cardSpinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        binding?.spinner2CloseCach?.adapter = cardSpinnerAdapter
        binding?.spinner2CloseCach?.isEnabled = true
    }

    private fun cardTypeListener() {
        binding?.spinner2CloseCach?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parentView: AdapterView<*>?,
                    selectedItemView: View?,
                    position: Int,
                    id: Long
                ) {
                    cardType = parentView?.selectedItem.toString()
                }

                override fun onNothingSelected(parentView: AdapterView<*>?) {}
            }

    }

    private fun getPayDevices(){
        viewModel.getPayDevices()
    }

    private fun transactionDelete(id: Long) {
        viewModel.transactionDelete(id)
    }

    private fun transactionUpdate(
        id: Long,
        casheirId: Int,
        description: String,
        amount: String,
        type: Int,
        payDeviceId: Int
    ) {
        cashInfo = CashInfo(casheirId, description, amount, type)
        viewModel.transactionUpdate(id, casheirId, description, amount, type , payDeviceId)
    }

    private fun transactionAdd(casheirId: Int, description: String, amount: String, type: Int ,payDeviceId: Int) {
        Log.e("TAG", "transactionAdd: $casheirId $description $amount $type $payDeviceId")
        viewModel.transactionAdd(casheirId, description, amount, type ,payDeviceId)
    }

    private fun transactionClose(
        ids: String,
        casheirId: Int,
        recivecards: Int,
        resendcards: Int
    ) {
        viewModel.transactionClose(ids, casheirId, recivecards, resendcards)
    }

    private fun subscribeOnTransactionAdd() {
        viewModel.transactionAdd.observe(viewLifecycleOwner) { response ->
            hideLoading()
            when (response) {
                is ApiWrapper.Success -> {
                    response.data?.let {
                        summeryCacheModels.add(
                            SummeryCacheModel(
                                it.id,
                                amount,
                                type,
                                if (type == 2) cardType else " ",
                                description
                            )
                        )
                        closeCashAdapter.submitList(summeryCacheModels)
                    }
                }
                is ApiWrapper.NetworkError -> {
                    Toast.makeText(
                        requireContext(),
                        requireContext().resources.getString(R.string.toastyNet),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is ApiWrapper.ApiError -> {
                    response.error?.let {
                        Log.e("TAG", "subscribeOnAddPerson: $it")
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                }
                is ApiWrapper.UnknownError -> {
                    Log.e("TAG", "subscribeOnAddPerson: ${response.message}")
                    Toast.makeText(
                        requireContext(),
                        requireContext().resources.getString(R.string.toastyError),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            resetValues()
        }
    }

    private fun subscribeOnTransactionUpdate() {
        viewModel.transactionUpdate.observe(viewLifecycleOwner) { response ->
            hideLoading()
            when (response) {
                is ApiWrapper.Success -> {
                    response.data?.let {
                        summeryCacheModels[summeryCacheModels.indexOf(summeryItemUpdate)] =
                            SummeryCacheModel(summeryItemUpdate?.id!! ,cashInfo?.amount!! ,cashInfo?.type!! ,if (type == 2) cardType else " " ,cashInfo?.description)
                        closeCashAdapter.submitList(summeryCacheModels)
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                }
                is ApiWrapper.NetworkError -> {
                    Toast.makeText(
                        requireContext(),
                        requireContext().resources.getString(R.string.toastyNet),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is ApiWrapper.ApiError -> {
                    response.error?.let {
                        Log.e("TAG", "subscribeOnAddPerson: $it")
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                }
                is ApiWrapper.UnknownError -> {
                    Log.e("TAG", "subscribeOnAddPerson: ${response.message}")
                    Toast.makeText(
                        requireContext(),
                        requireContext().resources.getString(R.string.toastyError),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            resetValues()
        }
    }

    private fun subscribeOnTransactionClose() {
        viewModel.transactionClose.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ApiWrapper.Success -> {
                    response.data?.let {
                        Toast.makeText(requireContext(), "با موفقیت ثبت شد", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                is ApiWrapper.NetworkError -> {
                    Toast.makeText(
                        requireContext(),
                        requireContext().resources.getString(R.string.toastyNet),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is ApiWrapper.ApiError -> {
                    response.error?.let {
                        Log.e("TAG", "subscribeOnAddPerson: $it")
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                }
                is ApiWrapper.UnknownError -> {
                    Log.e("TAG", "subscribeOnAddPerson: ${response.message}")
                    Toast.makeText(
                        requireContext(),
                        requireContext().resources.getString(R.string.toastyError),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            dismiss()
        }
    }

    private fun subscribeOnTransactionDelete() {
        viewModel.transactionDelete.observe(viewLifecycleOwner) { response ->
            hideLoading()
            when (response) {
                is ApiWrapper.Success -> {
                    response.data?.let {
                        summeryCacheModels.remove(summeryItemDelete)
                        closeCashAdapter.submitList(summeryCacheModels)
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                }
                is ApiWrapper.NetworkError -> {
                    Toast.makeText(
                        requireContext(),
                        requireContext().resources.getString(R.string.toastyNet),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is ApiWrapper.ApiError -> {
                    response.error?.let {
                        Log.e("TAG", "subscribeOnAddPerson: $it")
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                }
                is ApiWrapper.UnknownError -> {
                    Log.e("TAG", "subscribeOnAddPerson: ${response.message}")
                    Toast.makeText(
                        requireContext(),
                        requireContext().resources.getString(R.string.toastyError),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            resetValues()
        }
    }

    private fun subscribeOnPayDevices() {
        viewModel.payDevices.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ApiWrapper.Success -> {
                    response.data?.let { list ->
                        if (list.isEmpty()){
                            cardTypesArray.add(PayDevicesResponse(1 ,"s" ,""))
                            cardTypesArray.add(PayDevicesResponse(2 ,"a" ,""))
                            cardTypesArray.add(PayDevicesResponse(3 ,"h" ,""))
                            cardTypesArray.add(PayDevicesResponse(4 ,"d" ,""))
                            setUpCardTypeSpinner(cardTypesArray)
                        }else{
                            setUpCardTypeSpinner(list)
                        }
                    }
                }
                is ApiWrapper.NetworkError -> {
                    Toast.makeText(
                        requireContext(),
                        requireContext().resources.getString(R.string.toastyNet),
                        Toast.LENGTH_SHORT
                    ).show()
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
            }
        }
    }

    override fun onDeleteSelected(item: SummeryCacheModel) {
        showLoading()
        summeryItemDelete = item
        transactionDelete(item.id)
    }

    override fun onEditSelected(item: SummeryCacheModel) {
        isUpdatingMode = true
        summeryItemUpdate = item
        binding?.editCashAndAmountFReceive?.setText(item.price)
        binding?.editCashAndAmountFDescription?.setText(item.description)
        val position = (item.type) - 1
        binding?.spinner1CloseCach?.setSelection(position)
        if (position != 0){
            val spinnerPosition = cardSpinnerAdapter.getPosition(item.cartName)
            binding?.spinner2CloseCach?.setSelection(spinnerPosition)
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

    private fun showLoading(){
        binding?.loading?.visibility = View.VISIBLE
        binding?.btnCashAndAmountFCloseCach?.visibility = View.GONE
        binding?.btnCashAndAmountFCancel?.visibility = View.GONE
    }

    private fun hideLoading() {
        binding?.loading?.visibility = View.GONE
        binding?.btnCashAndAmountFCloseCach?.visibility = View.VISIBLE
        binding?.btnCashAndAmountFCancel?.visibility = View.VISIBLE
    }

    private fun resetValues() {
        binding?.editCashAndAmountFReceive?.setText("")
        binding?.editCashAndAmountFDescription?.setText("")
        binding?.spinner1CloseCach?.setSelection(0)
        binding?.spinner2CloseCach?.setSelection(0)
    }

    data class CashInfo(
        val casheirId: Int,
        val description: String,
        val amount: String,
        val type: Int)

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}