package com.rago.googlemapjetpackcompose.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rago.googlemapjetpackcompose.data.repositories.GoogleMapRepository
import com.rago.googlemapjetpackcompose.presentation.states.GoogleMapUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
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
}