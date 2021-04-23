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
import com.example.brightcity.R
import com.example.brightcity.api.responses.GetRelationResponse
import com.example.brightcity.api.responses.UserListResponse
import com.example.brightcity.api.safe.ApiWrapper
import com.example.brightcity.databinding.FragmentPersonalProfileBinding
import com.example.brightcity.ui.adapter.RelationAdapter
import com.example.brightcity.ui.viewmodels.AddPersonViewModel
import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddPersonFragment : DialogFragment(), RelationAdapter.Interaction,
    RelationFragment.OnRelationCreate, DatePickerDialog.OnDateSetListener {

    private val viewModel: AddPersonViewModel by viewModels()
    private var _binding: FragmentPersonalProfileBinding? = null
    private val binding get() = _binding
    private var userID: Long? = 0
    private var fileID: String? = null
    private var name: String = ""
    private var family: String = ""
    private var birthDay: String = ""
    private var mobile: String = ""
    private var nationalID: String = ""
    private var gender: Int = 1
    private var isParent: Int = 0
    private var adapter: RelationAdapter? = null
    private var lastUser: UserListResponse? = null
    private var relationList: ArrayList<UserListResponse>? = null
    private var isChargeClicked: Boolean = false

    companion object {
        fun newInstance(id: Long, fileId: String? = "0"): AddPersonFragment {
            val args = Bundle()
            args.putLong("userID", id)
            args.putString("fileID", fileId)
            val fragment = AddPersonFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = RelationAdapter(this)
        userID = arguments?.getLong("userID")
        fileID = arguments?.getString("fileID")
        relationList = ArrayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPersonalProfileBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding?.relativeLayout5?.RecyclerViewPesrsonalProfile?.adapter = adapter

        if (userID != 0L) {
             // it form ChargeFragment
            getRelation(userID!!)
            getUserInfo(userID!!)
        }

        subscribeOnAddPerson()
        subscribeOnUserList()
        subscribeOnSetRelation()
        subscribeOnGetRelation()
        subscribeOnUserInfo()

        getUserList()

        binding?.relativeLayout4?.switchForReportF1?.setSwitchTextAppearance(
            requireContext(),
            R.style.SwitchTextAppearance
        )
        binding?.relativeLayout4?.switchForReportF1?.setOnCheckedChangeListener { _, isChecked ->
            binding?.relativeLayout4?.switchTextChangeHer?.visibility =
                if (isChecked) View.INVISIBLE else View.VISIBLE
            binding?.relativeLayout4?.switchTextChangeHis?.visibility =
                if (isChecked) View.VISIBLE else View.INVISIBLE
            gender = if (isChecked) 0
            else 1
        }

        binding?.relativeLayout4?.checkPersonalF?.setOnCheckedChangeListener { _, isChecked ->
            isParent = if (isChecked) 1
            else 0
        }

        binding?.relativeLayout4?.imgUserAccountFBirthLeft?.setOnClickListener {
            val persianCalendar = PersianCalendar()
            val datePickerDialog = DatePickerDialog.newInstance(
                this, persianCalendar.persianYear,
                persianCalendar.persianMonth, persianCalendar.persianDay
            )
            datePickerDialog.show(requireActivity().fragmentManager, "")
        }

        binding?.btnPersonalFSubmit?.setOnClickListener {
            isChargeClicked = false
            if (checkValidValue()) {
                addPerson(
                    name,
                    family,
                    birthDay,
                    gender,
                    mobile,
                    nationalID,
                    if (binding?.relativeLayout4?.editPersonalFDescription?.text?.isEmpty()!!) " " else binding?.relativeLayout4?.editPersonalFDescription?.text.toString(),
                    isParent
                )
            } else Toast.makeText(requireContext(), "اطلاعات را کامل پر کنید", Toast.LENGTH_SHORT)
                .show()
        }

        binding?.btnPersonalFSubmitCharge?.setOnClickListener {
            isChargeClicked = true
            if (checkValidValue()) {
                addPerson(
                    name,
                    family,
                    birthDay,
                    gender,
                    mobile,
                    nationalID,
                    if (binding?.relativeLayout4?.editPersonalFDescription?.text?.isEmpty()!!) " " else binding?.relativeLayout4?.editPersonalFDescription?.text.toString(),
                    isParent
                )
            } else Toast.makeText(requireContext(), "اطلاعات را کامل پر کنید", Toast.LENGTH_SHORT)
                .show()

        }

        binding?.btnSubmitFCancel?.setOnClickListener { dismiss() }

        binding?.relativeLayout5?.btnPessonalProileFAdd?.setOnClickListener {
            lastUser?.let {
                RelationFragment(it, this).show(childFragmentManager, null)
            }

        }

    }


    private fun setUpUserRelationRecycler(item: UserListResponse) {
        relationList?.add(item)
        checkListEmpty()
        adapter?.submitList(relationList!!)
    }

    private fun checkListEmpty() {
        if (relationList?.isNotEmpty()!!) {
            binding?.relativeLayout5?.imgPersonalFNothing?.visibility = View.GONE
            binding?.relativeLayout5?.linearPersonalTitle?.visibility = View.VISIBLE
        } else {
            binding?.relativeLayout5?.imgPersonalFNothing?.visibility = View.VISIBLE
            binding?.relativeLayout5?.linearPersonalTitle?.visibility = View.GONE
        }
    }

    private fun checkValidValue(): Boolean {
        name = binding?.relativeLayout4?.editPersonalFName?.text.toString()
        family = binding?.relativeLayout4?.editPersonalFFamily?.text.toString()
        birthDay = binding?.relativeLayout4?.editPersonalFBirthday?.text.toString()
        mobile = binding?.relativeLayout4?.editPersonalFPhoneNumber?.text.toString()
        nationalID = binding?.relativeLayout4?.editPersonalFNationalId?.text.toString()
        return name.isNotEmpty() && family.isNotEmpty() && birthDay.isNotEmpty() && mobile.isNotEmpty() && nationalID.isNotEmpty()
    }

    private fun getRelation(userID: Long) {
        viewModel.getUserRelations(userID)
    }

    private fun getUserList(search: String? = null) {
        viewModel.getUserList(num = 1, page = 1, search, "DESC", order = "id")
    }

    private fun addPerson(
        name: String,
        family: String,
        birthDay: String,
        gender: Int,
        mobile: String,
        nationalID: String,
        description: String,
        isParent: Int
    ) {
        showLoading()
        viewModel.userAdd(name, family, birthDay, gender, mobile, nationalID, description, isParent)
    }

    private fun subscribeOnAddPerson() {
        viewModel.addPerson.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ApiWrapper.Success -> {
                    response.data?.let {
                        Log.e("TAG", "subscribeOnAddPerson: $it")
                        val id: Long = it.id
                        userID = it.id
                        if (relationList?.isNotEmpty()!!) {
                            relationList?.forEach { item ->
                                Log.e("TAG", "subscribeOnAddPerson: id $id , item.id ${item.id}  type ${item.type}", )
                                setUserRelation(id, item.id, item.type!!)
                            }
                        } else {
                            hideLoading()
                            Toast.makeText(requireContext(), "با موفقیت ثبت شد", Toast.LENGTH_SHORT)
                                .show()
                            if (isChargeClicked) {
                                   ChargeFragment.newInstance(id).show(childFragmentManager ,null)
                            }
                            dismiss()
                        }
                    }
                }
                is ApiWrapper.NetworkError -> {
                    hideLoading()
                    Toast.makeText(
                        requireContext(),
                        requireContext().resources.getString(R.string.toastyNet),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is ApiWrapper.ApiError -> {
                    response.error?.let {
                        hideLoading()
                        Log.e("TAG", "subscribeOnAddPerson: $it")
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                }
                is ApiWrapper.UnknownError -> {
                    hideLoading()
                    Log.e("TAG", "subscribeOnAddPerson: ${response.message}")
                    Toast.makeText(
                        requireContext(),
                        requireContext().resources.getString(R.string.toastyError),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun subscribeOnUserList() {
        viewModel.userList.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ApiWrapper.Success -> {
                    response.data?.let {
                        Log.e("TAG", "subscribeOnUserList: $it")
                        lastUser = it[0]
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
        }
    }

    private fun setUserRelation(userID: Long, relatedUser: Long, type: Int) {
        viewModel.setRelation(userID, relatedUser, type)
    }


    private fun subscribeOnSetRelation() {
        viewModel.setRelation.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ApiWrapper.Success -> {
                    response.data?.let {
                        Log.e("TAG", "subscribeOnSetRelation: $it")
                        relationList?.removeLastOrNull()
                        if (relationList?.size == 0) {
                            hideLoading()
                            if (isChargeClicked) {
                                ChargeFragment.newInstance(userID!!).show(childFragmentManager, null)
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "با موفقیت ثبت شد",
                                    Toast.LENGTH_SHORT
                                ).show()
                                dismiss()
                            }
                        }
                    }
                }
                is ApiWrapper.NetworkError -> {
                    hideLoading()
                    Toast.makeText(
                        requireContext(),
                        requireContext().resources.getString(R.string.toastyNet),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is ApiWrapper.ApiError -> {
                    hideLoading()
                    response.error?.let {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                }
                is ApiWrapper.UnknownError -> {
                    hideLoading()
                    Toast.makeText(
                        requireContext(),
                        requireContext().resources.getString(R.string.toastyError),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun subscribeOnGetRelation() {
        viewModel.userRelation.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ApiWrapper.Success -> {
                    response.data?.let {
                        if (it.isEmpty()) {
                            binding?.relativeLayout5?.linearPersonalTitle?.visibility = View.GONE
                            binding?.relativeLayout5?.imgPersonalFNothing?.visibility = View.VISIBLE
                        } else {
                            binding?.relativeLayout5?.linearPersonalTitle?.visibility = View.VISIBLE
                            binding?.relativeLayout5?.imgPersonalFNothing?.visibility = View.GONE
                            it.forEach { item -> setUpUserRelationRecycler(item.mapToUserList()) }

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

    private fun getUserInfo(userID: Long? = null) {
        viewModel.getUserStatus(userID)
    }

    private fun subscribeOnUserInfo() {
        viewModel.userStatus.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ApiWrapper.Success -> {
                    response.data?.let {
                        Log.e("TAG", "subscribeOnUserInfo: $it")
                        binding?.relativeLayout4?.editPersonalFName?.setText(it.name)
                        binding?.relativeLayout4?.editPersonalFFamily?.setText(it.family)
                        binding?.relativeLayout4?.editPersonalFBirthday?.text = it.birthdate
                        binding?.relativeLayout4?.editPersonalFNationalId?.setText(it.national_id)
                        binding?.relativeLayout4?.editPersonalFPhoneNumber?.setText(it.mobile)
                        binding?.relativeLayout4?.editPersonalFDescription?.setText(it.description)
                        binding?.relativeLayout4?.checkPersonalF?.isChecked = it.is_parent == 1
                        if (it.gender == 1) {
                            binding?.relativeLayout4?.switchTextChangeHis?.visibility = View.VISIBLE
                            binding?.relativeLayout4?.switchTextChangeHer?.visibility =
                                View.INVISIBLE
                            binding?.relativeLayout4?.switchForReportF1?.isChecked = true
                        } else {
                            binding?.relativeLayout4?.switchTextChangeHer?.visibility = View.VISIBLE
                            binding?.relativeLayout4?.switchTextChangeHis?.visibility =
                                View.INVISIBLE
                            binding?.relativeLayout4?.switchForReportF1?.isChecked = false
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDeleteRelationSelected(position: Int, item: UserListResponse) {
        relationList?.removeAt(position)
        checkListEmpty()
        adapter?.submitList(relationList!!)
    }

    override fun onRelationCreate(item: UserListResponse) {
        Log.e("TAG", "onRelationCreate: $item")
        setUpUserRelationRecycler(item)
    }

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val date = year.toString() + "/" + (monthOfYear + 1).toString() + "/" + dayOfMonth
        binding?.relativeLayout4?.editPersonalFBirthday?.text = date
    }


    private fun showLoading(){
        binding?.loading?.visibility = View.VISIBLE
        binding?.btnSubmitFCancel?.visibility = View.GONE
        binding?.btnPersonalFSubmitCharge?.visibility = View.GONE
        binding?.btnPersonalFSubmit?.visibility = View.GONE
    }

    private fun hideLoading(){
        binding?.loading?.visibility = View.GONE
        binding?.btnSubmitFCancel?.visibility = View.VISIBLE
        binding?.btnPersonalFSubmitCharge?.visibility = View.VISIBLE
        binding?.btnPersonalFSubmit?.visibility = View.VISIBLE
    }

}