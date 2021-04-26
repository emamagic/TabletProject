package com.example.brightcity.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.brightcity.R
import com.example.brightcity.api.responses.LastMessageResponse
import com.example.brightcity.api.responses.LogListResponse
import com.example.brightcity.api.responses.UserListResponse
import com.example.brightcity.api.safe.ApiWrapper
import com.example.brightcity.api.safe.MyJwt
import com.example.brightcity.databinding.FragmentDashboardBinding
import com.example.brightcity.interfaces.OnAddPersonListener
import com.example.brightcity.ui.MainFragment
import com.example.brightcity.ui.adapter.DashboardLogAdapter
import com.example.brightcity.ui.adapter.DashboardMessageAdapter
import com.example.brightcity.ui.adapter.DashboardUserListAdapter
import com.example.brightcity.ui.viewmodels.DashboardViewModel
import com.example.brightcity.util.Constance
import com.example.brightcity.util.onTextChange
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import io.socket.client.Ack
import io.socket.client.IO
import io.socket.client.Manager
import io.socket.client.Socket
import io.socket.engineio.client.Transport
import org.json.JSONArray
import org.json.JSONObject
import java.net.URISyntaxException
import javax.inject.Inject

@AndroidEntryPoint
class DashboardFragment: MainFragment(R.layout.fragment_dashboard), DashboardLogAdapter.Interaction,OnAddPersonListener ,DashboardUserListAdapter.Interaction ,DashboardMessageAdapter.Interaction{

    private val viewModel: DashboardViewModel by viewModels()
    private val TAG = "DashboardFragment"
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding
    private var mSocket: Socket? = null
    private lateinit var userListAdapter: DashboardUserListAdapter
    private lateinit var logListAdapter: DashboardLogAdapter
    private lateinit var messageListAdapter: DashboardMessageAdapter
    private lateinit var userList: ArrayList<UserListResponse>
    private lateinit var logList: ArrayList<LogListResponse>
    private lateinit var messageList: ArrayList<LastMessageResponse>
    @Inject
    lateinit var picasso: Picasso

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userListAdapter = DashboardUserListAdapter(this, false, picasso)
        logListAdapter = DashboardLogAdapter(this ,picasso)
        messageListAdapter = DashboardMessageAdapter(this)
        logList = ArrayList()
        messageList = ArrayList()
       socketConnect()

/*

        mSocket?.on(Socket.EVENT_CONNECT){ args ->
            Log.e(TAG, "connect $args")
        }

        mSocket?.on(Socket.EVENT_CONNECT_ERROR){ args ->
            Log.e(TAG, "connect error: ${args[0]}")
        }

        mSocket?.on("error"){args->
            Log.e(TAG, "connect error ${args[0]}", )
        }

        val logListInput = JSONObject()
        logListInput.put("num", 5)
        logListInput.put("page", 1)
        mSocket?.emit("logs/list", logListInput, Ack { args ->
            Handler(Looper.getMainLooper()).post {
                val ack = args[0] as JSONArray
                Log.e(TAG, "logs/list ack $ack", )
                //    setUpLogRecycler(filterDateToList(ack))
            }
        })


        val test = JSONObject()
        test.put("a", 1)
        mSocket?.emit("user/dashboard", test , Ack { args ->
            Handler(Looper.getMainLooper()).post {
                val ack = args[0] as JSONObject
                Log.e(TAG, "user/dashboard ack: $ack")
            }
        })

        val test2 = JSONObject()
        test2.put("a", 1)
        mSocket?.emit("transaction/dashboard" ,test2, Ack { args ->
            Handler(Looper.getMainLooper()).post {
                val ack = args[0] as JSONObject
                Log.e(TAG, "light_casheir ack $ack")
            }
        })
*/
        val userListInput = JSONObject()
        userListInput.put("num", 7)
        userListInput.put("page", 1)
        mSocket?.emit("user/list", userListInput, Ack { args ->
            Handler(Looper.getMainLooper()).post {
                val ack = args[0] as JSONArray
                Log.e(TAG, "user/list ack $ack: ")
                setUpUserListRecycler(filterDateToList(ack))
            }
        })



    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeOnLogList()
        subscribeOnUserDashboard()
       // subscribeOnUserList()
        subscribeOnTransactionDashboard()
        subscribeOnLastMessage()
        getTransactionDashboard()
        getLogList()
       // getUserList()
        getUserDashboard()
        getLastMessage()
        socketOnEvents()
        binding?.usersBox?.txtSearchMore?.setOnClickListener { SearchFragment().show(
            childFragmentManager,
            null
        ) }
        binding?.usersBox?.title?.setOnClickListener { BraceletFragment(this).show(
            childFragmentManager,
            null
        ) }

