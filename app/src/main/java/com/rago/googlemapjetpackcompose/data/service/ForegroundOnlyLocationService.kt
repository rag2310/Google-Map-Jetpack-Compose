package com.rago.googlemapjetpackcompose.data.service

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.location.Location
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.rago.googlemapjetpackcompose.R
import com.rago.googlemapjetpackcompose.data.repositories.LocationRepository
import com.rago.googlemapjetpackcompose.data.utils.Constants
import com.rago.googlemapjetpackcompose.data.utils.NotificationsUtils
import com.rago.googlemapjetpackcompose.data.utils.toText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class ForegroundOnlyLocationService : LifecycleService() {

    private var configurationChange = false
    private var serviceRunningInForeGround = false
    private val localBinder = LocalBinder()

    private lateinit var notificationManager: NotificationManager

    private var currentLocation: Location? = null

    private var currentTime: Long? = null

    @Inject
    lateinit var repository: LocationRepository

    private var locationFlow: Job? = null

    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, START_NOT_STICKY)
    }

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        stopForeground(STOP_FOREGROUND_REMOVE)
        serviceRunningInForeGround = false
        configurationChange = false
        return localBinder
    }

    override fun onRebind(intent: Intent?) {
        stopForeground(STOP_FOREGROUND_REMOVE)
        serviceRunningInForeGround = false
        configurationChange = false
        super.onRebind(intent)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        if (!configurationChange) {
            val notification = NotificationsUtils.makeBackGroundNotification(
                name = applicationContext.getString(R.string.app_name),
                description = currentLocation.toText(),
                idChanel = Constants.LOCATION_CHANNEL_ID,
                context = applicationContext,
            )
            startForeground(Constants.LOCATION_NOTIFICATION_ID, notification)
            serviceRunningInForeGround = true

        }
        return true
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        configurationChange = true
    }

    fun subscribeToLocationUpdates() {

        startService(Intent(applicationContext, ForegroundOnlyLocationService::class.java))
        locationFlow = repository.getLocations()
            .flowWithLifecycle(lifecycle = lifecycle, Lifecycle.State.STARTED)
            .onEach { location ->
                location?.let { myLocation ->
                    if (currentTime == null)
                        currentTime = System.currentTimeMillis()
                    Log.i(
                        "Background_Location",
                        "Location: ${myLocation.toText()}"
                    )
                    if (serviceRunningInForeGround) {
                        val notification = NotificationsUtils.makeBackGroundNotification(
                            name = applicationContext.getString(R.string.app_name),
                            description = myLocation.toText(),
                            idChanel = Constants.LOCATION_CHANNEL_ID,
                            context = applicationContext,
                        )
                        notificationManager.notify(
                            Constants.LOCATION_NOTIFICATION_ID,
                            notification
                        )
                    }
                    currentLocation = myLocation
                }
            }.launchIn(lifecycleScope)

    }

    fun unSubscribeToLocationUpdates() {
        locationFlow?.cancel()
    }

    inner class LocalBinder : Binder() {
        val service: ForegroundOnlyLocationService
            get() = this@ForegroundOnlyLocationService
    }
}
