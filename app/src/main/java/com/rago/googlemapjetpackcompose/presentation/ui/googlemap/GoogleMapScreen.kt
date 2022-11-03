package com.rago.googlemapjetpackcompose.presentation.ui.googlemap

import android.annotation.SuppressLint
import android.location.Location
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.rememberCameraPositionState
import com.rago.googlemapjetpackcompose.presentation.states.GoogleMapUIState

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

    var mapIsReady by remember {
        mutableStateOf(false)
    }

    val cameraPositionState = rememberCameraPositionState {}

    var firstTime by rememberSaveable {
        mutableStateOf(true)
    }

    LaunchedEffect(key1 = location, block = {
        location?.let {
            val latLng = LatLng(it.latitude, it.longitude)
            if (firstTime) {
                cameraPositionState.position = CameraPosition.fromLatLngZoom(latLng, 18f)
                firstTime = false
            }
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
                cameraPositionState = cameraPositionState
            )
        }
    }
}
