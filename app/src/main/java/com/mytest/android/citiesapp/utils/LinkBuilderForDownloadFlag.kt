package com.mytest.android.citiesapp.utils


object LinkBuilderForDownloadFlag {

    fun buildLink(countryCode: String): String {
        return LINK_TO_FLAG + countryCode + PNG
    }

    private const val LINK_TO_FLAG = "https://flagcdn.com/144x108/"
    private const val PNG = ".png"
}