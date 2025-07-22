package com.mahdi.rostamipour.chatgram.data.service

import android.content.Context
import android.content.SharedPreferences
import android.provider.Settings.Global.putInt
import androidx.core.content.edit

object MyPreferences {

    private const val PREF_NAME = "chat_pref"

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    var userId: Int?
        get() = if (prefs.contains("USERID")) prefs.getInt("USERID", 0) else null
        set(value) {
            prefs.edit {
                if (value != null) {
                    putInt("USERID", value)
                } else {
                    remove("USERID")
                }
            }
        }

    fun registerListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        prefs.registerOnSharedPreferenceChangeListener(listener)
    }

    fun unregisterListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        prefs.unregisterOnSharedPreferenceChangeListener(listener)
    }

}