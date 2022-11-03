package com.rago.googlemapjetpackcompose.data.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import javax.inject.Inject


class SharedLocationManager @Inject constructor(
    private val context: Context,
    externalScope: CoroutineScope
) {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    private val time = 1500L

    private val locationRequest = LocationRequest.Builder(
        Priority.PRIORITY_HIGH_ACCURACY,
        time
    ).build()

    @SuppressLint("MissingPermission")
    private val _locationUpdates = callbackFlow {
        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                trySend(result.lastLocation)
            }
        }

        if (!context.hasPermission(Manifest.permission.ACCESS_FINE_LOCATION) ||
            !context.hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            close()
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            callback,
            Looper.getMainLooper()
        ).addOnFailureListener { e ->
            close(e)
        }

        awaitClose {
            fusedLocationClient.removeLocationUpdates(callback)
        }
    }.shareIn(externalScope, replay = 0, started = SharingStarted.WhileSubscribed())

    fun locationFlow(): Flow<Location?> = _locationUpdates

    companion object {
        private const val TAG = "SharedLocationManager"
    }
}