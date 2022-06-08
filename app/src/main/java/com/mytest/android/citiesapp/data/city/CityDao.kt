package com.mytest.android.citiesapp.data.city

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CityDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCitiesToDatabase(cityEntities: List<CityEntity>)

    @Query("SELECT * FROM city_table WHERE city LIKE :searchQuery OR  country LIKE :searchQuery ORDER BY population DESC LIMIT 10")
    suspend fun findCites(searchQuery: String): List<CityEntity>

    @Query("SELECT * FROM city_table WHERE country LIKE :searchQuery ORDER BY population DESC")
    suspend fun findCitiesByCountryName(searchQuery: String): List<CityEntity>

    @Query("SELECT city FROM city_table WHERE capital = :capitalStatus AND country = :countryName")
    suspend fun findCapitalOfCountry(capitalStatus: String, countryName: String): String

    @Query("SELECT * FROM city_table WHERE capital = :capitalStatus")
    suspend fun findCountryWithCapital(capitalStatus: String): MutableList<CityEntity>

    @Query("SELECT city FROM city_table WHERE population > :minPopulation ORDER BY RANDOM() DESC LIMIT 4")
    suspend fun findFourRandomCityInRange(minPopulation: Int): MutableList<String>

    @Query("SELECT city FROM city_table ORDER BY RANDOM() DESC LIMIT 4")
    suspend fun findFourRandomCity(): MutableList<String>

    @Query("SELECT COUNT(*) FROM city_table")
    suspend fun countStrings(): Int

    @Query("SELECT * FROM city_table WHERE id = :cityId")
    suspend fun findCityById(cityId: Int): CityEntity
}