package com.rago.googlemapjetpackcompose.data.models

data class Step(
    val distanceMeters: Int,
    val endLocation: EndLocation,
    val navigationInstruction: NavigationInstruction,
    val polyline: Polyline,
    val startLocation: StartLocation,
    val staticDuration: String
)