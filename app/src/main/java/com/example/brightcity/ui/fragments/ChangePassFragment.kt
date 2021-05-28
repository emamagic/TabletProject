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
import com.example.brightcity.api.safe.ApiWrapper
import com.example.brightcity.api.safe.MyJwt
import com.example.brightcity.databinding.BoxChangePassBinding
import com.example.brightcity.ui.viewmodels.ChangePassViewModel
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
class ChangePassFragment: DialogFragment()  {

    private val viewModel: ChangePassViewModel by viewModels()
    private var _binding: BoxChangePassBinding? = null
    private val binding get() = _binding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BoxChangePassBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        subscribeOnChangePass()

        binding?.btnChangePassFCancel?.setOnClickListener { dismiss() }

        binding?.btnChangePassFPay?.setOnClickListener {
            val currentPass = binding?.txtChangPassFPass?.text.toString()
            val newPass = binding?.txtChangPassFNewPass?.text.toString()
            val retypePass = binding?.txtChangPassFNewPassRetype?.text.toString()
            changePass(currentPass ,newPass ,retypePass)
        }
    }

    private fun changePass(currentPass: String ,newPass: String ,retype: String){
        viewModel.changePass(currentPass, newPass, retype)
    }

    private fun subscribeOnChangePass(){
        viewModel.changePass.observe(viewLifecycleOwner) {response ->
            when(response){
                is ApiWrapper.Success -> {
                    response.data?.let {
                        Log.e("TAG", "subscribeOnChangePass: $it", )
                        Toast.makeText(requireContext(), "رمز عبور تغییر یافت", Toast.LENGTH_SHORT).show()
                    }
                }
                is ApiWrapper.NetworkError -> {
                    Toast.makeText(requireContext(), requireContext().resources.getString(R.string.toastyNet), Toast.LENGTH_SHORT).show()
                }
                is ApiWrapper.ApiError -> {
                    response.error?.let {
                        Log.e("TAG", "subscribeOnChangePass: $it", )
                        when(it.statusCode){
                            400 ->  Toast.makeText(requireContext(), "رمز عبور حداقل باید 6 کاراکتر باشد یا رمز و تکرار همخوانی ندارد", Toast.LENGTH_SHORT).show()
                            403 ->  Toast.makeText(requireContext(), "رمز فعلی صحیح نیست", Toast.LENGTH_SHORT).show()
                        }

                    }
                }
                is ApiWrapper.UnknownError -> {
                    Toast.makeText(requireContext(), requireContext().resources.getString(R.string.toastyError), Toast.LENGTH_SHORT).show()
                }
            }
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}