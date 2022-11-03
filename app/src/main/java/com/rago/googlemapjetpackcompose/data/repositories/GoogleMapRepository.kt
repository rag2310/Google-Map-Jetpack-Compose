package com.rago.googlemapjetpackcompose.data.repositories

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface GoogleMapRepository {
    fun getLocation(): Flow<Location?>
}