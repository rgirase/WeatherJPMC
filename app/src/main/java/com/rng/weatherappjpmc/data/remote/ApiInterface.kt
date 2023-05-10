package com.rng.weatherappjpmc.data.remote

import com.rng.weatherappjpmc.data.model.CoordinatesResponse
import com.rng.weatherappjpmc.data.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.QueryMap

/**
 * Interface for Retrofit API Calls
 */
interface ApiInterface {

    @GET("/data/2.5/weather")
    suspend fun getWeather(@QueryMap map: Map<String, String>): WeatherResponse

    @GET("/geo/1.0/direct")
    suspend fun getLatAndLong(@QueryMap map: Map<String, String>): List<CoordinatesResponse>

    companion object {
        const val BASE_URL = "https://api.openweathermap.org"
        const val API_KEY = "9bfdd0416fa140bb96c3d30f829786c9"
    }
}