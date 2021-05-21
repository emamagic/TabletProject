package com.example.brightcity.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.brightcity.R
import com.example.brightcity.api.safe.MyJwt
import com.example.brightcity.ui.fragments.HomeFragment
import com.example.brightcity.ui.fragments.LoginFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MyJwt.getSharedPref(this)

        if (savedInstanceState == null) {
            if (MyJwt.isLoggedIn()) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fram_main_container, HomeFragment())
                    .commit()
            } else {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fram_main_container, LoginFragment())
                    .commit()
            }
        }
    }
}

