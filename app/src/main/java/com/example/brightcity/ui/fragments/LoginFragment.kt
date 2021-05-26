package com.example.brightcity.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.brightcity.R
import com.example.brightcity.api.safe.ApiWrapper
import com.example.brightcity.api.safe.MyJwt
import com.example.brightcity.databinding.FragmentLoginBinding
import com.example.brightcity.ui.MainFragment
import com.example.brightcity.ui.viewmodels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment: MainFragment(R.layout.fragment_login) {

    private val TAG = "LoginFragment"
    private val viewModel: LoginViewModel by viewModels()
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater ,container ,false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subScribeOnUserLogin()
        binding?.btnLoginFSubmit?.setOnClickListener {
            val userName = binding?.edtLoginFName?.text.toString()
            val password = binding?.edtLoginFPass?.text.toString()
            if (userName.isNotEmpty() && password.isNotEmpty()){
            //    userLogin(userName ,password)
                // TODO: 5/21/2021 remove username pass
                showLoading()
                userLogin("casheir" ,"123456")
            }
            else toasty(resources.getString(R.string.txt_name_pass_empty) ,3)

        }

    }

    private fun subScribeOnUserLogin(){
        viewModel.userLogin.observe(viewLifecycleOwner) { response ->
            when(response){
                is ApiWrapper.Success -> {
                    response.data?.let {
                        MyJwt.setAuthoriseToken(it.access_token ,it.refresh_token)
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.constraint_login_container, HomeFragment())
                            .commit()
                    }
                }
                is ApiWrapper.NetworkError -> {
                    toastNet()
                }

                is ApiWrapper.ApiError -> response.error?.let {
                    toasty(it.message , 3)
                }

                is ApiWrapper.UnknownError -> {
                    Log.e(TAG, "subScribeOnUserLogin: ${response.message}")
                    toastError()
                }

            }
        }
    }
    
    private fun userLogin(userName: String ,password: String){ viewModel.userLogin(userName, password) }

    private fun showLoading() {
        binding?.loadingLogin?.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}