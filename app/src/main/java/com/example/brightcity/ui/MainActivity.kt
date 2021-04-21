package com.example.brightcity.ui

import android.app.PendingIntent
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.brightcity.R
import com.example.brightcity.api.safe.MyJwt
import com.example.brightcity.nfc.NdefMessageParser
import com.example.brightcity.nfc.NfcTextUtil.dumpTagData
import com.example.brightcity.ui.fragments.HomeFragment
import com.example.brightcity.ui.fragments.LoginFragment
import com.example.brightcity.util.Constance
import dagger.hilt.android.AndroidEntryPoint
import io.socket.client.IO
import io.socket.client.Manager
import io.socket.client.Socket
import io.socket.engineio.client.Transport
import java.net.URISyntaxException


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var nfcAdapter: NfcAdapter
    private lateinit var pendingIntent: PendingIntent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MyJwt.getSharedPref(this)

        if(savedInstanceState == null) {
            if (MyJwt.isLoggedIn()){
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fram_main_container, HomeFragment())
                    .commit()
            }else{
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fram_main_container, LoginFragment())
                    .commit()
            }
        }

//        try {
//            nfcAdapter = NfcAdapter.getDefaultAdapter(this)
//        }catch (e: NullPointerException){
//            Toast.makeText(this, "Not Support NFC", Toast.LENGTH_SHORT).show()
//            finish()
//        }
//
//
//
//            pendingIntent = PendingIntent.getActivity(
//                this, 0, Intent(this, this.javaClass).addFlags(
//                    Intent.FLAG_ACTIVITY_SINGLE_TOP
//                ), 0
//            )
    }

    override fun onResume() {
        super.onResume()
//        if (nfcAdapter != null){
//            if (!nfcAdapter.isEnabled){
//                startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS))
//                nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null)
//            }
//        }
    }

    override fun onPause() {
        super.onPause()
//        if (nfcAdapter != null){
//            nfcAdapter.disableForegroundDispatch(this)
//        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
      //  setIntent(intent)
      //  resolveIntent(intent)
    }

    private fun resolveIntent(intent: Intent?) {
        val action = intent?.action
        if (NfcAdapter.ACTION_TAG_DISCOVERED == action || NfcAdapter.ACTION_TECH_DISCOVERED == action || NfcAdapter.ACTION_NDEF_DISCOVERED == action){
            val rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            val msgs: Array<NdefMessage?>
            if (rawMsgs != null) {
                msgs = arrayOfNulls(rawMsgs.size)
                for (i in rawMsgs.indices) {
                    msgs[i] = rawMsgs.get(i) as NdefMessage?
                }
            } else {
                val empty = ByteArray(0)
                val id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID)
                val tag: Tag? = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG) as Tag?
                val payload = dumpTagData(tag).toByteArray()
                val record = NdefRecord(NdefRecord.TNF_UNKNOWN, empty, id, payload)
                val msg = NdefMessage(arrayOf(record))
                msgs = arrayOf(msg)
            }
            displayMsgs(msgs)
        }
    }

    private fun displayMsgs(msgs: Array<NdefMessage?>?) {
        if (msgs == null || msgs.isEmpty()) return
        val builder = StringBuilder()
        val records = NdefMessageParser.parse(msgs[0])
        val size = records.size
        for (i in 0 until size) {
            val record = records[i]
            val str = record.str()
            builder.append(str).append("\n")
        }
        Toast.makeText(this, builder.toString(), Toast.LENGTH_SHORT).show()
    }

}