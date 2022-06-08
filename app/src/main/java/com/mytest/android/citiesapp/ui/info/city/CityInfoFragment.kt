package com.mytest.android.citiesapp.ui.info.city

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.api.load
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.mytest.android.citiesapp.R
import com.mytest.android.citiesapp.databinding.FragmentCityInfoBinding
import com.mytest.android.citiesapp.utils.LinkBuilderForDownloadFlag
import com.mytest.android.citiesapp.utils.NumberSeparator

class CityInfoFragment : Fragment() {

    private val args: CityInfoFragmentArgs by navArgs()

    private lateinit var binding: FragmentCityInfoBinding

    private lateinit var cityInfoViewModel: CityInfoViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCityInfoBinding.inflate(inflater, container, false)

        cityInfoViewModel = ViewModelProvider(this)[CityInfoViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        cityInfoViewModel.findCityById(args.cityId)

        NumberSeparator.setNewLocale()
        observeViewModel()
    }

    private fun observeViewModel() {
        cityInfoViewModel.city.observe(viewLifecycleOwner, { cityEntity ->
            with(binding) {
                cityName.text = cityEntity.city
                cityName.movementMethod = ScrollingMovementMethod()
                population.text = cityEntity.population?.let { NumberSeparator.addNumberSeparator(it) }
                countryName.text = cityEntity.country
                countryFlag.load(LinkBuilderForDownloadFlag.buildLink(cityEntity.countryCode))

                countryName.setOnClickListener {
                    findNavController().navigate(
                        CityInfoFragmentDirections.actionCityInfoFragmentToCountryInfoFragment(cityEntity.country, cityEntity.countryCode)
                    )
                }
            }

            val mapFragment = childFragmentManager.findFragmentById(R.id.google_map) as? SupportMapFragment
            mapFragment?.getMapAsync { googleMap ->
                val latLng = LatLng(cityEntity.lat, cityEntity.lng)
                googleMap.addMarker(MarkerOptions().position(latLng).title(cityEntity.city))
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, MAP_ZOOM))
            }
        })
    }

    companion object {
        private const val MAP_ZOOM = 10f
    }
}