package com.rago.googlemapjetpackcompose.data.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import javax.inject.Inject

class CheckPermissions @Inject constructor(private val context: Context) {
    companion object {

        val REQUIRED_PERMISSIONS_DEFAULT = mutableListOf(
            Manifest.permission.RECORD_AUDIO
        ).apply {
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU) {
                add(Manifest.permission.POST_NOTIFICATIONS)
            }
        }.toTypedArray()

        val REQUIRED_PERMISSIONS_MAP = mutableListOf(
            Manifest.permission.ACCESS_FINE_LOCATION
        ).apply {
            add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }.toTypedArray()

        val REQUIRED_PERMISSION_SDK_R = mutableListOf(
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
        ).toTypedArray()
    }

    fun allPermissionsGranted(permitType: PermitType = PermitType.ALL): Boolean {
        return when (permitType) {
            PermitType.MAP -> {
                REQUIRED_PERMISSIONS_MAP.all {
                    ContextCompat.checkSelfPermission(
                        context, it
                    ) == PackageManager.PERMISSION_GRANTED
                }
            }
            PermitType.ALL -> REQUIRED_PERMISSIONS_DEFAULT.all {
                ContextCompat.checkSelfPermission(
                    context, it
                ) == PackageManager.PERMISSION_GRANTED
            }
            PermitType.SDK_R -> REQUIRED_PERMISSION_SDK_R.all {
                ContextCompat.checkSelfPermission(
                    context, it
                ) == PackageManager.PERMISSION_GRANTED
            }

        }
    }

    @Suppress("DEPRECATION")
    fun permissionsLocationService(): Boolean =
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.R) {
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) < 0
        } else getLocationMode() != Settings.Secure.LOCATION_MODE_HIGH_ACCURACY

    @Suppress("DEPRECATION")
    private fun getLocationMode(): Int {
        return Settings.Secure.getInt(
            context.contentResolver,
            Settings.Secure.LOCATION_MODE
        )
    }

    enum class PermitType {
        MAP,
        SDK_R,
        ALL
    }
}