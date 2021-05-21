package com.example.brightcity.ui

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.example.brightcity.R
import com.example.brightcity.databinding.MainFragmentBinding
import java.util.*

open class MainFragment(@LayoutRes val contentLayoutId: Int) :Fragment() {

    @LayoutRes
    private var mContentLayoutId = 0
    private var timer: Timer? = null
    private var callback: OnBackPressedCallback? = null
    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContentLayoutId = contentLayoutId
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (mContentLayoutId != 0) binding?.layoutStub?.layoutResource = mContentLayoutId
        binding?.layoutStub?.inflate()
    }

    @SuppressLint("ResourceAsColor")
    fun toasty(title: String, selectedMode: Int? = null) {
        val layout = layoutInflater.inflate(
            R.layout.toast_layout,
            requireView().findViewById(R.id.toast_root)
        )
        when (selectedMode) {

            1 -> {
                //correct
                layout.findViewById<ImageView>(R.id.toast_img)
                    .setImageResource(R.drawable.ic_corroct_toast)
                layout.findViewById<ConstraintLayout>(R.id.toast_root)
                    .setBackgroundResource(R.drawable.bg_corroct_toast)
            }
            2 -> {
                //Warning
                layout.findViewById<ImageView>(R.id.toast_img)
                    .setImageResource(R.drawable.ic_warning_toast)
                layout.findViewById<ConstraintLayout>(R.id.toast_root)
                    .setBackgroundResource(R.drawable.bg_warning_toast)
                layout.findViewById<TextView>(R.id.toast_txt).setTextColor(R.color.black)
            }
            3 -> {
                //Error
                layout.findViewById<ImageView>(R.id.toast_img)
                    .setImageResource(R.drawable.ic_error_toast)
                layout.findViewById<ConstraintLayout>(R.id.toast_root)
                    .setBackgroundResource(R.drawable.bg_error_toast)
            }
            else -> {
                //AnyNumber
                Toast.makeText(requireContext(), title, Toast.LENGTH_LONG).show()
            }

        }

        layout.findViewById<TextView>(R.id.toast_txt).text = title
        if(selectedMode!=null){
            Toast(requireActivity()).apply {
                setGravity(Gravity.BOTTOM, 0, 100)
                duration = Toast.LENGTH_LONG
                view = layout
            }.show()
        }
    }

    protected fun toastNet(text: String = requireContext().resources.getString(R.string.toastyNet)) {
       toasty(text, 2)
    }

    protected fun toastError(text: String = requireContext().resources.getString(R.string.toastyError)) {
        toasty(text, 3)
    }

    fun timery(delay: Long, call: () -> Unit): Timer {
        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                Handler(Looper.getMainLooper()).post {
                    call.invoke()
                }
            }
        }, delay)
        return timer!!
    }



    fun onMyBackPressed(owner: LifecycleOwner, call: () -> Unit) {
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                call()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(owner, callback!!)
    }

    override fun onDestroyView() {
        _binding = null
        if (callback != null){
            callback?.isEnabled = false
            callback?.remove()
        }
        if (timer != null){
            timer?.purge()
            timer?.cancel()
            timer = null
        }
        super.onDestroyView()
    }


}