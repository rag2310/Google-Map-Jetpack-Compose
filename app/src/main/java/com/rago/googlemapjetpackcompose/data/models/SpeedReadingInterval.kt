package com.rago.googlemapjetpackcompose.data.models

data class SpeedReadingInterval(
    val endPolylinePointIndex: Int,
    val speed: String,
    val startPolylinePointIndex: Int
)