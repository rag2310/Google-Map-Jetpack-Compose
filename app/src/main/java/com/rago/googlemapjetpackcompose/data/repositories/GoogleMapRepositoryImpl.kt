package com.rago.googlemapjetpackcompose.data.repositories

import android.location.Location
import com.rago.googlemapjetpackcompose.data.utils.SharedLocationManager
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GoogleMapRepositoryImpl @Inject constructor(
    private val sharedLocationManager: SharedLocationManager
) : GoogleMapRepository {
    override fun getLocation(): Flow<Location?> =
        sharedLocationManager.locationFlow()
}