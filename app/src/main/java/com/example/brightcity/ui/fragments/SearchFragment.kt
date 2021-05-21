package com.example.brightcity.ui.fragments

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import com.example.brightcity.api.responses.UserListResponse
import com.example.brightcity.databinding.FragmentSearchBinding
import com.example.brightcity.ui.adapter.MyPagingLoadStateAdapter
import com.example.brightcity.ui.adapter.UserListAdapter
import com.example.brightcity.ui.viewmodels.SearchViewModel
import com.example.brightcity.util.onTextChange
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint
class SearchFragment(private val isFromRelation: Boolean = false ,private val onItemRelationClicked: OnItemRelationClicked? = null): DialogFragment() , UserListAdapter.Interaction {

    private val viewModel: SearchViewModel by viewModels()
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding
    private var timer: Timer? = null
    private lateinit var searchList: ArrayList<UserListResponse>
    private lateinit var userListAdapter: UserListAdapter
    @Inject
    lateinit var picasso: Picasso


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater ,container ,false)
        return _binding?.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchList = ArrayList()
        userListAdapter = UserListAdapter(this ,picasso)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding?.imgSearchDialogClose?.setOnClickListener { dismiss() }
        getUserList()

        binding?.searchView?.onTextChange {
            timery(2000){
                if (!binding?.searchView?.text?.isEmpty()!!) getUserList(it)
                else getUserList()
            }
        }
    }

    private fun getUserList(search: String? = null ,order: String? = null ,asc: String? = null) {
        viewModel.getUserList(search, order ,asc).observe(viewLifecycleOwner) { response ->
            binding?.loading?.visibility = View.GONE

            userListAdapter.submitData(viewLifecycleOwner.lifecycle,response )
            binding?.recyclerSearchDialog?.adapter = userListAdapter.withLoadStateHeaderAndFooter(
                header = MyPagingLoadStateAdapter { userListAdapter.retry() },
                footer = MyPagingLoadStateAdapter { userListAdapter.retry() },
            )

            userListAdapter.addLoadStateListener { loadState ->
                binding?.recyclerSearchDialog?.isVisible = loadState.source.refresh is LoadState.NotLoading

                // empty view
                if (loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    userListAdapter.itemCount < 1
                ) {
                    binding?.recyclerSearchDialog?.isVisible = false
                    binding?.imgSearchFNothing?.visibility = View.VISIBLE
                }else{
                    binding?.recyclerSearchDialog?.isVisible = true
                    binding?.imgSearchFNothing?.visibility = View.GONE
                }

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        if (timer != null){
            timer?.purge()
            timer?.cancel()
            timer = null
        }
    }

    override fun onItemSelected(position: Int, item: UserListResponse) {
        if (!isFromRelation) {
            ChargeFragment.newInstance(item.id).show(childFragmentManager ,null)
        }
        else {
            onItemRelationClicked?.onItemRelationClicked(item)
            dismiss()
        }
    }

    private fun timery(delay: Long, call: () -> Unit): Timer {
        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                try {
                    Handler(Looper.getMainLooper()).post {
                        call.invoke()
                    }
                } catch (t: Throwable) {
                    Log.e("TAG", "run: ${t.message}")
                }
            }
        }, delay)
        return timer!!
    }

    interface OnItemRelationClicked{
        fun onItemRelationClicked(item: UserListResponse)
    }
}