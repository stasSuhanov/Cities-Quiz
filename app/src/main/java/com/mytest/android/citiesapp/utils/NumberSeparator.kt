package com.mytest.android.citiesapp.utils

import java.text.NumberFormat
import java.util.*

object NumberSeparator {

    private var formatter = NumberFormat.getInstance(Locale.getDefault())

    fun setNewLocale() {
        formatter = NumberFormat.getInstance(Locale.getDefault())
    }

    fun addNumberSeparator(number: Int): String {
        return formatter.format(number)
    }
}