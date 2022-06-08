package com.mytest.android.citiesapp.ui.info.country

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mytest.android.citiesapp.data.city.CityDatabase
import com.mytest.android.citiesapp.data.city.CityEntity
import com.mytest.android.citiesapp.data.city.CityRepository
import kotlinx.coroutines.launch

class CountryInfoViewModel(application: Application) : AndroidViewModel(application) {

    private val cityDao = CityDatabase.getDatabase(application).cityDao()
    private var repository = CityRepository(cityDao)

    private val _capital: MutableLiveData<String> = MutableLiveData()
    val capital: LiveData<String> get() = _capital

    private val _citiesList: MutableLiveData<List<CityEntity>> = MutableLiveData()
    val citiesList: LiveData<List<CityEntity>> get() = _citiesList

    private var isCityLoad = false

    fun findCapitalOfCountryAndCities(countryName: String) {
        if (!isCityLoad) {
            isCityLoad = true
            viewModelScope.launch {
                _capital.value = repository.findCapitalOfCountry(countryName)
                _citiesList.value = repository.findCitiesByCountryName(countryName)
            }
        }
    }
}