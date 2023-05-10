package com.rng.weatherappjpmc

import android.Manifest
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.rng.weatherappjpmc.data.model.lastLocation
import com.rng.weatherappjpmc.presentation.viewmodel.WeatherViewModel
import com.rng.weatherappjpmc.presentation.weather_home_screen.WeatherHomeScreen
import com.rng.weatherappjpmc.ui.theme.DarkBlue
import com.rng.weatherappjpmc.ui.theme.WeatherAppJPMCTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: WeatherViewModel by viewModels()
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

    // At the top level of your kotlin file:


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getLocationFromPreferences()
        checkForCurrentLocationWeather()

        setContent {
            WeatherAppJPMCTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = DarkBlue
                ) {
                    WeatherHomeScreen()
                }
            }
        }
    }

    private fun checkForCurrentLocationWeather() {
        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {
            viewModel.loadLocationFromCoordinates()
        }
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            )
        )
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WeatherAppJPMCTheme {

    }
}