package com.mytest.android.citiesapp.ui

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.google.android.gms.maps.MapView
import com.mytest.android.citiesapp.R
import com.mytest.android.citiesapp.ui.fragment.ChoiceOfDifficultyLevelFragmentDirections
import com.mytest.android.citiesapp.ui.fragment.ResultFragmentDirections
import com.mytest.android.citiesapp.utils.QuizPreferences
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadTheme()
        setContentView(R.layout.activity_main)

        preloadGoogleMap()
    }

    override fun onBackPressed() {
        val controller = findNavController(R.id.nav_host_fragment)
        when (controller.currentDestination?.id) {
            R.id.resultFragment -> controller.navigate(ResultFragmentDirections.actionResultFragmentToMainFragment())
            R.id.choiceOfDifficultyLevelFragment -> controller.navigate(
                ChoiceOfDifficultyLevelFragmentDirections.actionChoiceOfDifficultyLevelFragmentToMainFragment()
            )
            R.id.quizFragment -> makeAlertDialog()
            else -> super.onBackPressed()
        }
    }

    private fun makeAlertDialog() {
        val builder = AlertDialog.Builder(this)
        with(builder) {
            setPositiveButton(R.string.dialog_yes) { _, _ ->
                findNavController(R.id.nav_host_fragment).navigate(R.id.choiceOfDifficultyLevelFragment)
            }
            setNegativeButton(R.string.dialog_no) { _, _ -> }
            setTitle(R.string.alert_title)
            setMessage(R.string.dialog_alert)
            show()
        }
    }

    private fun preloadGoogleMap() {
        lifecycleScope.launch {
            val googleMap = MapView(applicationContext)
            googleMap.onCreate(null)
            googleMap.onPause()
            googleMap.onDestroy()
        }
    }

    private fun loadTheme() {
        if (isDarkTheme()) {
            setTheme(R.style.Theme_CitiesApp_Dark)
        } else {
            when (QuizPreferences.loadAppTheme(this)) {
                DEFAULT_THEME -> setTheme(R.style.Theme_CitiesApp)
                LIGHT_THEME -> setTheme(R.style.Theme_CitiesApp_Light)
                DARK_THEME -> setTheme(R.style.Theme_CitiesApp_Dark)
            }
        }
    }

    private fun isDarkTheme(): Boolean {
        return this.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    }

    companion object {
        const val DEFAULT_THEME = "default_theme"
        const val LIGHT_THEME = "light_theme"
        const val DARK_THEME = "dark_theme"
    }
}