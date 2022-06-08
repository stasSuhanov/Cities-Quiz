package com.mytest.android.citiesapp.ui.info.country

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.api.load
import coil.transform.RoundedCornersTransformation
import com.mytest.android.citiesapp.R
import com.mytest.android.citiesapp.databinding.FragmentCountryInfoBinding
import com.mytest.android.citiesapp.utils.LinkBuilderForDownloadFlag
import com.mytest.android.citiesapp.utils.NumberSeparator
import com.mytest.android.citiesapp.utils.RoundPopulation

class CountryInfoFragment : Fragment() {

    private val args: CountryInfoFragmentArgs by navArgs()

    private lateinit var binding: FragmentCountryInfoBinding

    private lateinit var countryInfoViewModel: CountryInfoViewModel

    private lateinit var adapter: CountryInfoFragmentAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCountryInfoBinding.inflate(inflater, container, false)

        adapter = CountryInfoFragmentAdapter()

        binding.recyclerViewCountryInfoFragment.adapter = adapter
        binding.recyclerViewCountryInfoFragment.layoutManager = LinearLayoutManager(requireContext())

        countryInfoViewModel = ViewModelProvider(this)[CountryInfoViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val countryName = args.countryName
        val countryCode = args.countryCode
        binding.countryNameInfo.text = countryName
        binding.countryFlag.load(LinkBuilderForDownloadFlag.buildLink(countryCode))
        binding.cityImage.load(R.drawable.background_city_placeholder) {
            transformations(RoundedCornersTransformation(ROUNDED_CORNERS, ROUNDED_CORNERS, ROUNDED_CORNERS, ROUNDED_CORNERS))
        }
        countryInfoViewModel.findCapitalOfCountryAndCities(countryName)

        NumberSeparator.setNewLocale()
        observeViewModel()
    }

    private fun observeViewModel() {
        countryInfoViewModel.capital.observe(viewLifecycleOwner, {
            binding.capitalName.text = it
            binding.capitalName.movementMethod = ScrollingMovementMethod()
        })
        countryInfoViewModel.citiesList.observe(viewLifecycleOwner, { cityEntity ->
            adapter.setItems(cityEntity)
            val population = cityEntity.sumOf { it.population ?: DEFAULT_POPULATION }
            binding.population.text = getString(
                R.string.total_population,
                NumberSeparator.addNumberSeparator(RoundPopulation().roundPopulation(population))
            )
        })
    }

    companion object {
        private const val ROUNDED_CORNERS = 10F
        private const val DEFAULT_POPULATION = 0
    }
}