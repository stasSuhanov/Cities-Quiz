package com.mytest.android.citiesapp.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.mytest.android.citiesapp.R
import com.mytest.android.citiesapp.databinding.FragmentMainBinding
import com.mytest.android.citiesapp.ui.fragment.NavigationCheckingFragment
import kotlinx.coroutines.launch

class MainFragment : NavigationCheckingFragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycleScope.launch {
            val isLoad = mainViewModel.countStringsInDatabase()
            if (isLoad == EMPTY_DATABASE) {
                with(binding) {
                    searchButton.visibility = View.INVISIBLE
                    quizButton.visibility = View.INVISIBLE
                    themesButton.visibility = View.INVISIBLE
                    aboutAppButton.visibility = View.INVISIBLE
                    progressBar.visibility = View.VISIBLE
                    waitingText.visibility = View.VISIBLE
                }
                val assets = context?.assets
                if (assets != null) {
                    val amountOfCities = mainViewModel.addCitiesToDatabase(assets)
                    Toast.makeText(
                        context,
                        getString(R.string.amount_cities, amountOfCities),
                        Toast.LENGTH_LONG
                    ).show()
                    with(binding) {
                        searchButton.visibility = View.VISIBLE
                        quizButton.visibility = View.VISIBLE
                        themesButton.visibility = View.VISIBLE
                        aboutAppButton.visibility = View.VISIBLE
                        progressBar.visibility = View.INVISIBLE
                        waitingText.visibility = View.INVISIBLE
                    }
                }
            }
        }

        navigate()
    }

    private fun navigate() {
        binding.quizButton.setOnClickListener {
            navigate(MainFragmentDirections.actionMainFragmentToChoiceOfDifficultyLevelFragment())
        }

        binding.searchButton.setOnClickListener {
            navigate(MainFragmentDirections.actionMainFragmentToSearchFragment())
        }

        binding.themesButton.setOnClickListener {
            navigate(MainFragmentDirections.actionMainFragmentToThemesFragment())
        }

        binding.aboutAppButton.setOnClickListener {
            navigate(MainFragmentDirections.actionMainFragmentToAboutAppFragment())
        }
    }

    companion object {
        const val EMPTY_DATABASE = 0
    }
}