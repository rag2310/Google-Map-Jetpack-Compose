package com.rago.googlemapjetpackcompose.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rago.googlemapjetpackcompose.data.repositories.HomeRepository
import com.rago.googlemapjetpackcompose.presentation.states.HomeUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {

    private val _homeUIState: MutableStateFlow<HomeUIState> = MutableStateFlow(
        HomeUIState(
            setInitService = ::setInitService,
            setGoGoogleMap = ::setGoGoogleMap,
            validation = ::validation
        )
    )

    val homeUIState: StateFlow<HomeUIState> = _homeUIState.asStateFlow()

    private fun setInitService(initService: () -> Unit) {
        viewModelScope.launch {
            _homeUIState.update {
                it.copy(initService = initService)
            }
        }
    }

    private fun setGoGoogleMap(goGoogleMap: () -> Unit) {
        viewModelScope.launch {
            _homeUIState.update {
                it.copy(goGoogleMap = goGoogleMap)
            }
        }
    }

    private fun validation() {
        viewModelScope.launch {
            when {
                !homeRepository.checkPermissionLocation() -> {
                    _homeUIState.update {
                        it.copy(getPermissions = true)
                    }
                }
                else -> {
                    _homeUIState.update {
                        it.copy(success = true)
                    }
                }
            }
        }
    }
}