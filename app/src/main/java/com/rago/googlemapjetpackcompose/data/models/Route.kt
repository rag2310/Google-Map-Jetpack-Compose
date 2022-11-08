package com.rago.googlemapjetpackcompose.data.models

data class Route(
    val description: String,
    val distanceMeters: Int,
    val duration: String,
    val legs: List<Leg>,
    val polyline: Polyline,
    val routeLabels: List<String>,
    val routeToken: String,
    val staticDuration: String,
    val travelAdvisory: TravelAdvisory,
    val viewport: Viewport
)