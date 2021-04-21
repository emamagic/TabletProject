package com.example.brightcity.ui.fragments

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Point
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import android.widget.Chronometer.OnChronometerTickListener
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.example.brightcity.R
import com.example.brightcity.api.safe.ApiWrapper
import com.example.brightcity.api.safe.MyJwt
import com.example.brightcity.databinding.FragmentHomeBinding
import com.example.brightcity.interfaces.OnProfileMenuListener
import com.example.brightcity.models.HomeSideMenuModel
import com.example.brightcity.ui.MainFragment
import com.example.brightcity.ui.adapter.HomeSideMenuAdapter
import com.example.brightcity.ui.viewmodels.HomeViewModel
import com.example.brightcity.util.Constance.AUDIT_FRAGMENT
import com.example.brightcity.util.Constance.BASE_URL
import com.example.brightcity.util.Constance.DASHBOARD_FRAGMENT
import com.example.brightcity.util.Constance.USER_ID
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import de.hdodenhof.circleimageview.CircleImageView
import java.text.DecimalFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


@AndroidEntryPoint
class HomeFragment: MainFragment(R.layout.fragment_home) , HomeSideMenuAdapter.Interaction ,OnProfileMenuListener {

    private val viewModel: HomeViewModel by viewModels()
    private val TAG = "HomeFragment"
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding
    private lateinit var adapter: HomeSideMenuAdapter
    private lateinit var list: ArrayList<HomeSideMenuModel>
    private lateinit var profileMenuListener: OnProfileMenuListener
    private var name: String = ""
    private var fileID: String? = ""
    @Inject
    lateinit var picasso: Picasso
    @Inject
    lateinit var pref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // test(jwtHelper)
        // mSocket?.connect()
        profileMenuListener = this
        adapter = HomeSideMenuAdapter(this)
        list = ArrayList()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.e(TAG, "onViewCreated: pref ${MyJwt.getAccessToken()}")
        subscribeOnUserLogout()
        subscribeOnUserStatus()
        subscribeOnDateTime()
        getDateTime()
        userStatus()

        childFragmentManager.beginTransaction().replace(
            R.id.frame_homeF_container,
            DashboardFragment()
        ).commit()
        setUpRecyclerView()
        binding?.imgHomeFPerson?.setOnClickListener { showPopupMenu() }
    }



/*    private fun accessHttps(jwtHelper: JwtHelper): IO.Options {

        return IO.Options().also { opts ->
            opts.query = "access_token=${jwtHelper.getToken(ACCESS_TOKEN_STATUS)}"
            opts.sslContext = SSLContext.getInstance("SSL").also { ssl ->
                // trustAllCerts  value part2
                ssl.init(null, arrayOf<TrustManager>(object : X509TrustManager {
                    override fun getAcceptedIssuers(): Array<X509Certificate> {
                        return arrayOf()
                    }

                    @SuppressLint("TrustAllX509TrustManager")
                    @Throws(CertificateException::class)
                    override fun checkClientTrusted(
                        chain: Array<X509Certificate>,
                        authType: String
                    ) {
                    }

                    @SuppressLint("TrustAllX509TrustManager")
                    @Throws(CertificateException::class)
                    override fun checkServerTrusted(
                        chain: Array<X509Certificate>,
                        authType: String
                    ) {
                    }
                }), null)
            }
            opts.reconnection = true
            opts.secure = true
            opts.timeout = 1500

        }

    }*/
