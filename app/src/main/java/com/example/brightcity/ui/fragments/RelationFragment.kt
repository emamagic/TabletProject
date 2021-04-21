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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import com.example.brightcity.R
import com.example.brightcity.api.responses.GetRelationResponse
import com.example.brightcity.api.responses.UserListResponse
import com.example.brightcity.api.safe.ApiWrapper
import com.example.brightcity.api.safe.MyJwt
import com.example.brightcity.databinding.FragmentRecordRelationshipsBinding
import com.example.brightcity.ui.viewmodels.RelationViewModel
import com.example.brightcity.util.Constance
import dagger.hilt.android.AndroidEntryPoint
import io.socket.client.Ack
import io.socket.client.IO
import io.socket.client.Manager
import io.socket.client.Socket
import io.socket.engineio.client.Transport
import org.json.JSONObject
import java.net.URISyntaxException

@AndroidEntryPoint
class RelationFragment(private val user: UserListResponse ,private val listener: OnRelationCreate): DialogFragment(),SearchFragment.OnItemRelationClicked {

    private val viewModel: RelationViewModel by viewModels()
    private var _binding: FragmentRecordRelationshipsBinding? = null
    private val binding get() = _binding
    private var relationTypes: ArrayList<String>? = null
    private var type: Int = 0
    private var lastUser: UserListResponse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        relationTypes = ArrayList()
        lastUser = user
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecordRelationshipsBinding.inflate(inflater ,container ,false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding?.textView3?.text = user.name
        relationTypeListener()


        relationTypes?.add("فرزند")
        relationTypes?.add("پدر_مادر")
        relationTypes?.add("خواهر_برادر")
        relationTypes?.add("پدربزرگ_مادربزرگ")
        relationTypes?.add("نوه")
        relationTypes?.add("سایر")

        relationType(relationTypes!!)

        binding?.btnRecodeRelationshipsFCancel?.setOnClickListener { dismiss() }
        binding?.btnRecodeRelationshipsFPay?.setOnClickListener {
            if (binding?.checkRecodeRelationshipsFChecked?.isChecked == true)
                lastUser?.let {
                    it.type = type
                    listener.onRelationCreate(it)
                    dismiss()
                }

            else Toast.makeText(requireContext(), "لطفا تیک را بزنید", Toast.LENGTH_SHORT).show()
        }
        binding?.relativeLayoutRecodeRelationshipsFReceive?.setOnClickListener {
            SearchFragment(true ,this).show(childFragmentManager ,null)
        }

    }

  private fun setUserRelation(userID: Long ,relatedUser: Long ,type: Int){
        viewModel.setRelation(userID, relatedUser, type)
    }


    private fun subscribeOnRelation(){
        viewModel.setRelation.observe(viewLifecycleOwner){response ->
            when(response){
                is ApiWrapper.Success -> {
                    response.data?.let {
                        Toast.makeText(requireContext(), it.status, Toast.LENGTH_SHORT).show()
                    }
                }
                is ApiWrapper.NetworkError -> {
                    Toast.makeText(requireContext(), requireContext().resources.getString(R.string.toastyNet), Toast.LENGTH_SHORT).show()
                }
                is ApiWrapper.ApiError -> {
                    response.error?.let {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                }
                is ApiWrapper.UnknownError -> {
                    Toast.makeText(requireContext(), requireContext().resources.getString(R.string.toastyError), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }



    private fun relationType(relationTypes: ArrayList<String>) {

        val citySpinnerAdapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_item,
            android.R.id.text1,
            relationTypes
        )
        citySpinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        binding?.spinner2?.adapter = citySpinnerAdapter
        binding?.spinner2?.isEnabled = true
    }

    private fun relationTypeListener() {
        binding?.spinner2?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                Log.e("TAG", "onItemSelected: province $id")
                type = (id+1).toInt()

            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemRelationClicked(item: UserListResponse) {
        lastUser = item
        binding?.textView3?.text = item.name
    }

    interface OnRelationCreate{
        fun onRelationCreate(item: UserListResponse)
    }

}