package com.rago.googlemapjetpackcompose.presentation.states

import androidx.compose.ui.graphics.Color
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polyline
import com.rago.googlemapjetpackcompose.data.utils.MyLocationSource

data class GoogleMapUIState(
    val myLocationSource: MyLocationSource = MyLocationSource(),
    val points: List<PointAndColor> = listOf(),
    val text: String = ""
)

data class PointAndColor(
    val points: List<LatLng>,
    val color: Color,
    val typeRoute: RouteLabels,
    val onClick: (Polyline) -> Unit
)


enum class RouteLabels {
    DEFAULT_ROUTE,
    DEFAULT_ROUTE_ALTERNATE
}

enum class Speed {
    TRAFFIC_JAM,
    NORMAL,
    SLOW
}