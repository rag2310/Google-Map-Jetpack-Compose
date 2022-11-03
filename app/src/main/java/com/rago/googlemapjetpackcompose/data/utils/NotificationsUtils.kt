package com.rago.googlemapjetpackcompose.data.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.rago.googlemapjetpackcompose.R

object NotificationsUtils {

    fun makeNotification(
        name: String,
        description: String,
        idChanel: String,
        idNotify: Int,
        context: Context,
        actions: Pair<String, PendingIntent?> = Pair("", null),
        pendingIntent: PendingIntent? = null
    ) {
        val notification =
            notification(name, description, idChanel, context, false, actions, pendingIntent)
        NotificationManagerCompat.from(context)
            .notify(idNotify, notification)
    }

    fun makeBackGroundNotification(
        name: String,
        description: String,
        idChanel: String,
        context: Context
    ): Notification = notification(
        name = name,
        description = description,
        idChanel = idChanel,
        context = context,
        background = true
    )

    private fun notification(
        name: String,
        description: String,
        idChanel: String,
        context: Context,
        background: Boolean = false,
        actions: Pair<String, PendingIntent?> = Pair("", null),
        pendingIntent: PendingIntent? = null
    ): Notification {
        val importance =
            if (background) NotificationManager.IMPORTANCE_LOW
            else NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(idChanel, name, importance)
        channel.description = description

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

        notificationManager?.createNotificationChannel(channel)

        val builder = NotificationCompat.Builder(context, idChanel)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(name)
            .setContentText(description)


        if (background) {
            builder.apply {
                setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                setDefaults(NotificationCompat.DEFAULT_ALL)
                setVibrate(LongArray(0))
            }
        } else {
            builder.apply {
                priority = NotificationCompat.PRIORITY_HIGH
                setVibrate(LongArray(2000))
            }
        }

        val (actionName, action) = actions

        action?.let {
            builder.addAction(
                R.drawable.ic_launcher_foreground,
                actionName,
                action
            )
        }

        pendingIntent?.let {
            builder.setContentIntent(it)
        }

        return builder.build()
    }
}
