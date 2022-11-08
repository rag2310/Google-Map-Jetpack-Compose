package com.rago.googlemapjetpackcompose.presentation.ui.googlemap

import android.annotation.SuppressLint
import android.location.Location
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.RoundCap
import com.google.maps.android.compose.*
import com.rago.googlemapjetpackcompose.data.utils.moveCamera
import com.rago.googlemapjetpackcompose.presentation.states.GoogleMapUIState
import com.rago.googlemapjetpackcompose.presentation.states.RouteLabels
import com.rago.googlemapjetpackcompose.presentation.states.RouteLabels.*
import kotlinx.coroutines.launch

@Composable
fun GoogleMapScreen(googleMapUIState: GoogleMapUIState, location: Location?) {
    GoogleMapContent(googleMapUIState = googleMapUIState, location = location)
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GoogleMapContent(googleMapUIState: GoogleMapUIState, location: Location?) {

    val mapProperties by remember {
        mutableStateOf(MapProperties(isMyLocationEnabled = true))
    }

    val uiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                myLocationButtonEnabled = false,
                zoomControlsEnabled = false
            )
        )
    }

    var mapIsReady by remember {
        mutableStateOf(false)
    }

    val cameraPositionState = rememberCameraPositionState {}

    var firstTime by rememberSaveable {
        mutableStateOf(true)
    }

    var userMoveCamera by remember {
        mutableStateOf(false)
    }

    var lastLocation by remember {
        mutableStateOf<LatLng?>(null)
    }

    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = location, block = {
        location?.let {
            val latLng = LatLng(it.latitude, it.longitude)
            if (!firstTime && !userMoveCamera) {
                cameraPositionState.moveCamera(latLng)
            }

            if (firstTime) {
                cameraPositionState.position = CameraPosition.fromLatLngZoom(latLng, 18f)
                firstTime = false
            }

            lastLocation = latLng
        }
    })

    LaunchedEffect(key1 = cameraPositionState.cameraMoveStartedReason, block = {
        if (cameraPositionState.cameraMoveStartedReason == CameraMoveStartedReason.GESTURE) {
            userMoveCamera = true
        }
    })

    Scaffold(Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize()) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                properties = mapProperties,
                locationSource = googleMapUIState.myLocationSource,
                onMapLoaded = {
                    mapIsReady = true
                },
                cameraPositionState = cameraPositionState,
                uiSettings = uiSettings
            ) {
                googleMapUIState.points.forEach {
                    Polyline(
                        points = it.points,
                        color = when (it.typeRoute) {
                            DEFAULT_ROUTE -> {
                                it.color
                            }
                            DEFAULT_ROUTE_ALTERNATE -> {
                                it.color.copy(alpha = 0.3f)
                            }
                        },
                        width = 25f,
                        endCap = RoundCap(),
                        startCap = RoundCap(),
                        onClick = it.onClick,
                        clickable = true
                    )
                }
            }
            if (!mapIsReady) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            Column(
                Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 16.dp)
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    AnimatedVisibility(visible = userMoveCamera) {
                        ElevatedCard(
                            onClick = {
                                scope.launch {
                                    lastLocation?.let {
                                        cameraPositionState.moveCamera(it)
                                        userMoveCamera = false
                                    }
                                }
                            },
                            shape = CircleShape,
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            )
                        ) {
                            Box(
                                modifier = Modifier.padding(10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Filled.MyLocation, contentDescription = null)
                            }
                        }
                    }
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(72.dp)
                        .background(Color.White),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = googleMapUIState.text)
                }
            }
        }
    }
}