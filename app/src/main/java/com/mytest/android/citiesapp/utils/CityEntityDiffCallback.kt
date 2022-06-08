package com.mytest.android.citiesapp.utils

import androidx.recyclerview.widget.DiffUtil
import com.mytest.android.citiesapp.data.city.CityEntity

class CityEntityDiffCallback(private val oldList: List<CityEntity>, private val newList: List<CityEntity>) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}