package com.rago.googlemapjetpackcompose.data.repositories

import android.location.Location
import com.rago.googlemapjetpackcompose.data.models.RouteApiResponse
import kotlinx.coroutines.flow.Flow

interface GoogleMapRepository {
    fun getLocation(): Flow<Location?>
    fun getJson(): RouteApiResponse?
    fun infoRoutes(distance: Int, duration: String): String
}