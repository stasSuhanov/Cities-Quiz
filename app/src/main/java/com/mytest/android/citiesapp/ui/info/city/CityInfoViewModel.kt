package com.mytest.android.citiesapp.ui.info.city

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mytest.android.citiesapp.data.city.CityDatabase
import com.mytest.android.citiesapp.data.city.CityEntity
import com.mytest.android.citiesapp.data.city.CityRepository
import kotlinx.coroutines.launch

class CityInfoViewModel(application: Application) : AndroidViewModel(application) {

    private val cityDao = CityDatabase.getDatabase(application).cityDao()
    private var repository = CityRepository(cityDao)

    private val _city: MutableLiveData<CityEntity> = MutableLiveData()
    val city: LiveData<CityEntity> get() = _city

    private var isCityLoad = false

    fun findCityById(cityId: Int) {
        if (!isCityLoad) {
            isCityLoad = true
            viewModelScope.launch { _city.value = repository.findCityById(cityId) }
        }
    }
}
