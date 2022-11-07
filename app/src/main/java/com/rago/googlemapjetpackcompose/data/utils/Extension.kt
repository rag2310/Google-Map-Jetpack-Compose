package com.rago.googlemapjetpackcompose.data.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHostController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState

fun Context.hasPermission(permission: String): Boolean {

    // Background permissions didn't exit prior to Q, so it's approved by default.
    if (permission == Manifest.permission.ACCESS_BACKGROUND_LOCATION &&
        android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.Q
    ) {
        return true
    }

    return ActivityCompat.checkSelfPermission(this, permission) ==
            PackageManager.PERMISSION_GRANTED
}

fun Location?.toText(): String {
    return if (this != null) {
        toString(latitude, longitude)
    } else {
        "Unknown location"
    }
}

private fun toString(lat: Double, lon: Double): String {
    return "($lat, $lon)"
}

fun NavHostController.navigateAndClean(
    toRoute: String
) {
    this.navigate(
        toRoute
    ) {
        popUpTo(0)
    }
}

suspend fun CameraPositionState.moveCamera(
    latLng: LatLng,
    duration: Int = 1_000,
    zoom: Float? = null
) {
    val cameraPosition = CameraPosition.fromLatLngZoom(
        latLng, zoom ?: 18f
    )
    this.animate(
        CameraUpdateFactory.newCameraPosition(cameraPosition), duration
    )
}