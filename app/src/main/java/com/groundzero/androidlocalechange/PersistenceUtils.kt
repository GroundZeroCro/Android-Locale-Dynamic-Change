package com.groundzero.androidlocalechange

import android.content.Context
import androidx.preference.PreferenceManager

class PersistenceUtils(context: Context) {

    private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun getValue(key: String) = sharedPreferences.getString(key, "")
    fun setValue(key: String, value: String) = sharedPreferences.edit().putString(key, value).commit()
}