package com.mytest.android.citiesapp.ui.search

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import coil.api.load
import com.mytest.android.citiesapp.R
import com.mytest.android.citiesapp.databinding.FragmentSearchBinding
import com.mytest.android.citiesapp.utils.LinkBuilderForDownloadFlag
import com.mytest.android.citiesapp.utils.NumberSeparator

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding

    private lateinit var searchViewModel: SearchViewModel

    private lateinit var adapter: SearchFragmentAdapter

    private var searchQuery: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        adapter = SearchFragmentAdapter()
        with(binding) {
            recyclerViewSearchFragment.adapter = adapter
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                recyclerViewSearchFragment.layoutManager = LinearLayoutManager(requireContext())
            } else {
                recyclerViewSearchFragment.layoutManager = GridLayoutManager(requireContext(), 2)
            }
        }

        searchViewModel = ViewModelProvider(this)[SearchViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showKeyBoard()
        binding.searchLine.doAfterTextChanged {
            searchViewModel.findCites(binding.searchLine.text.toString().trim())
            searchQuery = binding.searchLine.text.toString().trim()
        }

        NumberSeparator.setNewLocale()
        observeViewModel()
    }

    private fun observeViewModel() {
        searchViewModel.citiesList.observe(viewLifecycleOwner, { cityEntity ->
            adapter.updateItems(cityEntity)
            if (cityEntity.isEmpty()) {
                fadeInAnimation()
                binding.emptyQueryImage.visibility = View.VISIBLE
                binding.emptyQueryText.visibility = View.VISIBLE
            } else {
                fadeOutAnimation()
                binding.emptyQueryImage.visibility = View.GONE
                binding.emptyQueryText.visibility = View.GONE
            }

            val country = cityEntity.find { it.country.equals(searchQuery, true) }
            if (country != null) {
                binding.countryName.text = country.country
                binding.cardView.visibility = View.VISIBLE
                binding.countryFlag.load(LinkBuilderForDownloadFlag.buildLink(country.countryCode))
                view?.setOnClickListener {
                    findNavController().navigate(
                        SearchFragmentDirections.actionSearchFragmentToCountryInfoFragment(country.country, country.countryCode)
                    )
                }
            } else {
                binding.cardView.visibility = View.GONE
            }
        })
    }

    private fun showKeyBoard() {
        binding.searchLine.requestFocus()
        val manager: InputMethodManager? = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        manager?.showSoftInput(binding.searchLine, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun fadeInAnimation() {
        val fadeInAnimation = AnimationUtils.loadAnimation(context, R.anim.faid_in_animation_400)
        with(binding) {
            emptyQueryImage.animation = fadeInAnimation
            emptyQueryText.animation = fadeInAnimation
        }
    }

    private fun fadeOutAnimation() {
        val fadeInAnimation = AnimationUtils.loadAnimation(context, R.anim.faid_out_animation_200)
        with(binding) {
            emptyQueryImage.animation = fadeInAnimation
            emptyQueryText.animation = fadeInAnimation
        }
    }
}