package com.rago.googlemapjetpackcompose.data.repositories

import com.rago.googlemapjetpackcompose.data.utils.CheckPermissions
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val checkPermissions: CheckPermissions
) : HomeRepository {
    override fun checkPermissionLocation(): Boolean =
        checkPermissions.allPermissionsGranted(CheckPermissions.PermitType.MAP)
}