package com.rago.googlemapjetpackcompose.data.models

data class Leg(
    val distanceMeters: Int,
    val duration: String,
    val endLocation: EndLocation,
    val polyline: Polyline,
    val startLocation: StartLocation,
    val staticDuration: String,
    val steps: List<Step>,
    val travelAdvisory: TravelAdvisory
)