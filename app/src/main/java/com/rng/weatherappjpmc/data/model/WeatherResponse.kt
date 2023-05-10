package com.rng.weatherappjpmc.data.model

/**
 * Data class for WeatherResponse
 */

data class weather(val id: Int, val main: String, val description: String, val icon: String)

data class main(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Int,
    val humidity: Int
)

data class coord(val lon: Double?, val lat: Double?)

data class lastLocation(val lon: String?, val lat: String?)

data class wind(val speed: Double, val deg: Int)

data class clouds(val all: Int)

data class sys(val type: Int, val id: Int, val country: String, val sunrise: Long, val sunset: Long)

data class WeatherResponse(
    val coord: coord,
    val weather: List<weather>,
    val base: String,
    val main: main,
    val visibility: Int,
    val wind: wind,
    val clouds: clouds,
    val dt: Long,
    val sys: sys,
    val timezone: Int,
    val id: Int,
    val name: String,
    val cod: Int
)
