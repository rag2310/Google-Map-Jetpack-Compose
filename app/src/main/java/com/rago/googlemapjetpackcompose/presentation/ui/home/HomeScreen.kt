package com.rago.googlemapjetpackcompose.presentation.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.rago.googlemapjetpackcompose.data.utils.CheckPermissions
import com.rago.googlemapjetpackcompose.presentation.states.HomeUIState

@Composable
fun HomeScreen(homeUIState: HomeUIState) {
    HomeContent(homeUIState = homeUIState)
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true)
private fun HomeContent(homeUIState: HomeUIState = HomeUIState()) {

    val context = LocalContext.current

    val launcherBackground = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { map ->
        val result = map.all {
            it.value
        }
        if (result) {
            homeUIState.validation()
        }
    }

    val launcherLocations = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { map ->
        val result = map.all {
            it.value
        }
        if (result) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                launcherBackground.launch(CheckPermissions.REQUIRED_PERMISSION_SDK_R)
            } else {
                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.P) {
                    context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                } else {
                    homeUIState.validation()
                }
            }
        }
    }

    LaunchedEffect(key1 = homeUIState.success, block = {
        if (homeUIState.success) {
            homeUIState.initService()
            homeUIState.goGoogleMap()
        }
    })

    LaunchedEffect(key1 = homeUIState.getPermissions, block = {
        if (homeUIState.getPermissions) {
            launcherLocations.launch(CheckPermissions.REQUIRED_PERMISSIONS_MAP)
        }
    })

    Scaffold(
        Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            Button(onClick = homeUIState.validation) {
                Text(text = "Google Maps")
            }
        }
    }
}
