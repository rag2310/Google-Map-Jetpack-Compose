package com.rago.googlemapjetpackcompose.di

import com.rago.googlemapjetpackcompose.data.repositories.*
import com.rago.googlemapjetpackcompose.data.utils.CheckPermissions
import com.rago.googlemapjetpackcompose.data.utils.SharedLocationManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ServiceScoped
import dagger.hilt.android.scopes.ViewModelScoped

object RepositoriesModule {

    @Module
    @InstallIn(ServiceComponent::class)
    object RepositoriesAppModule {
        @Provides
        @ServiceScoped
        fun provideLocationRepository(
            sharedLocationManager: SharedLocationManager
        ): LocationRepository = LocationRepositoryImpl(sharedLocationManager)
    }

    @Module
    @InstallIn(ViewModelComponent::class)
    object RepositoriesViewModelModule {
        @Provides
        @ViewModelScoped
        fun provideHomeRepository(
            checkPermissions: CheckPermissions
        ): HomeRepository = HomeRepositoryImpl(checkPermissions)

        @Provides
        @ViewModelScoped
        fun provideGoogleMapRepository(sharedLocationManager: SharedLocationManager): GoogleMapRepository =
            GoogleMapRepositoryImpl(sharedLocationManager)
    }
}