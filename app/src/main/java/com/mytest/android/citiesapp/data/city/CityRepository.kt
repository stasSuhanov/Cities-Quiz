package com.mytest.android.citiesapp.data.city

class CityRepository(private val cityDao: CityDao) {

    suspend fun addCitiesToDatabase(cities: List<CityEntity>) = cityDao.addCitiesToDatabase(cities)

    suspend fun findCites(searchQuery: String): List<CityEntity> = cityDao.findCites("%$searchQuery%")

    suspend fun findCitiesByCountryName(searchQuery: String): List<CityEntity> = cityDao.findCitiesByCountryName("%$searchQuery%")

    suspend fun findCapitalOfCountry(countryName: String): String = cityDao.findCapitalOfCountry(PRIMARY, countryName)

    suspend fun findCountryWithCapital(): MutableList<CityEntity> = cityDao.findCountryWithCapital(PRIMARY)

    suspend fun findFourRandomCity(): MutableList<String> = cityDao.findFourRandomCity()

    suspend fun findFourRandomCityInRange(): MutableList<String> = cityDao.findFourRandomCityInRange(MIN_POPULATION)

    suspend fun findCityById(cityId: Int): CityEntity = cityDao.findCityById(cityId)

    suspend fun countStrings(): Int = cityDao.countStrings()

    companion object {
        private const val PRIMARY = "primary"
        private const val MIN_POPULATION = 100000
    }
}