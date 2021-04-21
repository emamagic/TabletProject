package com.example.brightcity.interfaces

import android.widget.PopupWindow

interface OnProfileMenuListener {
    fun onProfileMenuMessagesClicked(pw: PopupWindow)
    fun onProfileMenuExitClicked(pw: PopupWindow)
    fun onProfileMenuUserAccountClicked(pw: PopupWindow)
}