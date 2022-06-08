package com.mytest.android.citiesapp.ui.info.country

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mytest.android.citiesapp.R
import com.mytest.android.citiesapp.data.city.CityEntity
import com.mytest.android.citiesapp.utils.CityEntityDiffCallback
import com.mytest.android.citiesapp.utils.NumberSeparator
import com.mytest.android.citiesapp.utils.RoundPopulation

class CountryInfoFragmentAdapter : RecyclerView.Adapter<CountryInfoViewHolder>() {

    private val cityList = mutableListOf<CityEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryInfoViewHolder {
        return CountryInfoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_country, parent, false))
    }

    override fun onBindViewHolder(holder: CountryInfoViewHolder, position: Int) {
        val cityEntity = cityList[position]
        holder.bind(cityEntity.city, cityEntity.population ?: NO_POPULATION)

        holder.itemView.setOnClickListener { view ->
            view.findNavController().navigate(CountryInfoFragmentDirections.actionSearchFragmentToCityInfoFragment(cityEntity.id))
        }
    }

    override fun getItemCount(): Int = cityList.size

    fun setItems(newItems: List<CityEntity>) {
        val diffCallBack = CityEntityDiffCallback(cityList, newItems)
        val diffResult = DiffUtil.calculateDiff(diffCallBack)
        cityList.clear()
        cityList.addAll(newItems)
        diffResult.dispatchUpdatesTo(this)
    }

    companion object {
        const val NO_POPULATION = 0
    }
}

class CountryInfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val cityName = itemView.findViewById<TextView>(R.id.city_name_info)
    private val population = itemView.findViewById<TextView>(R.id.amount_population)

    fun bind(city: String, populationAmount: Int) {
        cityName.text = city
        population.text = NumberSeparator.addNumberSeparator(RoundPopulation().roundPopulation(populationAmount))
    }
}