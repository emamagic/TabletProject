package com.example.brightcity.ui.fragments

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.brightcity.R
import com.example.brightcity.api.responses.ProductListResponse
import com.example.brightcity.api.safe.ApiWrapper
import com.example.brightcity.databinding.FragmentIncentivePackagesBinding
import com.example.brightcity.databinding.PayBackFragmentBinding
import com.example.brightcity.interfaces.OnCallBackCharge
import com.example.brightcity.ui.adapter.ProductAdapter
import com.example.brightcity.ui.viewmodels.IncentiveViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class IncentiveFragment(private val call: OnCallBackCharge ,private val factorId: Long): DialogFragment() ,ProductAdapter.Interaction {

    private val viewModel: IncentiveViewModel by viewModels()
    private var _binding: FragmentIncentivePackagesBinding? = null
    private val binding get() = _binding
    private lateinit var productAdapter: ProductAdapter
    @Inject
    lateinit var picasso: Picasso

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentIncentivePackagesBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productAdapter = ProductAdapter(this ,picasso)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        subscribeOnAddProduct()
        subscribeOnProductList()

        productList()

        binding?.recyclerViewIncentiveF?.adapter = productAdapter
        binding?.recyclerViewIncentiveF?.layoutManager = GridLayoutManager(requireContext() ,5)

        binding?.imgIncentiveFClose?.setOnClickListener { dismiss() }

    }

    private fun setUpRecyclerProduct(list: List<ProductListResponse>) {
        productAdapter.submitList(list)
    }


    private fun productList() {
        viewModel.productList()
    }

    private fun addProduct(productId: Long ,factorId: Long){
        viewModel.addProduct(productId, factorId)
    }

    private fun subscribeOnProductList() {
        viewModel.productList.observe(viewLifecycleOwner){ response ->
            when(response){
                is ApiWrapper.Success -> {
                    response.data?.let {
                        Log.e("TAG", "subscribeOnPlay: $it")
                        setUpRecyclerProduct(it)
                    }
                }
                is ApiWrapper.ApiError -> {
                    response.error?.let {
                        Log.e("TAG", "subscribeOnProductList: $it")
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                }
                is ApiWrapper.UnknownError -> {
                    Log.e("TAG", "subscribeOnProductList: ${response.message}")
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

    private fun subscribeOnAddProduct() {
        viewModel.addProduct.observe(viewLifecycleOwner){ response ->
            when(response){
                is ApiWrapper.Success -> {
                    response.data?.let {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        dismiss()
                    }
                }
                is ApiWrapper.ApiError -> {
                    response.error?.let {
                        Log.e("TAG", "subscribeOnAddProduct: $it")
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                }
                is ApiWrapper.UnknownError -> {
                    Log.e("TAG", "subscribeOnAddProduct: ${response.message}")
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
        call.onViewStarted()
    }

    override fun onItemSelectedProduct(position: Int, item: ProductListResponse) {
        addProduct(item.id ,factorId)
    }

}