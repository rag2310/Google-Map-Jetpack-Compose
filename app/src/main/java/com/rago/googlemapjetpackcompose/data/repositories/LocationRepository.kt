package com.rago.googlemapjetpackcompose.data.repositories

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    fun getLocations(): Flow<Location?>
}