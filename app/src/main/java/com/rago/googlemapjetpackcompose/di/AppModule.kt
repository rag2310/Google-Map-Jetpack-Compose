package com.rago.googlemapjetpackcompose.di

import android.content.Context
import com.rago.googlemapjetpackcompose.data.utils.CheckPermissions
import com.rago.googlemapjetpackcompose.data.utils.SharedLocationManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideCheckPermissions(
        @ApplicationContext context: Context
    ): CheckPermissions = CheckPermissions(context)

    @Provides
    @Singleton
    fun provideSharedLocationManager(
        @ApplicationContext context: Context,
        @ApplicationScope externalScope: CoroutineScope
    ): SharedLocationManager =
        SharedLocationManager(context = context, externalScope = externalScope)
}