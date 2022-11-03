package com.rago.googlemapjetpackcompose.presentation.states

import com.rago.googlemapjetpackcompose.data.utils.MyLocationSource

data class GoogleMapUIState(
    val myLocationSource: MyLocationSource = MyLocationSource()
)