package com.rago.googlemapjetpackcompose.data.utils

import android.location.Location
import com.google.android.gms.maps.LocationSource

class MyLocationSource : LocationSource {

    private var listener: LocationSource.OnLocationChangedListener? = null

    override fun activate(listener: LocationSource.OnLocationChangedListener) {
        this.listener = listener
    }

    override fun deactivate() {
        this.listener = null
    }

    fun onLocationChanged(location: Location) {
        listener?.onLocationChanged(location)
    }
}