package com.rng.weatherappjpmc.data.repository

import com.rng.weatherappjpmc.utils.Resource
import com.rng.weatherappjpmc.data.model.CoordinatesResponse
import com.rng.weatherappjpmc.data.model.WeatherResponse
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    suspend fun getWeatherData(lat: String, long: String): Flow<Resource<WeatherResponse>>

    suspend fun getCoordinates(cityName: String): Flow<Resource<List<CoordinatesResponse>>>
}