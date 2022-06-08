package com.mytest.android.citiesapp.data.city

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mytest.android.citiesapp.data.city.CityEntity.Companion.CITY_TABLE

@Entity(tableName = CITY_TABLE)
data class CityEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val city: String,
    val cityAscii: String?,
    val countryCode: String,
    val country: String,
    val capital: String?,
    val population: Int?,
    val lat: Double,
    val lng: Double
) {
    companion object {
        const val CITY_TABLE = "city_table"
    }
}

