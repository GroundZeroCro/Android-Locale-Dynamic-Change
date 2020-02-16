package com.groundzero.androidlocalechange.locale

import android.os.Build
import android.os.LocaleList
import java.util.*


val defaultLocale: Locale
    get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) LocaleList.getDefault()[0] else Locale.getDefault()

infix fun Locale.equals(locale: Locale): Boolean =
    if (this == locale) true else country == locale.country && language == locale.language