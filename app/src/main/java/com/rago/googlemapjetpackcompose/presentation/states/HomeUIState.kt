package com.rago.googlemapjetpackcompose.presentation.states

data class HomeUIState(
    val success: Boolean = false,
    val initService: () -> Unit = {},
    val setInitService: (() -> Unit) -> Unit = {},
    val validation: () -> Unit = {},
    val goGoogleMap: () -> Unit = {},
    val setGoGoogleMap: (() -> Unit) -> Unit = {},
    val getPermissions: Boolean = false
)
