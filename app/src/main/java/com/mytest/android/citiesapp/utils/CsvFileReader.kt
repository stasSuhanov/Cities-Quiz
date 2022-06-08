package com.mytest.android.citiesapp.utils

import com.mytest.android.citiesapp.data.city.CityEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.Reader

enum class Column(val number: Int) {
    CITY(0),
    CITY_ASCII(1),
    CITY_LAT(2),
    CITY_LNG(3),
    COUNTRY(4),
    COUNTRY_CODE(5),
    CAPITAL(8),
    POPULATION(9),
}

class CsvFileReader {

    suspend fun readDataFromCsvFile(input: Reader): MutableList<CityEntity> {

        val reader = BufferedReader(input)
        var line: String?
        val cities: MutableList<CityEntity> = mutableListOf()

        @Suppress("BlockingMethodInNonBlockingContext")
        return withContext(Dispatchers.IO) {

            while (reader.readLine().also { line = it } != null) {
                val row: MutableList<String?> = line?.replace("\"", "")?.split(",")?.toMutableList() ?: continue

                val cityName = row[Column.CITY.number].toString()
                val cityASCII = row[Column.CITY_ASCII.number]?.takeIf { it.isNotEmpty() }.toString()
                val countryCode = row[Column.COUNTRY_CODE.number].toString().lowercase()
                val countryName = row[Column.COUNTRY.number].toString()
                val cityCapital = row[Column.CAPITAL.number]?.takeIf { it.isNotEmpty() }.toString()
                val population = row[Column.POPULATION.number]?.toIntOrNull() ?: DEFAULT_POPULATION
                val cityLat = row[Column.CITY_LAT.number]?.toDoubleOrNull() ?: DEFAULT_COORDINATES
                val cityLng = row[Column.CITY_LNG.number]?.toDoubleOrNull() ?: DEFAULT_COORDINATES

                val city = CityEntity(
                    0,
                    cityName,
                    cityASCII,
                    countryCode,
                    countryName,
                    cityCapital,
                    population,
                    cityLat,
                    cityLng
                )
                cities.add(city)
            }
            cities
        }
    }

    companion object {
        private const val DEFAULT_COORDINATES = 0.0
        private const val DEFAULT_POPULATION = 0
    }
}