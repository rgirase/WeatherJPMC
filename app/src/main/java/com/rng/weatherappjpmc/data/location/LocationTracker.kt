package com.rng.weatherappjpmc.data.location

import android.location.Location

interface LocationTracker {

    suspend fun getCurrentLocation(): Location?
}