        binding?.usersBox?.searchView?.onTextChange {
            timery(2000){
                if (!binding?.usersBox?.searchView?.text?.isEmpty()!!) getUserList(it)
                else getUserList()
            }
        }

    }

    private inline fun <reified T> filterDateToList(array: JSONArray): List<T>{
        val list = ArrayList<T>()
        for (i in 0 until array.length()) {
             list.add(Gson().fromJson(array.getJSONObject(i).toString(), T::class.java))
        }
        return list
    }

    private fun socketConnect(){
        try {
            val opts = IO.Options()
            mSocket = IO.socket(Constance.SOCKET_URL, opts)
        } catch (e: URISyntaxException) {
            Log.e(TAG, " mysocket url error ${e.message}")
        }
        mSocket?.connect()

        mSocket!!.io().on(Manager.EVENT_TRANSPORT) { args ->
            val transport: Transport = args[0] as Transport
            transport.on(Transport.EVENT_REQUEST_HEADERS) { args ->
                Log.v(TAG, "Caught EVENT_REQUEST_HEADERS after EVENT_TRANSPORT, adding headers")
                val mHeaders =
                    args[0] as MutableMap<String, List<String>>
                mHeaders["authorization"] = listOf("bearer ${MyJwt.getAccessToken()}")
            }
        }
    }

    private fun socketOnEvents(){

        mSocket?.on(Socket.EVENT_CONNECT){
            Log.e(TAG, "socket Connect: ")
        }
        mSocket?.on(Socket.EVENT_DISCONNECT){
            Log.e(TAG, "socket Disconnect: ")
        }

        mSocket?.on("logs/list"){ args ->
            Handler(Looper.getMainLooper()).post {
                val ack = args[0] as JSONArray
                Log.e(TAG, "logs/list: $ack")
                setUpLogRecycler(filterDateToList(ack))
            }
        }

        mSocket?.on("user/dashboard"){ args ->
            Handler(Looper.getMainLooper()).post {
                val ack = args[0] as JSONObject
                Log.e(TAG, "socket user/dashboard: $ack")
                binding?.counterBox?.registerCount?.text = ack.getString("new_registration")
                binding?.counterBox?.usersCount?.text = ack.getString("active_family")
                binding?.counterBox?.familyCount?.text = ack.getString("active_user")
            }
        }

        mSocket?.on("transaction/dashboard"){ args ->
            Handler(Looper.getMainLooper()).post {
                val ack = args[0] as JSONObject
                Log.e(TAG, "transaction/dashboard: $ack")
                binding?.creditBox?.purchasedValue?.text = ack.getString("sum_payed")
                binding?.creditBox?.consumedValue?.text = ack.getString("sum_used")
                binding?.creditBox?.returnedValue?.text = ack.getString("sum_back")
                binding?.creditBox?.negativeValue?.text = ack.getString("sum_negative")
            }
        }

        mSocket?.on("message/list"){ args ->
            Handler(Looper.getMainLooper()).post {
                val ack = args[0] as JSONArray
                Log.e(TAG, "message/list: $ack")
                setUpMessageRecycler(filterDateToList(ack))
            }
        }

        mSocket?.on("user/list"){ args ->
            Handler(Looper.getMainLooper()).post {
                val data = args[0] as JSONArray
                Log.e(TAG, "user/list ack $data: ")
                setUpUserListRecycler(filterDateToList(data))
            }
        }
    }

    private fun setUpUserListRecycler(list: List<UserListResponse>){
        userList = ArrayList()
        binding?.usersBox?.recyclerSearchBoxSearch?.adapter = userListAdapter
        userList.addAll(list)
        userListAdapter.submitList(userList)
    }

    private fun setUpLogRecycler(list: List<LogListResponse>){
        binding?.activityBox?.recyclerActivity?.adapter = logListAdapter
        logList.addAll(list)
        logListAdapter.submitList(logList)
    }

    private fun setUpMessageRecycler(list: List<LastMessageResponse>){
        binding?.messagesBox?.recyclerLastMessage?.adapter = messageListAdapter
        messageList.addAll(list)
        messageListAdapter.submitList(messageList)
    }

    private fun getLogList(){
        viewModel.getLogList()
    }

    private fun getUserDashboard(){
        viewModel.userDashboard()
    }

    private fun getUserList(search: String? = null){
        viewModel.getUserList(7,1,search = search)
    }

    private fun getTransactionDashboard(){
        viewModel.getTransactionDashboard()
    }

    private fun getLastMessage(){
        viewModel.getLastMessage()
    }

    private fun subscribeOnLogList(){
        viewModel.logList.observe(viewLifecycleOwner) { response ->
            binding?.loadingDashboard?.visibility = View.GONE
            when(response){
                is ApiWrapper.Success -> {
                    response.data?.let {
                        Log.e(TAG, "subscribeOnLogList: $it")
                        setUpLogRecycler(it)
                    }
                }
                is ApiWrapper.ApiError -> {
                    response.error?.let { toastError(it.message) }
                }
                is ApiWrapper.UnknownError -> {
                    toastError()
                }
                is ApiWrapper.NetworkError -> {
                    toastNet()
                }
            }
        }
    }

    private fun subscribeOnUserDashboard(){
        viewModel.userDashboard.observe(viewLifecycleOwner) { response ->
            binding?.loadingDashboard?.visibility = View.GONE
            when(response){
                is ApiWrapper.Success -> {
                    response.data?.let {
                        binding?.counterBox?.registerCount?.text = it.new_registration.toString()
                        binding?.counterBox?.usersCount?.text = it.active_user.toString()
                        binding?.counterBox?.familyCount?.text = it.active_family.toString()
                    }
                }
                is ApiWrapper.ApiError -> {
                    response.error?.let { toastError(it.message) }
                }
                is ApiWrapper.UnknownError -> {
                    toastError()
                }
                is ApiWrapper.NetworkError -> {
                    toastNet()
                }
            }
        }
    }

    private fun subscribeOnUserList(){
        viewModel.userList.observe(viewLifecycleOwner) { response ->
            binding?.loadingDashboard?.visibility = View.GONE
            when(response){
                is ApiWrapper.Success -> {
                    response.data?.let {
                        setUpUserListRecycler(it)
                    }
                }
                is ApiWrapper.ApiError -> {
                    response.error?.let { toastError(it.message) }
                }
                is ApiWrapper.UnknownError -> {
                    toastError()
                }
                is ApiWrapper.NetworkError -> {
                    toastNet()
                }
            }
        }
    }

    private fun subscribeOnTransactionDashboard(){
        viewModel.transactionDashboard.observe(viewLifecycleOwner) { response ->
            binding?.loadingDashboard?.visibility = View.GONE
            when(response){
                is ApiWrapper.Success -> {
                    response.data?.let {
                        binding?.creditBox?.purchasedValue?.text = it.sum_payed.toString()
                        binding?.creditBox?.consumedValue?.text = it.sum_used.toString()
                        binding?.creditBox?.returnedValue?.text = it.sum_back.toString()
                        binding?.creditBox?.negativeValue?.text = it.sum_negative.toString()
                    }
                }
                is ApiWrapper.ApiError -> {
                    response.error?.let { toastError(it.message) }
                }
                is ApiWrapper.UnknownError -> {
                    toastError()
                }
                is ApiWrapper.NetworkError -> {
                    toastNet()
                }
            }
        }
    }

    private fun subscribeOnLastMessage(){
        viewModel.lastMessage.observe(viewLifecycleOwner) { response ->
            binding?.loadingDashboard?.visibility = View.GONE
            when(response){
                is ApiWrapper.Success -> {
                    response.data?.let {
                        Log.e(TAG, "subscribeOnLastMessage: $it", )
                        setUpMessageRecycler(it)
                    }
                }
                is ApiWrapper.ApiError -> {
                    response.error?.let { toastError(it.message) }
                }
                is ApiWrapper.UnknownError -> {
                    toastError()
                }
                is ApiWrapper.NetworkError -> {
                    toastNet()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemSelected(position: Int, item: LogListResponse) {
        // Do Nothing
    }

    override fun onAddPersonClicked(dialog: DialogFragment) {
        dialog.dismiss()
        AddPersonFragment().show(childFragmentManager, null)
    }

    override fun onUserItemSelected(position: Int, item: UserListResponse) {
        ChargeFragment.newInstance(item.id).show(childFragmentManager ,null)
    }

    override fun onItemSelected(position: Int, item: LastMessageResponse) {
        // Do Nothing
    }

}