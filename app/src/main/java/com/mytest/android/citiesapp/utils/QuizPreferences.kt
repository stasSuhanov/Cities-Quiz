package com.mytest.android.citiesapp.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.mytest.android.citiesapp.ui.MainActivity

object QuizPreferences {

    fun saveBestScore(context: Context, value: Int, key: String) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun loadBestScore(context: Context, key: String): Int {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE)
        return sharedPreferences.getInt(key, START_SCORE)
    }

    fun saveAppTheme(context: Context, value: String) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(APP_THEME, value)
        editor.apply()
    }

    fun loadAppTheme(context: Context): String {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE)
        return sharedPreferences.getString(APP_THEME, MainActivity.DEFAULT_THEME).toString()
    }

    private const val PREF_NAME = "PREF_NAME"
    private const val APP_THEME = "APP_THEME"
    private const val START_SCORE = 0
}