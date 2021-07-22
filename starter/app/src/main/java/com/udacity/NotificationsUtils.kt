package com.udacity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat

private val REQUEST_CODE = 0
private val FLAGS = 0

/**
 * Builds and delivers the notification.
 */
fun NotificationManager.sendNotification(
    context: Context,
    contentIntent: Intent,
    channelId: String,
    notificationId: Int
) {

    val contentPendingIntent = PendingIntent.getActivity(
        context,
        notificationId,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    val checkStatusPendingIntent = PendingIntent.getBroadcast(
        context,
        REQUEST_CODE,
        contentIntent,
        FLAGS
    )

    val builder = NotificationCompat.Builder(context, channelId)
        .setContentTitle(context.getString(R.string.notification_title))
        .setContentText(context.getString(R.string.notification_description))
        .setContentIntent(contentPendingIntent)
        .setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .addAction(
            R.drawable.ic_assistant_black_24dp,
            context.getString(R.string.notification_button),
            checkStatusPendingIntent
        )
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setAutoCancel(true)

    notify(notificationId, builder.build())
}

fun NotificationManager.createNotificationChannel(
    notificationChannelId: String,
    notificationChannelName: String
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationChannel = NotificationChannel(
            notificationChannelId,
            notificationChannelName,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            setShowBadge(false)
            enableLights(true)
            lightColor = Color.WHITE
            enableVibration(true)
        }
        createNotificationChannel(notificationChannel)
    }
}

fun NotificationManager.cancelNotifications() {
    cancelAll()
}