package com.groundzero.androidlocalechange

import android.annotation.TargetApi
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.properties.Delegates
import kotlin.reflect.KProperty


class MainActivity : AppCompatActivity() {

    private val buttons = listOf("hr", "it", "es")
    private var activeButton: String by Delegates.observable("hr", ::dataChanged)

    private var persistenceUtils: PersistenceUtils? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        set_croatian_button.setOnClickListener { changeLocale(buttons[0]) }
        set_italian_button.setOnClickListener { changeLocale(buttons[1]) }
        set_spanish_button.setOnClickListener { changeLocale(buttons[2]) }
        persistenceUtils = PersistenceUtils(this).also {
            activeButton = if (it.getValue(LOCALE_KEY)!! == "") {
                buttons[0]
            } else {
                it.getValue(LOCALE_KEY)!!
            }
        }
    }

    private fun changeLocale(localeString: String) {
        activeButton = localeString
        recreate()
    }

    private fun dataChanged(property: KProperty<*>, oldValue: String, newValue: String) {
        when (oldValue) {
            buttons[0] -> set_croatian_button.isChecked = false
            buttons[1] -> set_italian_button.isChecked = false
            buttons[2] -> set_spanish_button.isChecked = false
        }
        when (newValue) {
            buttons[0] -> {
                set_croatian_button.isChecked = true
                persistenceUtils!!.setValue(LOCALE_KEY, buttons[0])
            }
            buttons[1] -> {
                set_italian_button.isChecked = true
                persistenceUtils!!.setValue(LOCALE_KEY, buttons[1])
            }
            buttons[2] -> {
                set_spanish_button.isChecked = true
                persistenceUtils!!.setValue(LOCALE_KEY, buttons[2])
            }
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(updateBaseContextLocale(newBase!!))
    }

    private fun updateBaseContextLocale(context: Context): Context? {

        persistenceUtils ?: let {
            persistenceUtils = PersistenceUtils(context)
        }

        val language: String = persistenceUtils!!.getValue(LOCALE_KEY) ?: buttons[0]
        val locale = Locale(language)
        Locale.setDefault(locale)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            updateResourcesLocale(context, locale)
        } else updateResourcesLocaleLegacy(context, locale)
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun updateResourcesLocale(context: Context, locale: Locale): Context? {
        val configuration: Configuration = context.getResources().getConfiguration()
        configuration.setLocale(locale)
        return context.createConfigurationContext(configuration)
    }

    @Suppress("DEPRECATION")
    private fun updateResourcesLocaleLegacy(context: Context, locale: Locale): Context? {
        val resources: Resources = context.getResources()
        val configuration: Configuration = resources.getConfiguration()
        configuration.locale = locale
        resources.updateConfiguration(configuration, resources.getDisplayMetrics())
        return context
    }

    companion object {
        const val LOCALE_KEY = "locale_key"
    }
}
