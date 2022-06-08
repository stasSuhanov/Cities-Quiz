package com.mytest.android.citiesapp.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mytest.android.citiesapp.R
import com.mytest.android.citiesapp.data.city.CityEntity
import com.mytest.android.citiesapp.ui.info.country.CountryInfoFragmentAdapter.Companion.NO_POPULATION
import com.mytest.android.citiesapp.utils.CityEntityDiffCallback
import com.mytest.android.citiesapp.utils.NumberSeparator
import com.mytest.android.citiesapp.utils.RoundPopulation

class SearchFragmentAdapter : RecyclerView.Adapter<SearchFragmentViewHolder>() {

    private val cityList = mutableListOf<CityEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchFragmentViewHolder {
        return SearchFragmentViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_city, parent, false))
    }

    override fun onBindViewHolder(holder: SearchFragmentViewHolder, position: Int) {
        val cityEntity = cityList[position]
        holder.bind(cityEntity.city, cityEntity.country, cityEntity.population ?: NO_POPULATION, cityEntity.countryCode)

        holder.itemView.setOnClickListener { view ->
            view.findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToCityInfoFragment(cityEntity.id))
        }
    }

    override fun getItemCount(): Int = cityList.size

    fun updateItems(newItems: List<CityEntity>) {
        val diffCallBack = CityEntityDiffCallback(cityList, newItems)
        val diffResult = DiffUtil.calculateDiff(diffCallBack)
        cityList.clear()
        cityList.addAll(newItems)
        diffResult.dispatchUpdatesTo(this)
    }
}

class SearchFragmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val cityName = itemView.findViewById<TextView>(R.id.city_name)
    private val countryName = itemView.findViewById<TextView>(R.id.country_name)
    private val populationOfCity = itemView.findViewById<TextView>(R.id.amount_population)

    fun bind(city: String, country: String, population: Int, countryCode: String) {
        cityName.text = city
        countryName.text = country
        populationOfCity.text = NumberSeparator.addNumberSeparator(RoundPopulation().roundPopulation(population))

        countryName.setOnClickListener {
            it.findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToCountryInfoFragment(country, countryCode))
        }
    }
}