package com.groundzero.androidlocalechange

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import com.groundzero.androidlocalechange.locale.LocaleHelper

class App: Application() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "hr"))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        LocaleHelper.onAttach(applicationContext, "hr")
    }
}