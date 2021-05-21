package com.example.brightcity.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.brightcity.R
import com.example.brightcity.api.responses.TransactionListResponse
import com.example.brightcity.api.safe.ApiWrapper
import com.example.brightcity.databinding.FragmentAuditBinding
import com.example.brightcity.ui.MainFragment
import com.example.brightcity.ui.adapter.AuditAdapter
import com.example.brightcity.ui.viewmodels.AuditViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuditFragment: MainFragment(R.layout.fragment_audit) ,AuditAdapter.Interaction {

    private val viewModel: AuditViewModel by viewModels()
    private var _binding: FragmentAuditBinding? = null
    private val binding get() = _binding
    private lateinit var adapter: AuditAdapter
    private lateinit var list: ArrayList<TransactionListResponse>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = AuditAdapter(this)
        list = ArrayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAuditBinding.inflate(inflater ,container ,false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeOnTransactionList()
        getAuditList()
        binding?.btnAuditCloseCache?.setOnClickListener { CloseCacheFragment().show(childFragmentManager ,null) }
    }


    private fun getAuditList(){
        viewModel.getTransactionList()
    }

    private fun subscribeOnTransactionList(){
        viewModel.transactionList.observe(viewLifecycleOwner) { response ->
            hideLoading()
            when(response){
                is ApiWrapper.Success ->  response.data?.let { setUpAuditRecycler(it) }
                is ApiWrapper.NetworkError ->  toastNet()
                is ApiWrapper.ApiError ->  response.error?.let { toastError(it.message) }
                is ApiWrapper.UnknownError -> {
                    Log.e("TAG", "subscribeOnTransactionList: sss ${response.message}")
                    toastError()
                }
            }
        }
    }

    private fun setUpAuditRecycler(mList: List<TransactionListResponse>){
        binding?.recyclerAudit?.adapter = adapter
        list.addAll(mList)
        adapter.submitList(list)
    }

    fun hideLoading() {
        binding?.loading?.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemSelected(position: Int, item: TransactionListResponse) {

    }
}