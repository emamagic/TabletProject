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
import com.example.brightcity.databinding.FragmentUserAccountBinding
import com.example.brightcity.ui.viewmodels.UserAccountViewModel
import com.example.brightcity.util.Constance
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import io.socket.client.Ack
import io.socket.client.IO
import io.socket.client.Manager
import io.socket.client.Socket
import io.socket.engineio.client.Transport
import okhttp3.RequestBody
import org.json.JSONObject
import java.net.URISyntaxException
import javax.inject.Inject

@AndroidEntryPoint
class UserAccountFragment : DialogFragment() {

    private val viewModel: UserAccountViewModel by viewModels()
    private var _binding: FragmentUserAccountBinding? = null
    private val binding get() = _binding
    private var fileID: String? = ""
    private var name: String = ""
    private var birthDay: String = ""
    private var family: String = ""
    private var mobile: String = ""
    private var role: String = ""
    private var nationalID: Long = 0
    private var gender: Int = 0
    private var description: String = " "
    private var isParent: Int = 0

    @Inject
    lateinit var picasso: Picasso

    companion object {
        fun newInstance(fileId: String?): UserAccountFragment {
            val args = Bundle()
            args.putString("fileID", fileId)
            val fragment = UserAccountFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fileID = arguments?.getString("fileID")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserAccountBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        if (fileID != null)
            picasso.load("${Constance.BASE_URL}file/download?id=${fileID}").fit()
                .placeholder(R.drawable.ic_avatar).fit().into(binding?.circleImageView)

        subscribeOnUserInfo()
        subscribeOnUpdateUserInfo()

        // if you send userID null then server return info of cashier
        getUserInfo(null)

        binding?.btnUserAccountFChangeAccount?.setOnClickListener {
            ChangePassFragment().show(
                childFragmentManager,
                null
            )
        }

        binding?.btnUserAccountFCancel?.setOnClickListener { dismiss() }

        binding?.btnUserAccountFPay?.setOnClickListener {
            if (binding?.editUserAccountFNumber?.text.toString().isNotEmpty()) {
                updateUserInfo(
                    mobile = binding?.editUserAccountFNumber?.text.toString().toLong(),
                    name = name,
                    family = family,
                    birthDay = birthDay,
                    gender = gender,
                    isParent = isParent,
                    nationalID = nationalID,
                    description = description
                )
            } else Toast.makeText(
                requireContext(),
                "لطفا شماره تلفن را وارد نمایید",
                Toast.LENGTH_SHORT
            ).show()

        }


    }

    private fun getUserInfo(userID: Long? = null) {
        viewModel.getUserStatus(userID)
    }

    private fun subscribeOnUserInfo() {
        viewModel.userStatus.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ApiWrapper.Success -> {
                    response.data?.let {
                        Log.e("TAG", "subscribeOnUserInfo: $it")
                        name = it.name
                        birthDay = it.birthdate
                        family = it.family
                        mobile = it.mobile
                        role = it.role
                        nationalID = it.national_id.toLong()
                        gender = it.gender
                        isParent = it.is_parent
                        binding?.editUserAccountFName?.text = it.name
                        binding?.editUserAccountFBirth?.text = it.birthdate
                        binding?.editUserAccountFFamily?.text = it.family
                        binding?.editUserAccountFNumber?.setText(it.mobile)
                        binding?.editUserFAccess?.text = it.role
                        binding?.editUserAccountFNationalId?.text = it.national_id
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
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                }
                is ApiWrapper.UnknownError -> {
                    Toast.makeText(
                        requireContext(),
                        requireContext().resources.getString(R.string.toastyError),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun updateUserInfo(
        name: String? = null,
        family: String? = null,
        birthDay: String? = null,
        gender: Int? = null,
        mobile: Long? = null,
        nationalID: Long? = null,
        description: String? = null,
        isParent: Int? = null
    ) {
        viewModel.updateInfo(
            name,
            family,
            birthDay,
            gender,
            mobile,
            nationalID,
            description,
            isParent
        )
    }

    private fun subscribeOnUpdateUserInfo() {
        viewModel.updateInfo.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ApiWrapper.Success -> {
                    response.data?.let {
                        Log.e("TAG", "userUpdate: ${response.data}")
                        Toast.makeText(requireContext(), "با موفقیت انجام شد", Toast.LENGTH_SHORT).show()
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
                        Log.e("TAG", "userUpdate: ${response.error}")
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                }
                is ApiWrapper.UnknownError -> {
                    Log.e("TAG", "userUpdate: ${response.message}")
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}