package com.rng.weatherappjpmc.presentation.viewmodel

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.rng.weatherappjpmc.MainActivity
import com.rng.weatherappjpmc.data.location.LocationTracker
import com.rng.weatherappjpmc.presentation.weather_home_screen.WeatherHomeScreenState
import com.rng.weatherappjpmc.data.repository.WeatherRepository
import com.rng.weatherappjpmc.presentation.weather_home_screen.WeatherHomeScreenEvents
import com.rng.weatherappjpmc.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val locationTracker: LocationTracker,
    private val dataStore: DataStore<Preferences>
) :
    ViewModel() {
    var weatherState by mutableStateOf(WeatherHomeScreenState())
    private var lat: String = ""
    private var long: String = ""
    private var searchJob: Job? = null

    /**
     * This function is called when the user clicks on the search button
     */
    fun getLocationFromPreferences() {
        viewModelScope.launch {
            weatherState = weatherState.copy(isLoading = true)
            dataStore.data.collect { preferences ->
                preferences[LAST_LAT]?.let { lat ->
                    preferences[LAST_LONG]?.let { long ->
                        getWeatherData(lat, long)
                    }
                }
            }
        }
    }

    /**
     * This function is called when the user gives permission to access the location
     */

    fun loadLocationFromCoordinates() {
        viewModelScope.launch {
            weatherState = weatherState.copy(isLoading = true)
            locationTracker.getCurrentLocation()?.let { location ->
                getWeatherData(location.latitude.toString(), location.longitude.toString())
                lat = location.latitude.toString()
                long = location.longitude.toString()
                storeCurrentLocation(lat, long)
            } ?: kotlin.run {
                weatherState = weatherState.copy(
                    isLoading = false,
                )
            }
        }

    }

    /**
     * This function stores last location in the data store
     */

    private fun storeCurrentLocation(lat: String, long: String) {
        viewModelScope.launch {
            dataStore.edit { data ->
                data[LAST_LAT] = lat
                data[LAST_LONG] = long
            }
        }
    }

    /**
     * This function is called after getting the coordinates to get the weather data
     */

    private fun getWeatherData(lat: String, long: String) {
        viewModelScope.launch {
            weatherRepository.getWeatherData(lat, long).collect { result ->
                when (result) {
                    is Resource.Error -> {
                        weatherState = weatherState.copy(
                            errorMessage = result.message
                        )
                    }

                    is Resource.Success -> {
                        result.data?.let { data ->
                            weatherState = weatherState.copy(
                                weatherResponse = data
                            )
                        }
                    }

                    is Resource.Loading -> {
                        weatherState = weatherState.copy(isLoading = result.isLoading)
                    }

                }
            }
        }

    }

    /**
     * This function is called after the user enters the city name to get the coordinates
     */

    private fun getCoordinates(cityName: String) {
        viewModelScope.launch {
            weatherRepository.getCoordinates(cityName).collect() {
                when (it) {
                    is Resource.Error -> {
                        weatherState = weatherState.copy(
                            isLoading = false,
                            errorMessage = it.message
                        )
                    }

                    is Resource.Success -> {
                        it.data?.let { data ->
                            weatherState = weatherState.copy(
                                isLoading = false,
                                errorMessage = null,
                                coordinatesResponse = data
                            )
                            getWeatherData(data[0].lat.toString(), data[0].lon.toString())
                            storeCurrentLocation(data[0].lat.toString(), data[0].lon.toString())
                            lat = data[0].lat.toString()
                            long = data[0].lon.toString()
                        }
                    }

                    is Resource.Loading -> {
                        weatherState = weatherState.copy(isLoading = it.isLoading)
                    }
                }
            }
        }

    }

    /**
     * This function is called when the user clicks on the search button
     */
    fun onEvent(event: WeatherHomeScreenEvents) {
        when (event) {
            is WeatherHomeScreenEvents.OnSearchQueryChange -> {
                weatherState = weatherState.copy(searchQuery = event.query)
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(700L)
                    getCoordinates(event.query)
                }
            }

        }
    }

    companion object {
        val LAST_LAT = stringPreferencesKey("last_lat")
        val LAST_LONG = stringPreferencesKey("last_long")
    }
}