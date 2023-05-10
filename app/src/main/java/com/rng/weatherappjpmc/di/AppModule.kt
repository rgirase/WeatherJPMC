package com.rng.weatherappjpmc.di

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.rng.weatherappjpmc.data.location.LocationTracker
import com.rng.weatherappjpmc.data.location.LocationTrackerImpl
import com.rng.weatherappjpmc.data.remote.ApiInterface
import com.rng.weatherappjpmc.data.repository.WeatherRepository
import com.rng.weatherappjpmc.data.repository.WeatherRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

/**
 * Dependency Provider for Application Components
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    const val LOCATION_FILE = "location.preferences_pb"

    /**
     * Provides Retrofit Instance
     */
    @Provides
    @Singleton
    fun provideApiInterface(): ApiInterface {
        return Retrofit.Builder().baseUrl(ApiInterface.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create()).build()
            .create(ApiInterface::class.java)
    }

    /**
     * Provides WeatherRepository Instance
     */

    @Provides
    @Singleton
    fun provideWeatherRepository(weatherRepositoryImpl: WeatherRepositoryImpl): WeatherRepository {
        return weatherRepositoryImpl
    }

    /**
     * Provides FusedLocationProviderClient Instance
     */

    @Provides
    @Singleton
    fun provideFusedLocationProviderClient(app: Application): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(app)
    }

    /**
     * Provides LocationTracker Instance
     */

    @Provides
    @Singleton
    fun provideLocationRepository(locationRepositoryImpl: LocationTrackerImpl): LocationTracker {
        return locationRepositoryImpl
    }

    /**
     * Provides DataStore Single Instance
     */
    @Singleton
    @Provides
    fun providePreferencesDataStore(@ApplicationContext appContext: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { appContext.preferencesDataStoreFile(LOCATION_FILE) }
        )
    }

}