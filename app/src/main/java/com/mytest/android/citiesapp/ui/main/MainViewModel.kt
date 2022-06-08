package com.mytest.android.citiesapp.ui.main

import android.app.Application
import android.content.res.AssetManager
import androidx.lifecycle.AndroidViewModel
import com.mytest.android.citiesapp.data.city.CityDatabase
import com.mytest.android.citiesapp.data.city.CityEntity
import com.mytest.android.citiesapp.data.city.CityRepository
import com.mytest.android.citiesapp.utils.CsvFileReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStreamReader

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val cityDao = CityDatabase.getDatabase(application).cityDao()
    private var repository = CityRepository(cityDao)

    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun addCitiesToDatabase(assetManager: AssetManager): Int {
        var count = 0

        withContext(Dispatchers.IO) {
            val citiesList = mutableListOf<CityEntity>()
            val input = InputStreamReader(assetManager.open(FILE_PATH))
            CsvFileReader().readDataFromCsvFile(input).forEach {
                citiesList.add(it)
            }

            repository.addCitiesToDatabase(citiesList)
            count = citiesList.size
        }
        return count
    }

    suspend fun countStringsInDatabase(): Int {
        var amountStringsInDatabase: Int

        withContext(Dispatchers.IO) {
            amountStringsInDatabase = repository.countStrings()
        }
        return amountStringsInDatabase
    }

    companion object {
        private const val FILE_PATH = "database/worldcities.csv"
    }
}