package com.rago.googlemapjetpackcompose.presentation.viewmodels

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.maps.android.PolyUtil
import com.rago.googlemapjetpackcompose.data.repositories.GoogleMapRepository
import com.rago.googlemapjetpackcompose.presentation.states.GoogleMapUIState
import com.rago.googlemapjetpackcompose.presentation.states.PointAndColor
import com.rago.googlemapjetpackcompose.presentation.states.RouteLabels
import com.rago.googlemapjetpackcompose.presentation.states.Speed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GoogleMapViewModel @Inject constructor(
    googleMapRepository: GoogleMapRepository
) : ViewModel() {

    private val _googleMapUIState: MutableStateFlow<GoogleMapUIState> = MutableStateFlow(
        GoogleMapUIState()
    )
    val googleMapUIState: StateFlow<GoogleMapUIState> = _googleMapUIState.asStateFlow()

    val location = flow {
        emitAll(googleMapRepository.getLocation())
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = null
    )

    init {
        viewModelScope.launch {
            val routeApiResponse = googleMapRepository.getJson()
            routeApiResponse?.let { apiResponse ->
                apiResponse.routes.forEach { route ->
                    val points = PolyUtil.decode(route.polyline.encodedPolyline)

                    val pointAndColors = mutableListOf<PointAndColor>()
                    route.travelAdvisory.speedReadingIntervals.forEach {
                        val start = it.startPolylinePointIndex
                        val end = it.endPolylinePointIndex
                        val color = when (it.speed) {
                            Speed.SLOW.name -> {
                                Color.Yellow
                            }
                            Speed.NORMAL.name -> {
                                Color.Blue
                            }
                            Speed.TRAFFIC_JAM.name -> {
                                Color.Red
                            }
                            else -> {
                                Color.Blue
                            }
                        }
                        val routeLabels = route.routeLabels.first()

                        val newPoints = points.subList(start, end + 1)
                        val pointAndColor = PointAndColor(
                            newPoints,
                            color,
                            if (routeLabels == RouteLabels.DEFAULT_ROUTE.name) RouteLabels.DEFAULT_ROUTE else RouteLabels.DEFAULT_ROUTE_ALTERNATE
                        ) {
                            _googleMapUIState.update { mapUIState ->
                                mapUIState.copy(
                                    text = googleMapRepository.infoRoutes(
                                        route.distanceMeters,
                                        route.duration
                                    )
                                )
                            }
                        }
                        pointAndColors.add(pointAndColor)
                        if (pointAndColor.typeRoute == RouteLabels.DEFAULT_ROUTE) {
                            _googleMapUIState.update { mapUIState ->
                                mapUIState.copy(
                                    text = googleMapRepository.infoRoutes(
                                        route.distanceMeters,
                                        route.duration
                                    )
                                )
                            }
                        }
                    }
                    _googleMapUIState.update { mapUIState ->
                        val old = mapUIState.points
                        val new = mutableListOf<PointAndColor>()
                        new.addAll(old)
                        new.addAll(pointAndColors)

                        mapUIState.copy(points = new.sortedByDescending { it.typeRoute })
                    }
                }
            }
        }
    }
}


