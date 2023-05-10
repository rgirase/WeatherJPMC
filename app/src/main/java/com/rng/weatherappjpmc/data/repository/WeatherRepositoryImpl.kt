package com.rng.weatherappjpmc.data.repository


import com.rng.weatherappjpmc.data.model.CoordinatesResponse
import com.rng.weatherappjpmc.data.model.WeatherResponse
import com.rng.weatherappjpmc.data.remote.ApiInterface
import com.rng.weatherappjpmc.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(private val apiInterface: ApiInterface) :
    WeatherRepository {

    /**
     * Fetches Weather Data from API
     */
    override suspend fun getWeatherData(
        lat: String,
        long: String
    ): Flow<Resource<WeatherResponse>> {
        return flow {
            emit(Resource.Loading(true))
            val weather = try {
                apiInterface.getWeather(
                    mapOf(
                        "lat" to lat,
                        "lon" to long,
                        "exclude" to "minutely,hourly",
                        "appid" to ApiInterface.API_KEY
                    )
                )
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error("IO Exception", null))
                null
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("HTTP API Error ${e.code()} -> ${e.message()}", null))
                null
            }
            weather?.let {
                emit(Resource.Loading(false))
                emit(Resource.Success(data = it))

            }
        }
    }

    /**
     * Fetches Coordinates from API
     */

    override suspend fun getCoordinates(cityName: String): Flow<Resource<List<CoordinatesResponse>>> {
        return flow {
            val coordinatesResponse = try {
                apiInterface.getLatAndLong(
                    mapOf(
                        "q" to cityName,
                        "limit" to "1",
                        "appid" to ApiInterface.API_KEY
                    )
                )
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error("${e.message}", null))
                null
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("HTTP API Error ${e.code()} -> ${e.message()}", null))
                null
            }
            coordinatesResponse?.let {
                if (it.isNotEmpty()) {
                    emit(Resource.Success(data = it))
                } else {
                    emit(Resource.Error("Not able to find your location", null))
                }
            }
        }
    }
}