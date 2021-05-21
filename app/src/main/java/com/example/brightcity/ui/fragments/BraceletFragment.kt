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
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.brightcity.R
import com.example.brightcity.api.responses.UserListResponse
import com.example.brightcity.api.safe.ApiWrapper
import com.example.brightcity.databinding.FragmentBraceletBinding
import com.example.brightcity.interfaces.OnAddPersonListener
import com.example.brightcity.ui.adapter.BraceletAdapter
import com.example.brightcity.ui.viewmodels.BraceletViewModel
import com.example.brightcity.util.onTextChange
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint
class BraceletFragment(private val onAddPersonClicked: OnAddPersonListener): DialogFragment() ,BraceletAdapter.Interaction {

    private val viewModel: BraceletViewModel by viewModels()
    private var _binding: FragmentBraceletBinding? = null
    private val binding get() = _binding
    private lateinit var searchList: ArrayList<UserListResponse>
    private lateinit var searchAdapter: BraceletAdapter
    private var timer: Timer? = null
    @Inject
    lateinit var picasso: Picasso


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchAdapter = BraceletAdapter(this ,picasso)
        searchList = ArrayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBraceletBinding.inflate(inflater ,container ,false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        subscribeOnUserList()
        binding?.imgSearchDialogClose?.setOnClickListener { dismiss() }
        binding?.txtBraceletNew?.setOnClickListener { onAddPersonClicked.onAddPersonClicked(this) }

        binding?.searchView?.onTextChange {
            timery(2000){
                if (!binding?.searchView?.text?.isEmpty()!!) getUserList(it)
                else getUserList("xxxxxx")
            }
        }

    }


    private fun setUpSearchRecycler(list: List<UserListResponse>){
        binding?.recyclerSearchBracelet?.adapter = searchAdapter
        searchAdapter.submitList(list)
    }

    private fun getUserList(search: String? = null){
        viewModel.getUserList(search)
    }

    private fun subscribeOnUserList(){
        viewModel.userList.observe(viewLifecycleOwner){response ->
            when(response){
                is ApiWrapper.Success -> {
                    response.data?.let {
                        if (it.isEmpty()){
                            binding?.txtBraceletNew?.visibility = View.VISIBLE
                            binding?.linearBraceletRecyclerTitle?.visibility = View.GONE
                            binding?.txtBraceletNoBody?.visibility = View.VISIBLE
                            setUpSearchRecycler(it)
                        }else {
                            binding?.txtBraceletNew?.visibility = View.GONE
                            binding?.linearBraceletRecyclerTitle?.visibility = View.VISIBLE
                            binding?.txtBraceletNoBody?.visibility = View.GONE
                            setUpSearchRecycler(it)
                        }
                    }
                }
                is ApiWrapper.NetworkError -> {
                    Toast.makeText(requireContext(), requireContext().resources.getString(R.string.toastyNet), Toast.LENGTH_SHORT).show()
                }
                is ApiWrapper.ApiError -> {
                    response.error?.let {
                        Log.e("TAG", "subscribeOnAddPerson: $it", )
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                }
                is ApiWrapper.UnknownError -> {
                    Log.e("TAG", "subscribeOnAddPerson: ${response.message}", )
                    Toast.makeText(requireContext(), requireContext().resources.getString(R.string.toastyError), Toast.LENGTH_SHORT).show()
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
        ChargeFragment.newInstance(item.id).show(childFragmentManager ,null)

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

}