/*    fun test(jwtHelper: JwtHelper){
        try {
            val socketUrl: String = SOCKET_URL
            val hostnameVerifier = HostnameVerifier { _, _ -> true }
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
                override fun getAcceptedIssuers(): Array<X509Certificate?> {
                    return arrayOfNulls(0)
                }
            })
            val trustManager = trustAllCerts[0] as X509TrustManager
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, null)
            val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory
            val okHttpClient = okhttp3.OkHttpClient.Builder()
                .hostnameVerifier(hostnameVerifier)
                .sslSocketFactory(sslSocketFactory, trustManager)
                .build()
            val opts = IO.Options()
            opts.query = "access_token=${jwtHelper.getToken(ACCESS_TOKEN_STATUS)}"
            opts.callFactory = okHttpClient
            opts.webSocketFactory = okHttpClient
            mSocket = IO.socket(socketUrl, opts)
        } catch (e: URISyntaxException) {
            throw RuntimeException(e)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: KeyManagementException) {
            e.printStackTrace()
        }

    }*/

    private fun userLogout(){
        viewModel.userLogout()
    }

    private fun userStatus(){
        viewModel.userStatus()
    }

    private fun getDateTime(){
        viewModel.getDateTime()
    }

    private fun subscribeOnDateTime(){
        viewModel.date.observe(viewLifecycleOwner){ response ->
            when(response){
                is ApiWrapper.Success -> {
                    response.data?.let {
                        binding?.txtHomeFDate?.text = it.date
                    }
                }
                is ApiWrapper.NetworkError -> {
                    toastNet()
                }
                is ApiWrapper.UnknownError -> {
                    toastError()
                }
                is ApiWrapper.ApiError -> {
                    response.error?.let { toastError(it.message) }
                }
            }
        }
    }

    private fun subscribeOnUserStatus(){
        viewModel.userStatus.observe(viewLifecycleOwner){ response ->
            when(response){
                is ApiWrapper.Success -> {
                    response.data?.let {
                        pref.edit().putLong(USER_ID ,it.id).apply()
                        name = it.name
                        fileID = it.fileId
                        if (it.fileId != null)
                        picasso.load("${BASE_URL}file/download?id=${fileID}")
                            .fit().placeholder(R.drawable.ic_avatar).fit().into(
                                binding?.imgHomeFPerson
                            )
                    }
                }
                is ApiWrapper.NetworkError -> {
                    toastNet()
                }
                is ApiWrapper.UnknownError -> {
                    toastError()
                }
                is ApiWrapper.ApiError -> {
                    response.error?.let { toastError(it.message) }
                }
            }
        }
    }

    private fun subscribeOnUserLogout(){

        viewModel.userLogout.observe(viewLifecycleOwner){ response ->
            when(response){
                is ApiWrapper.Success -> {
                    response.data?.let {
                        Log.e(TAG, "subscribeOnUserLogout: ${it.result}", )
                        if (it.result == "logout accessed") {
                            MyJwt.clear()
                            childFragmentManager.beginTransaction()
                                .replace(R.id.constraint_root_container, LoginFragment())
                                .commit()
                        }
                    }
                }
                is ApiWrapper.NetworkError -> {
                    toastNet()
                }
                is ApiWrapper.UnknownError -> {
                    toastError()
                }
                is ApiWrapper.ApiError -> {
                    response.error?.let { toastError(it.message) }
                }
            }
        }
    }

    private fun showPopupMenu(){
        val location = IntArray(2)
        binding?.imgHomeFPerson?.getLocationOnScreen(location)
        val point = Point()
        point.x = location[0]
        point.y = location[1]
        val OFFSET_X = -30
        val OFFSET_Y = binding?.imgHomeFPerson?.height ?: 50
        var inflater: LayoutInflater? = null
        var layout: View? = null
        if (layout == null || inflater == null){
            inflater = requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            layout = inflater.inflate(R.layout.popup_menu, null)
        }
        val pw = PopupWindow(layout, 350, LinearLayout.LayoutParams.WRAP_CONTENT, true)
        pw.showAtLocation(layout, Gravity.NO_GRAVITY, point.x + OFFSET_X, point.y + OFFSET_Y)
        layout?.findViewById<TextView>(R.id.txt_menu_dialog_name)?.text = name
        picasso.load("${BASE_URL}file/download?id=${fileID}").fit().placeholder(R.drawable.ic_avatar).fit().into(
            layout?.findViewById<CircleImageView>(
                R.id.img_menu_dialog_profile
            )
        )
        layout?.findViewById<TextView>(R.id.txt_menu_dialog_message)?.setOnClickListener { profileMenuListener.onProfileMenuMessagesClicked(
            pw
        ) }
        layout?.findViewById<TextView>(R.id.txt_menu_dialog_account)?.setOnClickListener { profileMenuListener.onProfileMenuUserAccountClicked(
            pw
        ) }
        layout?.findViewById<TextView>(R.id.txt_menu_dialog_exit)?.setOnClickListener { profileMenuListener.onProfileMenuExitClicked(
            pw
        ) }
    }

    private fun getTime(): String{
        val instance: Calendar = Calendar.getInstance()
        val hour: Int = instance.get(Calendar.HOUR)
        val minute: Int = instance.get(Calendar.MINUTE)
        return "${hour}:${minute}"
    }

    private fun setUpRecyclerView(){
        binding?.recyclerHomeFSideMenu?.adapter = adapter
        list.add(
            HomeSideMenuModel(
                DASHBOARD_FRAGMENT, "داشبورد"
            )
        )
        list.add(
            HomeSideMenuModel(
                AUDIT_FRAGMENT, "حسابرسی"
            )
        )
        adapter.submitList(list)
    }

    override fun onItemSelected(type: Int) {
        if (type == DASHBOARD_FRAGMENT){
            childFragmentManager.beginTransaction().replace(
                R.id.frame_homeF_container,
                DashboardFragment()
            ).commit()
        }else{
            childFragmentManager.beginTransaction().replace(
                R.id.frame_homeF_container,
                AuditFragment()
            ).commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onProfileMenuMessagesClicked(pw: PopupWindow) {
        pw.dismiss()

    }

    override fun onProfileMenuExitClicked(pw: PopupWindow) {
        pw.dismiss()
        userLogout()
    }

    override fun onProfileMenuUserAccountClicked(pw: PopupWindow) {
        pw.dismiss()
        UserAccountFragment().show(childFragmentManager, null)
    }

}