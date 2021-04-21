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
class ChangePassFragment: DialogFragment() {

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
          //  changePass(binding?.txtChangPassFPass?.text.toString() ,binding?.txtChangPassFNewPass?.text.toString() ,binding?.txtChangPassFNewPassRetype?.text.toString())
            changePass("light_casheir" ,binding?.txtChangPassFNewPass?.text.toString() ,binding?.txtChangPassFNewPassRetype?.text.toString())
        }
    }

    private fun changePass(currentPass: String ,newPass: String ,retype: String){
        viewModel.changePass(currentPass, newPass, retype)
    }

/*    private fun changePassSocket(currentPass: String ,newPass: String ,retype: String){
        val changePass = JSONObject()
        changePass.put("current", currentPass)
        changePass.put("password", newPass)
        changePass.put("retype", retype)
        mSocket?.emit("user/password", changePass, Ack { args ->
            Handler(Looper.getMainLooper()).post {
                val ack = args[0] as JSONObject
                Log.e("TAG", "user/password ack $ack")
                 Toast.makeText(requireContext(), ack.getString("message"), Toast.LENGTH_SHORT).show()
            }
        })
    }*/

    private fun subscribeOnChangePass(){
        viewModel.changePass.observe(viewLifecycleOwner) {response ->
            when(response){
                is ApiWrapper.Success -> {
                    response.data?.let {
                        Log.e("TAG", "subscribeOnChangePass: $it", )
                        Toast.makeText(requireContext(), it.result, Toast.LENGTH_SHORT).show()
                        dismiss()
                    }
                }
                is ApiWrapper.NetworkError -> {
                    Toast.makeText(requireContext(), requireContext().resources.getString(R.string.toastyNet), Toast.LENGTH_SHORT).show()
                }
                is ApiWrapper.ApiError -> {
                    response.error?.let {
                        Toast.makeText(requireContext(), it.validation?.message, Toast.LENGTH_SHORT).show()
                    }
                }
                is ApiWrapper.UnknownError -> {
                    Toast.makeText(requireContext(), requireContext().resources.getString(R.string.toastyError), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}