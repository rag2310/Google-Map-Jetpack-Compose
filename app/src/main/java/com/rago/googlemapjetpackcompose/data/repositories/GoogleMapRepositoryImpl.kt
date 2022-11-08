package com.rago.googlemapjetpackcompose.data.repositories

import android.content.Context
import android.location.Location
import com.google.gson.Gson
import com.rago.googlemapjetpackcompose.R
import com.rago.googlemapjetpackcompose.data.models.RouteApiResponse
import com.rago.googlemapjetpackcompose.data.utils.SharedLocationManager
import kotlinx.coroutines.flow.Flow
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject

class GoogleMapRepositoryImpl @Inject constructor(
    private val sharedLocationManager: SharedLocationManager, private val context: Context
) : GoogleMapRepository {
    override fun getLocation(): Flow<Location?> = sharedLocationManager.locationFlow()

    override fun getJson(): RouteApiResponse? {
        val test = context.resources.openRawResource(R.raw.test)
        val json = inputStreamToString(test) ?: return null

        return Gson().fromJson(json, RouteApiResponse::class.java)
    }

    override fun infoRoutes(distance: Int, duration: String): String {
        val km = "%.2f".format(distance / 1000.0)
        val seg = duration.filter { it.isDigit() }
        val min: Int = seg.toInt() / 60
        var hour = 0
        if (min >= 60) {
            hour = min / 60
        }
        val minResult = min - (hour * 60)
        val segResult = seg.toInt() - (min * 60)
        return if (hour > 0)
            "$hour h $minResult min $segResult seg $km KM"
        else
            "$minResult min $segResult seg $km KM"
    }

    private fun inputStreamToString(inputStream: InputStream): String? {
        return try {
            val bytes = ByteArray(inputStream.available())
            inputStream.read(bytes, 0, bytes.size)
            String(bytes)
        } catch (e: IOException) {
            null
        }
    }
}