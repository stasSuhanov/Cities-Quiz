package com.mytest.android.citiesapp.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mytest.android.citiesapp.R
import com.mytest.android.citiesapp.databinding.FragmentThemesBinding
import com.mytest.android.citiesapp.ui.MainActivity
import com.mytest.android.citiesapp.utils.QuizPreferences

class ThemesFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentThemesBinding

    private var currentTheme: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentThemesBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.defaultThemeButton.setOnClickListener(this)
        binding.lightThemeButton.setOnClickListener(this)
        binding.darkThemeButton.setOnClickListener(this)

        currentTheme = QuizPreferences.loadAppTheme(requireContext())
        val currentThemeName = when (currentTheme) {
            MainActivity.DEFAULT_THEME -> DEFAULT_THEME_NAME
            MainActivity.LIGHT_THEME -> LIGHT_THEME_NAME
            MainActivity.DARK_THEME -> DARK_THEME_NAME
            else -> DEFAULT_THEME_NAME
        }
        binding.currentTheme.text = getString(R.string.current_theme, currentThemeName)
    }

    override fun onClick(view: View) {
        setTheme(view.id)
    }

    private fun setTheme(buttonId: Int) {
        when (buttonId) {
            R.id.default_theme_button -> {
                QuizPreferences.saveAppTheme(requireContext(), MainActivity.DEFAULT_THEME)
                activity?.setTheme(R.style.Theme_CitiesApp)
            }
            R.id.light_theme_button -> {
                QuizPreferences.saveAppTheme(requireContext(), MainActivity.LIGHT_THEME)
                activity?.setTheme(R.style.Theme_CitiesApp_Light)
            }
            R.id.dark_theme_button -> {
                QuizPreferences.saveAppTheme(requireContext(), MainActivity.DARK_THEME)
                activity?.setTheme(R.style.Theme_CitiesApp_Dark)
            }
        }
        if (currentTheme != QuizPreferences.loadAppTheme(requireContext())) {
            activity?.finish()
            activity?.startActivity(Intent(activity, activity?.javaClass))
            activity?.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }

    companion object {
        private const val DEFAULT_THEME_NAME = "Default"
        private const val LIGHT_THEME_NAME = "Light"
        private const val DARK_THEME_NAME = "Dark"
    }
}