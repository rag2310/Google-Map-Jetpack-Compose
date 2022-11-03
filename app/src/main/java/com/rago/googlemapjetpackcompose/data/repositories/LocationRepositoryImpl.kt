package com.rago.googlemapjetpackcompose.data.repositories

import android.location.Location
import com.rago.googlemapjetpackcompose.data.utils.SharedLocationManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val sharedLocationManager: SharedLocationManager
) : LocationRepository {
    override fun getLocations(): Flow<Location?> = sharedLocationManager.locationFlow()
}