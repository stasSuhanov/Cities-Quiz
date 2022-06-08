package com.mytest.android.citiesapp.ui.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mytest.android.citiesapp.data.city.CityDatabase
import com.mytest.android.citiesapp.data.city.CityEntity
import com.mytest.android.citiesapp.data.city.CityRepository
import kotlinx.coroutines.launch

class SearchViewModel(application: Application) : AndroidViewModel(application) {

    private val cityDao = CityDatabase.getDatabase(application).cityDao()
    private var repository = CityRepository(cityDao)

    private val _citiesList: MutableLiveData<List<CityEntity>> = MutableLiveData()
    val citiesList: LiveData<List<CityEntity>> get() = _citiesList

    fun findCites(searchQuery: String) {
        if (searchQuery.length >= MIN_SYMBOLS) {
            viewModelScope.launch {
                _citiesList.value = repository.findCites(searchQuery)
            }
        }
    }

    companion object {
        private const val MIN_SYMBOLS = 2
    }
}