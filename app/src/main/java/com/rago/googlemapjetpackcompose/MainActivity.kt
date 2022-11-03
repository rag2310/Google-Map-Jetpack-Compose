package com.rago.googlemapjetpackcompose

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rago.googlemapjetpackcompose.data.service.ForegroundOnlyLocationService
import com.rago.googlemapjetpackcompose.data.utils.navigateAndClean
import com.rago.googlemapjetpackcompose.presentation.states.GoogleMapUIState
import com.rago.googlemapjetpackcompose.presentation.states.HomeUIState
import com.rago.googlemapjetpackcompose.presentation.ui.googlemap.GoogleMapScreen
import com.rago.googlemapjetpackcompose.presentation.ui.home.HomeScreen
import com.rago.googlemapjetpackcompose.presentation.ui.theme.GoogleMapJetpackComposeTheme
import com.rago.googlemapjetpackcompose.presentation.viewmodels.GoogleMapViewModel
import com.rago.googlemapjetpackcompose.presentation.viewmodels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var foregroundOnlyLocationServiceBound = false
    private var foregroundOnlyLocationService: ForegroundOnlyLocationService? = null

    private val foregroundOnlyLocationServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as ForegroundOnlyLocationService.LocalBinder
            foregroundOnlyLocationService = binder.service
            foregroundOnlyLocationServiceBound = true
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            foregroundOnlyLocationService?.unSubscribeToLocationUpdates()
            foregroundOnlyLocationService = null
            foregroundOnlyLocationServiceBound = false
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            GoogleMapJetpackComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navHostController = rememberNavController()
                    NavHost(
                        modifier = Modifier
                            .fillMaxSize()
                            .navigationBarsPadding()
                            .background(Color.Red),
                        navController = navHostController,
                        startDestination = "home"
                    ) {
                        composable("home") {
                            val homeViewModel: HomeViewModel = hiltViewModel()
                            val homeUIState: HomeUIState by homeViewModel.homeUIState.collectAsState()
                            LaunchedEffect(key1 = Unit, block = {
                                homeUIState.setInitService {
                                    initService()
                                }
                                homeUIState.setGoGoogleMap {
                                    navHostController.navigateAndClean("map")
                                }
                            })
                            HomeScreen(homeUIState = homeUIState)
                        }
                        composable("map") {
                            val googleMapViewModel: GoogleMapViewModel = hiltViewModel()
                            val googleMapUIState: GoogleMapUIState by googleMapViewModel.googleMapUIState.collectAsState()
                            val location by googleMapViewModel.location.collectAsState()
                            LaunchedEffect(key1 = location, block = {
                                location?.let {
                                    googleMapUIState.myLocationSource.onLocationChanged(it)
                                }
                            })
                            GoogleMapScreen(googleMapUIState = googleMapUIState, location = location)
                        }
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Intent(this, ForegroundOnlyLocationService::class.java).also { intent ->
            this.bindService(
                intent,
                foregroundOnlyLocationServiceConnection,
                Context.BIND_AUTO_CREATE
            )
        }
    }

    override fun onStop() {
        if (foregroundOnlyLocationServiceBound) {
            this.unbindService(foregroundOnlyLocationServiceConnection)
            foregroundOnlyLocationServiceBound = false
        }
        super.onStop()
    }

    private fun initService() {
        foregroundOnlyLocationService?.subscribeToLocationUpdates() ?: Log.i(
            "LocationService",
            "Service Not Bound"
        )
    }
}