package com.mytest.android.citiesapp.utils

import kotlin.math.pow

class RoundPopulation {
    fun roundPopulation(population: Int): Int {
        val power = getCountsOfDigits(population)
        val divider = ((10.0.pow(power - 2)).toInt())

        return when {
            population <= -1 -> 0
            population == 0 || power <= 3 -> population
            else -> ((population + ((10.0.pow(power - 3)).toInt() * 5)) / divider) * divider
        }
    }

    private fun getCountsOfDigits(number: Int): Int {
        return number.toString().length
    }
}